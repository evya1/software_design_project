package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

public class BookletPopupController {
    private Stage stage;
    private SimpleClient client;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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

        EventBus.getDefault().register(this);
        purchaseConfirmedTxt.setText("Purchase Confirmed!");
        bookletPurchasedTxt.setText("Booklet Purchased Successfuly!");

        okayBtn.setOnAction(event -> okayBtnControl(event));

    }

    @FXML
    void okayBtnControl(ActionEvent event) {
        Stage stage = (Stage) okayBtn.getScene().getWindow();
        client.moveScene(PRIMARY_SCREEN, stage, null);
    }
}
