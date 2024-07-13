package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Booklet;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Payment;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.BOOKLET_POP_UP_MESSAGE;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PRIMARY_SCREEN;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.changeControlBorderColor;


public class PurchaseController implements ClientDependent {
    private Message localMessage;
    private SimpleClient client;
    private Scene prevScreen;
    private Purchase purchase;

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public void setPrevScreen(Scene prevScreen) {
        this.prevScreen = prevScreen;
    }

    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }

    @FXML
    private TextField cardNumField;

    @FXML
    private Text cardNumber;

    @FXML
    private Button confirmPurchaseBtn;

    @FXML
    private Text cvv;

    @FXML
    private TextField cvvField;

    @FXML
    private Text id;

    @FXML
    private TextField idNumberField;

    @FXML
    private Text lastName;

    @FXML
    private Text privateName;

    @FXML
    private TextField privateNameField;

    @FXML
    private Button returnBtn;

    @FXML
    private TextField surnameNameField;

    @FXML
    private Text email;

    @FXML
    private TextField emailField;

    @FXML
    private Text expireDate;

    @FXML
    private TextField expireField;

    private int flag; //TODO: Change the flag .

    @FXML
    public void initialize() {

        EventBus.getDefault().register(this);

        privateName.setText("Private Name:");
        privateNameField.setPromptText("Enter Private Name");
        lastName.setText("Last Name:");
        surnameNameField.setPromptText("Enter Last Name");
        id.setText("ID:");
        idNumberField.setPromptText("Enter 9 Digits ID");
        cardNumber.setText("Card Number:");
        cardNumField.setPromptText("Enter Card Number");
        cvv.setText("CVV:");
        cvvField.setPromptText("Enter CVV");
        email.setText("Email:");
        emailField.setPromptText("Enter Email Address");
        expireDate.setText("Expire Date:");
        expireField.setPromptText("Enter Credit Expiration Date (MM/YY)");


        confirmPurchaseBtn.setOnAction(event -> confirmBtnControl(event));
        returnBtn.setOnAction(event -> returnBtnControl(event));
    }

    @Subscribe
    public void handlePurchaseFromServer(GenericEvent<Purchase> event) {
        if (event.getData() != null && event.getData() instanceof Purchase) {
            Platform.runLater(() -> {
                //TODO: Add option per type.
                Purchase purchase = event.getData();
                System.out.println("Purchase received: " + purchase);
                loadBookletPopupScreen();
            });
        } else {
            System.out.println("Invalid event data or not a Purchase instance");
        }
    }

    private void loadBookletPopupScreen() {
        Stage stage = new Stage();
        client.moveScene(BOOKLET_POP_UP_MESSAGE,stage ,null);
        Stage newStage = (Stage) confirmPurchaseBtn.getScene().getWindow();
        client.moveScene(PRIMARY_SCREEN,newStage,null);
    }


    @FXML
    private void confirmBtnControl(ActionEvent event) {
        int[] errorFlag = new int[1];
        errorFlag[0] = 0;
        checkInput(errorFlag);

        Message message = new Message();
        message.setCustomer(new Customer());
        Customer customer = message.getCustomer();
        customer.setFirstName(privateNameField.getText());
        customer.setLastName(surnameNameField.getText());
        customer.setPersonalID(idNumberField.getText());
        customer.setEmail(emailField.getText());

        Payment payment = new Payment();
        payment.setCardNumber(cardNumField.getText());
        payment.setCvv(cvvField.getText());
        payment.setCustomer(customer);
        payment.setExpiryDate(convertToDate(expireField.getText()));
        customer.setPayment(payment);

        message.setCustomer(customer);
        message.setMessage(localMessage.getMessage());

        //System.out.println("checking " + message.getMessage()); //to see what kind of purchase
        if(errorFlag[0] == 0) {
            System.out.println("Payment Information Clear, Sending Message to server...");

            client.sendMessage(message);
        }

        //wait for server
    }


    @FXML
    private void returnBtnControl(ActionEvent event) {
        Stage stage = (Stage) returnBtn.getScene().getWindow();
        client.moveScene(localMessage.getSourceFXML(), stage, null);
    }

    private void checkInput(int[] errorFlag){
        colorAllTextBorders();
        if(cardNumField.getText().length() < 14){
            changeControlBorderColor(cardNumField, "red");
            errorFlag[0] = 1;
        }
        else if(cardNumField.getText().isEmpty()){
            changeControlBorderColor(cardNumField, "red");
            cardNumField.setPromptText("Enter Card Number");
            errorFlag[0] = 1;
        }

        if(idNumberField.getText().isEmpty()){
            changeControlBorderColor(idNumberField, "red");
            idNumberField.setPromptText("Enter ID Number");
            errorFlag[0] = 1;
        }
        else if(idNumberField.getText().length() != 9){
            changeControlBorderColor(idNumberField, "red");
            errorFlag[0] = 1;
        }

        if(surnameNameField.getText().isEmpty()){
            changeControlBorderColor(surnameNameField, "red");
            surnameNameField.setPromptText("Enter Last Name");
            errorFlag[0] = 1;
        }

        if(privateNameField.getText().isEmpty()){
            changeControlBorderColor(privateNameField, "red");
            privateNameField.setPromptText("Enter Private Name");
            errorFlag[0] = 1;
        }

        if(cvvField.getText().isEmpty()){
            changeControlBorderColor(cvvField, "red");
            cvvField.setPromptText("Enter CVV");
            errorFlag[0] = 1;
        }
        else if(cvvField.getText().length() != 3){
            changeControlBorderColor(cvvField, "red");
            errorFlag[0] = 1;
        }
        if(emailField.getText().isEmpty()){
            changeControlBorderColor(emailField, "red");
            emailField.setPromptText("Enter Email Address");
            errorFlag[0] = 1;
        }
        if(expireField.getText().isEmpty()){
            changeControlBorderColor(expireField, "red");
            expireField.setPromptText("Enter Credit Expiration Address");
            errorFlag[0] = 1;
        }
        else if(convertToDate(expireField.getText()) == null){
            changeControlBorderColor(expireField, "red");
            expireField.clear();
            expireField.setPromptText("Enter Expire Date (MM/YY)");
            errorFlag[0] = 1;
        }
    }

    private void colorAllTextBorders(){
        changeControlBorderColor(cardNumField, "null");
        changeControlBorderColor(idNumberField, "null");
        changeControlBorderColor(surnameNameField, "null");
        changeControlBorderColor(privateNameField, "null");
        changeControlBorderColor(cvvField, "null");
        changeControlBorderColor(emailField, "null");
        changeControlBorderColor(expireField, "null");
    }

    private LocalDate convertToDate(String expiryDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        try {
            // Parse the expiry date string to YearMonth and convert it to LocalDate with the first day of the month
            return YearMonth.parse(expiryDateString, formatter).atDay(1);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid expiry date format: " + expiryDateString);
            return null;
        }
    }


}






