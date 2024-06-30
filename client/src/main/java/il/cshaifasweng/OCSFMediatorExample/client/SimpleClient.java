package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import il.cshaifasweng.OCSFMediatorExample.client.PrimaryController;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

public class SimpleClient extends AbstractClient {

    private static final String EMPTY_MESSAGE_REQUEST = "blank";
    private static final String SHOW_ALL_MOVIES_REQUEST = "show all movies";
    private static final String CHANGE_SCREENING_TIMES_REQUEST = "change screening times of the movie";
    private static final String UPDATE_MOVIES_LIST_REQUEST = "update movies list";


    private static SimpleClient client = null;
    private static String ipAddress = "localhost";
    private static int port = 3000;

    public static int getClientPort() {
        return port;
    }

    public static void setClientPort(int port) {
        SimpleClient.port = port;
    }

    private SimpleClient(String host, int port) {
        super(host, port);
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient(ipAddress, port);
        }
        return client;
    }

    public static void setIpAddress(String ip) {
        ipAddress = ip;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        MessageObject message = (MessageObject) msg;
        String messageString = message.getMsg();
//        if (messageString.equals("show all movies")) {
//            messageHandler.updateMovies((List<Movie>)message.getObject()); //List<Movie> movies
//        } else
//
        if (messageString.equals("client added successfully")) {
            EventBus.getDefault().post(new NewSubscriberEvent(message));
        } else if (messageString.equals("Error! we got an empty message")) {
            EventBus.getDefault().post(new ErrorEvent(message));
        } else {
            EventBus.getDefault().post(new MessageEvent(message));
        }
    }

}
