package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

public class EmptyMessageHandler implements RequestHandler {
    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        message.setMessage("Error! we got an empty message");
        client.sendToClient(message);
        System.out.println("Sent empty message");
    }
}
