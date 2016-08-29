package actors;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import actors.SynchronizationActor.NewViewerMessage;

public class PresentationManagerActor extends UntypedActor {
    public final static String NAME = "presentation_manager";
    public final static String URL = "akka://application/user/" + NAME;

    public static class NewPresentationMessage {
        private final String presentationId;

        public NewPresentationMessage(String presentationId) {
            this.presentationId = presentationId;
        }

        public String getPresentationId() {
            return presentationId;
        }
    }

    public static class NewPresentationViewerMessage {
        private final String presentationId;

        public NewPresentationViewerMessage(String presentationId) {
            this.presentationId = presentationId;
        }

        public String getPresentationId() {
            return presentationId;
        }
    }

    public static Props props() {
        return Props.create(PresentationManagerActor.class);
    }

    private HashMap<String, ActorRef> presentations;

    public PresentationManagerActor() {
        presentations = new HashMap<String, ActorRef>();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof NewPresentationMessage)
            handleNewPresentation((NewPresentationMessage)message);
        else if (message instanceof NewPresentationViewerMessage)
            handleNewViewer((NewPresentationViewerMessage)message);
    }

    private void handleNewPresentation(NewPresentationMessage message) {
        String presentationId = message.getPresentationId();

        presentations.put(presentationId, sender());
    }

    private void handleNewViewer(NewPresentationViewerMessage message) {
        String presentationId = message.getPresentationId();
        ActorRef presentation = presentations.get(presentationId);

        if (presentation != null)
            presentation.tell(new NewViewerMessage(sender()), self());
    }
}
