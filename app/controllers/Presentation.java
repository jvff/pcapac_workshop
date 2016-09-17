package controllers;

import java.lang.reflect.Method;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.twirl.api.Html;

import com.fasterxml.jackson.databind.JsonNode;

import securesocial.core.java.UserAwareAction;

import views.html.presentation.start;

import actors.ViewerActor;

import models.User;

import static securesocial.core.java.SecureSocial.USER_KEY;

public class Presentation extends Controller {
    protected static class SlideHandler {
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

    @UserAwareAction
    public static Result start(String title, String route) {
        User user = (User)ctx().args.get(USER_KEY);

        if (user != null)
            session("user", "presenter");
        else
            session("user", "guest");

        return ok(start.render(title, route));
    }

    public static Result slide(SlideHandler[] slides, Integer number) {
        if (number >= 0 && number < slides.length)
            return slides[number].render();
        else
            return notFound();
    }

    public static WebSocket<JsonNode> synchronizationSocket(
            String presentationId) {
        String user = session("user");
        boolean authorizedToLead = user != null && user.equals("presenter");

        return WebSocket.withActor(
                ViewerActor.propsFor(presentationId, authorizedToLead));
    }
}
