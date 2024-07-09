package il.cshaifasweng.OCSFMediatorExample.server.coreLogic;


import il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers.*;

import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {
    private static HandlerFactory instance;
    private final Map<String, RequestHandler> handlers;

    private HandlerFactory() {
        handlers = new HashMap<>();
        initializeHandlers();
    }

    private void initializeHandlers() {
        handlers.put(RequestTypes.EMPTY_MESSAGE_REQUEST, new EmptyMessageHandler());
        handlers.put(RequestTypes.SHOW_ALL_MOVIES_REQUEST, new ShowAllMoviesHandler());
        handlers.put(RequestTypes.CHANGE_SCREENING_TIMES_REQUEST, new ChangeScreeningTimesHandler());
        handlers.put(RequestTypes.UPDATE_MOVIES_LIST_REQUEST, new UpdateMoviesListHandler());
        handlers.put(RequestTypes.GET_TIMESLOT_BY_MOVIEID_REQUEST, new GetTimeSlotByMovieID());
        handlers.put(RequestTypes.GET_PURCHASE_REQUEST, new NewPurchaseHandler());
    }

    public static synchronized HandlerFactory getInstance() {
        if (instance == null) {
            instance = new HandlerFactory();
        }
        return instance;
    }

    public RequestHandler getHandlerForRequest(String request) {
        if (request.isBlank() || request.startsWith(RequestTypes.EMPTY_MESSAGE_REQUEST)) {
            return handlers.get(RequestTypes.EMPTY_MESSAGE_REQUEST);
        }

        for (String requestType : handlers.keySet()) {
            if (request.startsWith(requestType)) {
                return handlers.get(requestType);
            }
        }
        return null;
    }
}
