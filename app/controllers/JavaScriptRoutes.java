package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.Routes;

import play.twirl.api.JavaScript;

public class JavaScriptRoutes extends Controller {
    public static Result jsRoutes() {
        response().setContentType("text/javascript");

        JavaScript router = Routes.javascriptRouter("jsRoutes",
                routes.javascript.Presentations.slide(),
                routes.javascript.Presentations.start(),
                routes.javascript.Presentations.synchronizationSocket(),
                routes.javascript.Terminal.token(),
                routes.javascript.Terminal.resize(),
                routes.javascript.Terminal.socket(),
                routes.javascript.Terminal.upload());

        return ok(router);
    }
}
