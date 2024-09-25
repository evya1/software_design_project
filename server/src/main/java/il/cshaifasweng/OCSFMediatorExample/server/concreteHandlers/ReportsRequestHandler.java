package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.*;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
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
            List<Report> reports = reportService.retrieveReportsByBranchAndMonth(requestData.month(), requestData);
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
            Month startMonth = DataCommunicationDB.getQuarterStartMonth(month); // Get the start month of the quarter

            // Get all the months in the quarter
            List<Month> quarterMonths = getQuarterMonths(startMonth);

            // Fetch reports for all months in the quarter using the extracted method
            List<Report> quarterlyReports = fetchReportsForMonths(quarterMonths, requestData);

            // Send the combined quarterly reports to the client
            client.sendToClient(MessageUtilForReports.createReportMessage(requestData.requestType(), quarterlyReports));

        } catch (Exception e) {
            System.err.println("Error retrieving quarterly reports: " + e.getMessage());
            e.printStackTrace();
            client.sendToClient(MessageUtilForReports.createErrorMessage("Failed to retrieve quarterly reports: " + e.getMessage()));
        }
    }

    private void handleYearlyReports(RequestData requestData, ConnectionToClient client) throws IOException {
        try {
            // Extract values to local variables for readability
            String requestType = requestData.requestType();

            // Get all months of the year
            List<Month> yearlyMonths = Arrays.asList(Month.values());

            // Fetch reports for all months in the year using the local variables
            List<Report> yearlyReports = fetchReportsForMonths(yearlyMonths, requestData);

            // Create and send the report message to the client
            Message reportMessage = MessageUtilForReports.createReportMessage(requestType, yearlyReports);
            client.sendToClient(reportMessage);

        } catch (Exception e) {
            // Log the error and send the error message to the client
            String errorMessage = "Failed to retrieve yearly reports: " + e.getMessage();
            System.err.println("Error retrieving yearly reports: " + e.getMessage());
            e.printStackTrace();
            client.sendToClient(MessageUtilForReports.createErrorMessage(errorMessage));
        }
    }

    private List<Month> getQuarterMonths(Month startMonth) {
        List<Month> quarterMonths = new ArrayList<>();
        quarterMonths.add(startMonth);

        // Assuming a 3-month quarter, add the next two months
        quarterMonths.add(startMonth.plus(1));  // Next month
        quarterMonths.add(startMonth.plus(2));  // Month after next

        return quarterMonths;
    }

    private List<Report> fetchReportsForMonths(List<Month> months, RequestData requestData) {
        List<Report> combinedReports = new ArrayList<>();

        for (Month currentMonth : months) {
            List<Report> reportsForMonth = reportService.retrieveReportsByBranchAndMonth(currentMonth, requestData);
            combinedReports.addAll(reportsForMonth);
        }
        return combinedReports;
    }
}
