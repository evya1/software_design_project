package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PAYMENT_SCREEN;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PRIMARY_SCREEN;

public class ChooseSeatsController implements ClientDependent {
    SimpleClient client;
    Message localMessage;

    //region Buttons
    @FXML
    private Button returnBtn;

    @FXML
    private Button homeScreenBtn;

    @FXML
    private Button purchaseTicketBtn;
    //endregion

    //region Rows
    @FXML
    private ImageView row1seat1;

    @FXML
    private ImageView row1seat10;

    @FXML
    private ImageView row1seat2;

    @FXML
    private ImageView row1seat3;

    @FXML
    private ImageView row1seat4;

    @FXML
    private ImageView row1seat5;

    @FXML
    private ImageView row1seat6;

    @FXML
    private ImageView row1seat7;

    @FXML
    private ImageView row1seat8;

    @FXML
    private ImageView row1seat9;

    @FXML
    private ImageView row2seat1;

    @FXML
    private ImageView row2seat10;

    @FXML
    private ImageView row2seat2;

    @FXML
    private ImageView row2seat3;

    @FXML
    private ImageView row2seat4;

    @FXML
    private ImageView row2seat5;

    @FXML
    private ImageView row2seat6;

    @FXML
    private ImageView row2seat7;

    @FXML
    private ImageView row2seat8;

    @FXML
    private ImageView row2seat9;

    @FXML
    private ImageView row3seat1;

    @FXML
    private ImageView row3seat10;

    @FXML
    private ImageView row3seat2;

    @FXML
    private ImageView row3seat3;

    @FXML
    private ImageView row3seat4;

    @FXML
    private ImageView row3seat5;

    @FXML
    private ImageView row3seat6;

    @FXML
    private ImageView row3seat7;

    @FXML
    private ImageView row3seat8;

    @FXML
    private ImageView row3seat9;

    @FXML
    private ImageView row4seat1;

    @FXML
    private ImageView row4seat10;

    @FXML
    private ImageView row4seat2;

    @FXML
    private ImageView row4seat3;

    @FXML
    private ImageView row4seat4;

    @FXML
    private ImageView row4seat5;

    @FXML
    private ImageView row4seat6;

    @FXML
    private ImageView row4seat7;

    @FXML
    private ImageView row4seat8;

    @FXML
    private ImageView row4seat9;

    @FXML
    private ImageView row5seat1;

    @FXML
    private ImageView row5seat10;

    @FXML
    private ImageView row5seat2;

    @FXML
    private ImageView row5seat3;

    @FXML
    private ImageView row5seat4;

    @FXML
    private ImageView row5seat5;

    @FXML
    private ImageView row5seat6;

    @FXML
    private ImageView row5seat7;

    @FXML
    private ImageView row5seat8;

    @FXML
    private ImageView row5seat9;

    @FXML
    private ImageView row6seat1;

    @FXML
    private ImageView row6seat10;

    @FXML
    private ImageView row6seat2;

    @FXML
    private ImageView row6seat3;

    @FXML
    private ImageView row6seat4;

    @FXML
    private ImageView row6seat5;

    @FXML
    private ImageView row6seat6;

    @FXML
    private ImageView row6seat7;

    @FXML
    private ImageView row6seat8;

    @FXML
    private ImageView row6seat9;

    @FXML
    private ImageView row7seat1;

    @FXML
    private ImageView row7seat10;

    @FXML
    private ImageView row7seat2;

    @FXML
    private ImageView row7seat3;

    @FXML
    private ImageView row7seat4;

    @FXML
    private ImageView row7seat5;

    @FXML
    private ImageView row7seat6;

    @FXML
    private ImageView row7seat7;

    @FXML
    private ImageView row7seat8;

    @FXML
    private ImageView row7seat9;
    //endregion

    @FXML
    private Text screenTitle;


    @FXML
    public void initialize(){
        //This is the eventbus used to transfer information from the client to the server and back
        EventBus.getDefault().register(this);

        //Implement GUI logic here related to initialization of objects.

    }

    @Subscribe
    public void handleMessageFromServer(MessageEvent event){
        //Implement logic here
    }

    @FXML
    void purchaseTicketAction(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) purchaseTicketBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("New Movie Ticket");
            message.setSourceFXML(localMessage.getSourceFXML());
            EventBus.getDefault().unregister(this);
            client.moveScene(PAYMENT_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void returnHomeAction(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) homeScreenBtn.getScene().getWindow();
            Message message = new Message();
            EventBus.getDefault().unregister(this);
            client.moveScene(PRIMARY_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void returnBtnAction(ActionEvent event) {
        Stage stage = (Stage) returnBtn.getScene().getWindow();
        client.moveScene(localMessage.getSourceFXML(), stage, null);
    }

    //region Interface Methods
    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }
    //endregion
}
