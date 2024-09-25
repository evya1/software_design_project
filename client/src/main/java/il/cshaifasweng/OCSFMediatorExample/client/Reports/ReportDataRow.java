package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.REPORT_TYPE_A;

public class ReportDataRow {
    private static int idCounter = 0;
    private int id;
    private String label;
    private Double amount;
    private String reportType;
    private String branch;

    // Default constructor that assigns a unique ID
    public ReportDataRow(String label, Double amount) {
        this.id = ++idCounter;
        setLabel(label);
        setAmount(amount);
    }

    // Constructor with reportType
    public ReportDataRow(String label, Double amount, String reportType) {
        this(label, amount);
        setReportType(reportType);
    }

    // Constructor with reportType and branch
    public ReportDataRow(String label, Double amount, String reportType, String branch) {
        this(label, amount, reportType);
        setBranch(branch);
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter and Setter for label
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label == null || label.isEmpty()) {
            this.label = "Unknown Label";
        } else {
            this.label = label;
        }
    }

    // Getter and Setter for amount
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        if (amount == null || amount < 0) {
            this.amount = 0.0;
        } else {
            this.amount = amount;
        }
    }

    // Getter and Setter for reportType
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        if (reportType == null || reportType.isEmpty()) {
            this.reportType = REPORT_TYPE_A.toString();
        } else {
            this.reportType = reportType;
        }
    }

    // Getter and Setter for branch
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
