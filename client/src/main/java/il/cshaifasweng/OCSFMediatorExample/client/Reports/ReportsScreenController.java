package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.util.ResourceBundle;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;

public class ReportsScreenController implements ClientDependent, Initializable {
    private final ChartFactory chartFactory = new ChartFactory();  // Use the ChartFactory class
    @FXML
    public Button ExitBtn;
    @FXML
    public Button newBtn;
    @FXML
    public MenuItem closeBtn;
    @FXML
    public BorderPane chartBorderPane;
    @FXML
    public MenuItem showBarChartMenuItem;
    @FXML
    public MenuItem showPieChartMenuItem;
    @FXML
    public MenuItem updateRefreshBtn;
    Message localMessage;
    private SimpleClient client;
    private String previousScreen;  // Store the previous screen's FXML path
    /**
     * Represents the context or scope for generating the chart.
     * This field can be used to determine the specific data or branch context
     * that will influence the content and title of the chart.
     * For example, it might represent a specific branch name,
     * a data filter criterion, or any other relevant context.
     */
    private Object chartContext;

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
     * Handles the action event to display a BarChart in the UI.
     * <p>
     * This method retrieves the context description based on the current chart context (e.g., branch name or all locations)
     * and uses the ChartFactory to create, configure, and display the BarChart in the specified BorderPane.
     * </p>
     *
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     */
    @FXML
    public void handleShowBarChart(ActionEvent actionEvent) {
        String contextDescription = chartFactory.getContextDescription(chartContext);
        chartFactory.prepareAndDisplayBarChart(contextDescription, chartBorderPane);
    }

    /**
     * Handles the action event to display a PieChart in the UI.
     * <p>
     * This method retrieves the context description based on the current chart context (e.g., branch name or all locations)
     * and uses the ChartFactory to create, configure, and display the PieChart in the specified BorderPane.
     * </p>
     *
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     */
    @FXML
    public void handleShowPieChart(ActionEvent actionEvent) {
        String contextDescription = chartFactory.getContextDescription(chartContext);
        chartFactory.prepareAndDisplayPieChart(contextDescription, chartBorderPane);
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

    public Object getChartContext() {
        return chartContext;
    }

    public void setChartContext(Object chartContext) {
        this.chartContext = chartContext;
    }
}
