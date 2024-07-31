package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

public class HandleComplaintController implements ClientDependent {
    private Message localMessage;
    private SimpleClient client;

    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }

    @FXML // fx:id="PurchaseTypeField"
    private TextField PurchaseTypeField; // Value injected by FXMLLoader

    @FXML // fx:id="complaintTitleField"
    private TextField complaintTitleField; // Value injected by FXMLLoader

    @FXML // fx:id="complaintTxtArea"
    private TextArea complaintTxtArea; // Value injected by FXMLLoader

    @FXML // fx:id="idField"
    private TextField idField; // Value injected by FXMLLoader

    @FXML // fx:id="lastNameField"
    private TextField lastNameField; // Value injected by FXMLLoader

    @FXML // fx:id="mailField"
    private TextField mailField; // Value injected by FXMLLoader

    @FXML // fx:id="moneyToReturnField"
    private TextField moneyToReturnField; // Value injected by FXMLLoader

    @FXML // fx:id="privateNameField"
    private TextField privateNameField; // Value injected by FXMLLoader

    @FXML // fx:id="returnBtn"
    private Button returnBtn; // Value injected by FXMLLoader

    @FXML // fx:id="submitComplaintBtn"
    private Button submitComplaintBtn; // Value injected by FXMLLoader

    boolean is_handled = false;
    Complaint complaint;

    @FXML
    private void initialize(){
        is_handled = false;
        complaint = localMessage.getComplaint();
        PurchaseTypeField.setText(complaint.getPurchaseType().toString());
        PurchaseTypeField.setDisable(true);
        complaintTitleField.setText(complaint.getComplaintTitle());
        complaintTitleField.setDisable(true);
        complaintTxtArea.setText(complaint.getComplaintContent());
        idField.setText(String.valueOf(complaint.getCustomerPId()));
        idField.setDisable(true);
        lastNameField.setText(complaint.getCustomer().getLastName());
        lastNameField.setDisable(true);
        mailField.setText(complaint.getCustomer().getEmail());
        mailField.setDisable(true);
        privateNameField.setText(complaint.getCustomer().getFirstName());
        privateNameField.setDisable(true);

    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();
            Platform.runLater(() -> {
                this.complaint = message.getComplaint();
            });
        }
    }

    @FXML
    void returnBtnController(ActionEvent event) {
        if (!is_handled) {
            complaint.setMoneyToReturn(-1);
            complaint.setComplaintStatus("Open");
        }
        Message message = new Message();
        message.setComplaint(complaint);
        message.setMessage("get complaints");
        message.setData("change complaint status");
        client.sendMessage(message);
        Stage stage = (Stage) returnBtn.getScene().getWindow();
        message.setMessage("");
        message.setSourceFXML(COMPLAINT_HANDLER_SCREEN);
        message.setEmployee(localMessage.getEmployee());
        EventBus.getDefault().unregister(this);
        client.moveScene(EMPLOYEE_SCREEN, stage, message);
    }

    @FXML
    void submitDecision(ActionEvent event) {
        try{
            double money = Double.parseDouble(moneyToReturnField.getText());
            String content = complaintTxtArea.getText();
            complaint.setComplaintStatus("Close");
            complaint.setMoneyToReturn(money);
            complaint.setComplaintContent(content);
            Message message = new Message();
            message.setMessage("get complaints");
            message.setData("change complaint status");
            message.setComplaint(complaint);
            is_handled = true;
            client.sendMessage(message);
            Stage stage = (Stage) submitComplaintBtn.getScene().getWindow();
            message.setMessage("");
            message.setSourceFXML(COMPLAINT_HANDLER_SCREEN);
            message.setEmployee(localMessage.getEmployee());
            EventBus.getDefault().unregister(this);
            client.moveScene(EMPLOYEE_SCREEN, stage, message);
        }catch (NumberFormatException e) {
            SimpleClient.showAlert(Alert.AlertType.ERROR,"Number Format Error","Please enter a valid number");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
