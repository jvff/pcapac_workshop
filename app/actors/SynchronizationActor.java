package actors;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class SynchronizationActor extends UntypedActor {
    public static Props props(ActorRef out) {
        return Props.create(SynchronizationActor.class, out);
    }

    private final ActorRef out;

    public SynchronizationActor(ActorRef out) {
        this.out = out;

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    out.tell("0", self());
                    Thread.sleep(1000);
                    out.tell("1", self());
                    Thread.sleep(1000);
                    out.tell("2", self());
                } catch (Exception ex) {
                    // Do nothing
                }

                self().tell(PoisonPill.getInstance(), self());
            }
        }.start();
    }

    @Override
    public void onReceive(Object message) {
    }
}
