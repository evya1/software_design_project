package il.cshaifasweng.OCSFMediatorExample.client.ComplaintFiles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ComplaintSubPopUpController {


    @FXML
    private Text firstLine;

    @FXML
    private Button okayBtn;

    @FXML
    private Text secondLine;

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

}
