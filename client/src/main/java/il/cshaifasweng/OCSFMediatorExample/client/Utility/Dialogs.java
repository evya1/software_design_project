package il.cshaifasweng.OCSFMediatorExample.client.Utility;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PRIMARY_SCREEN;

public class Dialogs {

    public static void yesNoDialog(String message, Runnable onYes, Runnable onNo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Customize the dialog pane (optional)
        DialogPane dialogPane = alert.getDialogPane();
        // dialogPane.getStylesheets().add(getClass().getResource("your-stylesheet.css").toExternalForm()); // if you have a stylesheet

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            onYes.run();
        } else {
            onNo.run();
        }
    }

    public static void popUpAndReturnToMainScreen(SimpleClient client, Control controlScene, String header, String content){
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Create a "Confirm" button and add it to the alert
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(confirmButton);

        // Handle the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                // User confirmed, perform the action
                Stage newStage = (Stage) controlScene.getScene().getWindow();
                client.moveScene(PRIMARY_SCREEN, newStage, null);
            }
        });
    }
}
