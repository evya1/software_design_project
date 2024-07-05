/**
 * Sample Skeleton for 'ServerLogin.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.ConnectException;




public class ServerLoginController implements ClientDependent {

    private SimpleClient client;

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
        portTextField.setText("");
    }

    @FXML
    void connectToServer(ActionEvent event) {
        // Declare variables for IP address and port
        String ipAddress;
        int port;

        // Check if the specific IP radio button is selected
        if (specificIpRadioBtn.isSelected()) {

            // Check if the IP or port text fields are empty
            if (ipTextField.getText().isEmpty() || portTextField.getText().isEmpty()) {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Field Empty Error", "Please Enter IP Address and Port Number");
                return;
            }
            try {
                // Try to read the IP address and port from the text fields
                ipAddress = ipTextField.getText();
                port = Integer.parseInt(portTextField.getText());
            } catch (NumberFormatException e) {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Parse failed", "Please enter a valid IP address and port");
                return;
            }
        } else {
            // Set the default IP address to localhost if the specific IP is not selected
            ipAddress = "localhost";
            if (!portTextField.getText().isEmpty()) {
                try {
                    port = Integer.parseInt(portTextField.getText());
                } catch (NumberFormatException e) {
                    SimpleClient.showAlert(Alert.AlertType.ERROR, "Parse failed", "Please enter a valid port");
                    return;
                }
            } else {
                // Set default port to 3000 if no port is specified
                port = 3000;
            }
        }

        // At this point, ipAddress and port should be set to valid values.
        client = new SimpleClient(ipAddress, port);
        try {
            client.openConnection();
            Stage stage = (Stage) connectBtn.getScene().getWindow();
            client.moveScene("primary", stage);
        } catch (ConnectException e) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to the server. Please check the IP address or Port and try again.");
            client = null;
        } catch (IOException e) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "IO Error", "An unexpected error occurred. Please try again.");
            client = null;
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

    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }
}
