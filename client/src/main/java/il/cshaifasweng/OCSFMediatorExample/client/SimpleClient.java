package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.FXMLUtils.loadFXML;


public class SimpleClient extends AbstractClient {

    private static String ipAddress;

    private static int port;

    public static int getClientPort() {
        return port;
    }

    public static void setClientPort(int port) {
        SimpleClient.port = port;
    }

    public SimpleClient(String host, int port) {
        super(host, port);
    }

    public static void setIpAddress(String ip) {
        ipAddress = ip;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    @Override
    protected void handleMessageFromServer(Object object) {
        Message message = (Message) object;
        String messageString = message.getMessage();
        System.out.println("LOG: new message from Server: " + messageString);
        if (messageString.equals("show all movies")) {
            List<Movie> movies = message.getMovies();
            EventBus.getDefault().post(new GenericEvent<List<Movie>>(movies));
        } else if (messageString.equals("time slots for specific movie")) {
            List<MovieSlot> screeningTimes = message.getMovieSlots();
            EventBus.getDefault().post(new GenericEvent<List<MovieSlot>>(screeningTimes));
        }
        else if(messageString.equals("new client")){
            System.out.println("A new client established\n");
        }
    }

    public void sendMessage(Message message) {
        // Add logic to handle the object if it's not null
        if (message != null) {
            try {
                sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCloseConnectionMessage() {
        Message closeMessage = new Message();
        closeMessage.setMessage("closeConnection");
        sendMessage(closeMessage);
    }

    public void moveScene(String scenePath, Stage stage) {
        try {
            Parent root = loadFXML(scenePath,this);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "IO Error", "An unexpected error occurred. Please try again.");
        }
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}