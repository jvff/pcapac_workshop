package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.F;

import actors.PresentationManagerActor.NewPresentationViewerMessage;

public class ViewerActor extends UntypedActor {
    public static class CommandMessage {
        private final JsonNode command;

        public CommandMessage(JsonNode command) {
            this.command = command;
        }

        public JsonNode getCommand() {
            return command;
        }
    }

    public static class SynchronizerConnectionMessage {
    }

    public static F.Function<ActorRef, Props> propsFor(
            final String presentationId, final boolean authorizedToLead) {
        return (ActorRef out) -> props(out, presentationId, authorizedToLead);
    }

    public static Props props(ActorRef out, String presentationId,
            boolean authorizedToLead) {
        return Props.create(ViewerActor.class, out, presentationId,
                authorizedToLead);
    }

    private final ActorRef out;

    private ActorRef synchronizer;
    private JsonNode currentSlidePosition;
    private String presentationId;
    private boolean authorizedToLead = false;

    public ViewerActor(ActorRef out, String presentationId,
            boolean authorizedToLead) {
        this.out = out;
        this.presentationId = presentationId;
        this.authorizedToLead = authorizedToLead;

        registerViewer();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof CommandMessage)
            handleCommand((CommandMessage)message);
        else if (message instanceof SynchronizerConnectionMessage)
            synchronizer = sender();
        else if (message instanceof JsonNode)
            handleMessage((JsonNode)message);
    }

    private void handleCommand(CommandMessage message) {
        currentSlidePosition = message.getCommand();
        sendSlidePosition();
    }

    private void sendSlidePosition() {
        if (currentSlidePosition != null)
            out.tell(currentSlidePosition, self());
    }

    private void handleMessage(JsonNode message) {
        if (requestsSlidePosition(message))
            sendSlidePosition();
        else if (authorizedToLead)
            notifySynchronizer((JsonNode)message);
    }

    private boolean requestsSlidePosition(JsonNode message) {
        JsonNode syncField = message.get("sync");

        return syncField != null
            && syncField.isValueNode()
            && syncField.booleanValue() == true;
    }

    private void notifySynchronizer(JsonNode message) {
        synchronizer.tell(message, self());
    }

    private void registerViewer() {
        ActorRef manager = getPresentationManager();
        NewPresentationViewerMessage message =
                new NewPresentationViewerMessage(presentationId);

        manager.tell(message, self());
    }

    private ActorRef getPresentationManager() {
        ActorSystem system = getContext().system();

        return system.actorFor(PresentationManagerActor.URL);
    }
}
