package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.MovieGridController;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieGenre;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.ADD_EDIT_MOVIE;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.COMPLAINT_SCREEN;

public class PrimaryController implements ClientDependent {


    private Message localMessage;


    @FXML
    private ComboBox<Branch> branchComboBox;

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

    private SimpleClient client;
    private List<Movie> movies;
    private List<Movie> filteredMovies;
    private List<Branch> branches;



    @FXML
    void initialize() {
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

    @Subscribe
    public void onMoviesReceived(GenericEvent<List<Movie>> event) {
        if (event.getData() != null && !event.getData().isEmpty() && event.getData().get(0) instanceof Movie) {
            Platform.runLater(() -> {
                this.movies = event.getData();
                loadMovies(movies);
                filteredMovies = movies;
            });
        }
    }

    @Subscribe
    public void onBranchesReceived(GenericEvent<List<Branch>> event){
        if(event.getData() != null && !event.getData().isEmpty() && event.getData().get(0) instanceof Branch){
            Platform.runLater(() -> {
                this.branches = event.getData();
                branchComboBox.getItems().clear();  // Clear previous items
                branchComboBox.getItems().addAll(branches);  // Add Branch objects directly
            });
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

//        // Load all movies
//        loadMovies(movies);
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
            message.setSourceFXML("Primary");
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

    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        // this.localMessage = message;
    }

    public void textChangeSearchField(InputMethodEvent inputMethodEvent) {
    }

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
    void chooseBranch(ActionEvent event) {
        Branch selectedBranch = branchComboBox.getSelectionModel().getSelectedItem();
        if (selectedBranch != null) {
            int branchId = selectedBranch.getId();
            Message message = new Message();
            message.setMessage(MOVIE_REQUEST);
            message.setData(GET_MOVIES_BY_BRANCH_ID);
            message.setBranchID(selectedBranch.getId());
            client.sendMessage(message);
        }

    }
}
