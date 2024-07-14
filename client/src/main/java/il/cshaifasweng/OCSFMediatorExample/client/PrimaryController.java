package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.MovieGridController;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieGenre;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

public class PrimaryController implements ClientDependent {

    //region: Attributes

    //region: FXML Attributes
    @FXML
    private ComboBox<Branch> branchComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button catalogButton;

    @FXML
    private Button customerPanelBtn;

    @FXML
    private Button submitNewMovieBtn;

    @FXML
    private Button employeePanelBtn;

    @FXML
    private ComboBox<String> filterByTypeComboBox;

    @FXML
    private ComboBox<MovieGenre> filterByGenreComboBox;

    @FXML
    private GridPane moveisListGrid;

    @FXML
    private Button homeScreenBtn;

    @FXML
    private Button clearFiltersBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button submitComplaintBtn;

    @FXML
    private VBox movieLayout;

    @FXML
    private Button bookletPurchaseBtn;
    //endregion

    //region: Additional Attributes
    private Message localMessage;
    private SimpleClient client;
    private List<Movie> movies;
    private List<Movie> filteredMovies;
    private List<Branch> branches;
    private List<MovieSlot> slots;
    //endregion

    //endregion

    @FXML
    void initialize() {
        datePicker.setVisible(false);
        datePicker.setEditable(false);
        filterByTypeComboBox.getItems().addAll("All Movies", "Upcoming Movies", "In Theater", "Viewing Package");
        filterByTypeComboBox.setValue("All Movies");
        filterByGenreComboBox.getItems().addAll(MovieGenre.values());

        homeScreenBtn.setDisable(true);
        Message message = new Message();
        message.setMessage("new client");
        client.sendMessage(message);
        message.setMessage(MOVIE_REQUEST);
        message.setData(SHOW_ALL_MOVIES);
        client.sendMessage(message);
        message.setMessage(BRANCH_THEATER_INFORMATION);
        message.setData(GET_BRANCHES);
        client.sendMessage(message);
        EventBus.getDefault().register(this);

        // Add listener to searchTextField
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterMovies());
    }

    //Subscriber Method
    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {

            //Creating a local message to handle the subMessage
            Message message = event.getMessage();

            //Request to show all the movies.
            if(message.getData().equals(SHOW_ALL_MOVIES)) {
                //Movies Received show all
                Platform.runLater(() -> {
                    this.movies = message.getMovies();
                    loadMovies(movies);
                    filteredMovies = movies;
                });
            }
            //Request to show all the branch names.
            else if(message.getData().equals(GET_BRANCHES)) {
                Platform.runLater(() -> {
                    this.branches = message.getBranches();
                    branchComboBox.getItems().clear();  // Clear previous items
                    branchComboBox.getItems().addAll(branches);  // Add Branch objects directly
                });
            }
            else if(message.getData().equals(GET_MOVIES_BY_BRANCH_ID)) {
                Platform.runLater(() -> {
                    this.movies = message.getMovies();
                    this.slots = message.getMovieSlots();
                    loadMovies(movies);
                    filteredMovies = movies;

                });
            }
        }
    }


    //region: Helper Methods
    private void loadMovies(List<Movie> moviesToDisplay) {

        moveisListGrid.getChildren().clear();
        // Ensure movies list is initialized and populated
        int col = 0;
        int row = 0;
        try {
            if (moviesToDisplay != null && !moviesToDisplay.isEmpty()) {
                for (Movie movie : moviesToDisplay) {
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
                    moveisListGrid.add(anchorPane, col++, row);
                    GridPane.setMargin(anchorPane, new Insets(15, 15, 15, 15));
                    anchorPane.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            chooseMovie(movie, (Stage) anchorPane.getScene().getWindow());
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterMovies() {
        String typeSelection = filterByTypeComboBox.getValue();
        MovieGenre genreSelection = filterByGenreComboBox.getValue();
        String searchText = searchTextField.getText().toLowerCase();

        filteredMovies = new ArrayList<>(movies);

        // Filter by type
        if (!typeSelection.equals("All Movies")) {
            filteredMovies.removeIf(movie -> {
                switch (typeSelection) {
                    case "Upcoming Movies":
                        return !movie.getMovieType().isUpcoming();
                    case "In Theater":
                        return !movie.getMovieType().isCurrentlyRunning();
                    case "Viewing Package":
                        return !movie.getMovieType().isPurchasable();
                    default:
                        return false;
                }
            });
        }

        // Filter by genre
        if (genreSelection != null) {
            filteredMovies.removeIf(movie -> !movie.getMovieGenre().equals(genreSelection));
        }

        // Filter by search text
        if (searchText != null && !searchText.isEmpty()) {
            filteredMovies.removeIf(movie -> !movie.getMovieName().toLowerCase().contains(searchText));
        }


        loadMovies(filteredMovies);
    }

    public void chooseMovie(Movie movie, Stage stage) {
        try {
            localMessage = new Message();
            localMessage.setSpecificMovie(movie);
            client.moveScene("catalogM/Movie", stage, localMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region: GUI Actions
    @FXML
    void purchaseBookletControl(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) bookletPurchaseBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("New Booklet");
            message.setSourceFXML("Primary");
            EventBus.getDefault().unregister(this);
            client.moveScene("purchases/paymentScreen", stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void chooseGenre(ActionEvent event) {
        filterMovies();
    }

    @FXML
    public void chooseType(ActionEvent event) {
        filterByGenreComboBox.getSelectionModel().clearSelection();
        filterByGenreComboBox.setPromptText("Choose Genre");

        filterMovies();
    }

    @FXML
    void chooseBranch(ActionEvent event) {
        datePicker.setEditable(true);
        datePicker.setVisible(true);
        Branch selectedBranch = branchComboBox.getSelectionModel().getSelectedItem();
        if (selectedBranch != null) {
            int branchId = selectedBranch.getId();
            Message message = new Message();
            message.setMessage(MOVIE_REQUEST);
            message.setData(GET_MOVIES_BY_BRANCH_ID);
            message.setBranchID(selectedBranch.getId());
            client.sendMessage(message);
            System.out.println("Message Branches sent");
        }

    }

    @FXML
    void clearFilters(ActionEvent event) {
        // Clear all filters
        filterByTypeComboBox.setValue("All Movies");
        filterByGenreComboBox.getSelectionModel().clearSelection();
        searchTextField.clear();
        Message message = new Message();
        message.setMessage(MOVIE_REQUEST);
        message.setData(SHOW_ALL_MOVIES);
        client.sendMessage(message);
    }

    @FXML
    void complaintController(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) submitComplaintBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("New Complaint");
            message.setSourceFXML(PRIMARY_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(COMPLAINT_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void employeeController(ActionEvent event) {

    }

    @FXML
    void customerController(ActionEvent event) {

    }

    @FXML
    void dateFilterPicker(ActionEvent event) {

    }

    @FXML
    public void newMovieAction(ActionEvent actionEvent) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) submitNewMovieBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("nfew movie");
            message.setSpecificMovie(movies.get(2));
            message.setSourceFXML("Primary");
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage ,message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void textChangeSearchField(InputMethodEvent inputMethodEvent) {
    }
    //endregion

    //region: Interface Methods
    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        // this.localMessage = message;
    }
    //endregion
}
