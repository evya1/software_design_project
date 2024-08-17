package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.MessageUtilForReports;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.RequestData;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.REPORT_DATA_RESPONSE;

/**
 * Handles sending and receiving of report-related requests and responses between the client and the server.
 */
public class ReportsRequestHandler {

    private final SimpleClient client;

    /**
     * Constructs a ReportsRequestHandler with the specified client.
     *
     * @param client The SimpleClient instance used to communicate with the server.
     */
    public ReportsRequestHandler(SimpleClient client) {
        this.client = client;
        EventBus.getDefault().register(this); // Registering the handler
    }

    /**
     * Subscribes to `MessageEvent` events posted on the EventBus and handles the received messages.
     * This method extracts the `Message` object from the event and delegates the handling
     * of the message to the `handleResponse` method.
     *
     * @param event The `MessageEvent` containing the message received from the server.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Message message = event.getMessage();
        if (REPORT_DATA_RESPONSE.equals(message.getMessage())) {
            EventBus.getDefault().post(new RefreshChartDataEvent(message.getReports(), message.getMessage()));
        }
        handleResponse(message);
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
            // Future cases for new message types can be added here
            default:
                System.out.println("Unhandled message type: " + messageType);
        }
    }

    /**
     * Handles the report data response by posting a ReportDataReceivedEvent.
     *
     * @param message The message containing the report data from the server.
     */
    private void handleReportDataResponse(Message message) {
        EventBus.getDefault().post(new ReportDataReceivedEvent(
                message.getReports(),
                message.isSingleReport(),
                message.getEmployee(),
                message.getBranch()
        ));
    }
}
