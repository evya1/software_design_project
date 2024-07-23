package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog.MovieGridController;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Booklet;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

import java.util.Arrays;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

public class EmployeeController implements ClientDependent {

    private static final Logger logger = LoggerFactory.getLogger(MovieAdditionController.class);

    @FXML
    private ComboBox<String> chooseItemComboBox;

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
    private Button showComplaintsBtn;

    @FXML
    private Button showReportsBtn;

    @FXML
    private Button changePriceBtn;

    @FXML
    private Button logINBtn;

    @FXML
    private Button logOUTBtn;

    //
    private Message localMessage;
    private SimpleClient client;
    private List<Employee> employees;
    //private List<Movie> movies;
    private Employee employee;



    @FXML
    private void initialize() {
        List<String> purchaseItems = Arrays.asList("Movie Ticket", "Movie Link", "Booklet");
        chooseItemComboBox.setItems(FXCollections.observableArrayList(purchaseItems));
        chooseItemComboBox.setVisible(false);
        showComplaintsBtn.setDisable(true);
        logOUTBtn.setVisible(false);
        showReportsBtn.setDisable(true);
        changePriceBtn.setDisable(true);
        changeContentBtn.setDisable(true);
        confirmChangeBtn.setDisable(true);
        Message message = new Message();
//        message.setMessage(MOVIE_REQUEST);
//        message.setData(SHOW_ALL_MOVIES);
//        client.sendMessage(message);
        message.setMessage(GET_EMPLOYEES);
        message.setData(SHOW_ALL_EMPLOYEES);
        client.sendMessage(message);
        EventBus.getDefault().register(this);
        checkMsg();
    }

    private void checkMsg() {
        if (localMessage.getSourceFXML() == ADD_EDIT_MOVIE) {
            chooseItemComboBox.setVisible(true);
            logINBtn.setVisible(false);
            logOUTBtn.setVisible(true);
            homeScreenBtn.setDisable(true);
            changeContentBtn.setDisable(false);
            changePriceBtn.setDisable(false);
        }
    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();
            if (message.getData().equals(SHOW_ALL_EMPLOYEES)) {
                this.employees = message.getEmployeeList();
            }
        }
    }

    public void chooseMovie(Movie movie, Stage stage) {
        try {
            localMessage = new Message();
            localMessage.setSpecificMovie(movie);
            EventBus.getDefault().unregister(this);
            client.moveScene("catalogM/Movie", stage, localMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void changeContent(ActionEvent event) {
        try {
            Stage stage = (Stage) changePriceBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("nfew movie");
            message.setSourceFXML(EMPLOYEE_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage ,message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void confirmChange(ActionEvent event) {

    }

    @FXML
    void homeScreen(ActionEvent event) {
        Stage stage = (Stage) homeScreenBtn.getScene().getWindow();
        EventBus.getDefault().unregister(this);
        client.moveScene(PRIMARY_SCREEN, stage, null);
    }

    @FXML
    void changePriceAction(ActionEvent event) {

    }

    @FXML
    void chooseItem(ActionEvent event) {

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
        homeScreenBtn.setDisable(true);
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
                chooseItemComboBox.setVisible(true);
                changeContentBtn.setDisable(false);
                changePriceBtn.setDisable(false);
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
        chooseItemComboBox.setVisible(false);
        homeScreenBtn.setDisable(false);
        logINBtn.setVisible(true);
        showComplaintsBtn.setDisable(true);
        logOUTBtn.setVisible(false);
        showReportsBtn.setDisable(true);
        changePriceBtn.setDisable(true);
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
