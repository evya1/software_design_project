package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.FXMLUtils.loadFXML;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private SimpleClient client;


    @Override
    public void start(Stage stage) throws IOException {
        //client = new SimpleClient("localhost",3000);
        scene = new Scene(loadFXML(CLIENT_CONFIG, client,null));
        stage.setScene(scene);

        EventBus.getDefault().register(this);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        EventBus.getDefault().unregister(this);
        if (client != null) {
            client.sendCloseConnectionMessage(); // Send close connection message
            client.closeConnection(); // Close the connection
        }
        super.stop();
    }

    @Subscribe
    public void onMessageEvent(Message message) {
    }



	public static void main(String[] args) {
        launch();
    }

}