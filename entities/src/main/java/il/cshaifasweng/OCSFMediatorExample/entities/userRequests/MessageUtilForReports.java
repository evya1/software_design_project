package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;

import java.time.Month;
import java.util.List;

public class MessageUtilForReports {

    public static RequestData buildRequestData(String operationType,
                                               ReportType reportType,
                                               ReportSpanType spanType,
                                               PurchaseType purchaseType,
                                               Employee employee,
                                               int year,
                                               Month month) {
        return new RequestDataBuilder()
                .setRequestType(operationType)
                .setReportType(reportType)
                .setReportSpanType(spanType)
                .setPurchaseType(purchaseType)
                .setEmployee(employee)
                .setYear(year)
                .setMonth(month)
                .build();
    }

    public static RequestData buildBasicRequestData(String operationType,
                                                    ReportType reportType,
                                                    ReportSpanType spanType,
                                                    Employee employee,
                                                    int year,
                                                    Month month,
                                                    PurchaseType purchaseType) {
        return new RequestDataBuilder()
                .setRequestType(operationType)
                .setReportType(reportType)
                .setReportSpanType(spanType)
                .setPurchaseType(purchaseType)
                .setEmployee(employee)
                .setYear(year)
                .setMonth(month)
                .build(); // No need to set defaults explicitly here
    }

    /**
     * Creates a Message object configured for report-related requests.
     *
     * @param requestData The data used to configure the message.
     * @return The configured Message object.
     */
    public static Message createReportMessage(RequestData requestData) {
        Message message = new Message();
        message.setMessage(requestData.requestType());
        message.setEmployee(requestData.employee());
        message.setBranch(requestData.branch());
        message.setReportSpanType(requestData.reportSpanType());
        message.setMonth(requestData.month());
        message.setYear(requestData.year());
        message.setPurchaseType(requestData.purchaseType());
        message.setReportType(requestData.reportType());
        message.setReportLabel(requestData.label());
        message.setReportDetails(requestData.details());
        message.setDataForGraphs(requestData.dataForGraphs());
        message.setSerializedReportData(requestData.serializedReportData());
        return message;
    }

    /**
     * Creates a Message object containing a list of reports.
     *
     * @param requestType The type of the request.
     * @param reports The list of reports to be sent in the message.
     * @return The configured Message object with reports.
     */
    public static Message createReportMessage(String requestType, List<Report> reports) {
        Message message = new Message();
        message.setMessage(requestType);
        message.setReports(reports);
        return message;
    }

    /**
     * Creates a Message object for error responses.
     *
     * @param errorMessage The error message to be sent.
     * @return The configured Message object containing the error message.
     */
    public static Message createErrorMessage(String errorMessage) {
        Message message = new Message();
        message.setMessage(errorMessage);
        return message;
    }

    /**
     * Extracts RequestData from the provided Message object.
     *
     * @param message The Message object containing the request details.
     * @return The constructed RequestData object.
     */
    public static RequestData extractRequestData(Message message) {
        return new RequestData(
                message.getMessage(),
                message.getReportType(),
                message.getEmployee(),
                message.getBranch(),
                message.getReportSpanType(),
                message.getMonth(),
                message.getYear(),
                message.getPurchaseType(),
                message.getReportLabel(),
                message.getReportDetails(),
                message.getDataForGraphs(),
                message.getSerializedReportData()
        );
    }
}

