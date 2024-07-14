package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

public class BookletPopupController implements ClientDependent {
    private Stage stage;
    private SimpleClient client;
    private Message localMessage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMessage(Message message){this.localMessage = message;}

    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @FXML
    private Text bookletPurchasedTxt;

    @FXML
    private Button okayBtn;

    @FXML
    private Text purchaseConfirmedTxt;

    @FXML
    public void initialize() {

        purchaseConfirmedTxt.setText("Purchase Confirmed!");
        bookletPurchasedTxt.setText("Booklet Purchased Successfully!");

        okayBtn.setOnAction(event -> okayBtnControl(event));

    }

    @FXML
    void okayBtnControl(ActionEvent event) {
        Stage stage = (Stage) okayBtn.getScene().getWindow();
        stage.close();
    }
}
