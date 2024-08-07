package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PRIMARY_SCREEN;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;

public class ReportsScreenController implements ClientDependent {
    private SimpleClient client;

    @FXML
    public void handleBackAction(ActionEvent actionEvent) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            Message message = new Message();
            message.setMessage("Back to Primary");
            message.setSourceFXML(REPORTS_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(PRIMARY_SCREEN, stage, message);
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
        // Handle any necessary message actions here if needed
    }
}
