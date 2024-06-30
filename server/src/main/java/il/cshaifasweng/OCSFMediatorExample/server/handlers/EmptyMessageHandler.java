package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

public class EmptyMessageHandler implements RequestHandler {
    @Override
    public void handle(MessageObject message, ConnectionToClient client) throws IOException {
        message.setMsg("Error! we got an empty message");
        client.sendToClient(message);
        System.out.println("Sent empty message");
    }
}
