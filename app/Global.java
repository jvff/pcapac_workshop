import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

import akka.actor.ActorSystem;

import securesocial.core.RuntimeEnvironment;

import actors.SynchronizationActor;

public class Global extends GlobalSettings {
    private RuntimeEnvironment environment = new services.Environment();

    @Override
    public void onStart(Application application) {
        ActorSystem system = Akka.system();

        system.actorOf(SynchronizationActor.props(), SynchronizationActor.NAME);
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass)
            throws Exception {
        try {
            return controllerClass
                .getDeclaredConstructor(RuntimeEnvironment.class)
                .newInstance(environment);
        } catch (NoSuchMethodException exception) {
            return super.getControllerInstance(controllerClass);
        }
    }
}
