package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CatalogController {
    public static List<Movie> movies;

    @FXML
    private Button backButton;

    @FXML
    private Button updateButton;

    @FXML
    private ListView<String> movieListView;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void initialize() {
        // Call loadMovies here to ensure the ListView is populated when the scene is loaded
        loadMovies();
    }

    private void loadMovies() {
        // Ensure movies list is initialized and populated
        if (movies != null && !movies.isEmpty()) {
            ObservableList<String> movieNames = FXCollections.observableArrayList();
            for (Movie movie : movies) {
                movieNames.add(movie.getMovieName());
            }
            movieListView.setItems(movieNames);
        }
    }

    @FXML
    public void handleBackButtonAction(ActionEvent event) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "IO Error", "An unexpected error occurred. Please try again.");
        }
    }

    @FXML
    public void handleUpdateButtonAction(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("update.fxml"));
//            Parent root = loader.load();
//
//            UpdateController updateController = loader.getController();
//            updateController.setMovies(movies);
//
//            Stage stage = (Stage) updateButton.getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "IO Error", "An unexpected error occurred. Please try again.");
//        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void chosenMovie(MouseEvent mouseEvent) {

    }

    public void chooseTypeAction(ActionEvent actionEvent) {
    }
}
