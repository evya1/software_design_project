package il.cshaifasweng.OCSFMediatorExample.server.coreLogic;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportService;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers.*;

import java.util.HashMap;
import java.util.Map;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.*;

public class HandlerFactory {
    private static HandlerFactory instance;
    private final Map<String, RequestHandler> handlers;
    private SimpleServer server;
    private ReportService reportService;

    public HandlerFactory(SimpleServer server, ReportService reportService) {
        this.server = server;
        this.reportService = reportService;
        System.out.println("HandlerFactory initialized with reportService: " + reportService);
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
        handlers.put(RequestTypes.CREATE_NEW_MOVIE, new ContentChangeHandler(server));
        handlers.put(RequestTypes.BRANCH_THEATER_INFORMATION, new BranchTheaterHandler());
        handlers.put(RequestTypes.GET_COMPLAINT_REQUEST, new ComplaintSubmissionHandler(server));
        handlers.put(RequestTypes.GET_EMPLOYEES, new EmployeesListHandler());
        handlers.put(RequestTypes.GET_CUSTOMER_INFO, new CustomerInfoHandler(server));
        handlers.put(RequestTypes.EMPLOYEE_INFORMATION, new EmployeeModificationHandler(server));
        handlers.put(RequestTypes.UPDATE_PURCHASE, new UpdatePurchaseHandler());
        // Register report request handlers
        handlers.put(FETCH_DAILY_REPORTS, new ReportsRequestHandler(reportService));
        handlers.put(FETCH_MONTHLY_REPORTS, new ReportsRequestHandler(reportService));
        handlers.put(FETCH_LAST_QUARTER_REPORT, new ReportsRequestHandler(reportService));
        handlers.put(FETCH_ALL_REPORTS, new ReportsRequestHandler(reportService));
        handlers.put(FETCH_PURCHASES_AND_COMPLAINTS, new ReportsRequestHandler(reportService));
    }

    public static synchronized HandlerFactory getInstance(SimpleServer server, ReportService reportService) {
        if (instance == null) {
            if (reportService == null) {
                throw new IllegalArgumentException("ReportService cannot be null");
            }
            System.out.println("Creating HandlerFactory instance with ReportService");
            instance = new HandlerFactory(server, reportService);
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
