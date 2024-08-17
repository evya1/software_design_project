package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import javafx.scene.layout.BorderPane;

public class ChartDataUpdatedEvent {
    private final Object contextParameter;
    private final boolean useLabel;
    private final Report report;
    private final String dataKey;
    private final BorderPane chartBorderPane;

    // Constructor
    public ChartDataUpdatedEvent(Object contextParameter, boolean useLabel, Report report, String dataKey, BorderPane chartBorderPane) {
        this.contextParameter = contextParameter;
        this.useLabel = useLabel;
        this.report = report;
        this.dataKey = dataKey;
        this.chartBorderPane = chartBorderPane;
    }

    public Object getContextParameter() {
        return contextParameter;
    }

    public boolean shouldUseLabel() {
        return useLabel;
    }

    public Report getReport() {
        return report;
    }

    public String getDataKey() {
        return dataKey;
    }

    public BorderPane getChartBorderPane() {
        return chartBorderPane;
    }
}
