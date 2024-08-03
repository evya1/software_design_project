package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

/**
 * The Report class represents a report entity with various attributes such as branch, report type, month, report date, details,
 * data for graphs, and a path to a file containing detailed data. It provides a builder for flexible and readable object creation.
 */
@Entity
@Table(name = "report")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    private Month month;

    private LocalDate reportDate;

    /**
     * Details for human-readable information.
     */
    private String details;

    /**
     * Data for charts/graphs. Key-value pairs to represent graph data.
     */
    @ElementCollection
    @CollectionTable(name = "report_data", joinColumns = @JoinColumn(name = "report_id"))
    @MapKeyColumn(name = "label")
    @Column(name = "value")
    private Map<String, Double> dataForGraphs;

    /**
     * Path to a file containing detailed data.
     */
    private String dataFilePath;

    // Default constructor required by JPA
    public Report() {}

    /**
     * Smart constructor with Builder pattern.
     * @param builder the builder object containing all required fields
     */
    public Report(Builder builder) {
        this.branch = builder.branch;
        this.reportType = builder.reportType;
        this.month = builder.month;
        this.reportDate = builder.reportDate;
        this.details = builder.details;
        this.dataForGraphs = builder.dataForGraphs;
        this.dataFilePath = builder.dataFilePath;
    }

    // Getters and setters

    /**
     * Gets the report ID.
     * @return the report ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the report ID.
     * @param id the report ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the branch associated with the report.
     * @return the branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Sets the branch associated with the report.
     * @param branch the branch
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * Gets the report type.
     * @return the report type
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * Sets the report type.
     * @param reportType the report type
     */
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    /**
     * Gets the month for the report.
     * @return the month
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Sets the month for the report.
     * @param month the month
     */
    public void setMonth(Month month) {
        this.month = month;
    }

    /**
     * Gets the report date.
     * @return the report date
     */
    public LocalDate getReportDate() {
        return reportDate;
    }

    /**
     * Sets the report date.
     * @param reportDate the report date
     */
    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * Gets the details of the report.
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets the details of the report.
     * @param details the details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Gets the data for graphs.
     * @return the data for graphs
     */
    public Map<String, Double> getDataForGraphs() {
        return dataForGraphs;
    }

    /**
     * Sets the data for graphs.
     * @param dataForGraphs the data for graphs
     */
    public void setDataForGraphs(Map<String, Double> dataForGraphs) {
        this.dataForGraphs = dataForGraphs;
    }

    /**
     * Gets the path to the file containing detailed data.
     * @return the data file path
     */
    public String getDataFilePath() {
        return dataFilePath;
    }

    /**
     * Sets the path to the file containing detailed data.
     * @param dataFilePath the data file path
     */
    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    /**
     * Builder class for Report.
     */
    public static class Builder {
        private Branch branch;
        private ReportType reportType;
        private Month month;
        private LocalDate reportDate;
        private String details;
        private Map<String, Double> dataForGraphs;
        private String dataFilePath;

        public Builder() {}

        public Builder withBranch(Branch branch) {
            this.branch = branch;
            return this;
        }

        public Builder withReportType(ReportType reportType) {
            this.reportType = reportType;
            return this;
        }

        public Builder withMonth(Month month) {
            this.month = month;
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

        public Builder withDataFilePath(String dataFilePath) {
            this.dataFilePath = dataFilePath;
            return this;
        }

        /**
         * Builds the Report instance.
         * @return the built Report instance
         */
        public Report build() {
            return new Report(this);
        }
    }
}
