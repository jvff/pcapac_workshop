package controllers.presentation;

import java.lang.reflect.Method;

import play.mvc.Result;
import play.twirl.api.Html;

import controllers.Presentations;

public class SlideHandler {
    private Class<?> slideClass;
    private Method renderMethod;

    public SlideHandler(String presentation, String slide)
            throws ClassNotFoundException, NoSuchMethodException {
        loadSlideClass(presentation, slide);
        loadRenderMethod();
    }

    private void loadSlideClass(String presentation, String slide)
            throws ClassNotFoundException {
        String className = "views.html." + presentation + ".slides." + slide;

        slideClass = Class.forName(className);
    }

    private void loadRenderMethod() throws NoSuchMethodException {
        renderMethod = slideClass.getDeclaredMethod("render");
    }

    public Result render() {
        if (renderMethod == null)
            return internalServerError();

        try {
            return ok((Html)renderMethod.invoke(null));
        } catch (Exception exception) {
            return internalServerError();
        }
    }

    private Result ok(Html body) {
        return Presentations.ok(body);
    }

    private Result internalServerError() {
        return Presentations.internalServerError();
    }
}
