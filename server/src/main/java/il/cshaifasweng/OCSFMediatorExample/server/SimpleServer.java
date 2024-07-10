package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.HandlerFactory;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleServer extends AbstractServer {
    private static final ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
    private final HandlerFactory handlerFactory;

    public SimpleServer(int port) {
        super(port);
        this.handlerFactory = HandlerFactory.getInstance(this);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        String request = message.getMessage();
        try {
            RequestHandler handler = handlerFactory.getHandlerForRequest(request);

            if (handler != null) {
                System.out.println("LOG: new message received from client " + message.getMessage());
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

    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        // Find the corresponding SubscribedClient and remove it from the list
        SubscribersList.removeIf(subscribedClient -> subscribedClient.getClient().equals(client));
        System.out.println("Client removed. Total clients: " + SubscribersList.size());
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
