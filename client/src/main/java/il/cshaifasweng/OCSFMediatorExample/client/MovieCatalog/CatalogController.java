package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CatalogController {
    private static List<Movie> movies;

    @FXML
    private Button backButton;

    @FXML
    private Button updateButton;

    @FXML
    private ListView<String> movieListView;

    @FXML
    private ScrollPane scrollPane;

    public static void setMovies(List<Movie> movies) {
        CatalogController.movies = movies;
    }

    @FXML
    void initialize() {
        // Call loadMovies here to ensure the ListView is populated when the scene is loaded
        SimpleClient.sendMessage("show all movies");
        EventBus.getDefault().register(this);
        loadMovies();
    }

    private void loadMovies() {
        // Ensure movies list is initialized and populated
        if (movies != null && !movies.isEmpty()) {
            System.out.println("This is the number of movies given " + movies.size());
            ObservableList<String> movieNames = FXCollections.observableArrayList();
            for (Movie movie : movies) {
                movieNames.add(movie.getMovieName());
            }
            movieListView.setItems(movieNames);
        }
    }

    @Subscribe
    public void onMoviesReceived(GenericEvent<List<Movie>> event) {
        if (event.getData() != null && !event.getData().isEmpty() && event.getData().getFirst() instanceof Movie) {
            CatalogController.movies = event.getData();
            loadMovies();
        }
        //EventBus.getDefault().unregister(this);
    }

    @FXML
    public void handleBackButtonAction(ActionEvent event) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            SimpleClient.moveScene("primary.fxml",stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdateButtonAction(ActionEvent event) {

    }

    public void chosenMovie(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String movieName = movieListView.getSelectionModel().getSelectedItem();
            if (movieName != null) {
                for (Movie movie : movies) {
                    if (movie.getMovieName().equals(movieName)) {
                        try {
                            MovieController.setMovie(movie);

                            Node node = (Node) mouseEvent.getSource();
                            Stage stage = (Stage) node.getScene().getWindow();
                            SimpleClient.moveScene("movieCatalog/Movie.fxml",stage);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("No movie selected.");
            }
        }
    }


    public void chooseTypeAction(ActionEvent actionEvent) {
    }
}
