package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Objects;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PrimaryController {
    @FXML
    private Button catalogButton;

    @FXML
    public void catalogController(ActionEvent event) {
        try {
            Stage stage = (Stage) catalogButton.getScene().getWindow();
            //TODO: Change the primary.fxml to the main scene.
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("movieCatalog/movieCatalog.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            SimpleClient.sendMessage("show all movies");

        }
        catch (IOException e){
            showAlert(Alert.AlertType.ERROR, "IO Error","An unexpected error occurred. Please try again.");
        }


    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
}
}

