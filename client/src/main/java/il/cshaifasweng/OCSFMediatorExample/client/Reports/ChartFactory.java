package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static il.cshaifasweng.OCSFMediatorExample.client.Reports.ReportsScreenConstants.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType.CHAIN_MANAGER;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.ALL_BRANCHES;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.INVALID_LABEL;
import java.util.logging.Logger;



public class ChartFactory {

    private static final Logger logger = Logger.getLogger(ChartFactory.class.getName());
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

    @Subscribe
    public void onReportDataReceived(ReportDataReceivedEvent event) {
        log("onReportDataReceived called.");
        updateChartContext(event);
        List<Report> reports = event.getReports();

        if (reports == null || reports.isEmpty()) {
            log("No reports received, using default chart data.");
            updateChartData(createGenericChartData());
        } else {
            log("Reports received, converting to chart data...");
            List<Pair<String, Double>> newChartData = convertReportsToChartData(reports);
            log("Converted chart data: " + newChartData);
            updateChartData(newChartData);
        }
    }

    private void updateChartData(List<Pair<String, Double>> newChartDataList) {
        log("Attempting to update chart data...");
        if (isChartDataDifferent(chartData, newChartDataList)) {
            log("Chart data is different. Updating chart...");
            chartData.setAll(newChartDataList);
            log("Chart data updated: " + chartData);

            // Post the update event
            EventBus.getDefault().post(new ChartDataUpdatedEvent(chartContext, false, null, null, chartBorderPane));
            log("ChartDataUpdatedEvent posted.");
        } else {
            log("Chart data is already up-to-date. No update needed.");
        }
    }

    @Subscribe
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
     * Converts a list of {@link Report} entities into chart data represented as a list of {@link Pair}&lt;{@link String}, {@link Double}&gt;.
     * <p>
     * This method processes each report, extracting the counts for specific products (e.g., Product A, Product B, Product C).
     * It uses {@link #extractProductCount(List, String)} to retrieve the counts for each product.
     * The resulting list can be used directly in chart components like {@link BarChart} or {@link PieChart}.
     * </p>
     *
     * @param reports A list of {@link Report} entities to be converted into chart data.
     * @return A list of {@link Pair}&lt;{@link String}, {@link Double}&gt; representing the chart data, with the product name as the key
     *         and the total count as the value.
     */
    private List<Pair<String, Double>> convertReportsToChartData(List<Report> reports) {
        log("Converting reports to chart data...");

        double productACount = extractProductCount(reports, PURCHASABLE_PRODUCT_A);
        double productBCount = extractProductCount(reports, PURCHASABLE_PRODUCT_B);
        double productCCount = extractProductCount(reports, PURCHASABLE_PRODUCT_C);

        List<Pair<String, Double>> chartDataList = createChartDataList(productACount, productBCount, productCCount);
        log("Converted chart data: " + chartDataList);

        return chartDataList;
    }

    /**
     * Provides a default product count when the product is missing in the report data.
     *
     * @param reports List of {@link Report} entities (not used but required by method signatures).
     * @return The default product count value.
     */
    private double extractProductCount(List<Report> reports, String productName) {
        double total = reports.stream()
                .filter(report -> report.getDataForGraphs().containsKey(productName))
                .mapToDouble(report -> report.getDataForGraphs().get(productName))
                .sum();

        // If no reports contained the product, return the default value
        return total > 0 ? total : DEFAULT_PRODUCT_COUNT;
    }

    private List<Pair<String, Double>> createChartDataList(double productACount, double productBCount, double productCCount) {
        return Arrays.asList(
                new Pair<>(PURCHASABLE_PRODUCT_A, productACount),
                new Pair<>(PURCHASABLE_PRODUCT_B, productBCount),
                new Pair<>(PURCHASABLE_PRODUCT_C, productCCount)
        );
    }

    /**
     * Creates a list of chart data pairs representing product counts using default values.
     * <p>
     * This method overloads {@link #createChartDataList(double, double, double)} to provide
     * default product counts for the chart data.
     * </p>
     *
     * @return A list of {@link Pair}&lt;{@link String}, {@link Double}&gt; containing product names and their default counts.
     */

    private List<Pair<String, Double>> createChartDataList() {
        return createChartDataList(DEFAULT_PRODUCT_COUNT,DEFAULT_PRODUCT_COUNT,DEFAULT_PRODUCT_COUNT);
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
     * Creates generic data for charts using default product counts.
     *
     * @return A list of {@link Pair}&lt;{@link String}, {@link Double}&gt; representing generic chart data.
     */
    public List<Pair<String, Double>> createGenericChartData() {
        return createChartDataList();
    }

    // Generic method for converting chart data to a specific chart type
    private <T> ObservableList<T> convertToChartData(List<Pair<String, Double>> genericData, Function<Pair<String, Double>, T> mapper) {
        return FXCollections.observableArrayList(genericData.stream().map(mapper).collect(Collectors.toList()));
    }

    // For internal chart data, we can reuse the same function without passing data
    private <T> ObservableList<T> convertToChartData(Function<Pair<String, Double>, T> mapper) {
        return convertToChartData(this.chartData, mapper);
    }

    // Usage for internal BarChart data
    public ObservableList<XYChart.Data<String, Number>> convertToBarChartData() {
        return convertToChartData(pair -> new XYChart.Data<>(pair.getKey(), pair.getValue()));
    }

    // Usage for internal PieChart data
    public ObservableList<PieChart.Data> convertToPieChartData() {
        return convertToChartData(pair -> new PieChart.Data(pair.getKey(), pair.getValue()));
    }

    // General method for chart creation
    private <T extends Chart> T createChart(Supplier<T> chartSupplier, String contextDescription) {
        T chart = chartSupplier.get();
        String chartTitle = generateTitle(contextDescription);

        if (chart instanceof BarChart<?, ?>) {
            @SuppressWarnings("unchecked")
            BarChart<String, Number> barChart = (BarChart<String, Number>) chart;  // Safe cast
            ObservableList<XYChart.Data<String, Number>> barChartData = convertToBarChartData();
            barChart.setTitle(chartTitle);
            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>(barChartData);
            barChart.getData().add(dataSeries);
        } else if (chart instanceof PieChart pieChart) {
            ObservableList<PieChart.Data> pieChartData = convertToPieChartData();
            pieChart.setTitle(chartTitle);
            pieChart.setData(pieChartData);
        }

        customizeChartAppearance(chart);
        return chart;
    }

    // Usage for BarChart
    public BarChart<String, Number> createBarChart(String contextDescription) {
        return createChart(() -> new BarChart<>(new CategoryAxis(), new NumberAxis()), contextDescription);
    }

    // Usage for PieChart
    public PieChart createPieChart(String contextDescription) {
        return createChart(PieChart::new, contextDescription);
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

        customizeChartAppearance(barChart);

        return barChart;
    }

    private void customizeChartAppearance(Chart chart) {
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefSize(CHART_PREF_WIDTH, CHART_PREF_HEIGHT);
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

        customizeChartAppearance(pieChart);

        return pieChart;
    }

    private <T extends Chart> void prepareAndDisplayChart(String contextDescription, BorderPane chartBorderPane, Supplier<T> chartSupplier, String chartType) {
        log("Preparing to display " + chartType + " for context: " + contextDescription);

        // Run UI update on the JavaFX Application Thread
        Platform.runLater(() -> {
            T chart = chartSupplier.get(); // Here, 'T' is a specific chart type (e.g., BarChart, PieChart)
            chartBorderPane.setCenter(chart);
            log(chartType + " displayed for context: " + contextDescription);
        });
    }

    public void prepareAndDisplayBarChart(String contextDescription, BorderPane chartBorderPane) {
        log("Preparing BarChart...");
        prepareAndDisplayChart(contextDescription, chartBorderPane, () -> createBarChart(contextDescription), BAR_CHART_TYPE);
    }

    public void prepareAndDisplayPieChart(String contextDescription, BorderPane chartBorderPane) {
        log("Preparing PieChart...");
        prepareAndDisplayChart(contextDescription, chartBorderPane, () -> createPieChart(contextDescription), PIE_CHART_TYPE);
    }

    public void populateBarChart(BarChart<String, Number> barChart, BorderPane chartBorderPane, ObservableList<XYChart.Data<String, Number>> barChartData) {
        if (!barChartData.isEmpty()) {
            log("Clearing old BarChart data...");
            barChart.getData().clear();
            log("Old BarChart data cleared.");
        } else {
            log("No new BarChart data to populate.");
        }

        // Proceed with populating the chart with new data
        log("Populating new BarChart data...");
        XYChart.Series<String, Number> series = new XYChart.Series<>(barChartData);
        barChart.getData().add(series);
        chartBorderPane.setCenter(barChart);
        log("BarChart data populated and displayed.");
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
        System.out.println("Populating PieChart with data: " + pieChartData);
        pieChart.getData().clear();
        pieChart.setData(pieChartData);
        chartBorderPane.setCenter(pieChart);
        System.out.println("PieChart data populated and displayed.");
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

    /**
     * Compares two lists of chart data to determine if they are different.
     * This method works for any type of chart data, such as {@link Pair}, {@link XYChart.Data}, or {@link PieChart.Data}.
     *
     * @param <T>                     The type of chart data being compared (e.g., {@link Pair}&lt;{@link String}, {@link Double}&gt;,
     *                                 {@link XYChart.Data}, or {@link PieChart.Data}).
     * @param oldDataList             The existing data in the chart.
     * @param newDataList             The new data to be compared.
     * @param keyExtractorFunction    Function to extract the key (category/label) from each data item.
     * @param valueExtractorFunction  Function to extract the value (numeric data) from each data item.
     * @return true if the two data sets are different, false if they are identical.
     */
    private <T> boolean isChartDataDifferent(List<T> oldDataList, List<T> newDataList,
                                             Function<T, String> keyExtractorFunction, Function<T, Number> valueExtractorFunction) {
        if (oldDataList.size() != newDataList.size()) return true;
        for (int i = 0; i < oldDataList.size(); i++) {
            T oldDataItem = oldDataList.get(i);
            T newDataItem = newDataList.get(i);

            String oldDataKey = keyExtractorFunction.apply(oldDataItem);
            String newDataKey = keyExtractorFunction.apply(newDataItem);
            Number oldDataValue = valueExtractorFunction.apply(oldDataItem);
            Number newDataValue = valueExtractorFunction.apply(newDataItem);

            if (!oldDataKey.equals(newDataKey) || Math.abs(oldDataValue.doubleValue() - newDataValue.doubleValue()) > 0.0001) {
                return true;
            }
        }
        return false;
    }

    private boolean isChartDataDifferent(List<Pair<String, Double>> oldData, List<Pair<String, Double>> newData) {
        return isChartDataDifferent(oldData, newData, Pair::getKey, Pair::getValue);
    }

    private void log(String message) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.info("[ChartFactory - " + methodName + "]: " + message);
    }
}
