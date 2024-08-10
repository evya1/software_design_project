package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

public class ChartFactory {

    /**
     * Retrieves the context description for the chart title based on the provided parameter.
     *
     * @param contextParameter an object used to determine the context description (e.g., branch name, all locations).
     * @return the context description as a String.
     */
    public String getContextDescription(Object contextParameter) {

        // Example: Logic to determine context description based on the parameter
        if (contextParameter == null) {
            return "All Branches";
        }

        // Example usage could be String, Integer, or any other type in the future
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
     * Creates and configures a BarChart based on the provided context description.
     * The chart is customized with appropriate labels, title, and appearance settings.
     *
     * @param contextDescription the context for which the chart is created (e.g., branch name, all locations).
     * @return a fully configured BarChart instance.
     */
    public BarChart<String, Number> createBarChart(String contextDescription) {
        String chartTitle = generateTitle(contextDescription);

        // Initialize the axes with labels
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Dates");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity Sold");

        // Create and configure the bar chart with axes and title
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(chartTitle);

        // Customize the appearance and behavior of the bar chart
        barChart.setLegendVisible(false);  // Hide the legend, since it's not needed for this chart.
        barChart.setAnimated(false);       // Disable animations for a static display of the chart.
        barChart.setPrefWidth(800);
        barChart.setPrefHeight(600);

        return barChart;
    }

    /**
     * Populates the BarChart with static data and adds it to the provided BorderPane.
     * This method handles the addition of data series to the chart and integrates the chart into the UI.
     *
     * @param barChart        the BarChart to which data will be added.
     * @param chartBorderPane the BorderPane where the chart will be displayed.
     */
    public void populateBarChart(BarChart<String, Number> barChart, BorderPane chartBorderPane) {

        // Prepare the data series
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Product Sold");

        // Provide static data for the chart
        dataSeries.getData().add(new XYChart.Data<>("Product A", 3000));
        dataSeries.getData().add(new XYChart.Data<>("Product B", 1500));
        dataSeries.getData().add(new XYChart.Data<>("Product C", 500));

        // Add data to the bar chart
        barChart.getData().add(dataSeries);

        // Add the bar chart to the BorderPane
        chartBorderPane.setCenter(barChart);
    }
}
