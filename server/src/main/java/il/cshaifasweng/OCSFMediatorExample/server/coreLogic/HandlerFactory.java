package il.cshaifasweng.OCSFMediatorExample.server.coreLogic;

import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers.*;

import java.util.HashMap;
import java.util.Map;

public class HandlerFactory {
    private static HandlerFactory instance;
    private final Map<String, RequestHandler> handlers;
    private SimpleServer server;

    private HandlerFactory(SimpleServer server) {
        this.server = server;
        handlers = new HashMap<>();
        initializeHandlers();
    }

    private void initializeHandlers() {
        handlers.put(RequestTypes.GET_COMPLAINTS,new ComplaintsHandler(server));
        handlers.put(RequestTypes.GET_PRICES,new PricesHandler());
        handlers.put(RequestTypes.EMPTY_MESSAGE_REQUEST, new EmptyMessageHandler());
        handlers.put(RequestTypes.MOVIES_REQUEST, new MovieRequestHandler(server));
        handlers.put(RequestTypes.CHANGE_SCREENING_TIMES_REQUEST, new ChangeScreeningTimesHandler());
        handlers.put(RequestTypes.UPDATE_MOVIES_LIST_REQUEST, new UpdateMoviesListHandler());
        handlers.put(RequestTypes.MOVIE_SLOT_INFORMATION, new MovieSlotInfoHandler());
        handlers.put(RequestTypes.GET_PURCHASE_REQUEST, new NewPurchaseHandler(server));
        handlers.put(RequestTypes.GET_MOVIE_TICKET_REQUEST, new NewPurchaseHandler(server));
        handlers.put(RequestTypes.GET_MOVIELINK_REQUEST, new NewPurchaseHandler(server));
        handlers.put(RequestTypes.CREATE_NEW_MOVIE, new ContentChangeHandler());
        handlers.put(RequestTypes.BRANCH_THEATER_INFORMATION, new BranchTheaterHandler());
        handlers.put(RequestTypes.GET_COMPLAINT_REQUEST, new ComplaintSubmissionHandler(server));
        handlers.put(RequestTypes.GET_EMPLOYEES, new EmployeesListHandler());
        handlers.put(RequestTypes.GET_CUSTOMER_INFO, new CustomerInfoHandler(server));
        handlers.put(RequestTypes.EMPLOYEE_INFORMATION, new EmployeeModificationHandler(server));
        handlers.put(RequestTypes.UPDATE_PURCHASE, new UpdatePurchaseHandler());
    }

    public static synchronized HandlerFactory getInstance(SimpleServer server) {
        if (instance == null) {
            instance = new HandlerFactory(server);
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
