package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The ReportData class represents a data point in a report, with a label and a value.
 */
@Entity
@Table(name = "report_data")
public class ReportData implements Serializable {

    /**
     * Represents the unique ID of the report data entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * References the report this data entry is associated with.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    /**
     * Represents the label or key for this data entry (e.g., a category or date).
     */
    @Column(name = "label", length = 255, nullable = false)
    private String label;

    /**
     * Represents the value associated with the label.
     */
    @Column(name = "value", nullable = false)
    private Double value;

    public ReportData() {}

    public ReportData(String label, Double value, Report report) {
        if (label == null || value == null) {
            throw new IllegalArgumentException("Label and value cannot be null");
        }
        this.label = label;
        this.value = value;
        this.report = report;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
