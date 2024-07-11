package il.cshaifasweng.OCSFMediatorExample.client.Utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;

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
}
