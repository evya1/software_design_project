package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

/**
 * The Report class represents a report entity with various attributes such as branch, report type, month, report date, details,
 * data for graphs, and a path to a file containing detailed data. It provides a builder for flexible and readable object creation.
 */
@Entity
@Table(name = "report")
public class Report implements Serializable {


    /**
     * Represents the unique ID of the report.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Represents the branch associated with the report.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column
    private ReportType reportType;

    /**
     * Represents the span type of the report (e.g., Daily, Monthly, Quarterly, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "report_span_type", nullable = false)
    private ReportSpanType reportSpanType;

    @Enumerated(EnumType.STRING)
    @Column
    private PurchaseType purchaseType;

    /**
     * Represents the month for which the report is generated.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = true)
    private Month month;

    /**
     * Represents the year for which the report is generated.
     */
    @Column(name = "year", nullable = false)
    private int year;

    /**
     * Represents the date the report was generated.
     */
    @Column(name = "report_date")
    private LocalDate reportDate;

    /**
     * Represents a descriptive label for the report.
     */
    @Column(name = "label", length = 255)
    private String label;

    /**
     * Contains human-readable details or notes about the report.
     */
    @Column(name = "details", length = 2000)
    private String details;

    /**
     * Stores simple key-value pairs for chart data. The key is the label, and the value is the data point.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "report_data", joinColumns = @JoinColumn(name = "report_id"))
    @MapKeyColumn(name = "label")
    @Column(name = "value")
    private Map<String, Double> dataForGraphs;

    /**
     * Stores complex or pre-calculated chart data in serialized (e.g., JSON) format.
     */
    @Lob
    @Column(name = "serialized_report_data", columnDefinition = "TEXT")
    private String serializedReportData;

    public Report() {}

    /**
     * Smart constructor with Builder pattern.
     *
     * @param builder the builder object containing all required fields
     */
    public Report(Builder builder) {
        this.branch = builder.branch;
        this.reportSpanType = builder.reportSpanType;
        this.month = builder.month;
        this.year = builder.year;
        this.reportDate = builder.reportDate;
        this.details = builder.details;
        this.dataForGraphs = builder.dataForGraphs;
        this.serializedReportData = builder.serializedReportData;
        this.label = builder.label;
        this.purchaseType = builder.purchaseType;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public ReportSpanType getSpanType() {
        return reportSpanType;
    }

    public void setReportSpanType(ReportSpanType reportSpanType) {
        this.reportSpanType = reportSpanType;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Map<String, Double> getDataForGraphs() {
        return dataForGraphs;
    }

    public void setDataForGraphs(Map<String, Double> dataForGraphs) {
        this.dataForGraphs = dataForGraphs;
    }

    public String getSerializedReportData() {
        return serializedReportData;
    }

    public void setSerializedReportData(String serializedReportData) {
        this.serializedReportData = serializedReportData;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(PurchaseType purchaseType) {
        this.purchaseType = purchaseType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    /**
     * Builder class for Report.
     */
    public static class Builder {
        private int year;
        private PurchaseType purchaseType;
        private Branch branch;
        private ReportSpanType reportSpanType;
        private Month month;
        private LocalDate reportDate;
        private String details;
        private Map<String, Double> dataForGraphs;
        private String serializedReportData;
        private String dataFilePath;
        private String label;
        private ReportType reportType;

        public Builder() {}

        public Builder withBranch(Branch branch) {
            this.branch = branch;
            return this;
        }

        public Builder withPurchaseType(PurchaseType purchaseType) {
            this.purchaseType = purchaseType;
            return this;
        }

        public Builder withReportType(ReportType reportType) {
            this.reportType = reportType;
            return this;
        }

        public Builder withReportSpanType(ReportSpanType reportSpanType) {
            this.reportSpanType = reportSpanType;
            return this;
        }

        public Builder withMonth(Month month) {
            this.month = month;
            return this;
        }

        public Builder withYear(int year) {
            this.year = year;
            return this;
        }

        public Builder withReportDate(LocalDate reportDate) {
            this.reportDate = reportDate;
            return this;
        }

        public Builder withDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder withDataForGraphs(Map<String, Double> dataForGraphs) {
            this.dataForGraphs = dataForGraphs;
            return this;
        }

        public Builder withSerializedReportData(String serializedReportData) {
            this.serializedReportData = serializedReportData;
            return this;
        }

        public Builder withDataFilePath(String dataFilePath) {
            this.dataFilePath = dataFilePath;
            return this;
        }

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        /**
         * Builds the Report instance.
         * @return the built Report instance
         */
        public Report build() {
            return new Report(this);
        }

        public ReportType getReportType() {
            return reportType;
        }

        public void setReportType(ReportType reportType) {
            this.reportType = reportType;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Report Details:\n");

        // ID
        sb.append("  ID: ").append(id).append("\n");

        // Branch (customized details)
        if (branch != null) {
            sb.append("  Branch: [ID: ").append(branch.getId())
                    .append(", Name: ").append(branch.getBranchName()).append("]\n");
        } else {
            sb.append("  Branch: N/A\n");
        }

        // Report Span Type
        sb.append("  Report Span Type: ").append(reportSpanType != null ? reportSpanType.name() : "N/A").append("\n");

        // Purchase Type
        sb.append("  Purchase Type: ").append(purchaseType != null ? purchaseType.name() : "N/A").append("\n");

        // Month
        sb.append("  Month: ").append(month != null ? month.name() : "N/A").append("\n");

        // Year
        sb.append("  Year: ").append(year).append("\n");

        // Report Date
        sb.append("  Report Date: ").append(reportDate != null ? reportDate.toString() : "N/A").append("\n");

        // Label
        sb.append("  Label: ").append(label != null && !label.isEmpty() ? label : "N/A").append("\n");

        // Details
        sb.append("  Details: ").append(details != null && !details.isEmpty() ? details : "N/A").append("\n");

        // Data for Graphs (Map)
        sb.append("  Data for Graphs: ");
        if (dataForGraphs != null && !dataForGraphs.isEmpty()) {
            sb.append("\n");
            dataForGraphs.forEach((key, value) -> sb.append("    ").append(key).append(": ").append(value).append("\n"));
        } else {
            sb.append("N/A\n");
        }

        // Serialized Report Data
        sb.append("  Serialized Report Data: ")
                .append(serializedReportData != null && !serializedReportData.isEmpty() ? serializedReportData : "N/A")
                .append("\n");

        return sb.toString();
    }
}
