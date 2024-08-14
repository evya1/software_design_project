package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Payment;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.BOOKLET_POP_UP_MESSAGE;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PACKAGE_POP_UP_MESSAGE;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PRIMARY_SCREEN;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.changeControlBorderColor;
import static il.cshaifasweng.OCSFMediatorExample.client.Utility.Dialogs.popUpAndReturnToMainScreen;


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

    private int flag;

    @FXML
    public void initialize() {

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void handlePurchaseFromServer(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();

            Platform.runLater(() -> {
                //TODO: Add option per type.
                if (Objects.equals(message.getMessage(), "New Purchase") && message.getPurchase() != null) {
                    Purchase purchase = message.getPurchase();
                    System.out.println("Purchase received: " + purchase);
                    final String title = "Purchase Confirmed!";
                    switch (purchase.getPurchaseType()) {
                        case BOOKLET:
                            popUpAndReturnToMainScreen(client, returnBtn,title,"Booklet Purchased Successfully!");
                            break;
                        case MOVIE_LINK:
                            popUpAndReturnToMainScreen(client,returnBtn,title,"Movie Link Purchased Successfully!");
                            break;
                        case MOVIE_TICKET:
                            popUpAndReturnToMainScreen(client,returnBtn,title,"Movie Ticket Purchased Successfully!");
                            break;
                        default:
                            System.out.println("Unknown Command");
                    }
                    EventBus.getDefault().unregister(this);
                }
            });

        } else {
            System.out.println("Invalid event data or not a Purchase instance");
        }
    }


    @FXML
    private void confirmBtnControl(ActionEvent event) {

        flag = checkInput();

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
        message.setSpecificMovie(localMessage.getSpecificMovie());

        if(flag == 0) {
            System.out.println("Payment Information Clear, Sending Message to server...");
            System.out.println(customer.getPersonalID());
            client.sendMessage(message);
        }

        //wait for server
    }


    @FXML
    private void returnBtnControl(ActionEvent event) {
        Stage stage = (Stage) returnBtn.getScene().getWindow();
        if (localMessage.getSourceFXML() == "catalogM/Movie")
        {
            Message message = new Message();
            message.setSpecificMovie(localMessage.getSpecificMovie());
            client.moveScene(localMessage.getSourceFXML(), stage, message);
        } else if (localMessage.getSourceFXML() == "Primary") {
            client.moveScene(localMessage.getSourceFXML(), stage, null);
        }

    }

    private int checkInput(){
        colorAllTextBorders();
        if(cardNumField.getText().length() < 14){
            changeControlBorderColor(cardNumField, "red");
            return 1;
        }
        else if(cardNumField.getText().isEmpty()){
            changeControlBorderColor(cardNumField, "red");
            cardNumField.setPromptText("Enter 14 Digits Number");
            return 1;
        }

        if(idNumberField.getText().isEmpty()){
            changeControlBorderColor(idNumberField, "red");
            idNumberField.setPromptText("Enter 9 Digits ID");
            return 1;
        }
        else if(idNumberField.getText().length() != 9){
            changeControlBorderColor(idNumberField, "red");
            return 1;
        }
        if(surnameNameField.getText().isEmpty()){
            changeControlBorderColor(surnameNameField, "red");
            surnameNameField.setPromptText("Enter Last Name");
            return 1;
        }
        if(privateNameField.getText().isEmpty()){
            changeControlBorderColor(privateNameField, "red");
            privateNameField.setPromptText("Enter Private Name");
            return 1;
        }
        if(cvvField.getText().isEmpty()){
            changeControlBorderColor(cvvField, "red");
            cvvField.setPromptText("Enter CVV");
            return 1;
        }
        else if(cvvField.getText().length() != 3){
            changeControlBorderColor(cvvField, "red");
            return 1;
        }
        if(emailField.getText().isEmpty()){
            changeControlBorderColor(emailField, "red");
            emailField.setPromptText("Enter Email Address");
            return 1;
        }
        if(expireField.getText().isEmpty()){
            changeControlBorderColor(expireField, "red");
            expireField.setPromptText("Enter Date (MM/YY)");
            return 1;
        }
        else if(convertToDate(expireField.getText()) == null){
            changeControlBorderColor(expireField, "red");
            expireField.clear();
            expireField.setPromptText("Enter Date (MM/YY)");
            return 1;
        }
        return 0;
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






