package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PriceConstants;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

public class EmployeeController implements ClientDependent {

    private static final Logger logger = LoggerFactory.getLogger(MovieAdditionController.class);

    @FXML
    private ListView<String> complaintsListView;

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
    //private List<Employee> employees;
    //private List<Movie> movies;
    private Employee employee;
    private PriceConstants prices;
    private List<Complaint> complaints;


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
        complaintsListView.setVisible(false);
        Message message = new Message();
//        message.setMessage(MOVIE_REQUEST);
//        message.setData(SHOW_ALL_MOVIES);
//        client.sendMessage(message);
        message.setMessage("get prices");
        message.setData("show all prices");
        client.sendMessage(message);
        message.setMessage("get complaints");
        message.setData("show all complaints");
        client.sendMessage(message);
        EventBus.getDefault().register(this);
        checkMsg();
    }

    private void checkMsg() {
        if (localMessage.getSourceFXML() == ADD_EDIT_MOVIE) {
            logINBtn.setVisible(false);
            logOUTBtn.setVisible(true);
            homeScreenBtn.setDisable(true);
            changeContentBtn.setDisable(false);
            changePriceBtn.setDisable(false);
            employee = localMessage.getEmployee();
            System.out.println(employee.getEmployeeType());
            System.out.println(employee.isActive());
            System.out.println(employee.getUsername());
            System.out.println(employee.getPassword());
        }
        else if (localMessage.getSourceFXML() == COMPLAINT_HANDLER_SCREEN)
        {
            logINBtn.setVisible(false);
            logOUTBtn.setVisible(true);
            homeScreenBtn.setDisable(true);
            showComplaintsBtn.setDisable(false);
            complaintsListView.setVisible(true);
            employee = localMessage.getEmployee();
        }
    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();
            if (message.getData().equals("password check")) {
                Platform.runLater(() -> {
                    this.employee = message.getEmployee();
                    if (employee == null) {
                        Dialog<String> errorDialog = new Dialog<>();
                        errorDialog.setTitle("Sign In Error");
                        errorDialog.setHeaderText("Your username or password is incorrect.");
                        errorDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                        errorDialog.showAndWait();
                    } else {
                        emp();
                    }
                });
            } else if (message.getData().equals("prices")) {
                Platform.runLater(() -> {
                    this.prices = message.getPrices();
                });
            } else if (message.getData().equals("show all complaints")) {
                Platform.runLater(() -> {
                    this.complaints = message.getComplaints();
                    List<String> comps = new ArrayList<>();
                    for (Complaint comp : complaints) {
                        String title = comp.getComplaintTitle();
                        comps.add(title);
                    }
                    complaintsListView.setItems(FXCollections.observableArrayList(comps));
                });
            } else if (message.getData().equals("employee is active")) {
                Platform.runLater(() -> {
                    employee = message.getEmployee();
                    System.out.println(employee.isActive());
                });
            } else if (message.getData().equals("change complaint status")) {
                Platform.runLater(() -> {
                    Message msg = new Message();
                    msg.setMessage("get complaints");
                    msg.setData("show all complaints");
                    client.sendMessage(msg);
                });
            }
        }
    }

    @FXML
    void changeContent(ActionEvent event) {
        try {
            Stage stage = (Stage) changePriceBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("nfew movie");
            message.setSourceFXML(EMPLOYEE_SCREEN);
            message.setEmployee(employee);
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void confirmChange(ActionEvent event) {
        Message message = new Message();
        message.setMessage("get prices");
        message.setData("confirm prices change");
        message.setPrices(prices);
        client.sendMessage(message);

    }

    @FXML
    void homeScreen(ActionEvent event) {
        Stage stage = (Stage) homeScreenBtn.getScene().getWindow();
        EventBus.getDefault().unregister(this);
        client.moveScene(PRIMARY_SCREEN, stage, null);
    }

    @FXML
    void changePriceAction(ActionEvent event) {
        chooseItemComboBox.setVisible(true);
        List<String> purchaseItems = Arrays.asList("Movie Ticket", "Movie Link", "Booklet");
        chooseItemComboBox.setItems(FXCollections.observableArrayList(purchaseItems));

    }

    @FXML
    void chooseItem(ActionEvent event) {
        String itemToChangePrice = chooseItemComboBox.getValue();
        chooseItemComboBox.setValue(itemToChangePrice);
        System.out.println(itemToChangePrice);
        try {
            if (itemToChangePrice != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Change " + itemToChangePrice + " Price");
                dialog.setHeaderText("New " + itemToChangePrice + " Price");
                dialog.setContentText("Set New Price: ");
                Optional<String> result = dialog.showAndWait();
                double newPrice = -1.0;
                if (result.isPresent() && !result.get().isEmpty()) {
                    newPrice = Double.parseDouble(result.get());
                }
                switch (itemToChangePrice) {
                    case "Movie Ticket":
                        prices.setNewMovieTicketPrice(newPrice);
                        break;
                    case "Movie Link":
                        prices.setNewMovieLinkPrice(newPrice);
                        break;
                    case "Booklet":
                        prices.setNewBookletPrice(newPrice);
                        break;
                    default:
                        return;
                }
                if (newPrice != -1) {
                    Message message = new Message();
                    message.setMessage("get prices");
                    message.setData("change price");
                    message.setPrices(prices);
                    client.sendMessage(message);
                }

                // Clear the selection and reset the button cell in the ComboBox
                Platform.runLater(() -> {
                    chooseItemComboBox.getSelectionModel().clearSelection();
                    chooseItemComboBox.setButtonCell(new ListCell<>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(chooseItemComboBox.getPromptText());
                            } else {
                                setText(item);
                            }
                        }
                    });
                });
            }
        } catch (NumberFormatException e) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Price Error", "Please enter a valid number");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void logIn(ActionEvent event) {
        try {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Sign In");
            ButtonType signInButtonType = new ButtonType("Sign In", ButtonBar.ButtonData.OK_DONE);
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

            // Set the result converter for the dialog
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == signInButtonType) {
                    return new Pair<>(username.getText(), password.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(usernamePassword -> {
                System.out.println(usernamePassword);
                boolean flag = false;
                Message message = new Message();
                message.setMessage(GET_EMPLOYEES);
                message.setData("check password");
                message.setUsernamePassword(usernamePassword.toString());
                String[] userpass = usernamePassword.toString().split("=");
                System.out.println(userpass[0] + " " + userpass[1]);
                client.sendMessage(message);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void emp() {
        logINBtn.setVisible(false);
        logOUTBtn.setVisible(true);
        homeScreenBtn.setDisable(true);
        employee.setActive(true);
        Message message = new Message();
        message.setMessage(GET_EMPLOYEES);
        message.setData("set employee as active");
        message.setEmployee(employee);
        client.sendMessage(message);

        switch (employee.getEmployeeType()) {
            case BASE:

                break;
            case SERVICE:
                showComplaintsBtn.setDisable(false);
                break;
            case BRANCH_MANAGER:
                showReportsBtn.setDisable(false);
                break;
            case CONTENT_MANAGER:
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
        complaintsListView.setVisible(false);
        showComplaintsBtn.setDisable(true);
        logOUTBtn.setVisible(false);
        showReportsBtn.setDisable(true);
        changePriceBtn.setDisable(true);
        changeContentBtn.setDisable(true);
        confirmChangeBtn.setDisable(true);
        employee.setActive(false);
        Message message = new Message();
        message.setMessage(GET_EMPLOYEES);
        message.setData("set employee as active");
        message.setEmployee(employee);
        client.sendMessage(message);

    }

    @FXML
    void showComplaints(ActionEvent event) {
        complaintsListView.setVisible(true);
    }

    @FXML
    void complaintToHandle(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedItem = complaintsListView.getSelectionModel().getSelectedItem();
            Complaint c = null;
            for (Complaint comp : complaints) {
                if (comp.getComplaintTitle() == selectedItem) {
                    c = comp;
                    break;
                }
            }
            c.setComplaintStatus("Under Review");
            Stage stage = (Stage) complaintsListView.getScene().getWindow();
            Message message = new Message();
            message.setMessage("get complaints");
            message.setData("change complaint status");
            message.setComplaint(c);
            client.sendMessage(message);
            message.setMessage("handle complaint");
            message.setSourceFXML(EMPLOYEE_SCREEN);
            message.setEmployee(employee);
            message.setComplaint(c);
            EventBus.getDefault().unregister(this);
            client.moveScene(COMPLAINT_HANDLER_SCREEN, stage, message);
        }
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
