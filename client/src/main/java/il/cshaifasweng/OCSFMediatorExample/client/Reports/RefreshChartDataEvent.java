package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import java.util.List;
import java.util.Collections;

public class RefreshChartDataEvent {
    private final List<Report> reports;
    private final String messageType;
    private final boolean isEmpty;

    // Constructor with reports and message type
    public RefreshChartDataEvent(List<Report> reports, String messageType) {
        this.reports = reports != null ? reports : Collections.emptyList();
        this.messageType = messageType;
        this.isEmpty = this.reports.isEmpty();
    }

    // Getter for reports list
    public List<Report> getReports() {
        return reports;
    }

    // Getter for message type
    public String getMessageType() {
        return messageType;
    }

    // Check if the report list is empty
    public boolean isEmpty() {
        return isEmpty;
    }
}
