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
import javassist.Loader;

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
            if (ipTextField.getText().isEmpty() || portTextField.getText().isEmpty()) {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Field Empty Error", "Please Enter IP Address and Port Number");
                return;
            }
            try {
                SimpleClient.setIpAddress(ipTextField.getText());
                System.out.println("LOG: Server address given " + ipTextField.getText());
                SimpleClient.setClientPort(Integer.parseInt(portTextField.getText()));
                System.out.println("LOG: Server port given " + Integer.parseInt(portTextField.getText()));
            } catch (NumberFormatException e) {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Parse failed","Please enter a valid IP address and port");
                return;
            }
        } else {
            SimpleClient.setIpAddress("localhost");
        }

        SimpleClient client = SimpleClient.getClient();
        try {
            client.openConnection();
            // Load the primary screen after successfully connecting
            Stage stage = (Stage) connectBtn.getScene().getWindow();
            //TODO: Change the primary.fxml to the main scene.
            SimpleClient.moveScene("primary", stage);
        } catch (ConnectException e) {
            // Show an alert if the connection is refused
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to the server. Please check the IP address or Port and try again.");

        } catch (IOException e) {
            //e.printStackTrace();
            // Handle other IO exceptions
            SimpleClient.showAlert(Alert.AlertType.ERROR, "IO Error", "An unexpected error occurred. Please try again.");

        }
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
