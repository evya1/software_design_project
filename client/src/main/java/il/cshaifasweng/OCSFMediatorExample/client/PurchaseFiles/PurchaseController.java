package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Payment;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


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
    public void initialize() {

        EventBus.getDefault().register(this);

        privateName.setText("Private Name:");
        lastName.setText("Last Name:");
        id.setText("ID:");
        cardNumber.setText("Card Number:");
        cvv.setText("CVV:");

        confirmPurchaseBtn.setOnAction(event -> confirmBtnControl(event));
        returnBtn.setOnAction(event -> returnBtnControl(event));
    }

    @Subscribe
    public void handlePurchaseFromServer(GenericEvent<Purchase> event) {
        if (event.getData() != null && event.getData() instanceof Purchase) {
            Platform.runLater(() -> {
                Purchase purchase = (Purchase) event.getData();
            });
        }
        //popup message (include inside moveScene to primary)

    }


    @FXML
    private void confirmBtnControl(ActionEvent event) {
        // Implement saving objects into DB and returning to home screen
        Message message = new Message();
        message.setCustomer(new Customer());
        Customer customer = message.getCustomer();
        customer.setFirstName(privateNameField.getText());
        customer.setLastName(surnameNameField.getText());
        customer.setPersonalID(idNumberField.getText());
        Payment payment = new Payment();
        payment.setCardNumber(cardNumField.getText());
        payment.setCvv(cvvField.getText());
        customer.setPayment(payment);
        message.setCustomer(customer);
        message.setMessage(localMessage.getMessage());
        client.sendMessage(message);

        //wait for server
    }

    @FXML
    private void returnBtnControl(ActionEvent event) {
        Stage stage = (Stage) returnBtn.getScene().getWindow();
        client.moveScene(localMessage.getSourceFXML(), stage, null);
    }

}






