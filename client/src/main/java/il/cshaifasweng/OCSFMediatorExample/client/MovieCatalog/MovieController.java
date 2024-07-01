package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    @FXML // fx:id="updateScreeningBtn"
    private Button updateScreeningBtn; // Value injected by FXMLLoader


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

        castTextField.setEditable(false);
        descreptionTextField.setEditable(false);
        producerTextField.setEditable(false);
        titleTextField.setEditable(false);

        //Send request to get movie slots.
        SimpleClient.sendMessage("get movie slot by movie ID",movie);

        //register to EventBus
        EventBus.getDefault().register(this);

        castTextField.setText(movie.getMainCast());
        descreptionTextField.setText(movie.getMovieDescription());
        producerTextField.setText(movie.getProducer());
        titleTextField.setText(movie.getMovieName());
    }
    //if(event.getData() != null && !event.getData().isEmpty() && event.getData().get(0) instanceof Movie) {
    @Subscribe
    public void handleScreeningTimes(GenericEvent<List<MovieSlot>> event) {
            if (event.getData() != null && !event.getData().isEmpty() && event.getData().getFirst() instanceof MovieSlot) {

                ObservableList<LocalDateTime> times = FXCollections.observableArrayList();
                for (MovieSlot slot : event.getData()) {
                    times.add(slot.getStartDateTime());
                }
                screeningTimesListView.setItems(times);
            }
            //EventBus.getDefault().unregister(this);
        }

    @FXML
    void backToCatalog(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            SimpleClient.moveScene("movieCatalog/movieCatalog.fxml",stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateScreeningFromDB(ActionEvent event) {

    }
    @FXML
    public void openModifyScreening(MouseEvent mouseEvent) {

    }
}
