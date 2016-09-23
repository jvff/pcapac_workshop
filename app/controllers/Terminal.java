package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Akka;
import play.libs.Json;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import actors.TerminalActor;
import actors.TerminalManagerActor;

import static java.util.UUID.randomUUID;

import static actors.TerminalManagerActor.ResizeTerminalMessage;

public class Terminal extends Controller {
    public static Result token() {
        ObjectNode result = Json.newObject();

        result.put("terminal_id", createTerminalId());

        return ok(result);
    }

    private static String createTerminalId() {
        return randomUUID().toString();
    }

    public static Result resize(String terminalId) {
        DynamicForm parameters = Form.form().bindFromRequest();
        String columns = parameters.get("columns");
        String rows = parameters.get("rows");

        try {
            resizeTerminal(terminalId, columns, rows);
        } catch (Exception cause) {
            String message = "Failed to resize terminal " + terminalId + " to "
                    + columns + "x" + rows;

            Logger.warn(message, cause);

            return internalServerError();
        }

        return ok();
    }

    private static void resizeTerminal(String terminalId, String columnsString,
            String rowsString) throws Exception {
        short columns = Short.parseShort(columnsString);
        short height = Short.parseShort(rowsString);

        resizeTerminal(terminalId, columns, height);
    }

    private static void resizeTerminal(String terminalId, short columns,
            short rows) {
        ActorSystem system = Akka.system();
        ActorRef terminalManager = system.actorFor(TerminalManagerActor.URL);
        ResizeTerminalMessage message = new ResizeTerminalMessage(terminalId,
                columns, rows);

        Logger.debug("resizeTerminal - terminal ID: " + terminalId);

        terminalManager.tell(message, null);
    }

    public static WebSocket<String> socket(String terminalId) {
        return WebSocket.withActor(TerminalActor.propsFor(terminalId));
    }
}
