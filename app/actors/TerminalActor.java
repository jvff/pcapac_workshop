package actors;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import play.libs.Akka;
import play.libs.F;
import play.Logger;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import actors.TerminalManagerActor;
import actors.TerminalManagerActor.NewTerminalMessage;

import static java.nio.charset.StandardCharsets.UTF_8;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_23;

public class TerminalActor extends UntypedActor {
    private static final int CONTAINER_PORT = 15100;
    private static final int CMD_KEY = 1;
    private static final int CMD_RESIZE = 2;

    public static class ResizeMessage {
        private final short columns;
        private final short rows;

        public ResizeMessage(short columns, short rows) {
            this.columns = columns;
            this.rows = rows;
        }

        public void writeTo(Writer out) throws IOException {
            writeShort(columns, out);
            writeShort(rows, out);
        }

        private void writeShort(short value, Writer out) throws IOException {
            out.write((value >> 8) & 0xFF);
            out.write(value & 0xFF);
        }
    }

    public static Props props(ActorRef out, String sessionId) {
        return Props.create(TerminalActor.class, out, sessionId);
    }

    public static F.Function<ActorRef, Props> propsFor(final String sessionId) {
        return (ActorRef out) -> props(out, sessionId);
    }

    private final ActorRef out;

    private String containerAddress;
    private Socket containerSocket;
    private Writer containerWriter;
    private Reader containerReader;

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

    public TerminalActor(ActorRef out, String sessionId) {
        this.out = out;

        startContainer();
        notifyInstantiationToTerminalManager(sessionId);
    }

    @Override
    public void onReceive(Object message) {
        try {
            unsafelyHandleMessage(message);
        } catch (IOException exception) {
            Logger.error("Failed to handle message: " + message);
        }
    }

    private void unsafelyHandleMessage(Object message) throws IOException {
        if (message instanceof String)
            sendDataToContainer((String)message);
        else if (message instanceof ResizeMessage)
            resizeContainerTerminal((ResizeMessage)message);
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

            containerReader = new InputStreamReader(socketInput, UTF_8);
            containerWriter = new OutputStreamWriter(socketOut, UTF_8);
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

    private void sendDataToContainer(String dataMessage) throws IOException {
        for (byte data : dataMessage.getBytes()) {
            containerWriter.write(CMD_KEY);
            containerWriter.write(data);
            containerWriter.flush();
        }
    }

    private void resizeContainerTerminal(ResizeMessage message)
            throws IOException {
        containerWriter.write(CMD_RESIZE);

        message.writeTo(containerWriter);
    }

    private void notifyInstantiationToTerminalManager(String sessionId) {
        ActorSystem system = Akka.system();
        ActorRef manager = system.actorFor(TerminalManagerActor.URL);

        manager.tell(new NewTerminalMessage(sessionId), self());
    }
}
