package actors.terminal;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.nio.file.Path;
import java.util.function.Consumer;

import play.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createTempDirectory;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_23;

public class TerminalContainer {
    private static final int CONTAINER_PORT = 15100;

    private DockerClient docker;
    private String ipAddress;
    private String id;

    private Socket socket;
    private Writer writer;
    private Reader reader;

    private Consumer<Character> listener;

    public TerminalContainer() throws IOException {
        createDockerClient();
        createContainer();
        startContainer();
        connectToContainer();
    }

    private void createDockerClient() {
        DockerClientConfig dockerConfig = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withDockerHost("unix:///var/run/docker.sock")
            .withApiVersion(VERSION_1_23)
            .build();

        docker = DockerClientBuilder.getInstance(dockerConfig).build();
    }

    private void createContainer() {
        Device gpuDevices = new Device("mrw", "/dev/dri", "/dev/dri");

        id = docker.createContainerCmd("janitovff/pcapac_git_shell")
            .withDevices(gpuDevices)
            .exec()
            .getId();

        Logger.debug("Created container: " + id);
    }

    private void startContainer() {
        docker.startContainerCmd(id).exec();

        Logger.debug("Started container: " + id);
    }

    private void connectToContainer() throws IOException {
        getIpAddress();
        openConnection();
        configureStreams();
    }

    private void getIpAddress() {
        ipAddress = docker.inspectContainerCmd(id)
            .exec()
            .getNetworkSettings()
            .getIpAddress();

        Logger.debug("IP address of container " + id + " is " + ipAddress);
    }

    private void openConnection() {
        boolean connected = false;
        int connectionAttempt = 0;
        int maxAttempts = 5;

        while (!connected && connectionAttempt < maxAttempts) {
            try {
                connected = tryToOpenConnection();
            } catch (IOException cause) {
                Logger.error("Failed to connect to container at " + ipAddress
                        + ":" + CONTAINER_PORT, cause);

                connectionAttempt += 1;

                delay(500);
            }
        }
    }

    private void delay(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException cause) {
            Logger.warn("Interrupted during connection retry delay", cause);
        }
    }

    private boolean tryToOpenConnection() throws IOException {
        Logger.debug("Connecting to container at " + ipAddress + ":"
                + CONTAINER_PORT);

        socket = new Socket(ipAddress, CONTAINER_PORT);

        Logger.debug("Connected to container at " + ipAddress + ":"
                + CONTAINER_PORT);

        return true;
    }

    private void configureStreams() throws IOException {
        InputStream socketInput = socket.getInputStream();
        OutputStream socketOut = socket.getOutputStream();

        reader = new InputStreamReader(socketInput, UTF_8);
        writer = new OutputStreamWriter(socketOut, UTF_8);
    }

    public File getFile(String path) throws IOException {
        InputStream stream = docker.copyFileFromContainerCmd(id, path).exec();
        TarInputStream tarStream = new TarInputStream(stream);
        TarEntry tarEntry = tarStream.getNextEntry();

        Logger.debug("Found entry: " + tarEntry.getSize());

        return copyFromStreamToNewFile(tarStream, path, tarEntry.getSize());
    }

    private File copyFromStreamToNewFile(InputStream inputStream,
            String originalFilePath, long size) throws IOException {
        File destinationFile = createDestinationFile(originalFilePath);
        FileOutputStream outputStream = new FileOutputStream(destinationFile);

        Logger.debug("Copying between streams to: " + destinationFile);

        copyBetweenStreams(inputStream, outputStream, size);

        return destinationFile;
    }

    private File createDestinationFile(String sourceFilePath)
            throws IOException {
        String fileName = getFileName(sourceFilePath);
        Path destinationDirectoryPath = createTempDirectory("container-dl-");
        File destinationDirectory = destinationDirectoryPath.toFile();
        File destinationFile = new File(destinationDirectory, fileName);

        destinationDirectory.deleteOnExit();
        destinationFile.deleteOnExit();

        return destinationFile;
    }

    private String getFileName(String filePath) {
        int noDirectories = -1;
        int endOfDirectories = filePath.lastIndexOf('/');

        if (endOfDirectories == noDirectories)
            return filePath;
        else
            return filePath.substring(endOfDirectories + 1);
    }

    private void copyBetweenStreams(InputStream source, OutputStream sink,
            long bytes) throws IOException {
        byte[] buffer = new byte[4096];
        long remainingBytes = bytes;

        while (remainingBytes > 0) {
            long bytesToRead = Math.min(buffer.length, remainingBytes);
            int bytesRead = source.read(buffer, 0, (int)bytesToRead);

            sink.write(buffer, 0, bytesRead);

            remainingBytes -= bytesRead;
        }

        safelyClose(sink, "sink");
        safelyClose(source, "source");
    }

    private void safelyClose(Closeable closeableObject, String name) {
        try {
            closeableObject.close();
        } catch (Exception cause) {
            Logger.warn("Failed to close " + name, cause);
        }
    }

    public void sendData(char operation, String data) throws IOException {
        for (char dataPiece : data.toCharArray()) {
            writer.write(operation);
            writer.write(dataPiece);
        }

        writer.flush();
    }

    public void sendOperation(char operation, TerminalMessage message)
            throws IOException {
        writer.write(operation);
        message.writeTo(writer);
        writer.flush();
    }

    public void registerListener(Consumer<Character> listener) {
        this.listener = listener;

        listenerThread.start();
    }

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

    private void runListenerLoop() throws IOException {
        final int END_OF_STREAM_INDICATOR = -1;

        int character = reader.read();

        while (character != END_OF_STREAM_INDICATOR) {
            listener.accept((char)character);

            character = reader.read();
        }
    }
}
