package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class RequestDataBuilder {
    private String requestType;
    private ReportType reportType;
    private Employee employee;
    private Branch branch;
    private ReportSpanType reportSpanType;
    private Month month;
    private int year;
    private PurchaseType purchaseType;
    private String label;
    private String details;
    private Map<String, Double> dataForGraphs;
    private String serializedReportData;

    public RequestDataBuilder() {
        // Initialize default values
        this.label = "Default Label";
        this.details = "Default Details";
        this.dataForGraphs = new HashMap<>();
        this.serializedReportData = "";
    }

    public RequestDataBuilder setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public RequestDataBuilder setReportType(ReportType reportType) {
        this.reportType = reportType;
        return this;
    }

    public RequestDataBuilder setEmployee(Employee employee) {
        this.employee = employee;
        this.branch = employee != null ? employee.getBranch() : null;
        return this;
    }

    public RequestDataBuilder setReportSpanType(ReportSpanType reportSpanType) {
        this.reportSpanType = reportSpanType;
        return this;
    }

    public RequestDataBuilder setMonth(Month month) {
        this.month = month;
        return this;
    }

    public RequestDataBuilder setYear(int year) {
        this.year = year;
        return this;
    }

    public RequestDataBuilder setPurchaseType(PurchaseType purchaseType) {
        this.purchaseType = purchaseType;
        return this;
    }

    public RequestDataBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public RequestDataBuilder setDetails(String details) {
        this.details = details;
        return this;
    }

    public RequestDataBuilder setDataForGraphs(Map<String, Double> dataForGraphs) {
        this.dataForGraphs = dataForGraphs;
        return this;
    }

    public RequestDataBuilder setSerializedReportData(String serializedReportData) {
        this.serializedReportData = serializedReportData;
        return this;
    }

    public RequestData build() {
        return new RequestData(
                requestType, reportType, employee, branch, reportSpanType,
                month, year, purchaseType, label, details,
                dataForGraphs, serializedReportData);
    }

    // Method to construct RequestData
    public static RequestData buildRequestData(String operationType,
                                               ReportType reportType,
                                               ReportSpanType spanType,
                                               PurchaseType purchaseType,
                                               Employee employee,
                                               int year,
                                               Month month,
                                               String label,
                                               String details,
                                               Map<String, Double> dataForGraphs,
                                               String serializedReportData) {
        return new RequestDataBuilder()
                .setRequestType(operationType)
                .setReportType(reportType)
                .setReportSpanType(spanType)
                .setPurchaseType(purchaseType)
                .setEmployee(employee)
                .setYear(year)
                .setMonth(month)
                .setLabel(label)
                .setDetails(details)
                .setDataForGraphs(dataForGraphs)
                .setSerializedReportData(serializedReportData)
                .build();
    }
}
