package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.F;

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
    private final ActorRef synchronizer;
    private String presentationId;
    private boolean authorizedToLead = false;

    public ViewerActor(ActorRef out, String presentationId,
            boolean authorizedToLead) {
        this.out = out;
        this.presentationId = presentationId;
        this.authorizedToLead = authorizedToLead;

        synchronizer = getSynchronizer();

        registerViewer();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof CommandMessage)
            handleCommand((CommandMessage)message);
        else if (authorizedToLead)
            notifySynchronizer((JsonNode)message);
    }

    private void handleCommand(CommandMessage message) {
        out.tell(message.getCommand(), self());
    }

    private void notifySynchronizer(JsonNode message) {
        synchronizer.tell(message, self());
    }

    private void registerViewer() {
        synchronizer.tell("new viewer", self());
    }

    private ActorRef getSynchronizer() {
        ActorSystem system = getContext().system();

        return system.actorFor(SynchronizationActor.URL);
    }
}
