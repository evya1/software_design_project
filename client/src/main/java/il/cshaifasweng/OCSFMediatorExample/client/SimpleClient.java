package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
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
        String messageData = message.getData();
        System.out.println("LOG: new message from Server: " + messageString);

        if (messageString.equals(SHOW_ALL_MOVIES)) {
            List<Movie> movies = message.getMovies();
            EventBus.getDefault().post(new GenericEvent<List<Movie>>(movies));

        } else if (messageString.equals("time slots for specific movie")) {
            List<MovieSlot> screeningTimes = message.getMovieSlots();
            EventBus.getDefault().post(new GenericEvent<List<MovieSlot>>(screeningTimes));
        }
        else if(messageString.equals("new client")){
            System.out.println("A new client established\n");
        }
        else if(messageString.equals("New Purchase")){
            Purchase purchase = message.getPurchase();
            EventBus.getDefault().post(new GenericEvent<Purchase>(purchase));
        }
        else if(messageString.equals("New Complaint")){
            Complaint complaint = message.getComplaint();
            EventBus.getDefault().post(new GenericEvent<Complaint>(complaint));
        }
        else if(messageString.equals("Content Change")){

            Message updateRequest = new Message();
            updateRequest.setMessage("show all movies");
            sendMessage(updateRequest);
        } else if (messageString.equals(BRANCH_THEATER_INFORMATION)) {
            switch (messageData)
            {
                case GET_BRANCHES:
                    List<Branch> branches = message.getBranches();
                    EventBus.getDefault().post(new GenericEvent<List<Branch>>(branches));
            }
            
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

    public void moveScene(String scenePath, Stage stage, Message msg) {
        try {
            Parent root = loadFXML(scenePath,this,msg);
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