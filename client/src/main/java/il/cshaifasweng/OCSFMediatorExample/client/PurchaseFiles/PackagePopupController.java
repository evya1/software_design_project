package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PackagePopupController implements ClientDependent {
    private Stage stage;
    private SimpleClient client;
    private Message localMessage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMessage(Message message){this.localMessage = message;}

    public void setClient(SimpleClient client) {
        this.client = client;
    }


    @FXML
    private Button okayBtn;

    @FXML
    public void initialize() {
        okayBtn.setOnAction(this::okayBtnControl);
    }
    @FXML
    void okayBtnControl(ActionEvent event) {
        Stage stage = (Stage) okayBtn.getScene().getWindow();
        stage.close();
    }
}
