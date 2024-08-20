package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.*;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.time.Month;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.*;

public class ReportsRequestHandler implements RequestHandler {
    private final ReportService reportService;

    public ReportsRequestHandler(ReportService reportService) {
        this.reportService = reportService;
        System.out.println("ReportsRequestHandler initialized with reportService: " + reportService);
    }

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        String requestType = message.getMessage();

        // Extract RequestData from Message
        RequestData requestData = MessageUtilForReports.extractRequestData(message);

        try {
            switch (requestType) {
                case FETCH_MONTHLY_REPORTS:
                    handleMonthlyReports(requestData, client);
                    break;
                case FETCH_LAST_QUARTER_REPORT:
                    handleQuarterlyReports(requestData, client);
                    break;
                // Add more cases for different report types
                default:
                    client.sendToClient(MessageUtilForReports.createErrorMessage(INVALID_REPORT_REQUEST));
                    break;
            }
        } catch (Exception e) {
            // Log the exception and send an error message to the client
            System.err.println("Error handling report request: " + e.getMessage());
            e.printStackTrace();
            client.sendToClient(MessageUtilForReports.createErrorMessage("An error occurred while processing the report request."));
        }
    }

    private void handleMonthlyReports(RequestData requestData, ConnectionToClient client) throws IOException {
        try {
            List<Report> reports = reportService.retrieveReportsByBranchAndMonth(
                    requestData.branch(), requestData.month(), requestData.purchaseType(), requestData.reportType()
            );
            client.sendToClient(MessageUtilForReports.createReportMessage(requestData.requestType(), reports));
        } catch (Exception e) {
            System.err.println("Error retrieving monthly reports: " + e.getMessage());
            e.printStackTrace();
            client.sendToClient(MessageUtilForReports.createErrorMessage("Failed to retrieve monthly reports."));
        }
    }

    private void handleQuarterlyReports(RequestData requestData, ConnectionToClient client) throws IOException {
        try {
            Month month = requestData.month();
            Month startMonth = DataCommunicationDB.getQuarterStartMonth(month);

            // Make sure to pass the necessary parameters: branch, startMonth, purchaseType, and reportType
            List<Report> reports = reportService.retrieveReportsByBranchAndMonth(
                    requestData.branch(), startMonth, requestData.purchaseType(), requestData.reportType()
            );

            client.sendToClient(MessageUtilForReports.createReportMessage(requestData.requestType(), reports));
        } catch (Exception e) {
            // Handle the exception and notify the client
            System.err.println("Error retrieving quarterly reports: " + e.getMessage());
            e.printStackTrace();
            client.sendToClient(MessageUtilForReports.createErrorMessage("Failed to retrieve quarterly reports."));
        }
    }
}
