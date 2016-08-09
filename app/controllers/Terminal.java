package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import actors.TerminalActor;

public class Terminal extends Controller {
    public static Result resize() {
        DynamicForm parameters = Form.form().bindFromRequest();

        System.err.println("Terminal resized: " + parameters.get("columns")
                + "x" + parameters.get("rows"));

        return ok();
    }

    public static WebSocket<String> socket() {
        return WebSocket.withActor(TerminalActor::props);
    }
}
