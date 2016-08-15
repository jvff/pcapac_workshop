package controllers;

import java.lang.reflect.Method;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.twirl.api.Html;

import securesocial.core.java.UserAwareAction;

import views.html.Presentation.start;
import views.html.Presentation.slides.*;

import actors.PresenterActor;
import actors.ViewerActor;

import models.User;

import static securesocial.core.java.SecureSocial.USER_KEY;

public class Presentation extends Controller {
    private static class SlideHandler {
        private Method renderMethod;

        SlideHandler(Class slideClass) {
            try {
                renderMethod = slideClass.getDeclaredMethod("render");
            } catch (NoSuchMethodException exception) {
                renderMethod = null;
            }
        }

        Result render() {
            if (renderMethod == null)
                return internalServerError();

            try {
                return ok((Html)renderMethod.invoke(null));
            } catch (Exception exception) {
                return internalServerError();
            }
        }
    }

    private static SlideHandler[] slides;

    static {
        slides = new SlideHandler[] {
            new SlideHandler(title.class),
            new SlideHandler(agenda.class),
            new SlideHandler(introduction.class),
            new SlideHandler(motivation.class),
            new SlideHandler(solution.class),
            new SlideHandler(version_control_system.class),
            new SlideHandler(git.class),
            new SlideHandler(installation.class),
            new SlideHandler(basic_concepts.class),
            new SlideHandler(managing_the_directory.class),
            new SlideHandler(terminal.class),
            new SlideHandler(thanks.class)
        };
    }

    @UserAwareAction
    public static Result start() {
        User user = (User)ctx().args.get(USER_KEY);

        if (user != null)
            session("user", "presenter");
        else
            session("user", "guest");

        return ok(start.render());
    }

    public static Result slide(Integer number) {
        if (number >= 0 && number < slides.length)
            return slides[number].render();
        else
            return notFound();
    }

    public static WebSocket<String> synchronizationSocket() {
        String user = session("user");

        if (user == null || !user.equals("presenter"))
            return WebSocket.withActor(ViewerActor::props);
        else
            return WebSocket.withActor(PresenterActor::props);
    }
}
