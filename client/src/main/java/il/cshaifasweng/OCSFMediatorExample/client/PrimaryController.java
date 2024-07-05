package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PrimaryController implements ClientDependent {
    @FXML
    private Button catalogButton;
    private SimpleClient client;

    @FXML
    public void catalogController(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) catalogButton.getScene().getWindow();
            client.moveScene("catalogM/movieCatalog", stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize()
    {
        Message message = new Message();
        message.setMessage("new client");
        client.sendMessage(message);
    }
    public void setClient(SimpleClient client) {
        this.client = client;
    }
}

