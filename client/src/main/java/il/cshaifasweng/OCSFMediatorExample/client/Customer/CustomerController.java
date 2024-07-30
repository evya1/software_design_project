package il.cshaifasweng.OCSFMediatorExample.client.Customer;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CustomerController implements ClientDependent {

    private SimpleClient client;
    private Message localMessage;
    private boolean connectedFlag = false;
    private Customer localCustomer;

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
    private TableColumn<Complaint, Integer> complaintIdCol;

    @FXML
    private TableColumn<Complaint, String> complaintTitleCol;

    @FXML
    private TableColumn<Complaint, String> complaintDescriptionCol;

    @FXML
    private TableColumn<Complaint, String> complaintStatusCol;

    @FXML
    private TableColumn<Complaint, LocalDateTime> dateOfComplaintCol;

    @FXML
    private TableColumn<Complaint, PurchaseType> typeOfPurchaseCol;

    @FXML
    private TableView<Complaint> tableViewComplaints;



    @FXML
    void initialize() {
        loggedOutButtons();
        EventBus.getDefault().register(this);


        //Initialize the table values
        //Initialize the columns
        complaintIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        complaintTitleCol.setCellValueFactory(new PropertyValueFactory<>("complaintTitle"));
        complaintDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("complaintContent"));
        complaintStatusCol.setCellValueFactory(new PropertyValueFactory<>("complaintStatus"));
        typeOfPurchaseCol.setCellValueFactory(new PropertyValueFactory<>("purchaseType"));
        // Setting up the date column with a custom format
        dateOfComplaintCol.setCellFactory(column -> new TableCell<Complaint, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    // Format the LocalDateTime
                    setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                }
            }
        });

        // Make sure to set the cell value factory if not already set
        dateOfComplaintCol.setCellValueFactory(new PropertyValueFactory<>("dateOfComplaint"));

    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        Platform.runLater(() -> {
            Message message = event.getMessage();
            String displayMessage = "Customer wasn't found";
            if (message.getCustomer() != null) {
                localCustomer = message.getCustomer();
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
        if (localCustomer == null) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "No Customer", "You must be logged in to view complaints.");
            return;
        }

        // Assuming complaints are fetched and stored in localCustomer object correctly
        if (localCustomer.getComplaints() != null && !localCustomer.getComplaints().isEmpty()) {
            tableViewComplaints.getItems().clear(); // Clear existing items
            tableViewComplaints.getItems().addAll(localCustomer.getComplaints()); // Add new items
            tableViewComplaints.refresh(); // Refresh the table view to display new data
        } else {
            SimpleClient.showAlert(Alert.AlertType.INFORMATION, "No Complaints", "There are no complaints to display.");
        }
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
