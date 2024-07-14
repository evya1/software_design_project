package il.cshaifasweng.OCSFMediatorExample.client.ComplaintFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Payment;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.ResourceBundle;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.changeControlBorderColor;
import static java.awt.Color.red;

public class ComplaintSubmissionController implements ClientDependent {
    private Message localMessage;
    private SimpleClient client;

    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {this.localMessage = message;}

    @FXML
    private TextArea complaintTxtArea;

    @FXML
    private Text headlineTxt;

    @FXML
    private TextField idField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField mailField;

    @FXML
    private CheckBox otherCheckbox;

    @FXML
    private TextField privateNameField;

    @FXML
    private CheckBox purchasedCheckbox;

    @FXML
    private ComboBox<PurchaseType> purchasedItemCombobox;

    @FXML
    private Button returnBtn;

    @FXML
    private Text subHeadlineTxt;

    @FXML
    private Button submitComplaintBtn;

    @FXML
    private TextField complaintTitleField;



    @FXML
    public void initialize() {

        EventBus.getDefault().register(this);

        headlineTxt.setText("Enter Your Personal Information:");
        subHeadlineTxt.setText("How can we help?");
        complaintTitleField.setPromptText("Enter Complaint Title");
        complaintTxtArea.setPromptText("Enter Your Complaint Here:");
        purchasedItemCombobox.setItems(FXCollections.observableArrayList(PurchaseType.values()));
        purchasedItemCombobox.setDisable(true);
        purchasedItemCombobox.setPromptText("Choose Purchased Item");
        privateNameField.setPromptText("Private Name");
        idField.setPromptText("9 Digits ID Number");
        lastNameField.setPromptText("Last Name");
        mailField.setPromptText("Email Address");
        purchasedCheckbox.setText("I want to complain on a Purchased Item");
        otherCheckbox.setText("Other");

        submitComplaintBtn.setOnAction(event -> submitComplaintBtnController(event));
        returnBtn.setOnAction(event -> returnBtnController(event));
        otherCheckbox.setOnAction(event -> otherCheckController(event));
        purchasedCheckbox.setOnAction(event -> purchasedCheckController(event));
    }

    @Subscribe
    public void handleComplaintFromServer(GenericEvent<Complaint> event){
        if (event.getData() != null) {
            Platform.runLater(() -> {
                Complaint complaint = event.getData();
                System.out.println("Complaint received: " + complaint);

                loadSubmissionPopupScreen();

            });
        }else {
            System.out.println("Invalid event data or not a Complaint instance");
        }
    }

    @FXML
    void otherCheckController(ActionEvent event) {
        if (otherCheckbox.isSelected()) {
            purchasedCheckbox.setDisable(true);
            purchasedItemCombobox.setDisable(true);
        }
        else{
            purchasedCheckbox.setDisable(false);
        }

    }

    @FXML
    void purchasedCheckController(ActionEvent event) {
        if (purchasedCheckbox.isSelected()) {
            otherCheckbox.setDisable(true);
            purchasedItemCombobox.setDisable(false);
        }
        else {
            otherCheckbox.setDisable(false);
            purchasedItemCombobox.setDisable(true);
        }
    }

    @FXML
    void returnBtnController(ActionEvent event) {
        Stage stage = (Stage) returnBtn.getScene().getWindow();
        client.moveScene(localMessage.getSourceFXML(), stage, null);
    }

    @FXML
    void submitComplaintBtnController(ActionEvent event) {

        int flag = checkFields();

        Message message = new Message();
        message.setCustomer(new Customer());
        Customer customer = message.getCustomer();

        customer.setFirstName(privateNameField.getText());
        customer.setLastName(lastNameField.getText());
        customer.setPersonalID(idField.getText());
        customer.setEmail(mailField.getText());

        message.setComplaintTitle(complaintTitleField.getText());
        message.setData(complaintTxtArea.getText());
        message.setPurchaseType(purchasedItemCombobox.getValue());
        message.setCustomer(customer);
        message.setMessage(localMessage.getMessage());

        if(flag == 0) {
            System.out.println("Complaint Clear... Sending to Server...");
            client.sendMessage(message);
        }

    }

    private void loadSubmissionPopupScreen() {
        Stage stage = new Stage();
        client.moveScene(SUBMISSION_POP_UP_MESSAGE,stage ,null);
        Stage newStage = (Stage) submitComplaintBtn.getScene().getWindow();
        client.moveScene(PRIMARY_SCREEN,newStage,null);
    }

    public int checkFields(){
        colorAllTextBorders();
        if (!purchasedCheckbox.isSelected() && !otherCheckbox.isSelected()) {
            changeControlBorderColor(purchasedCheckbox, "red");
            changeControlBorderColor(otherCheckbox, "red");
            return 1;
        }
        else if (purchasedCheckbox.isSelected() && purchasedItemCombobox.getValue() == null) {
            changeControlBorderColor(purchasedItemCombobox, "red");
            return 1;
        }
        if(privateNameField.getText().isEmpty()){
            changeControlBorderColor(privateNameField, "red");
            return 1;
        }
        if(lastNameField.getText().isEmpty()){
            changeControlBorderColor(lastNameField, "red");
            return 1;
        }
        if(idField.getText().isEmpty()){
            changeControlBorderColor(idField, "red");
            return 1;
        } else if (idField.getText().length() != 9) {
            changeControlBorderColor(idField, "red");
            idField.setPromptText("Enter 9 digits ID");
            return 1;
        }
        if(mailField.getText().isEmpty()){
            changeControlBorderColor(mailField, "red");
            return 1;
        }
        if(complaintTitleField.getText().isEmpty()){
            changeControlBorderColor(complaintTitleField, "red");
            return 1;
        }
        if(complaintTxtArea.getText().isEmpty()){
            changeControlBorderColor(complaintTxtArea, "red");
            return 1;
        }
        return 0;
    }

    public void colorAllTextBorders(){
        changeControlBorderColor(purchasedCheckbox, "null");
        changeControlBorderColor(purchasedItemCombobox, "null");
        changeControlBorderColor(privateNameField, "null");
        changeControlBorderColor(lastNameField, "null");
        changeControlBorderColor(idField, "null");
        changeControlBorderColor(mailField, "null");
        changeControlBorderColor(complaintTitleField, "null");
        changeControlBorderColor(complaintTxtArea, "null");
        changeControlBorderColor(otherCheckbox, "null");
    }

}
