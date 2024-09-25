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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;



public class PrimaryController implements ClientDependent {
    public ImageView logoBtn;

    //region Attributes

    //region FXML Attributes
    @FXML
    private ComboBox<Branch> branchComboBox;
    @FXML
    private ComboBox<String> filterByTypeComboBox;
    @FXML
    private ComboBox<MovieGenre> filterByGenreComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane moveisListGrid;
    @FXML
    private VBox movieLayout;
    @FXML
    private Button catalogButton;
    @FXML
    private Button customerPanelBtn;
    @FXML
    private Button employeePanelBtn;
    @FXML
    private Button homeScreenBtn;
    @FXML
    private Button clearFiltersBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button bookletPurchaseBtn;
    @FXML
    private Button submitComplaintBtn;
    @FXML
    private TextField searchTextField;

    //endregion

    //region Additional Attributes
    private Message localMessage;
    private SimpleClient client;
    private List<Movie> movies;
    private List<Movie> filteredMovies;
    private List<Branch> branches;
    private List<MovieSlot> slots;
    private Set<LocalDate> datesWithMovies = new HashSet<>();
    //endregion

    //endregion

    @FXML
    void initialize() {
        // Initialize UI components and set initial state
        datePicker.setVisible(false);
        datePicker.setEditable(false);
        filterByTypeComboBox.getItems().addAll("All Movies", "Upcoming Movies", "In Theater", "Viewing Package");
        filterByTypeComboBox.setValue("All Movies");
        filterByGenreComboBox.getItems().addAll(MovieGenre.COMEDY, MovieGenre.ACTION, MovieGenre.HORROR, MovieGenre.DRAMA, MovieGenre.ADVENTURE, MovieGenre.DOCUMENTARY);
        homeScreenBtn.setDisable(true);

        // Send initial requests to the server
        Message message = new Message();
        message.setMessage("new client");
        client.sendMessage(message);
        message.setMessage(MOVIE_REQUEST);
        message.setData(SHOW_ALL_MOVIES);
        client.sendMessage(message);
        message.setMessage(BRANCH_THEATER_INFORMATION);
        message.setData(GET_BRANCHES);
        client.sendMessage(message);

        // Register EventBus to receive messages
        EventBus.getDefault().register(this);

        // Add listener to searchTextField for real-time filtering
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterMovies());

        // Customize datePicker cells to highlight dates with movies
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && !empty) {
                    if (datesWithMovies.contains(date)) {
                        this.getStyleClass().add("date-with-movie");
                    } else {
                        this.getStyleClass().remove("date-with-movie");
                    }
                }
            }
        });

        // Set action for datePicker to filter movies by selected date
        datePicker.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                filterMoviesByDate(selectedDate);
            }
        });
    }

    // Method to handle data received through EventBus
    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {
            // Creating a local message to handle the subMessage
            Message message = event.getMessage();

            // Handle different types of messages
            if (message.getData().equals(SHOW_ALL_MOVIES)) {
                // Movies received, show all
                Platform.runLater(() -> {
                    this.movies = message.getMovies();
                    loadMovies(movies);
                    filteredMovies = movies;
                });
            } else if (message.getData().equals(GET_BRANCHES)) {
                // Branch information received, populate branchComboBox
                Platform.runLater(() -> {
                    this.branches = message.getBranches();
                    branchComboBox.getItems().clear();
                    branchComboBox.getItems().addAll(branches);
                });
            } else if (message.getData().equals(GET_MOVIES_BY_BRANCH_ID)) {
                // Movies and slots for a specific branch received
                Platform.runLater(() -> {
                    this.movies = message.getMovies();
                    this.slots = message.getMovieSlots();
                    updateDatesWithMovies();
                    loadMovies(movies);
                    filteredMovies = movies;
                });
            }
        }
    }

    //region Helper Methods
    // Method to load and display movies in the grid
    private void loadMovies(List<Movie> moviesToDisplay) {
        moveisListGrid.getChildren().clear();
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

    // Method to filter movies based on selected filters
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

        // Load filtered movies
        loadMovies(filteredMovies);
    }

    // Method to handle movie selection
    public void chooseMovie(Movie movie, Stage stage) {
        try {
            localMessage = new Message();
            localMessage.setSpecificMovie(movie);
            localMessage.setSourceFXML(PRIMARY_SCREEN);
            client.moveScene("catalogM/Movie", stage, localMessage);
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to filter movies by selected date
    private void filterMoviesByDate(LocalDate selectedDate) {
        if (selectedDate == null || slots == null) return;

        List<Movie> moviesToDisplay = slots.stream()
                .filter(slot -> slot.getStartDateTime().toLocalDate().equals(selectedDate))
                .map(MovieSlot::getMovie)
                .distinct()
                .collect(Collectors.toList());

        Platform.runLater(() -> loadMovies(moviesToDisplay));
    }

    // Method to update dates with movies for highlighting in datePicker
    private void updateDatesWithMovies() {
        datesWithMovies.clear();
        for (MovieSlot slot : slots) {
            LocalDate date = slot.getStartDateTime().toLocalDate();
            datesWithMovies.add(date);
        }
    }
    //endregion

    //region GUI Actions
    // Method to handle booklet purchase action
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
            message.setSourceFXML(PRIMARY_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(PAYMENT_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to handle genre selection action
    @FXML
    void chooseGenre(ActionEvent event) {
        filterMovies();
    }

    // Method to handle type selection action
    @FXML
    public void chooseType(ActionEvent event) {
        filterByGenreComboBox.getSelectionModel().clearSelection();
        filterByGenreComboBox.setPromptText("Choose Genre");

        filterMovies();
    }

    // Method to handle branch selection action
    @FXML
    void chooseBranch(ActionEvent event) {
        datePicker.setEditable(true);
        datePicker.setVisible(true);
        filterByGenreComboBox.getSelectionModel().clearSelection();
        filterByGenreComboBox.setPromptText("Choose Genre");
        filterByTypeComboBox.setValue("All Movies");
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

    // Method to clear all filters
    @FXML
    void clearFilters(ActionEvent event) {
        filterByTypeComboBox.setValue("All Movies");
        branchComboBox.getSelectionModel().clearSelection();
        branchComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Branch item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(branchComboBox.getPromptText());
                }
            }
        });
        filterByGenreComboBox.getSelectionModel().clearSelection();
        filterByGenreComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(MovieGenre item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(filterByGenreComboBox.getPromptText());
                }
            }
        });
        datePicker.setEditable(false);
        datePicker.setVisible(false);
        searchTextField.clear();

        Message message = new Message();
        message.setMessage(MOVIE_REQUEST);
        message.setData(SHOW_ALL_MOVIES);
        client.sendMessage(message);
    }

    // Method to handle complaint submission action
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

    // Method to open employee panel
    @FXML
    void employeeController(ActionEvent event) {
        try {
            Stage stage = (Stage) employeePanelBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("employee panel");
            message.setSourceFXML(PRIMARY_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(EMPLOYEE_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open customer panel
    @FXML
    void customerController(ActionEvent event) {
        try {
            Stage stage = (Stage) customerPanelBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("customer panel");
            message.setSourceFXML(PRIMARY_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(CUSTOMER_SCREEN, stage, message);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void dateFilterPicker(ActionEvent event) {
        // Not implemented
    }

    @FXML
    public void textChangeSearchField(InputMethodEvent inputMethodEvent) {
        // Not implemented
    }
    //endregion

    //region Interface Methods
    // Method to set client instance
    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        // this.localMessage = message;
    }

    // Method to open admin panel
    public void openAdminPanel(MouseEvent mouseEvent) {
        logoBtn.setOnMouseClicked(event -> {
            if (event.getClickCount() == 5) {
                try {
                    Stage stage = (Stage) logoBtn.getScene().getWindow();
                    Message message = new Message();
                    message.setSourceFXML(PRIMARY_SCREEN);
                    EventBus.getDefault().unregister(this);
                    client.moveScene(ADMIN_SCREEN, stage, message);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //endregion
}
