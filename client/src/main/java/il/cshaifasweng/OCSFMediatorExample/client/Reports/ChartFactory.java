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
     * The chart is customized with appropriate labels, title, and appearance settings.
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
     * Creates and configures a PieChart based on the provided context description.
     * The chart is customized with appropriate labels, title, and appearance settings.
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
     * Populates the BarChart with data and adds it to the provided BorderPane.
     *
     * @param barChart        the BarChart to be populated and displayed.
     * @param chartBorderPane the BorderPane where the chart will be displayed.
     */
    public void populateBarChart(BarChart<String, Number> barChart, BorderPane chartBorderPane) {
        chartBorderPane.setCenter(barChart);
    }

    /**
     * Populates the PieChart with data and adds it to the provided BorderPane.
     *
     * @param pieChart        the PieChart to be populated and displayed.
     * @param chartBorderPane the BorderPane where the chart will be displayed.
     */
    public void populatePieChart(PieChart pieChart, BorderPane chartBorderPane) {
        chartBorderPane.setCenter(pieChart);
    }
}
