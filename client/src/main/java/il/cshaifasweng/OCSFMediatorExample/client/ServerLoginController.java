/**
 * Sample Skeleton for 'ServerLogin.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Objects;

public class ServerLoginController {

    public TextField portTextField;
    @FXML // fx:id="IpSelection"
    private ToggleGroup IpSelection; // Value injected by FXMLLoader

    @FXML // fx:id="connectBtn"
    private Button connectBtn; // Value injected by FXMLLoader

    @FXML // fx:id="localhostRadioBtn"
    private RadioButton localhostRadioBtn; // Value injected by FXMLLoader

    @FXML // fx:id="specificIpBtn"
    private RadioButton specificIpRadioBtn; // Value injected by FXMLLoader

    @FXML // fx:id="specificIpTextField"
    private TextField ipTextField; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        // Initialize the text field state based on the selected radio button
        ipTextField.setEditable(false);
        portTextField.setEditable(false);
    }

    @FXML
    void connectToServer(ActionEvent event) {
        if (specificIpRadioBtn.isSelected()) {
            SimpleClient.setIpAddress(ipTextField.getText());
            SimpleClient.setClientPort(Integer.parseInt(portTextField.getText()));
        } else {
            SimpleClient.setIpAddress("localhost");
        }

        SimpleClient client = SimpleClient.getClient();
        try {
            client.openConnection();
            // Load the primary screen after successfully connecting
            Stage stage = (Stage) connectBtn.getScene().getWindow();
            //TODO: Change the primary.fxml to the main scene.
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("primary.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (ConnectException e) {
            // Show an alert if the connection is refused
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to the server. Please check the IP address or Port and try again.");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle other IO exceptions
            showAlert(Alert.AlertType.ERROR, "IO Error", "An unexpected error occurred. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Button getConnectBtn() {
        return connectBtn;
    }

    public void ipAddress(ActionEvent actionEvent) {
        ipTextField.setEditable(specificIpRadioBtn.isSelected());
        portTextField.setEditable(specificIpRadioBtn.isSelected());
        if(localhostRadioBtn.isSelected()){
            portTextField.setText("");
            ipTextField.setText("");
        }
        
    }
}