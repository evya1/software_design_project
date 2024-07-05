package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.HandlerFactory;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SimpleServer extends AbstractServer {
    private static final ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

    public SimpleServer(int port) {
        super(port);
    }


    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        try {
            HandlerFactory factory = HandlerFactory.getInstance();
            RequestHandler handler = factory.getHandlerForRequest(request);

            if (handler != null) {
                System.out.println("yoyo");
                handler.handle(message, client);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);  // Call the superclass method if necessary
        SubscribersList.add(new SubscribedClient(client));
        System.out.println("Client added. Total clients: " + SubscribersList.size());
        Message message = new Message();
        message.setMessage("new client");
        sendToAllClients(message);
    }

    public void sendToAllClients(Message message) {
        try {
            for (SubscribedClient SubscribedClient : SubscribersList) {
                SubscribedClient.getClient().sendToClient(message);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
