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

import actors.terminal.TerminalActor;
import actors.terminal.TerminalManagerActor;

import static java.util.UUID.randomUUID;

import static actors.terminal.TerminalActor.ResizeMessage;
import static actors.terminal.TerminalActor.UploadFileMessage;
import static actors.terminal.TerminalManagerActor.SendTerminalMessage;

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
        ResizeMessage message = new ResizeMessage(columns, rows);

        forwardMessage(terminalId, message);
    }

    public static WebSocket<String> socket(String terminalId) {
        return WebSocket.withActor(TerminalActor.propsFor(terminalId));
    }

    public static Result upload(String terminalId, String path) {
        String fileContents = request().body().asText();

        uploadFile(terminalId, path, fileContents);

        return ok();
    }

    private static void uploadFile(String terminalId, String path,
            String contents) {
        UploadFileMessage message = new UploadFileMessage(path, contents);

        forwardMessage(terminalId, message);
    }

    private static void forwardMessage(String terminalId, Object message) {
        ActorSystem system = Akka.system();
        ActorRef terminalManager = system.actorFor(TerminalManagerActor.URL);
        SendTerminalMessage forwardRequest = new SendTerminalMessage(terminalId,
                message);

        Logger.debug("forwardMessage - terminal ID: " + terminalId);

        terminalManager.tell(forwardRequest, null);
    }
}
