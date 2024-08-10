package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.util.ResourceBundle;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;

public class ReportsScreenController implements ClientDependent, Initializable {
    @FXML
    public Button ExitBtn;
    @FXML
    public Button newBtn;
    @FXML
    public MenuItem closeBtn;
    @FXML
    public BorderPane ChartBorderPane;
    @FXML
    public MenuItem showBarChartMenuItem;
    @FXML
    public MenuItem showPieChartMenuItem;
    @FXML
    public MenuItem updateRefreshBtn;
    Message localMessage;
    private SimpleClient client;

    private String previousScreen;  // Store the previous screen's FXML path

    @FXML
    public void handleBackAction(ActionEvent actionEvent) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Message message = new Message();
            message.setMessage("Back to " + previousScreen.replace("_SCREEN", "").replace("_", " ").toLowerCase() + " screen");
            message.setSourceFXML(REPORTS_SCREEN);
            message.setEmployee(localMessage.getEmployee());
            EventBus.getDefault().unregister(this);
            client.moveScene(previousScreen, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
        this.previousScreen = message.getSourceFXML();  // Store the previous screen's FXML path
    }

    public void handleNewBtn(ActionEvent actionEvent) {
    }

    /**
     * Generates a title for the chart based on the given context.
     *
     * @param contextDescription a description of the context, such as a branch name or scope of the data.
     * @return a dynamically generated chart title as a String.
     */
    private String generateChartTitle(String contextDescription) {
        StringBuilder titleBuilder = new StringBuilder("Overview: ");

        if (contextDescription == null || contextDescription.isEmpty()) {
            titleBuilder.append("All Branches");
        } else {
            titleBuilder.append(contextDescription);
        }

        return titleBuilder.toString();
    }

    /**
     * Creates and configures a BarChart with specified parameters.
     *
     * @param chartTitle the title of the chart, based on the data context.
     * @return a fully configured BarChart instance.
     */
    private BarChart<String, Number> createBarChart(String chartTitle) {

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
     * Retrieves the context description for the chart title.
     * This method simulates fetching the context description, such as the branch name,
     * which could be provided by another controller or field in the application.
     *
     * @return the context description as a String.
     */
    private String getContextDescription() {
        // Example: This could come from another controller, a field, or external logic
        return "Specific Branch"; // Replace with actual logic to fetch context
    }


    /**
     * Handles the action of displaying a bar chart in the report screen.
     *
     * @param actionEvent the event that triggers the bar chart display.
     */
    public void handleShowBarChart(ActionEvent actionEvent) {

        // Retrieve context description (e.g., branch name, all locations) from an external source
        String contextDescription = getContextDescription();  // Method or field that provides the context
        String chartTitle = generateChartTitle(contextDescription);

        // Initialize and configure the bar chart include its title
        BarChart<String, Number> barChart = createBarChart(chartTitle);

        // Prepare the data series
        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.setName("Product Sold");

        // Provide static data for the chart
        data.getData().add(new XYChart.Data<>("Product A", 3000));
        data.getData().add(new XYChart.Data<>("Product B", 1500));
        data.getData().add(new XYChart.Data<>("Product C", 500));

        // Add data to the bar chart
        barChart.getData().add(data);

        // Add the bar chart to the BorderPane
        ChartBorderPane.setCenter(barChart);
    }

    public void handleShowPieChart(ActionEvent actionEvent) {
    }

    public void handleCloseBtn(ActionEvent actionEvent) {
    }

    public void handleUpdateData(ActionEvent actionEvent) {

    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
