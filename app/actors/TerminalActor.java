package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.Props;

public class TerminalActor extends UntypedActor {
    public static Props props(ActorRef out) {
        return Props.create(TerminalActor.class, out);
    }

    private final ActorRef out;

    public TerminalActor(ActorRef out) {
        this.out = out;

        System.err.println("New TerminalActor");

        out.tell(" $", self());
    }

    @Override
    public void onReceive(Object message) {
        System.err.print("Received message: ");

        if (message instanceof String)
            System.err.println(message);
        else
            System.err.println("?");
    }
}
