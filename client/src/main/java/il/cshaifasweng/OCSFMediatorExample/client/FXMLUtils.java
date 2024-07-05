package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class FXMLUtils {

    public static Parent loadFXML(String fxml, SimpleClient client) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLUtils.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory(clazz -> {
            try {
                Object controller = clazz.getDeclaredConstructor().newInstance();
                if (controller instanceof ClientDependent) {
                    ((ClientDependent) controller).setClient(client);
                }
                else {
                    System.err.println("Warning: Controller " + clazz.getName() + " does not implement ClientDependent interface.");
                }
                return controller;
            } catch (Exception ex) {
                System.err.println("Error in creating controller " + clazz.getName() + ": " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        });
        return fxmlLoader.load();
    }
}