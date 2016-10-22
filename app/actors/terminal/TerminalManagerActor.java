package actors.terminal;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import play.Logger;

public class TerminalManagerActor extends UntypedActor {
    public final static String NAME = "terminal_manager";
    public final static String URL = "akka://application/user/" + NAME;

    private static abstract class TerminalMessage {
        private final String terminalId;

        public TerminalMessage(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getTerminalId() {
            return terminalId;
        }
    }

    public static class NewTerminalMessage extends TerminalMessage {
        public NewTerminalMessage(String terminalId) {
            super(terminalId);
        }
    }

    public static class SendTerminalMessage extends TerminalMessage {
        private final Object message;

        public SendTerminalMessage(String terminalId, Object message) {
            super(terminalId);

            this.message = message;
        }

        public void forwardTo(ActorRef terminalHandler, ActorRef sender) {
            terminalHandler.tell(message, sender);
        }
    }

    public static Props props() {
        return Props.create(TerminalManagerActor.class);
    }

    private HashMap<String, ActorRef> terminals;

    public TerminalManagerActor() {
        terminals = new HashMap<String, ActorRef>();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof NewTerminalMessage)
            handleNewTerminal((NewTerminalMessage)message);
        else if (message instanceof SendTerminalMessage)
            handleMessageToTerminal((SendTerminalMessage)message);
    }

    private void handleNewTerminal(NewTerminalMessage message) {
        String terminalId = message.getTerminalId();

        terminals.put(terminalId, sender());
    }

    private void handleMessageToTerminal(SendTerminalMessage message) {
        String terminalId = message.getTerminalId();
        ActorRef terminalHandler = terminals.get(terminalId);

        if (terminalHandler != null)
            message.forwardTo(terminalHandler, sender());
    }
}
