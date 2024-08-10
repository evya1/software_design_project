package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.util.ResourceBundle;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;

public class ReportsScreenController implements ClientDependent, Initializable {
    private Message localMessage;
    private SimpleClient client;
    private Employee employee;

    private String previousScreen;  // Store the previous screen's FXML path

    @FXML
    public void handleBackAction(ActionEvent actionEvent) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            if (actionEvent.getSource() instanceof javafx.scene.Node) { // Check if the event source can be cast to a Node
                Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
                Message message = new Message();
                message.setMessage("Back to " + previousScreen.replace("_SCREEN", "").replace("_", " ").toLowerCase() + " screen");
                message.setSourceFXML(REPORTS_SCREEN);
                message.setEmployee(getLocalMessage().getEmployee());
                EventBus.getDefault().unregister(this);
                client.moveScene(previousScreen, stage, message);
            } else {
                System.err.println("Action event source is not a Node, cannot retrieve the stage.");
            }
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
        setLocalMessage(message);
        this.previousScreen = message.getSourceFXML();  // Store the previous screen's FXML path
        Message localMessage = getLocalMessage();
        if (localMessage != null) {
            setEmployee(localMessage.getEmployee());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Message localMessage = getLocalMessage();
        if (localMessage != null) {
            setEmployee(localMessage.getEmployee());
        }
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Message getLocalMessage() {
        return localMessage;
    }

    public void setLocalMessage(Message localMessage) {
        this.localMessage = localMessage;
    }
}
