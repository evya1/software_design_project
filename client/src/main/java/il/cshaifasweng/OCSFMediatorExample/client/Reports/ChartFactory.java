package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType.CHAIN_MANAGER;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.ALL_BRANCHES;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.INVALID_LABEL;

public class ChartFactory {

    public static final int PIE_CHART_LABEL_LINE_LENGTH = 50;
    public static final int PIE_CHART_START_ANGLE = 180;
    public static final int CHART_PREF_WIDTH = 800;
    public static final int CHART_PREF_HEIGHT = 600;

    /**
     * Observable list holding the chart data, allowing for automatic UI updates when data changes.
     */
    private final ObservableList<Pair<String, Double>> chartData;
    private Object chartContext; // Holds the current context for the chart
    private BorderPane chartBorderPane;

    // Constructor updated to initialize the observable chart data
    public ChartFactory() {
        this.chartData = FXCollections.observableArrayList(createGenericChartData());
        EventBus.getDefault().register(this);
    }

    /**
     * Handles the `ReportDataReceivedEvent` posted to the EventBus.
     * <p>
     * This method is automatically called when a `ReportDataReceivedEvent` is posted to the EventBus.
     * It updates the chart context and chart data based on the reports received in the event.
     * The method also triggers a `ChartDataUpdatedEvent` to notify other components that the chart data has been updated.
     * </p>
     *
     * @param event The `ReportDataReceivedEvent` containing the list of reports and associated data.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReportDataReceived(ReportDataReceivedEvent event) {
        updateChartContext(event);
        List<Report> reports = event.getReports();

        // Convert the reports to chart data
        chartData.setAll(convertReportsToChartData(reports));

        // Notify that chart data has been updated
        // Pass chartBorderPane as part of the event or call a method that handles UI updates
        EventBus.getDefault().post(new ChartDataUpdatedEvent(chartContext, false, null, null, chartBorderPane));
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChartDataUpdatedEvent(ChartDataUpdatedEvent event) {
        String contextDescription = getContextDescription(event.getContextParameter());
        if (event.shouldUseLabel()) {
            contextDescription = generateLabel(event.getReport(), event.getDataKey());
        } else {
            contextDescription = generateTitle(contextDescription);
        }
        prepareAndDisplayBarChart(contextDescription, event.getChartBorderPane());
        prepareAndDisplayPieChart(contextDescription, event.getChartBorderPane());
    }

    /**
     * Converts a list of {@link Report} entities into chart data represented as a list of {@link Pair<String, Double>}.
     * <p>
     * This method processes each report, extracting the key-value pairs from the {@code dataForGraphs} map.
     * Each key-value pair is converted into a {@link Pair} where the key is combined with metadata from the {@link Report}
     * entity (such as branch name, report type, and month) to form a comprehensive label.
     * The resulting list can be used directly in chart components like {@link BarChart} or {@link PieChart}.
     * </p>
     *
     * @param reports A list of {@link Report} entities to be converted into chart data.
     * @return A list of {@link Pair<String, Double>} representing the chart data, with the label containing
     * contextual information from the report and the value representing the data point.
     */
    private List<Pair<String, Double>> convertReportsToChartData(List<Report> reports) {
        return reports.stream()
                .flatMap(report -> report.getDataForGraphs().entrySet().stream()
                        .map(entry -> new Pair<>(generateLabel(report, entry.getKey()), entry.getValue())))
                .collect(Collectors.toList());
    }

    /**
     * Refreshes the chart data by fetching new data in the background.
     * <p>
     * This method triggers a background task that fetches updated chart data, ensuring
     * that the operation does not block the UI thread. Once the data is fetched, it updates
     * the observable list and posts a `ChartDataUpdatedEvent` to notify that the chart data has been updated.
     * </p>
     */
    public void refreshChartData() {
        Task<List<Pair<String, Double>>> task = new Task<>() {
            @Override
            protected List<Pair<String, Double>> call() {
                // Simulate data fetching from a database or external source
                return createGenericChartData();  // Replace with real data fetching logic
            }
        };

        task.setOnSucceeded(event -> {
            chartData.setAll(task.getValue());
            EventBus.getDefault().post(new ChartDataUpdatedEvent(chartContext, false, null, null, null)); // Notify that chart data is updated
        });

        new Thread(task).start();
    }

    /**
     * Generates a title for the chart based on the given context.
     * <p>
     * This method creates a descriptive title for a chart using the provided context description.
     * The generated title usually starts with "Overview: " followed by the specific context.
     * </p>
     * <p><b>Example:</b></p>
     * <pre>{@code
     * String title = chartFactory.generateTitle("Branch A");
     * // Returns: "Overview: Branch A"
     * }</pre>
     *
     * @param contextDescription a description of the context, such as a branch name or scope of the data.
     * @return a dynamically generated chart title as a String.
     */
    private String generateTitle(String contextDescription) {
        StringBuilder titleBuilder = new StringBuilder("Overview: ");

        if (contextDescription == null || contextDescription.isEmpty()) {
            titleBuilder.append(ALL_BRANCHES);
        } else {
            titleBuilder.append(contextDescription);
        }

        return titleBuilder.toString();
    }

    /**
     * Creates generic data for charts, ensuring that the values are of type Double.
     * <p>
     * This method generates a list of key-value pairs representing the data for charts.
     * The data is generic and can be used for various types of charts like BarChart and PieChart.
     * </p>
     * <p><b>Example:</b></p>
     * <pre>{@code
     * List<Pair<String, Double>> data = chartFactory.createGenericChartData();
     * // Returns: [("Product A", 3000.0), ("Product B", 1500.0), ("Product C", 300.0)]
     * }</pre>
     *
     * @return a List of Pair<String, Double> representing the data.
     */
    public List<Pair<String, Double>> createGenericChartData() {
        return Arrays.asList(new Pair<>("Product A", 3000.0), new Pair<>("Product B", 1500.0), new Pair<>("Product C", 300.0));
    }

    /**
     * Converts generic chart data to BarChart data.
     * <p>
     * This method transforms the generic key-value pair data into an ObservableList that can be
     * directly used by a BarChart. The conversion ensures that the data aligns with the expected
     * structure for the BarChart.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * List<Pair<String, Double>> genericData = chartFactory.createGenericChartData();
     * ObservableList<XYChart.Data<String, Number>> barChartData = chartFactory.convertToBarChartData(genericData);
     * }</pre>
     *
     * @param genericData the generic chart data.
     * @return an ObservableList of XYChart.Data for use in BarChart.
     */
    public ObservableList<XYChart.Data<String, Number>> convertToBarChartData(List<Pair<String, Double>> genericData) {
        return FXCollections.observableArrayList(genericData.stream().map(pair -> new XYChart.Data<String, Number>(pair.getKey(), pair.getValue())).collect(Collectors.toList()));
    }

    /**
     * Converts the current observable chart data to BarChart data.
     * <p>
     * This method transforms the observable chart data into an {@code ObservableList}
     * that can be directly used by a {@code BarChart}. The conversion ensures that the
     * data aligns with the expected structure for the BarChart.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * ObservableList<XYChart.Data<String, Number>> barChartData = chartFactory.convertToBarChartData();
     * }</pre>
     *
     * @return an ObservableList of {@code XYChart.Data} for use in {@code BarChart}.
     */
    public ObservableList<XYChart.Data<String, Number>> convertToBarChartData() {
        return FXCollections.observableArrayList(chartData.stream().map(pair -> new XYChart.Data<String, Number>(pair.getKey(), pair.getValue())).collect(Collectors.toList()));
    }


    /**
     * Converts generic chart data to PieChart data.
     * <p>
     * This method transforms the generic key-value pair data into an ObservableList that can be
     * directly used by a PieChart. The conversion ensures that the data aligns with the expected
     * structure for the PieChart.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * List<Pair<String, Double>> genericData = chartFactory.createGenericChartData();
     * ObservableList<PieChart.Data> pieChartData = chartFactory.convertToPieChartData(genericData);
     * }</pre>
     *
     * @param genericData the generic chart data.
     * @return an ObservableList of PieChart.Data for use in PieChart.
     */
    public ObservableList<PieChart.Data> convertToPieChartData(List<Pair<String, Double>> genericData) {
        return FXCollections.observableArrayList(genericData.stream().map(pair -> new PieChart.Data(pair.getKey(), pair.getValue())).collect(Collectors.toList()));
    }

    /**
     * Converts the current observable chart data to PieChart data.
     * <p>
     * This method transforms the observable chart data into an {@code ObservableList}
     * that can be directly used by a {@code PieChart}. The conversion ensures that the
     * data aligns with the expected structure for the PieChart.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * ObservableList<PieChart.Data> pieChartData = chartFactory.convertToPieChartData();
     * }</pre>
     *
     * @return an ObservableList of {@code PieChart.Data} for use in {@code PieChart}.
     */
    public ObservableList<PieChart.Data> convertToPieChartData() {
        return FXCollections.observableArrayList(chartData.stream().map(pair -> new PieChart.Data(pair.getKey(), pair.getValue())).collect(Collectors.toList()));
    }

    /**
     * Creates and configures a BarChart based on the provided context description using the current observable chart data.
     * <p>
     * This method prepares a BarChart using the context description and the observable chart data.
     * It first converts the current observable chart data to a format suitable for a BarChart.
     * The BarChart is customized with labels, a title, and other settings.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * BarChart<String, Number> barChart = chartFactory.createBarChart("Branch A");
     * }</pre>
     *
     * @param contextDescription the context for which the chart is created (e.g., branch name, all locations).
     * @return a fully configured BarChart instance.
     */
    public BarChart<String, Number> createBarChart(String contextDescription) {
        ObservableList<XYChart.Data<String, Number>> barChartData = convertToBarChartData();
        return createBarChart(barChartData, contextDescription);
    }

    /**
     * Creates and configures a BarChart based on the provided context description and data.
     * <p>
     * This method is used when the data for the BarChart is already available in an ObservableList format.
     * It customizes the BarChart with appropriate labels, a title, and other appearance settings based on
     * the provided context description.
     * </p>
     * <p><b>Example:</b></p>
     * <pre>{@code
     * ObservableList<XYChart.Data<String, Number>> data = ...; // Your BarChart data
     * BarChart<String, Number> barChart = chartFactory.createBarChart(data, "Branch A");
     * }</pre>
     *
     * @param data               the data to be displayed in the BarChart.
     * @param contextDescription the context for which the chart is created (e.g., branch name, all locations).
     * @return a fully configured BarChart instance.
     */
    public BarChart<String, Number> createBarChart(ObservableList<XYChart.Data<String, Number>> data, String contextDescription) {
        String chartTitle = generateTitle(contextDescription);

        // Initialize the axes with labels
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Dates");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity Sold");

        // Create and configure the bar chart with axes and title
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(chartTitle);

        // Prepare the data series
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>(data);
        dataSeries.setName("Product Sold");

        // Add data to the bar chart
        barChart.getData().add(dataSeries);

        // Customize the appearance and behavior of the bar chart
        barChart.setLegendVisible(false);  // Hide the legend, since it's not needed for this chart.
        barChart.setAnimated(false);       // Disable animations for a static display of the chart.
        barChart.setPrefSize(CHART_PREF_WIDTH, CHART_PREF_HEIGHT);

        return barChart;
    }

    /**
     * Creates and configures a PieChart based on the provided context description using the current observable chart data.
     * <p>
     * This method prepares a PieChart using the context description and the observable chart data.
     * It first converts the current observable chart data to a format suitable for a PieChart.
     * The PieChart is customized with labels, a title, and other settings.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * PieChart pieChart = chartFactory.createPieChart("Branch A");
     * }</pre>
     *
     * @param contextDescription the context for which the chart is created (e.g., branch name, all locations).
     * @return a fully configured PieChart instance.
     */
    public PieChart createPieChart(String contextDescription) {
        ObservableList<PieChart.Data> pieChartData = convertToPieChartData();
        return createPieChart(pieChartData, contextDescription);
    }

    /**
     * Creates and configures a PieChart based on the provided context description and data.
     * <p>
     * This method is used when the data for the PieChart is already available in an ObservableList format.
     * It customizes the PieChart with appropriate labels, a title, and other appearance settings based on
     * the provided context description.
     * </p>
     * <p><b>Example:</b></p>
     * <pre>{@code
     * ObservableList<PieChart.Data> data = ...; // Your PieChart data
     * PieChart pieChart = chartFactory.createPieChart(data, "Branch A");
     * }</pre>
     *
     * @param data               the data to be displayed in the PieChart.
     * @param contextDescription the context for which the chart is created (e.g., branch name, all locations).
     * @return a fully configured PieChart instance.
     */
    public PieChart createPieChart(ObservableList<PieChart.Data> data, String contextDescription) {
        String chartTitle = generateTitle(contextDescription);

        // Create and configure the pie chart with data and title
        PieChart pieChart = new PieChart(data);
        pieChart.setTitle(chartTitle);

        // Customize the appearance and behavior of the pie chart
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(PIE_CHART_LABEL_LINE_LENGTH);
        pieChart.setStartAngle(PIE_CHART_START_ANGLE);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(false);  // Hide the legend, since it's not needed for this chart.
        pieChart.setAnimated(false);       // Disable animations for a static display of the chart.
        pieChart.setPrefSize(CHART_PREF_WIDTH, CHART_PREF_HEIGHT);

        return pieChart;
    }


    /**
     * Prepares and displays a BarChart by creating it and populating it in the provided BorderPane.
     * <p>
     * This method schedules the creation and display of the BarChart on the JavaFX Application Thread
     * using {@code Platform.runLater}. This ensures that all UI updates are thread-safe and occur
     * on the correct thread.
     * </p>
     *
     * @param contextDescription the context description (e.g., branch name, all locations).
     * @param chartBorderPane    the BorderPane where the BarChart will be displayed.
     */
    public void prepareAndDisplayBarChart(String contextDescription, BorderPane chartBorderPane) {
        Platform.runLater(() -> {
            BarChart<String, Number> barChart = createBarChart(contextDescription);
            ObservableList<XYChart.Data<String, Number>> barChartData = convertToBarChartData();
            populateBarChart(barChart, chartBorderPane, barChartData);
        });
    }

    /**
     * Prepares and displays a PieChart by creating it and populating it in the provided BorderPane.
     * <p>
     * This method schedules the creation and display of the PieChart on the JavaFX Application Thread
     * using {@code Platform.runLater}. This ensures that all UI updates are thread-safe and occur
     * on the correct thread.
     * </p>
     *
     * @param contextDescription the context description (e.g., branch name, all locations).
     * @param chartBorderPane    the BorderPane where the PieChart will be displayed.
     */
    public void prepareAndDisplayPieChart(String contextDescription, BorderPane chartBorderPane) {
        Platform.runLater(() -> {
            PieChart pieChart = createPieChart(contextDescription);
            ObservableList<PieChart.Data> pieChartData = convertToPieChartData();
            populatePieChart(pieChart, chartBorderPane, pieChartData);
        });
    }


        /**
         * Populates the BarChart with data and adds it to the provided BorderPane.
         * <p>
         * This method is responsible for displaying the BarChart in the UI by adding it to the specified
         * BorderPane. It allows the BarChart to be dynamically added to any part of the UI as needed.
         * </p>
         * <p><b>Usage:</b></p>
         * <pre>{@code
         * BarChart<String, Number> barChart = ...; // Your BarChart instance
         * chartFactory.populateBarChart(barChart, chartBorderPane);
         * }</pre>
         *
         * @param barChart        the BarChart to be populated and displayed.
         * @param chartBorderPane the BorderPane where the chart will be displayed.
         */
        public void populateBarChart(BarChart<String, Number> barChart, BorderPane chartBorderPane, ObservableList<XYChart.Data<String, Number>> barChartData) {
            XYChart.Series<String, Number> series = new XYChart.Series<>(barChartData);
            barChart.getData().add(series);
            chartBorderPane.setCenter(barChart);
        }

    /**
     * Populates the PieChart with data and adds it to the provided BorderPane.
     * <p>
     * This method is responsible for displaying the PieChart in the UI by adding it to the specified
     * BorderPane. It allows the PieChart to be dynamically added to any part of the UI as needed.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * PieChart pieChart = ...; // Your PieChart instance
     * chartFactory.populatePieChart(pieChart, chartBorderPane);
     * }</pre>
     *
     * @param pieChart        the PieChart to be populated and displayed.
     * @param chartBorderPane the BorderPane where the chart will be displayed.
     */
    public void populatePieChart(PieChart pieChart, BorderPane chartBorderPane, ObservableList<PieChart.Data> pieChartData) {
        pieChart.setData(pieChartData);
        chartBorderPane.setCenter(pieChart);
    }

    /**
     * Returns the observable chart data.
     * <p>
     * This method provides access to the observable list of chart data, which can be used
     * for dynamic updates in the UI when the data changes.
     * </p>
     *
     * @return the observable list of chart data.
     */
    public ObservableList<Pair<String, Double>> getChartData() {
        return chartData;
    }

    /**
     * Retrieves the context description for the chart title based on the provided parameter.
     * <p>
     * This method determines the context description to be used in chart titles based on the provided
     * context parameter. It is typically used to differentiate charts by branch or location.
     * </p>
     * <p><b>Example:</b></p>
     * <pre>{@code
     * String description = chartFactory.getContextDescription("Branch A");
     * // Returns: "Branch A"
     *
     * String defaultDescription = chartFactory.getContextDescription(null);
     * // Returns: "All Branches"
     * }</pre>
     *
     * @param contextParameter an object used to determine the context description (e.g., branch name, all locations).
     * @return the context description as a String.
     */
    public String getContextDescription(Object contextParameter) {
        if (contextParameter == null) {
            return "Static Data";
        } else if (contextParameter == CHAIN_MANAGER) {
            return ALL_BRANCHES;
        }
        return contextParameter.toString();
    }

    /**
     * Generates a label for each data point in the chart, combining report-specific details
     * such as the branch name, report span type, and month with a specific data key.
     * <p>
     * This method constructs a label that can be used in chart components like BarChart or PieChart,
     * providing a descriptive string that reflects the context of the data.
     * </p>
     * <p>
     * The method first assembles a label using the branch name, report span type (e.g., daily, monthly),
     * and month if available. These parts are separated by a dash (" - "). Finally, the specific data key
     * from the `dataForGraphs` map is appended to create a unique and contextually meaningful label.
     * </p>
     * <p>
     * If the generated label turns out to be invalid (e.g., contains only dashes), the method logs a warning
     * and returns a placeholder label indicating a problem.
     * </p>
     *
     * @param report  The report object containing relevant metadata, such as branch, report span type, and month.
     * @param dataKey The specific key from the `dataForGraphs` map representing a particular metric or data point within the report.
     *                For example, this could be "Total Sales", "Number of Complaints", or any other tracked metric.
     * @return A string label that combines the report details and the data key, or a placeholder if the label is unsatisfactory.
     */
    private String generateLabel(Report report, String dataKey) {
        String branchName = report.getBranch() != null ? report.getBranch().getBranchName() : "";
        String reportSpanType = report.getReportType().toString();
        String month = report.getMonth() != null ? report.getMonth().name() : "";

        StringBuilder label = new StringBuilder();

        String separator = " - ";
        if (!branchName.isEmpty()) {
            label.append(branchName).append(separator);
        }
        label.append(reportSpanType).append(separator);
        if (!month.isEmpty()) {
            label.append(month).append(separator);
        }
        label.append(dataKey);

        String finalLabel = label.toString().trim();

        // Check if the label is invalid
        if (finalLabel.equals("-") || finalLabel.equals("- -") || finalLabel.equals("- - -")) {
            // Log the issue and return a placeholder label
            System.out.println("Label generation resulted in an invalid label, returning placeholder.");
            return INVALID_LABEL;
        }

        return finalLabel;
    }

    /**
     * Updates the chart context based on the details provided in the `ReportDataReceivedEvent`.
     * <p>
     * This method sets the `chartContext` field using the `getContextDescription` method,
     * determining the context based on whether the event is related to a chain manager or a specific branch.
     * </p>
     *
     * @param event The `ReportDataReceivedEvent` containing information about the employee type and branch.
     */
    private void updateChartContext(ReportDataReceivedEvent event) {
        if (event.getEmployeeType() == CHAIN_MANAGER) {
            this.chartContext = getContextDescription(ALL_BRANCHES); // Use existing method to get the description
        } else if (event.getBranch() != null) {
            this.chartContext = getContextDescription(event.getBranchName()); // Use existing method for branch context
        } else {
            this.chartContext = getContextDescription("Unknown Context"); // Use existing method for fallback context
        }
    }

    public BorderPane getChartBorderPane() {
        return chartBorderPane;
    }

    public void setChartBorderPane(BorderPane chartBorderPane) {
        this.chartBorderPane = chartBorderPane;
    }
}
