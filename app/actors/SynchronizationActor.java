package actors;

import java.util.LinkedList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;

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
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof NewViewerMessage)
            handleMessage((NewViewerMessage)message);
        else if (message instanceof JsonNode)
            sendCommandToViewers((JsonNode)message);
    }

    private void handleMessage(NewViewerMessage message) {
        ActorRef viewer = message.getViewer();

        viewers.add(viewer);

        viewer.tell(new SynchronizerConnectionMessage(), self());
    }

    private void sendCommandToViewers(JsonNode command) {
        CommandMessage message = new CommandMessage(command);

        for (ActorRef viewer : viewers)
            viewer.tell(message, self());
    }
}
