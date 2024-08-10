package il.cshaifasweng.OCSFMediatorExample.client.ComplaintFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;

import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.changeControlBorderColor;
import static il.cshaifasweng.OCSFMediatorExample.client.Utility.Dialogs.popUpAndReturnToMainScreen;

public class ComplaintSubmissionController implements ClientDependent {
    public ComboBox<Branch> branchComboBox;
    public CheckBox branchCheckBox;
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
        purchasedItemCombobox.setItems(FXCollections.observableArrayList(PurchaseType.values()));
        purchasedItemCombobox.setDisable(true);
        branchComboBox.setDisable(true);
        submitComplaintBtn.setOnAction(event -> submitComplaintBtnController(event));
        returnBtn.setOnAction(event -> returnBtnController(event));
        otherCheckbox.setOnAction(event -> otherCheckController(event));
        purchasedCheckbox.setOnAction(event -> purchasedCheckController(event));

        Message message = new Message();
        message.setMessage(BRANCH_THEATER_INFORMATION);
        message.setData(GET_BRANCHES);
        client.sendMessage(message);
    }

    @Subscribe
    public void handleComplaintFromServer(MessageEvent event){
        if (event != null) {
            Message message = event.getMessage();
            //Handling new Complaint from server.
            if(message.getMessage().equals(GET_COMPLAINT_REQUEST)){
                Platform.runLater(() -> {

                    Complaint complaint = message.getComplaint();
                    System.out.println("Complaint received: " + complaint);
                    // Show confirmation dialog
                    popUpAndReturnToMainScreen(client, submitComplaintBtn,
                            "Your Complaint has been submitted successfully!",
                            "A response will be sent to your E-mail within 24 hours.");
                });
            }
            //Handling Branch or Theater related information.
            if(message.getMessage().equals(BRANCH_THEATER_INFORMATION)){
                if(message.getData().equals(GET_BRANCHES)){
                    Platform.runLater(()->{
                        branchComboBox.getItems().addAll(message.getBranches());
                    });
                }
            }

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
        EventBus.getDefault().unregister(this);
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
        message.setBranch(branchComboBox.getSelectionModel().getSelectedItem());

        if(flag == 0) {
            System.out.println("Complaint Clear... Sending to Server...");
            client.sendMessage(message);
        }

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
        if(purchasedCheckbox.isSelected()){
            //If the purchase type is Movie Ticket a branch is required.
            PurchaseType type = purchasedItemCombobox.getSelectionModel().getSelectedItem();
            if(type == PurchaseType.MOVIE_TICKET && branchCheckBox.isSelected() && branchComboBox.getSelectionModel().isEmpty()){
                changeControlBorderColor(branchComboBox,"red");
                return 1;
            }
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
        changeControlBorderColor(branchComboBox,"null");
    }

    @FXML
    public void selectBranchCBAction(ActionEvent event) {

    }

    @FXML
    public void relatedBranchAction(ActionEvent event) {
        boolean isChecked = branchCheckBox.isSelected();
        if(isChecked){
            branchComboBox.setDisable(false);
        }
        else{
            branchComboBox.setDisable(true);
            branchComboBox.getSelectionModel().clearSelection();
        }
    }
}
