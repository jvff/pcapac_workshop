package controllers;

import play.mvc.Result;

import securesocial.core.java.UserAwareAction;

import views.html.intro_to_opencl.slides.*;

public class IntroductionToOpenCLPresentation extends Presentation {
    private static SlideHandler[] slides = new SlideHandler[] {
        new SlideHandler(title.class),
        new SlideHandler(thanks.class),
    };

    @UserAwareAction
    public static Result start() {
        return Presentation.start("Introduction to OpenCL", "intro_to_opencl");
    }

    public static Result slide(Integer number) {
        return Presentation.slide(slides, number);
    }
}
