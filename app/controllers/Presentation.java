package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.Presentation.start;

public class Presentation extends Controller {
    public static Result start() {
        return ok(start.render());
    }
}
