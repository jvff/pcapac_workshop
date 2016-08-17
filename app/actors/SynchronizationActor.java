package actors;

import java.util.LinkedList;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;

import actors.ViewerActor.CommandMessage;

public class SynchronizationActor extends UntypedActor {
    public final static String NAME = "synchronizer";
    public final static String URL = "akka://application/user/" + NAME;

    public static Props props() {
        return Props.create(SynchronizationActor.class);
    }

    private LinkedList<ActorRef> viewers;

    public SynchronizationActor() {
        viewers = new LinkedList<ActorRef>();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String)
            handleMessage((String)message);
        else if (message instanceof JsonNode)
            sendCommandToViewers((JsonNode)message);
    }

    private void handleMessage(String message) {
        if (message.equals("new viewer"))
            viewers.add(sender());
    }

    private void sendCommandToViewers(JsonNode command) {
        CommandMessage message = new CommandMessage(command);

        for (ActorRef viewer : viewers)
            viewer.tell(message, self());
    }
}
