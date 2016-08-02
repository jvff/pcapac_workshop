package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.Presentation.start;
import views.html.Presentation.slides.thanks;

public class Presentation extends Controller {
    public static Result start() {
        return ok(start.render());
    }

    public static Result slide(Integer number) {
        return ok(thanks.render());
    }
}
