package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.MessageUtilForReports;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.RequestData;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.REPORT_DATA_RESPONSE;

/**
 * Handles sending and receiving of report-related requests and responses between the client and the server.
 */
public class ReportsRequestHandler {

    private static ReportsRequestHandler instance;
    private SimpleClient client;

    // Private constructor to prevent instantiation
    private ReportsRequestHandler() {
    }

    // Get the singleton instance
    public static synchronized ReportsRequestHandler getInstance() {
        if (instance == null) {
            instance = new ReportsRequestHandler();
        }
        return instance;
    }

    // Initialize with the client (set the SimpleClient instance)
    public void initialize(SimpleClient client) {
        if (this.client == null) {
            this.client = client;
        }
    }

    /**
     * Sends a request to the server with the specified request data.
     *
     * @param requestData The request data containing all the necessary parameters for the request.
     */
    public void sendRequest(RequestData requestData) {
        try {
            Message message = MessageUtilForReports.createReportMessage(requestData);
            client.sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the response from the server based on the message type.
     *
     * @param message The message received from the server.
     */
    public void handleResponse(Message message) {
        String messageType = message.getMessage();
        switch (messageType) {
            case REPORT_DATA_RESPONSE:
                handleReportDataResponse(message);
                break;
            default:
                System.out.println("Unhandled message type: " + messageType);
        }
    }

    /**
     * Handles the report data response by posting a ReportDataReceivedEvent.
     *
     * @param message The message containing the report data from the server.
     */
    public void handleReportDataResponse(Message message) {
        List<Report> reports = message.getReports();
        if (reports != null && !reports.isEmpty()) {
            EventBus.getDefault().post(new ReportDataReceivedEvent(
                    reports,
                    message.isSingleReport(),
                    message.getEmployee(),
                    message.getBranch()
            ));

            // Triggering the dataReceived method via EventBus
            EventBus.getDefault().post(new MessageEvent(message));
        } else {
            System.out.println("No reports received from the server.");
        }
    }
}
