package actors;

import java.util.LinkedList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;

import actors.PresentationManagerActor.NewPresentationMessage;
import actors.ViewerActor.CommandMessage;
import actors.ViewerActor.SynchronizerConnectionMessage;

public class SynchronizationActor extends UntypedActor {
    public static class NewViewerMessage {
        private final ActorRef viewer;

        public NewViewerMessage(ActorRef viewer) {
            this.viewer = viewer;
        }

        public ActorRef getViewer() {
            return viewer;
        }
    }

    public static Props props(String presentationId) {
        return Props.create(SynchronizationActor.class, presentationId);
    }

    private LinkedList<ActorRef> viewers;
    private String presentationId;

    public SynchronizationActor(String presentationId) {
        this.presentationId = presentationId;

        viewers = new LinkedList<ActorRef>();

        registerPresentation();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof NewViewerMessage)
            handleMessage((NewViewerMessage)message);
        else if (message instanceof JsonNode)
            handleMessage((JsonNode)message);
    }

    private void handleMessage(NewViewerMessage message) {
        ActorRef viewer = message.getViewer();

        viewers.add(viewer);

        viewer.tell(new SynchronizerConnectionMessage(), self());
    }

    private void handleMessage(JsonNode message) {
        if (containsSyncData(message))
            sendCommandToViewers(message);
    }

    private boolean containsSyncData(JsonNode message) {
        JsonNode slideField = message.get("slide");
        JsonNode stepField = message.get("step");

        return slideField != null && slideField.isInt()
            && stepField != null && stepField.isInt();
    }

    private void sendCommandToViewers(JsonNode command) {
        CommandMessage message = new CommandMessage(command);

        for (ActorRef viewer : viewers)
            viewer.tell(message, self());
    }

    private void registerPresentation() {
        ActorRef manager = getPresentationManager();
        NewPresentationMessage message =
                new NewPresentationMessage(presentationId);

        System.err.println("Registering presentation: " + presentationId);
        manager.tell(message, self());
    }

    private ActorRef getPresentationManager() {
        ActorSystem system = getContext().system();

        return system.actorFor(PresentationManagerActor.URL);
    }
}
