package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
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


public class MovieController implements ClientDependent {
    private static Movie movie;
    private SimpleClient client;

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
        Message message = new Message();
        message.setMessage("get movie slot by movie ID");
        message.setSpecificMovie(movie);
        client.sendMessage(message);

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
            client.moveScene("catalogM/movieCatalog", stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateScreeningFromDB(ActionEvent event) {
        try {
            screeningTimesListView.getItems().clear();
            Message message = new Message();
            message.setMessage("get movie slot by movie ID");
            message.setSpecificMovie(movie);
            client.sendMessage(message);

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

                    //Check if the result exists before proceeding.
                    LocalDateTime newStart = null;
                    if (result.isPresent()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
                        newStart = LocalDateTime.parse(result.get(), formatter);
                        if(newStart.isBefore(LocalDateTime.now()))
                        {
                            SimpleClient.showAlert(Alert.AlertType.ERROR,"Unavailable Time","The time entered cannot be before today.");
                            return;
                        }
                        for (MovieSlot slot : movie.getMovieScreeningTime()) {
                            if (slot.getStartDateTime().equals(newStart)) {
                                SimpleClient.showAlert(Alert.AlertType.ERROR,"Time Error", "The time you entered is already there");
                                return;
                            }
                        }

                    LocalDateTime newEnd = newStart.plusMinutes(movie.getMovieDuration());
                    System.out.println(newEnd);
                    movie.getMovieScreeningTime().get(slotIndex).setStartDateTime(newStart);
                    movie.getMovieScreeningTime().get(slotIndex).setEndDateTime(newEnd);
                    Message message = new Message();
                    message.setMessage("change screening times of the movie");
                    message.setSpecificMovie(movie);
                    client.sendMessage(message);
                    }
                }catch (DateTimeParseException ParseException) {
                    SimpleClient.showAlert(Alert.AlertType.ERROR,"Time Error","Please enter a valid time");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }
}
