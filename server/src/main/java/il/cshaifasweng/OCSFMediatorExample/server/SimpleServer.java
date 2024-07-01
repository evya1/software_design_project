package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import il.cshaifasweng.OCSFMediatorExample.server.handlers.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleServer extends AbstractServer {
    private static final ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

    private static final String EMPTY_MESSAGE_REQUEST = "blank";
    private static final String SHOW_ALL_MOVIES_REQUEST = "show all movies";
    private static final String CHANGE_SCREENING_TIMES_REQUEST = "change screening times of the movie";
    private static final String UPDATE_MOVIES_LIST_REQUEST = "update movies list";
    private static final String GET_TIMESLOT_BY_MOVIEID_REQUEST = "get movie slot by movie ID";

    private final Map<String, RequestHandler> handlers = new HashMap<>();

    public SimpleServer(int port) {
        super(port);
        initializeHandlers();
    }

    private void initializeHandlers() {
        handlers.put(EMPTY_MESSAGE_REQUEST, new EmptyMessageHandler());
        handlers.put(SHOW_ALL_MOVIES_REQUEST, new ShowAllMoviesHandler());
        handlers.put(CHANGE_SCREENING_TIMES_REQUEST, new ChangeScreeningTimesHandler());
        handlers.put(UPDATE_MOVIES_LIST_REQUEST, new UpdateMoviesListHandler());
        handlers.put(GET_TIMESLOT_BY_MOVIEID_REQUEST, new GetTimeSlotByMovieID());
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        MessageObject message = (MessageObject) msg;
        String request = message.getMsg();

        try {
            RequestHandler handler = null;

            if (request.isBlank()) {
                handler = handlers.get(EMPTY_MESSAGE_REQUEST);
            } else if (request.startsWith(SHOW_ALL_MOVIES_REQUEST)) {
                handler = handlers.get(SHOW_ALL_MOVIES_REQUEST);
            } else if (request.startsWith(CHANGE_SCREENING_TIMES_REQUEST)) {
                System.out.println("roro");
                handler = handlers.get(CHANGE_SCREENING_TIMES_REQUEST);
            } else if (request.equals(UPDATE_MOVIES_LIST_REQUEST)) {
                handler = handlers.get(UPDATE_MOVIES_LIST_REQUEST);
            } else if (request.startsWith(GET_TIMESLOT_BY_MOVIEID_REQUEST)) {
                handler = handlers.get(GET_TIMESLOT_BY_MOVIEID_REQUEST);
            }

            if (handler != null) {
                System.out.println("yoyo");
                handler.handle(message, client);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
