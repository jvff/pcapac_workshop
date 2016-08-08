package actors;

import akka.actor.ActorRef;
import akka.actor.Props;

public class PresenterActor extends ViewerActor {
    public static Props props(ActorRef out) {
        return Props.create(PresenterActor.class, out);
    }

    public PresenterActor(ActorRef out) {
        super(out, true);
    }
}
