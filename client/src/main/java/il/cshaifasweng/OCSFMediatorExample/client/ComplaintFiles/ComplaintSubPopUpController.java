package il.cshaifasweng.OCSFMediatorExample.client.ComplaintFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ComplaintSubPopUpController implements ClientDependent {


    @FXML
    private Text firstLine;

    @FXML
    private Button okayBtn;

    @FXML
    private Text secondLine;

    private SimpleClient client;
    private Message localMessage;

    @FXML
    public void initialize() {

        firstLine.setText("Your Complaint has been submitted successfully!");
        secondLine.setText("A response will be sent to your E-mail within 24 hours.");

        okayBtn.setOnAction(event -> okayBtnControl(event));

    }
    @FXML
    void okayBtnControl(ActionEvent event) {
        Stage stage = (Stage) okayBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }
}
