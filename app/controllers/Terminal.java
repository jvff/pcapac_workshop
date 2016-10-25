package controllers;

import java.io.File;
import java.util.Arrays;

import scala.concurrent.Await;
import scala.concurrent.Future;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Akka;
import play.libs.Json;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import actors.terminal.ResizeTerminalMessage;
import actors.terminal.TerminalActor;
import actors.terminal.TerminalManagerActor;
import actors.terminal.DownloadFileMessage;
import actors.terminal.UploadFileMessage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.UUID.randomUUID;

import static akka.pattern.Patterns.ask;

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
        ResizeTerminalMessage message =
                new ResizeTerminalMessage(columns, rows);

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

    public static Result download(String terminalId, String path) {
        File downloadedFile = downloadFile(terminalId, "/home/user/" + path);

        if (downloadedFile == null)
            return notFound();
        else
            return ok(downloadedFile);
    }

    private static File downloadFile(String terminalId, String path) {
        Future<Object> reply = requestToDownloadFile(terminalId, path);
        Timeout timeout = new Timeout(1, SECONDS);

        try {
            return (File) Await.result(reply, timeout.duration());
        } catch (Exception cause) {
            Logger.warn("Failed to download file: " + path, cause);

            return null;
        }
    }

    private static Future<Object> requestToDownloadFile(
            String terminalId, String path) {
        DownloadFileMessage message = new DownloadFileMessage(path);

        return forwardRequestMessage(terminalId, message);
    }

    private static Future<Object> forwardRequestMessage(String terminalId,
            Object message) {
        ActorSystem system = Akka.system();
        ActorRef terminalManager = system.actorFor(TerminalManagerActor.URL);
        SendTerminalMessage forwardRequest = new SendTerminalMessage(terminalId,
                message);
        Timeout timeout = new Timeout(2, SECONDS);

        Logger.debug("forwardRequestMessage - terminal ID: " + terminalId);

        return ask(terminalManager, forwardRequest, timeout);
    }
}
