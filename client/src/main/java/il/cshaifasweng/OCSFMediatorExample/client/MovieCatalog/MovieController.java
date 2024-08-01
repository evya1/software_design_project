package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    public Button movieTicketBtn;
    @FXML
    public Button viewPackageBtn;
    @FXML
    public ComboBox<Branch> branchComboBox;
    @FXML
    public TableView<MovieSlot> screeningTableView;
    @FXML
    public TableColumn<MovieSlot,LocalDateTime> dateCol;
    @FXML
    public TableColumn<MovieSlot,LocalDateTime> timeCol;
    @FXML
    public TableColumn<MovieSlot,Integer> theaterNumCol;


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

        dateCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        theaterNumCol.setCellValueFactory(new PropertyValueFactory<>("theaterId"));

        // Format date and time columns
        dateCol.setCellFactory(column -> new TableCell<MovieSlot, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : formatter.format(item));
            }
        });

        timeCol.setCellFactory(column -> new TableCell<MovieSlot, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : formatter.format(item));
            }
        });

        Message message = new Message();
        message.setMessage(BRANCH_THEATER_INFORMATION);
        message.setData(GET_BRANCHES_BY_MOVIE_ID);
        message.setMovieID(movie.getId());
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

        //Setting the GUI according to the movie type.
        screeningTableView.setVisible(false);
        screeningTableView.setDisable(true);
        movieTicketBtn.setVisible(false);
        if(!movie.getMovieType().isPurchasable()){
            viewPackageBtn.setDisable(true);
            viewPackageBtn.setVisible(false);
        }

    }

    //if(event.getData() != null && !event.getData().isEmpty() && event.getData().get(0) instanceof Movie) {
    @Subscribe
    public void handleScreeningTimes(MessageEvent event) {
        System.out.println("Entered handleScreeningTime");

            Message message = event.getMessage();
            if(message.getMessage().equals(BRANCH_THEATER_INFORMATION))
            {
                switch (message.getData()){
                    case GET_BRANCHES_BY_MOVIE_ID:
                        Platform.runLater(()->{
                            branchComboBox.getItems().addAll(message.getBranches());
                        });
                        break;
                    case GET_MOVIE_SLOT_BY_MOVIE_ID_AND_BRANCH_ID:
                        Platform.runLater(()->{
                            screeningTableView.setVisible(true);
                            screeningTableView.setDisable(false);
                            List<MovieSlot> movieSlots = message.getMovieSlots();
                            ObservableList<MovieSlot> observableSlots = FXCollections.observableArrayList(movieSlots);
                            screeningTableView.setItems(observableSlots);
                            movieTicketBtn.setVisible(true);
                            movieTicketBtn.setDisable(false);
                        });

            }
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

    public void purchaseTicketsAction(ActionEvent actionEvent) {
        MovieSlot selectedMovieSlot = screeningTableView.getSelectionModel().getSelectedItem();
        if(selectedMovieSlot != null)
        {
            EventBus.getDefault().unregister(this);
            Message message = new Message();
            message.setMovieSlot(selectedMovieSlot);
            try {
                EventBus.getDefault().unregister(this);
                Stage stage = (Stage) movieTicketBtn.getScene().getWindow();
                client.moveScene(CHOOSE_SEATS_SCREEN, stage, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            SimpleClient.showAlert(Alert.AlertType.ERROR,"Screening Error","Please choose a screening time before moving to purchase.");
        }
    }

    public void purchasePackageAction(ActionEvent actionEvent) {
    }

    public void selectBranchAction(ActionEvent actionEvent) {
        Branch selectedBranch = branchComboBox.getSelectionModel().getSelectedItem();
        Message message = new Message();
        message.setMessage(BRANCH_THEATER_INFORMATION);
        message.setData(GET_MOVIE_SLOT_BY_MOVIE_ID_AND_BRANCH_ID);
        message.setMovieID(movie.getId());
        message.setBranchID(selectedBranch.getId());
        client.sendMessage(message);
    }
}
