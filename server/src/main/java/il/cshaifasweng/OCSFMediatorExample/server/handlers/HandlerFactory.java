package il.cshaifasweng.OCSFMediatorExample.server.handlers;


import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {
    private static HandlerFactory instance;
    private final Map<String, RequestHandler> handlers;

    private HandlerFactory() {
        handlers = new HashMap<>();
        handlers.put(RequestTypes.EMPTY_MESSAGE_REQUEST, new EmptyMessageHandler());
        handlers.put(RequestTypes.SHOW_ALL_MOVIES_REQUEST, new ShowAllMoviesHandler());
        handlers.put(RequestTypes.CHANGE_SCREENING_TIMES_REQUEST, new ChangeScreeningTimesHandler());
        handlers.put(RequestTypes.UPDATE_MOVIES_LIST_REQUEST, new UpdateMoviesListHandler());
        handlers.put(RequestTypes.GET_TIMESLOT_BY_MOVIEID_REQUEST, new GetTimeSlotByMovieID());
    }

    public static synchronized HandlerFactory getInstance() {
        if (instance == null) {
            instance = new HandlerFactory();
        }
        return instance;
    }

    public RequestHandler getHandlerForRequest(String request) {
        if (request.isBlank()) {
            return handlers.get(RequestTypes.EMPTY_MESSAGE_REQUEST);
        } else if (request.startsWith(RequestTypes.SHOW_ALL_MOVIES_REQUEST)) {
            return handlers.get(RequestTypes.SHOW_ALL_MOVIES_REQUEST);
        } else if (request.startsWith(RequestTypes.CHANGE_SCREENING_TIMES_REQUEST)) {
            return handlers.get(RequestTypes.CHANGE_SCREENING_TIMES_REQUEST);
        } else if (request.equals(RequestTypes.UPDATE_MOVIES_LIST_REQUEST)) {
            return handlers.get(RequestTypes.UPDATE_MOVIES_LIST_REQUEST);
        } else if (request.startsWith(RequestTypes.GET_TIMESLOT_BY_MOVIEID_REQUEST)) {
            return handlers.get(RequestTypes.GET_TIMESLOT_BY_MOVIEID_REQUEST);
        }
        return null;
    }
}
