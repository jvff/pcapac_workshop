package controllers;

import java.lang.reflect.Method;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.Routes;
import play.twirl.api.Html;
import play.twirl.api.JavaScript;

import views.html.Presentation.start;
import views.html.Presentation.slides.*;

import actors.ViewerActor;

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
            new SlideHandler(thanks.class)
        };
    }

    public static Result start() {
        return ok(start.render());
    }

    public static Result slide(Integer number) {
        if (number >= 0 && number < slides.length)
            return slides[number].render();
        else
            return notFound();
    }

    public static Result jsRoutes() {
        response().setContentType("text/javascript");

        JavaScript router = Routes.javascriptRouter("jsRoutes",
                routes.javascript.Presentation.synchronizationSocket());

        return ok(router);
    }

    public static WebSocket<String> synchronizationSocket() {
        return WebSocket.withActor(ViewerActor::props);
    }
}
