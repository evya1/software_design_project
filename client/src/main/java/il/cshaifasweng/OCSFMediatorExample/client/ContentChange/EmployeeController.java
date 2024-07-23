package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.MovieGridController;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.ADD_EDIT_MOVIE;

public class EmployeeController implements ClientDependent {

    private static final Logger logger = LoggerFactory.getLogger(MovieAdditionController.class);


    @FXML
    private Button changeContentBtn;

    @FXML
    private Button confirmChangeBtn;

    @FXML
    private Button homeScreenBtn;

    @FXML
    private GridPane moveisListGrid;

    @FXML
    private VBox movieLayout;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button showComplaintsBtn;

    @FXML
    private Button showReportsBtn;

    @FXML
    private Button submitNewMovieBtn;

    @FXML
    private Button logINBtn;

    @FXML
    private Button logOUTBtn;

    //
    private Message localMessage;
    private SimpleClient client;
    private List<Employee> employees;
    private List<Movie> movies;
    private Employee employee;



    @FXML
    private void initialize() {
        showComplaintsBtn.setDisable(true);
        logOUTBtn.setVisible(false);
        scrollPane.setVisible(false);
        showReportsBtn.setDisable(true);
        submitNewMovieBtn.setDisable(true);
        changeContentBtn.setDisable(true);
        confirmChangeBtn.setDisable(true);
        Message message = new Message();
        message.setMessage(MOVIE_REQUEST);
        message.setData(SHOW_ALL_MOVIES);
        client.sendMessage(message);
        message.setMessage(GET_EMPLOYEES);
        message.setData(SHOW_ALL_EMPLOYEES);
        client.sendMessage(message);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();
            if (message.getData().equals(SHOW_ALL_MOVIES)) {
                Platform.runLater(() -> {
                    this.movies = message.getMovies();
                    loadMovies(movies);
                });
            }
            if (message.getData().equals(SHOW_ALL_EMPLOYEES)) {
                this.employees = message.getEmployeeList();
            }
        }
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
    void changeContent(ActionEvent event) {

    }

    @FXML
    void confirmChange(ActionEvent event) {

    }

    @FXML
    void homeScreen(ActionEvent event) {
        Stage stage = (Stage) homeScreenBtn.getScene().getWindow();
        EventBus.getDefault().unregister(this);
        client.moveScene(localMessage.getSourceFXML(), stage, null);
    }

    @FXML
    void newMovieAction(ActionEvent event) {
        try {
            Stage stage = (Stage) submitNewMovieBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage(NEW_MOVIE_TEXT_REQUEST);
            logger.info("Moving scene");
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage ,message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logIn(ActionEvent event) {
        try {
            Dialog<Pair<String,String>> dialog = new Dialog<>();
            dialog.setTitle("Sign In");
            ButtonType signInButtonType = new ButtonType("Sign In", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(signInButtonType, ButtonType.CANCEL);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField username = new TextField();
            username.setPromptText("Username");
            PasswordField password = new PasswordField();
            password.setPromptText("Password");

            grid.add(new Label("Username:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Password:"), 0, 1);
            grid.add(password, 1, 1);

            dialog.getDialogPane().setContent(grid);
            dialog.showAndWait();
            Pair<String,String> usernamePassword = new Pair<>(username.getText(),password.getText());
            System.out.println(usernamePassword);
            boolean flag = false;
            for (Employee e : employees) {
                if (e.getUsername().equalsIgnoreCase(usernamePassword.getKey())
                            && e.getPassword().equals(usernamePassword.getValue())) {
                    flag = true;
                    employee = e;
                }
            }
            if (!flag) {
                Dialog<String> errorDialog = new Dialog<>();
                errorDialog.setTitle("Sign In Error");
                errorDialog.setHeaderText("Your username or password is incorrect.");
                errorDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                errorDialog.showAndWait();
            }else {
                emp();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void emp(){
        logINBtn.setVisible(false);
        logOUTBtn.setVisible(true);
        switch (employee.getEmployeeType()) {
            case BASE:

                break;
            case SERVICE:
                showComplaintsBtn.setDisable(false);
                break;
            case THEATER_MANAGER:
                showReportsBtn.setDisable(false);
                break;
            case CONTENT_MANAGER:
                scrollPane.setVisible(true);
                changeContentBtn.setDisable(false);
                submitNewMovieBtn.setDisable(false);
                break;
            case CHAIN_MANAGER:
                confirmChangeBtn.setDisable(false);
                showReportsBtn.setDisable(false);
                break;
            default:
        }

    }

    @FXML
    void logOut(ActionEvent event) {
        logINBtn.setVisible(true);
        showComplaintsBtn.setDisable(true);
        logOUTBtn.setVisible(false);
        scrollPane.setVisible(false);
        showReportsBtn.setDisable(true);
        submitNewMovieBtn.setDisable(true);
        changeContentBtn.setDisable(true);
        confirmChangeBtn.setDisable(true);
    }

    @FXML
    void showComplaints(ActionEvent event) {

    }

    @FXML
    void showReports(ActionEvent event) {

    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }

    public void setMessage(Message message) {
        this.localMessage = message;
    }
}
