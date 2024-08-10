package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;

public class ReportsScreenController implements ClientDependent {
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
}
