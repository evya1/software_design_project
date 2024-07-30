package il.cshaifasweng.OCSFMediatorExample.client.Customer;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;


public class CustomerController implements ClientDependent {

    private SimpleClient client;
    private Message localMessage;
    private boolean connectedFlag = false;

    @FXML
    private Button homeScreenBtn;

    @FXML
    private Button loginLogoutBtn;

    @FXML
    private Button submitComplaintBtn;


    @FXML
    private Button viewComplaintsBtn;

    @FXML
    private Button viewPurchasesBtn;


    @FXML
    private Label welcomeLabel;


    @FXML
    void initialize() {
        loggedOutButtons();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        Platform.runLater(() -> {
            Message message = event.getMessage();
            String displayMessage = "Customer wasn't found";
            if (message.getCustomer() != null) {
                displayMessage = message.getCustomer().getFirstName() + " " + message.getCustomer().getLastName();
                welcomeLabel.setText("Welcome " + message.getCustomer().getFirstName() + " " + message.getCustomer().getLastName() + " Choose the information you wish to view from the side menu");
                loggedInButtons();
                SimpleClient.showAlert(Alert.AlertType.INFORMATION, "Customer connected", displayMessage);
            } else {
                connectedFlag = false;
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Wrong information", displayMessage);
            }
        });
    }

    @FXML
    void complaintController(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) submitComplaintBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("New Complaint");
            message.setSourceFXML(CUSTOMER_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(COMPLAINT_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loginLogoutAction(ActionEvent event) {
        if (connectedFlag) {
            loggedOutButtons();
            SimpleClient.showAlert(Alert.AlertType.INFORMATION, "Logged out", "Logged out successfully.");

            // Perform logout
        } else {
            try {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Customer sign In");
                ButtonType signInButtonType = new ButtonType("Sign In", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(signInButtonType, ButtonType.CANCEL);
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                PasswordField id = new PasswordField();
                id.setPromptText("Enter your id");

                grid.add(new Label("Customer ID:"), 0, 1);
                grid.add(id, 1, 1);

                dialog.getDialogPane().setContent(grid);

                // Set the result converter for the dialog
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == signInButtonType) {
                        return id.getText();
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(customerID -> {
                    if (customerID != null && !customerID.isEmpty()) {
                        System.out.println(customerID);
                        Message message = new Message();
                        message.setMessage(GET_CUSTOMER_INFO);
                        message.setData(GET_CUSTOMER_ID);
                        message.setCustomerID(customerID);
                        client.sendMessage(message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loggedInButtons(){
        connectedFlag = true;
        loginLogoutBtn.setText("Logout");
        homeScreenBtn.setDisable(true);
        viewComplaintsBtn.setDisable(false);
        viewPurchasesBtn.setDisable(false);
        viewComplaintsBtn.setVisible(true);
        viewPurchasesBtn.setVisible(true);
    }

    private void loggedOutButtons(){
        connectedFlag = false;
        homeScreenBtn.setDisable(false);
        viewComplaintsBtn.setDisable(true);
        viewPurchasesBtn.setDisable(true);
        viewComplaintsBtn.setVisible(false);
        viewPurchasesBtn.setVisible(false);
        loginLogoutBtn.setText("Login");
        welcomeLabel.setText("Welcome to the customer panel, login to view your information");
    }
    @FXML
    void returnHomeAction(ActionEvent event) {
        EventBus.getDefault().unregister(this);
        // Navigate to the home screen
    }


    @FXML
    void viewComplaintsAction(ActionEvent event) {

    }

    @FXML
    void viewPurchasesAction(ActionEvent event) {

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
