package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChartFactory {

    public static final int PIE_CHART_LABEL_LINE_LENGTH = 50;
    public static final int PIE_CHART_START_ANGLE = 180;
    public static final int CHART_PREF_WIDTH = 800;
    public static final int CHART_PREF_HEIGHT = 600;

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
            return "All Branches";
        }
        return contextParameter.toString();
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
            titleBuilder.append("All Branches");
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
        return Arrays.asList(
                new Pair<>("Product A", 3000.0),
                new Pair<>("Product B", 1500.0),
                new Pair<>("Product C", 300.0)
        );
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
        return FXCollections.observableArrayList(
                genericData.stream()
                        .map(pair -> new XYChart.Data<String, Number>(pair.getKey(), pair.getValue()))
                        .collect(Collectors.toList())
        );
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
        return FXCollections.observableArrayList(
                genericData.stream()
                        .map(pair -> new PieChart.Data(pair.getKey(), pair.getValue()))
                        .collect(Collectors.toList())
        );
    }

    /**
     * Creates and configures a BarChart based on the provided context description.
     * <p>
     * This method prepares a BarChart using the context description and generic data. It first generates
     * the data using the {@link #createGenericChartData()} method and then converts it to a format suitable
     * for a BarChart. The BarChart is customized with labels, a title, and other settings.
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
        // Create generic data for the charts
        List<Pair<String, Double>> genericData = createGenericChartData();

        // Convert generic data to BarChart data
        ObservableList<XYChart.Data<String, Number>> barChartData = convertToBarChartData(genericData);

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
     * Prepares and displays a BarChart by creating it and populating it in the provided BorderPane.
     * <p>
     * This method is a convenient wrapper that combines chart creation and display. It generates
     * the BarChart using the provided context description and then adds it to the specified BorderPane
     * for display in the UI.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * chartFactory.prepareAndDisplayBarChart("Branch A", chartBorderPane);
     * }</pre>
     *
     * @param contextDescription the context description (e.g., branch name, all locations).
     * @param chartBorderPane    the BorderPane where the BarChart will be displayed.
     */
    public void prepareAndDisplayBarChart(String contextDescription, BorderPane chartBorderPane) {
        BarChart<String, Number> barChart = createBarChart(contextDescription);
        populateBarChart(barChart, chartBorderPane);
    }

    /**
     * Creates and configures a PieChart based on the provided context description.
     * <p>
     * This method prepares a PieChart using the context description and generic data. It first generates
     * the data using the {@link #createGenericChartData()} method and then converts it to a format suitable
     * for a PieChart. The PieChart is customized with labels, a title, and other settings.
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

        // Create generic data for the charts
        List<Pair<String, Double>> genericData = createGenericChartData();

        // Convert generic data to PieChart data
        ObservableList<PieChart.Data> pieChartData = convertToPieChartData(genericData);

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
     * Prepares and displays a PieChart by creating it and populating it in the provided BorderPane.
     * <p>
     * This method is a convenient wrapper that combines chart creation and display. It generates
     * the PieChart using the provided context description and then adds it to the specified BorderPane
     * for display in the UI.
     * </p>
     * <p><b>Usage:</b></p>
     * <pre>{@code
     * chartFactory.prepareAndDisplayPieChart("Branch A", chartBorderPane);
     * }</pre>
     *
     * @param contextDescription the context description (e.g., branch name, all locations).
     * @param chartBorderPane    the BorderPane where the PieChart will be displayed.
     */
    public void prepareAndDisplayPieChart(String contextDescription, BorderPane chartBorderPane) {
        PieChart pieChart = createPieChart(contextDescription);
        populatePieChart(pieChart, chartBorderPane);
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
    public void populateBarChart(BarChart<String, Number> barChart, BorderPane chartBorderPane) {
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
    public void populatePieChart(PieChart pieChart, BorderPane chartBorderPane) {
        chartBorderPane.setCenter(pieChart);
    }
}
