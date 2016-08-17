package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;

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

    public static Props props(ActorRef out) {
        return Props.create(ViewerActor.class, out);
    }

    private final ActorRef out;
    private final ActorRef synchronizer;
    private boolean authorizedToLead = false;

    public ViewerActor(ActorRef out) {
        this.out = out;

        synchronizer = getSynchronizer();

        registerViewer();
    }

    public ViewerActor(ActorRef out, boolean authorizedToLead) {
        this(out);

        this.authorizedToLead = authorizedToLead;
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
