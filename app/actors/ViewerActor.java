package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.actor.Props;

public class ViewerActor extends UntypedActor {
    public static class CommandMessage {
        private final String command;

        public CommandMessage(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

    public static Props props(ActorRef out) {
        return Props.create(ViewerActor.class, out);
    }

    private final ActorRef out;
    private final ActorRef synchronizer;

    public ViewerActor(ActorRef out) {
        this.out = out;

        synchronizer = getSynchronizer();

        registerViewer();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof CommandMessage)
            handleCommand((CommandMessage)message);
        else
            notifySynchronizer((String)message);
    }

    private void handleCommand(CommandMessage message) {
        out.tell(message.getCommand(), self());
    }

    private void notifySynchronizer(String message) {
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
