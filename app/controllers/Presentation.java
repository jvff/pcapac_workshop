package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.Presentation.start;
import views.html.Presentation.slides.*;

public class Presentation extends Controller {
    public static Result start() {
        return ok(start.render());
    }

    public static Result slide(Integer number) {
        if (number == 0)
            return ok(title.render());
        else if (number == 1)
            return ok(thanks.render());
        else
            return notFound();
    }
}
