import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

import akka.actor.ActorSystem;

import actors.SynchronizationActor;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        ActorSystem system = Akka.system();

        system.actorOf(SynchronizationActor.props(), SynchronizationActor.NAME);
    }
}
