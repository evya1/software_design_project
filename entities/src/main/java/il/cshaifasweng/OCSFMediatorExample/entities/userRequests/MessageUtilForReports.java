package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;

import java.time.Month;

public class MessageUtilForReports {

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
     * Builds a RequestData object based on the given parameters.
     *
     * @param operationType The operation type for the request.
     * @param typeOfReport  The report type.
     * @param spanType      The span type of the report.
     * @param purchaseType  The purchase type associated with the report.
     * @param employee      The employee requesting the report.
     * @param year          The year for the report.
     * @param month         The month for the report.
     * @return The constructed RequestData object.
     */
    public static RequestData buildRequestData(String operationType, ReportType typeOfReport, ReportSpanType spanType, PurchaseType purchaseType, Employee employee, int year, int month) {
        return new RequestData(
                operationType,
                typeOfReport,
                employee,
                employee.getBranch(),
                spanType,
                Month.of(month),
                year,
                purchaseType,
                null, // Label
                null, // Details
                null, // Data for graphs
                null  // Serialized report data
        );
    }


    public static RequestData buildBasicRequestData(String operationType,
                                                    ReportType reportType,
                                                    ReportSpanType spanType,
                                                    Employee employee,
                                                    int year,
                                                    int month,
                                                    PurchaseType purchaseType) {
        // Create a new RequestData object using the constructor
        return new RequestData(
                operationType, // Assuming requestType is a String
                reportType,
                employee,
                null, // Branch is null for now, can be set later if needed
                spanType,
                Month.of(month),
                year,
                purchaseType,
                null, // Label can be set later
                null, // Details can be set later
                null, // Data for graphs can be set later
                null  // Serialized report data can be set later
        );
    }
}

