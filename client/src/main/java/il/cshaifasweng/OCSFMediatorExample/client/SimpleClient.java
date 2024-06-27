package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.greenrobot.eventbus.EventBus;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;

    private SimpleClient(String host, int port) {
        super(host, port);
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3000);
        }
        return client;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        String messageString = message.getMessage();
        if (messageString.equals("update submitters IDs")) {
            EventBus.getDefault().post(new UpdateMessageEvent(message));
        } else if (messageString.equals("client added successfully")) {
            EventBus.getDefault().post(new NewSubscriberEvent(message));
        } else if (messageString.equals("Error! we got an empty message")) {
            EventBus.getDefault().post(new ErrorEvent(message));
        } else {
            EventBus.getDefault().post(new MessageEvent(message));
        }
    }

}
