package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChooseSeatsController implements ClientDependent {
    SimpleClient client;
    Message localMessage;




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
