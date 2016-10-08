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
    private static final char CMD_KEY = 'k';
    private static final char CMD_RESIZE = 'r';
    private static final char CMD_UPLOAD = 'u';

    private static abstract class Message {
        public abstract void writeTo(Writer out) throws IOException;

        protected void writeInt(int value, Writer out) throws IOException {
            writeAlgorisms(value, out);
            out.write(';');
        }

        private void writeAlgorisms(int value, Writer out) throws IOException {
            while (value > 0) {
                int algorism = value % 64;

                writeAlgorism(algorism, out);

                value /= 64;
            }
        }

        private void writeAlgorism(int algorism, Writer out)
                throws IOException {
            if (algorism < 26)
                out.write(algorism + 'A');
            else if (algorism < 52)
                out.write(algorism - 26 + 'a');
            else if (algorism < 62)
                out.write(algorism - 52 + '0');
            else if (algorism == 62)
                out.write('+');
            else if (algorism == 63)
                out.write('/');
        }
    }

    public static class ResizeMessage extends Message {
        private final short columns;
        private final short rows;

        public ResizeMessage(short columns, short rows) {
            this.columns = columns;
            this.rows = rows;
        }

        @Override
        public void writeTo(Writer out) throws IOException {
            writeInt(columns, out);
            writeInt(rows, out);
        }
    }

    public static class UploadFileMessage extends Message {
        private final String path;
        private final String contents;

        public UploadFileMessage(String path, String contents) {
            this.path = path;
            this.contents = contents;
        }

        @Override
        public void writeTo(Writer out) throws IOException {
            writeString(path, out);
            writeString(contents, out);
        }

        public void writeString(String string, Writer out) throws IOException {
            writeInt(string.length(), out);
            out.write(string);
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
        else if (message instanceof UploadFileMessage)
            uploadFileToContainer((UploadFileMessage)message);
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
        containerWriter.write(CMD_KEY);
        containerWriter.write(dataMessage);
        containerWriter.flush();
    }

    private void resizeContainerTerminal(ResizeMessage message)
            throws IOException {
        containerWriter.write(CMD_RESIZE);

        message.writeTo(containerWriter);
    }

    private void uploadFileToContainer(UploadFileMessage message)
            throws IOException {
        containerWriter.write(CMD_UPLOAD);

        message.writeTo(containerWriter);
    }

    private void notifyInstantiationToTerminalManager(String sessionId) {
        ActorSystem system = Akka.system();
        ActorRef manager = system.actorFor(TerminalManagerActor.URL);

        manager.tell(new NewTerminalMessage(sessionId), self());
    }
}
