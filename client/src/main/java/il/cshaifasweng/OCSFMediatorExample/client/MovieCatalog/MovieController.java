package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;


public class MovieController implements ClientDependent {
    private Movie movie;
    private SimpleClient client;
    private Message localMessage;


    public void setTheTimeSlots(List<MovieSlot> theTimeSlots) {
        this.movie.setMovieScreeningTime(theTimeSlots);
    }

    @FXML
    private Button backBtn;

    @FXML
    private TextField genreTextField;

    @FXML // fx:id="updateScreeningBtn"
    private Button updateScreeningBtn; // Value injected by FXMLLoader

    @FXML // fx:id="movieImageView"
    private ImageView movieImageView; // Value injected by FXMLLoader

    @FXML
    private TextField castTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField producerTextField;

    @FXML
    private ListView<LocalDateTime> screeningTimesListView;

    @FXML
    private TextField titleTextField;


    @FXML
    void initialize() {

        castTextField.setEditable(false);
        descriptionTextArea.setEditable(false);
        producerTextField.setEditable(false);
        titleTextField.setEditable(false);
        genreTextField.setEditable(false);

        //Send request to get movie slots.
        Message message = new Message();
        message.setMessage(GET_MOVIE_SLOT_BY_MOVIE_ID);
        message.setSpecificMovie(movie);
        client.sendMessage(message);

        //register to EventBus
        EventBus.getDefault().register(this);

        castTextField.setText(movie.getMainCast());
        descriptionTextArea.setText(movie.getMovieDescription());
        producerTextField.setText(movie.getProducer());
        titleTextField.setText(movie.getMovieName() + "    -    " + movie.getHebrewMovieName());
        genreTextField.setText(movie.getMovieGenre().toString());

        if (movie.getImage() != null) {
            InputStream imageStream = new ByteArrayInputStream(movie.getImage());
            Image image = new Image(imageStream);
            movieImageView.setImage(image);
        }

    }

    //if(event.getData() != null && !event.getData().isEmpty() && event.getData().get(0) instanceof Movie) {
    @Subscribe
    public void handleScreeningTimes(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Platform.runLater(() -> {

                setTheTimeSlots(message.getMovieSlots());
                ObservableList<LocalDateTime> times = FXCollections.observableArrayList();
                for (MovieSlot slot : message.getMovieSlots()) {
                    if (!movie.getMovieType().isUpcoming()) {
                        times.add(slot.getStartDateTime());
                    }
                }
                // Sort the times
                Collections.sort(times);
                screeningTimesListView.setItems(times);
            });
        }
    }

    @FXML
    void backToCatalog(ActionEvent event) {
        try {
            EventBus.getDefault().unregister(this);
            Stage stage = (Stage) backBtn.getScene().getWindow();
            client.moveScene(PRIMARY_SCREEN, stage,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void openModifyScreening(MouseEvent mouseEvent) {

    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
        this.movie = (Movie) message.getSpecificMovie();
    }
}
