package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieSlot;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    }

    public static void sendMessage(Message message) {
        // Add logic to handle the object if it's not null
        if (message != null) {
            try {
                client.sendToServer(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void moveScene(String scenePath, Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SimpleClient.class.getResource(scenePath)));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
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