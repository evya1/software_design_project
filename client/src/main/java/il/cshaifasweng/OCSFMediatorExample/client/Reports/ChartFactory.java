package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;

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
     * Creates data for the PieChart.
     *
     * @return an ObservableList of PieChart.Data to be used in the PieChart.
     */
    public ObservableList<PieChart.Data> createPieChartData() {
        return FXCollections.observableArrayList(
                new PieChart.Data("Product A", 3000),
                new PieChart.Data("Product B", 1500),
                new PieChart.Data("Product C", 300)
        );
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
        barChart.setPrefWidth(CHART_PREF_WIDTH);
        barChart.setPrefHeight(CHART_PREF_HEIGHT);

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

    /**
     * Populates the PieChart with static data and adds it to the provided BorderPane.
     *
     * @param pieChart        the PieChart to which data will be added.
     * @param chartBorderPane the BorderPane where the chart will be displayed.
     */
    public void populatePieChart(PieChart pieChart, BorderPane chartBorderPane) {
        chartBorderPane.setCenter(pieChart);
    }

}
