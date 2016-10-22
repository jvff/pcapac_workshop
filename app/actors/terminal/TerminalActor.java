package actors.terminal;

import java.io.File;
import java.io.IOException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;

import play.libs.Akka;
import play.libs.F;
import play.Logger;

import actors.terminal.TerminalManagerActor.NewTerminalMessage;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TerminalActor extends UntypedActor {
    private static final char CMD_KEY = 'k';
    private static final char CMD_RESIZE = 'r';
    private static final char CMD_UPLOAD = 'u';

    public static Props props(ActorRef out, String sessionId) {
        return Props.create(TerminalActor.class, out, sessionId);
    }

    public static F.Function<ActorRef, Props> propsFor(final String sessionId) {
        return (ActorRef out) -> props(out, sessionId);
    }

    private final ActorRef out;
    private TerminalContainer container;

    public TerminalActor(ActorRef out, String sessionId) {
        this.out = out;

        if (tryToStartContainer())
            notifyInstantiationToTerminalManager(sessionId);
    }

    private boolean tryToStartContainer() {
        try {
            startContainer();

            return true;
        } catch (IOException cause) {
            Logger.error("Failed to start container", cause);

            self().tell(PoisonPill.getInstance(), self());

            return false;
        }
    }

    private void startContainer() throws IOException {
        container = new TerminalContainer();

        container.registerListener(character -> sendCharacter(character));
    }

    private void sendCharacter(char character) {
        String message = Character.toString(character);

        out.tell(message, self());
    }

    private void notifyInstantiationToTerminalManager(String sessionId) {
        ActorSystem system = Akka.system();
        ActorRef manager = system.actorFor(TerminalManagerActor.URL);

        manager.tell(new NewTerminalMessage(sessionId), self());
    }

    @Override
    public void onReceive(Object message) {
        try {
            if (container != null)
                unsafelyHandleMessage(message);
        } catch (IOException exception) {
            Logger.error("Failed to handle message: " + message);
        }
    }

    private void unsafelyHandleMessage(Object message) throws IOException {
        if (message instanceof String)
            container.sendData(CMD_KEY, (String)message);
        else if (message instanceof ResizeTerminalMessage)
            container.sendOperation(CMD_RESIZE, (TerminalMessage)message);
        else if (message instanceof UploadFileMessage)
            container.sendOperation(CMD_UPLOAD, (TerminalMessage)message);
        else if (message instanceof DownloadFileMessage)
            handleDownloadFileMessage((DownloadFileMessage)message);
    }

    private void handleDownloadFileMessage(DownloadFileMessage message) {
        String path = message.getPath();

        try {
            File file = container.getFile(path);
            Logger.debug("Got file: " + file);

            sender().tell(file, self());
        } catch (Exception cause) {
            String errorMessage =
                    "Failed to download file from container: " + path;

            Logger.error(errorMessage, cause);
        }
    }
}
