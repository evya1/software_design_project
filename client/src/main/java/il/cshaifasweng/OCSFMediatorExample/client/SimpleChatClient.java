package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.CatalogController;
import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.MovieController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.xml.catalog.Catalog;

import static il.cshaifasweng.OCSFMediatorExample.client.FXMLUtils.loadFXML;

/**
 * JavaFX App
 */
public class SimpleChatClient extends Application {

    private static Scene scene;
    private SimpleClient client;


    @Override
    public void start(Stage stage) throws IOException {
        //client = new SimpleClient("localhost",3000);
        scene = new Scene(loadFXML("ServerLogin", client));
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
    public void onMessageEvent(MessageEvent message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION,
                    String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n",
                            message.getMessage().getId(),
                            message.getMessage().getMessage(),
                            message.getMessage().getTimeStamp().format(dtf))
            );
            alert.setTitle("new message");
            alert.setHeaderText("New Message:");
            alert.show();
        });
    }


	public static void main(String[] args) {
        launch();
    }

}