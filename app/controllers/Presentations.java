package controllers;

import java.io.InputStream;
import java.io.IOException;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

import securesocial.core.java.UserAwareAction;

import actors.ViewerActor;
import controllers.presentation.Presentation;
import controllers.presentation.PresentationCache;
import controllers.presentation.SlideHandler;
import models.User;
import views.html.presentation.start;

import static securesocial.core.java.SecureSocial.USER_KEY;

public class Presentations extends Controller {
    private static PresentationCache presentations = new PresentationCache();

    @UserAwareAction
    public static Result start(String presentationName) {
        Presentation presentation = presentations.get(presentationName);

        configureUserSession();

        if (presentation == null)
            return notFound();
        else
            return ok(start.render(presentation));
    }

    private static void configureUserSession() {
        User user = (User)ctx().args.get(USER_KEY);

        if (user != null)
            session("user", "presenter");
        else
            session("user", "guest");
    }

    public static Result slide(String presentation, Integer number) {
        SlideHandler[] slides = getSlideHandlersFor(presentation);

        if (number >= 0 && number < slides.length)
            return slides[number].render();
        else
            return notFound();
    }

    private static SlideHandler[] getSlideHandlersFor(String presentationName) {
        Presentation presentation = presentations.get(presentationName);

        if (presentation == null)
            return new SlideHandler[0];
        else
            return presentation.getSlideHandlers();
    }

    public static Result figure(String presentation, String figurePath) {
        InputStream figure = getFigure(presentation, figurePath);

        setContentTypeForPath(figurePath);

        if (figure != null)
            return ok(figure);
        else
            return notFound();
    }

    private static InputStream getFigure(String presentationName,
            String figurePath) {
        Presentation presentation = presentations.get(presentationName);

        if (presentation == null)
            return null;
        else
            return tryToGetFigureFrom(presentation, figurePath);
    }

    private static InputStream tryToGetFigureFrom(Presentation presentation,
            String figurePath) {
        try {
            return presentation.getFigure(figurePath);
        } catch (IOException cause) {
            Logger.warn("Failed to get figure " + figurePath + " for "
                    + "presentation " + presentation.getName());

            return null;
        }
    }

    private static void setContentTypeForPath(String path) {
        String extension = getExtensionOf(path);
        String contentType = getContentTypeFor(extension);

        response().setContentType(contentType);
    }

    private static String getExtensionOf(String path) {
        int lastDot = path.lastIndexOf('.');

        if (lastDot < 0)
            return "";
        else
            return path.substring(lastDot + 1).toLowerCase();
    }

    private static String getContentTypeFor(String extension) {
        if (extension.equals("svg"))
            return "image/svg+xml";
        else if (isImageExtension(extension))
            return "image/" + extension;
        else
            return "application/octet-stream";
    }

    private static boolean isImageExtension(String extension) {
        return extension.equals("png")
            || extension.equals("bmp")
            || extension.equals("gif")
            || extension.equals("jpg")
            || extension.equals("jpeg");
    }

    public static WebSocket<JsonNode> synchronizationSocket(
            String presentationId) {
        String user = session("user");
        boolean authorizedToLead = user != null && user.equals("presenter");

        return WebSocket.withActor(
                ViewerActor.propsFor(presentationId, authorizedToLead));
    }
}
