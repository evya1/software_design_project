package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
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
     * Handles the action of displaying a bar chart in the report screen.
     *
     * @param actionEvent the event that triggers the bar chart display.
     */
    public void handleShowBarChart(ActionEvent actionEvent) {

        // Initialize and configure the bar chart
        BarChart<String, Number> barChart = getBarChart();

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

    /**
     * Creates and configures a BarChart with predefined settings.
     *
     * @return a configured BarChart instance.
     */
    private BarChart<String, Number> getBarChart() {

        // Initialize the axes with labels
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Dates");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity Sold");

        // Create and configure the bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Report of " + localMessage.getSourceFXML());
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setPrefWidth(800);
        barChart.setPrefHeight(600);

        return barChart;
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
