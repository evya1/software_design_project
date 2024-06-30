package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieSlot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.time.LocalDateTime;
import java.util.List;


public class MovieController {
    private static Movie movie;

    public static void setMovie(Movie movie) {
        MovieController.movie = movie;
    }

    public static void setTheTimeSlots(List<MovieSlot> theTimeSlots) {
        MovieController.movie.setMovieScreeningTime(theTimeSlots);
    }

    public static String getMovie()
    {
        return movie.getMovieName();
    }

    @FXML
    private Button backBtn;

    @FXML
    private TextField castTextField;

    @FXML
    private TextField descreptionTextField;

    @FXML
    private TextField producerTextField;

    @FXML
    private ListView<LocalDateTime> screeningTimesListView;

    @FXML
    private TextField titleTextField;

    @FXML
    void initialize() {

        SimpleClient.sendMessage("get movie slot by movie ID",movie);
        EventBus.getDefault().register(this);
        castTextField.setText(movie.getMainCast());
        descreptionTextField.setText(movie.getMovieDescription());
        producerTextField.setText(movie.getProducer());
        titleTextField.setText(movie.getMovieName());



        // Print MovieSlots
        ObservableList<MovieSlot> movieSlots = FXCollections.observableArrayList();
        movie.setMovieScreeningTime(movieSlots);
        System.out.println(movieSlots.size());

    }

    @FXML
    void backToCatalog(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            SimpleClient.moveScene("movieCatalog.fxml",stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
