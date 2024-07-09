package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class StyleUtil {
    //Method to change the control border color.
    public static void changeControlBorderColor(Control control, String borderColor) {
        control.setStyle(String.format("-fx-border-color: %s;", borderColor));
    }

    //Method to change the control text color.
    public static void changeControlTextColor(Control control, String textColor) {
        control.setStyle(String.format("-fx-text-fill: %s;", textColor));
    }

    public static void addTextInputListener(Control control, String color) {
        if (control instanceof TextField) {
            ((TextField) control).textProperty().addListener((observable, oldValue, newValue) -> {
                changeControlBorderColor(control, color); // Change border color to blue when typing
            });
        } else if (control instanceof TextArea) {
            ((TextArea) control).textProperty().addListener((observable, oldValue, newValue) -> {
                changeControlBorderColor(control, color); // Change border color to blue when typing
            });
        }
    }


}
