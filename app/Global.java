import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

import akka.actor.ActorSystem;

import actors.PresentationManagerActor;
import actors.terminal.TerminalManagerActor;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        ActorSystem system = Akka.system();

        createTerminalManager(system);
        createPresentationManager(system);
    }

    private void createTerminalManager(ActorSystem system) {
        String actorName = TerminalManagerActor.NAME;

        system.actorOf(TerminalManagerActor.props(), actorName);
    }

    private void createPresentationManager(ActorSystem system) {
        String actorName = PresentationManagerActor.NAME;

        system.actorOf(PresentationManagerActor.props(), actorName);
    }
}
