package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieSlot;
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


public class MovieController {
    private static Movie movie;

    public static void setMovie(Movie movie) {
        MovieController.movie = movie;
    }

    public static void setTheTimeSlots(List<MovieSlot> theTimeSlots) {
        MovieController.movie.setMovieScreeningTime(theTimeSlots);
    }

    public static String getMovie() {
        return movie.getMovieName();
    }

    @FXML
    private Button backBtn;

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

        //Send request to get movie slots.
        SimpleClient.sendMessage("get movie slot by movie ID", movie);

        //register to EventBus
        EventBus.getDefault().register(this);

        castTextField.setText(movie.getMainCast());
        descriptionTextArea.setText(movie.getMovieDescription());
        producerTextField.setText(movie.getProducer());
        titleTextField.setText(movie.getMovieName());

        if (movie.getImage() != null) {
            InputStream imageStream = new ByteArrayInputStream(movie.getImage());
            Image image = new Image(imageStream);
            movieImageView.setImage(image);
        }

    }

    //if(event.getData() != null && !event.getData().isEmpty() && event.getData().get(0) instanceof Movie) {
    @Subscribe
    public void handleScreeningTimes(GenericEvent<List<MovieSlot>> event) {
        if (event.getData() != null && !event.getData().isEmpty() && event.getData().getFirst() instanceof MovieSlot) {

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Platform.runLater(() -> {

                setTheTimeSlots(event.getData());
                ObservableList<LocalDateTime> times = FXCollections.observableArrayList();
                for (MovieSlot slot : event.getData()) {
                    if (!movie.getUpcomingMovies().isUpcoming()) {
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
            SimpleClient.moveScene("movieCatalog/movieCatalog.fxml", stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateScreeningFromDB(ActionEvent event) {
        try {
            screeningTimesListView.getItems().clear();
            SimpleClient.sendMessage("get movie slot by movie ID", movie);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void openModifyScreening(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            LocalDateTime selectedItem = screeningTimesListView.getSelectionModel().getSelectedItem();
            MovieSlot currentSlot = null;
            int slotIndex = -1;
            for (int i = 0; i < movie.getMovieScreeningTime().size(); i++) {
                if (movie.getMovieScreeningTime().get(i).getStartDateTime() == selectedItem ) {
                    currentSlot = movie.getMovieScreeningTime().get(i);
                    slotIndex = i;
                }
            }

            if (selectedItem != null && currentSlot != null) {
                try {
                    TextInputDialog dialog = new TextInputDialog(selectedItem.toString());
                    dialog.setTitle("Modify Screening Time");
                    dialog.setHeaderText("Modify the time in this format yyyyMMdd HHmm");
                    dialog.setContentText("New time:");
                    Optional<String> result = dialog.showAndWait();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
                    LocalDateTime newStart = LocalDateTime.parse(result.get(), formatter);
                    for (MovieSlot slot : movie.getMovieScreeningTime()) {
                        if (slot.getStartDateTime().equals(newStart)) {
                            SimpleClient.showAlert(Alert.AlertType.ERROR,"Time Eror", "The time you entered is already there");
                            return;
                        }
                    }
                    LocalDateTime newEnd = newStart.plusMinutes(movie.getMovieDuration());
                    System.out.println(newEnd);
                    movie.getMovieScreeningTime().get(slotIndex).setStartDateTime(newStart);
                    movie.getMovieScreeningTime().get(slotIndex).setEndDateTime(newEnd);
                    SimpleClient.sendMessage("change screening times of the movie",movie);
                }catch (DateTimeParseException ParseException) {
                    SimpleClient.showAlert(Alert.AlertType.ERROR,"Time Error","Please enter a valid time");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
