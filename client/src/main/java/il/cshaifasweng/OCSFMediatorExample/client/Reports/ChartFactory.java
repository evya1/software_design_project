package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private BorderPane chartBorderPane;

    // Constructor updated to initialize the observable chart data
    public ChartFactory() {
        this.chartData = FXCollections.observableArrayList(createGenericChartData());
        EventBus.getDefault().register(this);
    }

    public void updateChartForSpecificPurchaseTypeAndAmount(PurchaseType purchaseType, int purchasedItemCount) {
        System.out.println("ChartFactory: updateChartForSpecificPurchaseTypeAndAmount: PurchaseType = " + purchaseType + ", Amount = " + purchasedItemCount);

        // Normalize the purchase type name for consistent string comparison
        String normalizedPurchaseTypeName = normalizeProductNameForCaseInsensitiveComparison(purchaseType.name());
        ObservableList<Pair<String, Double>> currentChartDataList = this.chartData;
        boolean existingEntryFoundAndUpdated = false;

        // Iterate through the current chart data to find a matching entry
        for (int index = 0; index < currentChartDataList.size(); index++) {
            Pair<String, Double> currentChartEntry = currentChartDataList.get(index);
            String normalizedKey = normalizeProductNameForCaseInsensitiveComparison(currentChartEntry.getKey());

            // Create a boolean variable with a self-documenting name for the guard condition
            boolean isNormalizedPurchaseTypeMatchingChartEntry = normalizedKey.equals(normalizedPurchaseTypeName);

            if (isNormalizedPurchaseTypeMatchingChartEntry) {
                // Update the value for the matching purchase type
                System.out.println("ChartFactory: updateChartForSpecificPurchaseTypeAndAmount: Found matching entry. Updating value...");
                currentChartDataList.set(index, new Pair<>(currentChartEntry.getKey(), currentChartEntry.getValue() + purchasedItemCount));
                existingEntryFoundAndUpdated = true;
                break;
            }
        }

        // If no existing entry was found, add a new one for this purchase type
        if (!existingEntryFoundAndUpdated) {
            System.out.println("ChartFactory: updateChartForSpecificPurchaseTypeAndAmount: No existing entry found. Adding new entry for: " + normalizedPurchaseTypeName);
            currentChartDataList.add(new Pair<>(normalizedPurchaseTypeName, (double) purchasedItemCount));
        }

        // Update and refresh the chart with the updated data
        refreshAndUpdateChartWithCurrentData(chartBorderPane, currentChartDataList);
        System.out.println("ChartFactory: updateChartForSpecificPurchaseTypeAndAmount: Chart updated for PurchaseType = " + purchaseType + ".");
    }

    private List<Pair<String, Double>> prepareChartDataFromReportEntities(List<Report> reportEntityList) {
        // Check if report data is null or empty, and use generic data if so
        if (reportEntityList == null || reportEntityList.isEmpty()) {
            return createGenericChartData();  // Use generic chart data when no reports are provided
        }
        // Convert report entities into chart data
        return convertReportsToChartData(reportEntityList);
    }

    public void refreshChartWithReportData(List<Report> reportEntityList) {
        System.out.println("ChartFactory: refreshChartWithReportData: Received " + reportEntityList.size() + " reports.");
        if (reportEntityList.isEmpty()) {
            System.out.println("ChartFactory: refreshChartWithReportData: No reports provided. Using default chart data.");
        }

        List<Pair<String, Double>> newChartData = prepareChartDataFromReportEntities(reportEntityList);

        System.out.println("ChartFactory: refreshChartWithReportData: Prepared chart data: " + newChartData);
        refreshAndUpdateChartWithCurrentData(chartBorderPane, FXCollections.observableArrayList(newChartData));

        System.out.println("ChartFactory: refreshChartWithReportData: Chart refreshed with new report data.");
    }

    private <T extends Chart> void updateChartWithNewData(T chartToUpdate, ObservableList<?> newChartData) {
        if (chartToUpdate instanceof PieChart) {
            PieChart pieChart = (PieChart) chartToUpdate;
            pieChart.getData().clear();  // Clear existing data

            // Convert the data from Pair<String, Double> to PieChart.Data
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Object entry : newChartData) {
                Pair<String, Double> pair = (Pair<String, Double>) entry;
                pieChartData.add(new PieChart.Data(pair.getKey(), pair.getValue()));
            }

            pieChart.setData(pieChartData);  // Set new data
            pieChart.setLabelsVisible(true);  // Make sure labels are visible
            pieChart.layout();  // Force layout refresh to display changes
        } else if (chartToUpdate instanceof BarChart) {
            BarChart<String, Number> barChart = (BarChart<String, Number>) chartToUpdate;
            barChart.getData().clear();  // Clear existing data

            // Assuming newChartData is a List of XYChart.Data<String, Number>
            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>((ObservableList<XYChart.Data<String, Number>>) newChartData);
            barChart.getData().add(dataSeries);  // Set new data
            barChart.layout();  // Force layout refresh to display changes
        } else {
            // Log unsupported chart type for the data update
            System.out.println("Unsupported chart type for updating with new data.");
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
     * It uses {@link #calculateTotalProductValueAcrossAllReports(List, String)} to retrieve the counts for each product.
     * The resulting list can be used directly in chart components like {@link BarChart} or {@link PieChart}.
     * </p>
     *
     * @param reports A list of {@link Report} entities to be converted into chart data.
     * @return A list of {@link Pair}&lt;{@link String}, {@link Double}&gt; representing the chart data, with the product name as the key
     *         and the total count as the value.
     */
    private List<Pair<String, Double>> convertReportsToChartData(List<Report> reports) {
        System.out.println("Converting reports to chart data...");

        // Extract counts for each product from the reports
        double productACount = calculateTotalProductValueAcrossAllReports(reports, PURCHASABLE_PRODUCT_A);
        double productBCount = calculateTotalProductValueAcrossAllReports(reports, PURCHASABLE_PRODUCT_B);
        double productCCount = calculateTotalProductValueAcrossAllReports(reports, PURCHASABLE_PRODUCT_C);

        // Print the extracted counts for each product
        System.out.println("Product A Count: " + productACount);
        System.out.println("Product B Count: " + productBCount);
        System.out.println("Product C Count: " + productCCount);

        // Create a list of chart data pairs using the extracted counts
        List<Pair<String, Double>> chartDataList = createChartDataList(productACount, productBCount, productCCount);

        // Print the final chart data list
        System.out.println("Converted chart data: " + chartDataList);

        return chartDataList;
    }

    private double calculateTotalProductValueAcrossAllReports(List<Report> reports, String productName) {
        System.out.println("Calculating total value for product: " + productName);

        // Normalize the provided product name: convert to lowercase, replace underscores with spaces, and trim whitespace
        String normalizedProductName = normalizeProductNameForCaseInsensitiveComparison(productName);

        // Log each report's data for debugging purposes
        logReportDataForGraphs(reports);

        // Calculate the total value for the specified product across all reports
        double totalProductValue = reports.stream()
                // Filter out reports that contain the normalized product name (case-insensitive check)
                .filter(report -> doesReportContainNormalizedProductName(report, normalizedProductName))
                // Extract the product value from the filtered reports
                .mapToDouble(report -> extractProductValueFromReportData(report, normalizedProductName))
                .sum(); // Sum all the values for the product across reports

        System.out.println("Total value for " + productName + ": " + totalProductValue);

        // Return the total value, or the default value if no valid value was found
        return totalProductValue > 0 ? totalProductValue : DEFAULT_PRODUCT_COUNT;
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
        return FXCollections.observableArrayList(
                genericData.stream()
                        .map(mapper)
                        .collect(Collectors.toList())
        );
    }

    // For internal chart data, we can reuse the same function without passing external data
    private <T> ObservableList<T> convertToChartData(Function<Pair<String, Double>, T> mapper) {
        return convertToChartData(this.chartData, mapper);
    }

    public ObservableList<XYChart.Data<String, Number>> convertToBarChartData(List<Pair<String, Double>> chartData) {
        return FXCollections.observableArrayList(
                chartData.stream()
                        .map(pair -> new XYChart.Data<>(pair.getKey(), (Number) pair.getValue()))  // Cast Double to Number
                        .collect(Collectors.toList())
        );
    }

    public ObservableList<PieChart.Data> convertToPieChartData(List<Pair<String, Double>> chartData) {
        return FXCollections.observableArrayList(
                chartData.stream()
                        .map(pair -> new PieChart.Data(pair.getKey(), pair.getValue()))  // Convert Pair<String, Double> to PieChart.Data
                        .collect(Collectors.toList())
        );
    }

    // Usage for internal BarChart data without passing the external chartData list
    public ObservableList<XYChart.Data<String, Number>> convertToBarChartData() {
        return convertToChartData(pair -> new XYChart.Data<>(pair.getKey(), pair.getValue()));
    }

    // Usage for internal PieChart data without passing the external chartData list
    public ObservableList<PieChart.Data> convertToPieChartData() {
        return convertToChartData(pair -> new PieChart.Data(pair.getKey(), pair.getValue()));
    }

    // General method for chart creation
    private <T extends Chart> T createChart(Supplier<T> chartSupplier, String contextDescription) {
        T chart = chartSupplier.get();
        String chartTitle = generateTitle(contextDescription);

        if (chart instanceof BarChart) {
            setupBarChart((BarChart) chart, chartTitle, convertToBarChartData());
        } else if (chart instanceof PieChart) {
            setupPieChart((PieChart) chart, chartTitle, convertToPieChartData());
        }

        customizeChartAppearance(chart);
        return chart;
    }

    private void setupBarChart(BarChart<String, Number> barChart, String chartTitle, ObservableList<XYChart.Data<String, Number>> dataList) {
        barChart.setTitle(chartTitle);
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>(dataList);

        // Ensure that each data point corresponds to a product and is styled differently
        applyCssClasses(dataSeries.getData(), XYChart.Data::getXValue); // Ensure proper CSS class for each bar

        barChart.getData().add(dataSeries);
    }

    // Method to set up PieChart
    private void setupPieChart(PieChart pieChart, String chartTitle, ObservableList<PieChart.Data> dataList) {
        pieChart.setTitle(chartTitle);

        applyCssClasses(dataList, PieChart.Data::getName);

        pieChart.setData(dataList);
    }

    // Method to apply CSS class based on product type, with a listener to handle late node creation
    private <D> void applyCssClasses(ObservableList<D> dataList, Function<D, String> productExtractor) {
        for (D data : dataList) {
            String product = productExtractor.apply(data);
            Node node = getNodeFromData(data);

            // If the node doesn't exist yet, listen for its creation and apply the style later
            if (node == null) {
                if (data instanceof XYChart.Data<?, ?> xyData) {
                    xyData.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            applyCssClassForProduct(product, newNode);
                        }
                    });
                } else if (data instanceof PieChart.Data pieData) {
                    pieData.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            applyCssClassForProduct(product, newNode);
                        }
                    });
                }
            } else {
                applyCssClassForProduct(product, node);
            }
        }
    }

    // Applies a specific CSS class to a node based on the product name
    private void applyCssClassForProduct(String product, Node node) {
        if (PURCHASABLE_PRODUCT_A.equals(product)) {
            node.getStyleClass().add(CHART_CSS_CLASS_PRODUCT_A);
        } else if (PURCHASABLE_PRODUCT_B.equals(product)) {
            node.getStyleClass().add(CHART_CSS_CLASS_PRODUCT_B);
        } else if (PURCHASABLE_PRODUCT_C.equals(product)) {
            node.getStyleClass().add(CHART_CSS_CLASS_PRODUCT_C);
        } else {
            // Optionally handle unknown product types
            node.getStyleClass().add(CHART_CSS_CLASS_DEFAULT_PRODUCT);
        }
    }

    // Helper method to retrieve Node from different chart data types
    private Node getNodeFromData(Object data) {
        if (data instanceof XYChart.Data) {
            return ((XYChart.Data<?, ?>) data).getNode();
        } else if (data instanceof PieChart.Data) {
            return ((PieChart.Data) data).getNode();
        }
        return null;
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
            T chart = chartSupplier.get();
            System.out.println(chart.toString()); // Here, 'T' is a specific chart type (e.g., BarChart, PieChart)
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

    private <T extends Chart> void updateChart(T chart, ObservableList<Pair<String, Double>> newChartData) {
        ObservableList<?> chartData;
        if (chart instanceof PieChart) {
            chartData = convertToPieChartData(FXCollections.observableArrayList(newChartData));  // Casting back to List if needed
        } else if (chart instanceof BarChart) {
            chartData = convertToBarChartData(FXCollections.observableArrayList(newChartData));  // Casting back to List if needed
        } else {
            log("Unsupported chart type for updating chart.");
            return;
        }
        updateChartWithNewData(chart, chartData);
    }

    private void refreshAndUpdateChartWithCurrentData(BorderPane chartDisplayPane, ObservableList<Pair<String, Double>> updatedChartDataList) {
        // Get the current chart from the display pane (either BarChart or PieChart)
        Node existingChartNode = chartDisplayPane.getCenter();

        // Check the type of chart and update accordingly
        if (existingChartNode instanceof Chart chartToUpdate) {
            // Cast the node to a Chart and update its data
            updateChartWithNewData(chartToUpdate, updatedChartDataList);
        } else {
            // Log that no chart was found in the center of the BorderPane
            System.out.println("No existing chart found in the BorderPane to update.");
        }
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
        String reportSpanType = report.getSpanType().toString();
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

    /**
     * Normalizes the product name by converting it to lowercase, replacing underscores with spaces,
     * and trimming any leading or trailing whitespace.
     *
     * @param productName The original product name to be normalized.
     * @return The normalized product name for case-insensitive comparison.
     */
    public String normalizeProductNameForCaseInsensitiveComparison(String productName) {
        return productName.toLowerCase().replace('_', ' ').strip();
    }

    /**
     * Logs the dataForGraphs map for each report in the list, useful for debugging and understanding the reports.
     *
     * @param reports The list of reports whose dataForGraphs maps should be printed.
     */
    private void logReportDataForGraphs(List<Report> reports) {
        for (Report report : reports) {
            System.out.println("Report Data for Graphs: " + report.getDataForGraphs());
        }
    }

    /**
     * Checks if a given report contains the normalized product name in its dataForGraphs map
     * (case-insensitive check, considering underscores replaced by spaces).
     *
     * @param report The report to check.
     * @param normalizedProductName The normalized product name to look for.
     * @return True if the report contains the product, otherwise false.
     */
    private boolean doesReportContainNormalizedProductName(Report report, String normalizedProductName) {
        return report.getDataForGraphs().keySet().stream()
                .anyMatch(productKey -> normalizeProductNameForCaseInsensitiveComparison(productKey)
                        .equals(normalizedProductName));
    }

    /**
     * Extracts the value of the specified product from the report's dataForGraphs map
     * (case-insensitive check, considering underscores replaced by spaces).
     *
     * @param report The report from which to extract the product value.
     * @param normalizedProductName The normalized product name for which to extract the value.
     * @return The value associated with the product in the report, or 0.0 if not found.
     */
    private double extractProductValueFromReportData(Report report, String normalizedProductName) {
        return report.getDataForGraphs().entrySet().stream()
                .filter(entry -> normalizeProductNameForCaseInsensitiveComparison(entry.getKey())
                        .equals(normalizedProductName))
                .mapToDouble(Map.Entry::getValue)
                .findFirst()
                .orElse(0.0);
    }
}
