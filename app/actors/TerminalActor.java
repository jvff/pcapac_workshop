package actors;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.Props;

import play.Logger;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_23;

public class TerminalActor extends UntypedActor {
    private static final int CONTAINER_PORT = 15100;

    public static Props props(ActorRef out) {
        return Props.create(TerminalActor.class, out);
    }

    private final ActorRef out;

    private String containerAddress;
    private Socket containerSocket;
    private PrintWriter containerWriter;
    private InputStreamReader containerReader;

    private Thread listenerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                runListenerLoop();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    });

    public TerminalActor(ActorRef out) {
        this.out = out;

        startContainer();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            containerWriter.print((String)message);
            containerWriter.flush();
        }
    }

    private void startContainer() {
        createContainer();
        connectToContainer();
        listenerThread.start();
    }

    private void createContainer() {
        DockerClientConfig dockerConfig = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withDockerHost("unix:///var/run/docker.sock")
            .withApiVersion(VERSION_1_23)
            .build();

        DockerClient docker = DockerClientBuilder.getInstance(dockerConfig)
            .build();

        String containerId = docker
            .createContainerCmd("janitovff/pcapac_git_shell")
            .exec()
            .getId();

        Logger.debug("Created container: " + containerId);

        docker.startContainerCmd(containerId).exec();

        Logger.debug("Started container: " + containerId);

        containerAddress = docker.inspectContainerCmd(containerId)
            .exec()
            .getNetworkSettings()
            .getIpAddress();

        Logger.debug("IP address of container " + containerId + " is "
                + containerAddress);
    }

    private void connectToContainer() {
        try {
            Logger.debug("Waiting to connect to container at "
                    + containerAddress + ":" + CONTAINER_PORT);

            Thread.sleep(2000);

            Logger.debug("Connecting to container at " + containerAddress + ":"
                    + CONTAINER_PORT);

            containerSocket = new Socket(containerAddress, CONTAINER_PORT);

            Logger.debug("Connected to container at " + containerAddress + ":"
                    + CONTAINER_PORT);

            InputStream socketInput = containerSocket.getInputStream();
            OutputStream socketOut = containerSocket.getOutputStream();

            containerReader = new InputStreamReader(socketInput);
            containerWriter = new PrintWriter(socketOut);
        } catch (Exception cause) {
            Logger.error("Failed to connect to container at " + containerAddress
                    + ":" + CONTAINER_PORT, cause);
        }
    }

    private void runListenerLoop() throws IOException {
        final int END_OF_STREAM_INDICATOR = -1;

        int character = containerReader.read();

        while (character != END_OF_STREAM_INDICATOR) {
            sendCharacter((char)character);

            character = containerReader.read();
        }
    }

    private void sendCharacter(char character) {
        String message = Character.toString(character);

        out.tell(message, self());
    }
}
