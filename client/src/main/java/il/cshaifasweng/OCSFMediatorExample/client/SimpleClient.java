package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.CatalogController;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import org.greenrobot.eventbus.EventBus;

import javax.xml.catalog.Catalog;
import java.io.IOException;
import java.util.List;

public class SimpleClient extends AbstractClient {

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
        if (messageString.equals("show all movies")) {
            List<Movie> movies = (List<Movie>)message.getObject();
            EventBus.getDefault().post(movies);
        }
    }
    public static void sendMessage(String messageContent) {

        MessageObject message = new MessageObject(messageContent);
        try {
            client.sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}