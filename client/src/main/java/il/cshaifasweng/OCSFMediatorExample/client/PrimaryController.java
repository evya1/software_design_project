package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.MovieGridController;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class PrimaryController implements ClientDependent {

    private Message localMessage;

    @FXML
    private Button catalogButton;
    @FXML
    private Button customerPanelBtn;

    @FXML
    private Button employeePanelBtn;

    @FXML
    private ComboBox<String> filterByTypeComboBox;

    @FXML
    private GridPane moveisListGrid;

    @FXML
    private Button homeScreenBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button submitComplaintBtn;

    @FXML
    private VBox movieLayout;

    private SimpleClient client;
    private static List<Movie> movies;

    @FXML
    public void catalogController(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) catalogButton.getScene().getWindow();
            client.moveScene("catalogM/movieCatalog", stage,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        filterByTypeComboBox.getItems().addAll("All Movies", "Upcoming Movies", "In Theater", "Viewing Package");
        homeScreenBtn.setDisable(true);
        Message message = new Message();
        message.setMessage("new client");
        client.sendMessage(message);
        message.setMessage("show all movies");
        client.sendMessage(message);
        EventBus.getDefault().register(this);
        filterByTypeComboBox.setValue("All Movies");
        loadMovies();
    }

    private void loadMovies() {
        // Ensure movies list is initialized and populated
        int col = 0;
        int row = 0;
        try {
            if (movies != null && !movies.isEmpty()) {
                //ObservableList<ImageView> moviesImages = FXCollections.observableArrayList();
                for (Movie movie : movies) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("catalogM/movieGrid.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    anchorPane.setPrefSize(200, 300);
                    MovieGridController movieGridController = fxmlLoader.getController();
                    movieGridController.setData(movie);
                    if (col == 5) {
                        col = 0;
                        row++;
                    }
                    System.out.println(movie.getMovieName());
                    // moveisListGrid.getChildren().add(anchorPane);
                    moveisListGrid.add(anchorPane, col++, row);
                    GridPane.setMargin(anchorPane, new Insets(10,10,10,10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onMoviesReceived(GenericEvent<List<Movie>> event) {
        if (event.getData() != null && !event.getData().isEmpty() && event.getData().getFirst() instanceof Movie) {
            Platform.runLater(() -> {
                PrimaryController.movies = event.getData();
                loadMovies();
            });
        }
    }


    public void chooseType(ActionEvent event) {
        String selection = filterByTypeComboBox.getValue();


        if (movies != null && !movies.isEmpty()) {

        }
    }

    public void chooseMovie(MouseEvent event) {

    }

    @FXML
    void complaintController(ActionEvent event) {

    }

    @FXML
    void employeeController(ActionEvent event) {

    }

    @FXML
    void customerController(ActionEvent event) {

    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }
}

