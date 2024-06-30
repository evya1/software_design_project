package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PrimaryController {
    @FXML
    private Button catalogButton;

    @FXML
    public void catalogController(ActionEvent event) {
        try {
            SimpleClient.sendMessage("show all movies");
            Stage stage = (Stage) catalogButton.getScene().getWindow();
            SimpleClient.moveScene("movieCatalog/movieCatalog.fxml",stage);
        } catch (Exception e) {
           e.printStackTrace();
        }


    }



}

