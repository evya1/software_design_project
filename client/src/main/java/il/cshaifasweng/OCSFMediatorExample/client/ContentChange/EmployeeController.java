package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.FXMLUtils;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PriceConstants;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.GET_EMPLOYEES;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;

public class EmployeeController implements ClientDependent {

    private static final Logger logger = LoggerFactory.getLogger(MovieAdditionController.class);
    public Button stampBookletBtn;

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
    private Employee employee;
    private PriceConstants prices;
    private List<Complaint> complaints;


    @FXML
    private void initialize() {
        disableUIElements();
        logger.info("Starting Initialization.");
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

        switch (localMessage.getSourceFXML()) {
            case ADD_EDIT_MOVIE -> {
                logINBtn.setVisible(false);
                logOUTBtn.setVisible(true);
                homeScreenBtn.setDisable(true);
                changeContentBtn.setDisable(false);
                changePriceBtn.setDisable(false);
                employee = localMessage.getEmployee();
                printEmplyoee(localMessage.getEmployee());
                enableButtonsAccordingToEmployeeType(employee.getEmployeeType());
            }
            case COMPLAINT_HANDLER_SCREEN -> {
                logINBtn.setVisible(false);
                logOUTBtn.setVisible(true);
                homeScreenBtn.setDisable(true);
                showComplaintsBtn.setDisable(false);
                complaintsListView.setVisible(true);
                employee = localMessage.getEmployee();
                printEmplyoee(localMessage.getEmployee());
                enableButtonsAccordingToEmployeeType(employee.getEmployeeType());
            }
            case REPORTS_SCREEN -> {
                logINBtn.setVisible(false);
                logOUTBtn.setVisible(true);
                homeScreenBtn.setDisable(true);
                employee = localMessage.getEmployee();
                printEmplyoee(localMessage.getEmployee());
                enableButtonsAccordingToEmployeeType(employee.getEmployeeType());
            }
        }
    }

    private void printEmplyoee(Employee employee){
        System.out.println(employee.getFirstName());
        System.out.println(employee.getLastName());
        System.out.println(employee.getPassword());
        System.out.println(employee.getUsername());
        System.out.println(employee.isActive());
    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();
            switch (message.getData()) {
                case "password check" -> Platform.runLater(() -> {
                    this.employee = message.getEmployee();
                    if (employee == null) {
                        Dialog<String> errorDialog = new Dialog<>();
                        errorDialog.setTitle("Sign In Error");
                        errorDialog.setHeaderText("Your username or password is incorrect.");
                        errorDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                        errorDialog.showAndWait();
                    } else {
                        if (employee.isActive())
                            SimpleClient.showAlert(Alert.AlertType.ERROR, "Employee Error", "Employee is already active");
                        else
                            emp();
                    }
                });
                case "prices" -> Platform.runLater(() -> {
                    this.prices = message.getPrices();
                });
                case "show all complaints" -> Platform.runLater(() -> {
                    this.complaints = message.getComplaints();
                    List<String> comps = new ArrayList<>();
                    for (Complaint comp : complaints) {
                        String title = comp.getComplaintTitle();
                        comps.add(title);
                    }
                    complaintsListView.setItems(FXCollections.observableArrayList(comps));
                });
                case "employee is active" -> Platform.runLater(() -> {
                    employee = message.getEmployee();
                    System.out.println("Incoming Message from Server, Employee status login " +employee.isActive());
                });
                case "change complaint status" -> Platform.runLater(() -> {
                    Message msg = new Message();
                    msg.setMessage("get complaints");
                    msg.setData("show all complaints");
                    client.sendMessage(msg);
                });
            }
        }
    }

    @FXML
    void logIn(ActionEvent event) {

        //region Setting up a logout for the existing Employee.
        Stage stage = (Stage) homeScreenBtn.getScene().getWindow();
        EventHandler<WindowEvent> existingHandler = stage.getOnCloseRequest();

        stage.setOnCloseRequest(eventLogout -> {
            // Perform additional action
            if(employee != null){
                employee.setActive(false);
                Message message = new Message();
                message.setMessage(GET_EMPLOYEES);
                message.setData("set employee as active");
                message.setEmployee(employee);
                client.sendMessage(message);
            }

            // Call the existing handler if it exists
            if (existingHandler != null) {
                existingHandler.handle(eventLogout);
            }
        });
        //endregion

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
                String[] userPass = usernamePassword.toString().split("=");
                System.out.println(userPass[0] + " " + userPass[1]);
                client.sendMessage(message);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void logOut(ActionEvent event) {
        chooseItemComboBox.setVisible(false);
        homeScreenBtn.setDisable(false);
        logINBtn.setVisible(true);
        logOUTBtn.setVisible(false);
        disableUIElements();
        employee.setActive(false);
        Message message = new Message();
        message.setMessage(GET_EMPLOYEES);
        message.setData("set employee as active");
        message.setEmployee(employee);
        client.sendMessage(message);
        employee = null;
    }

    @FXML
    public void stampBookletAction(ActionEvent event) {

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
        enableButtonsAccordingToEmployeeType(employee.getEmployeeType());
    }

    private void enableButtonsAccordingToEmployeeType(EmployeeType type){
        switch (type) {
            case BASE:
                stampBookletBtn.setVisible(true);
                stampBookletBtn.setDisable(false);
                break;
            case SERVICE:
                showComplaintsBtn.setDisable(false);
                showComplaintsBtn.setVisible(true);
                break;
            case BRANCH_MANAGER:
                showReportsBtn.setDisable(false);
                showReportsBtn.setVisible(true);
                break;
            case CONTENT_MANAGER:
                changeContentBtn.setDisable(false);
                changeContentBtn.setVisible(true);
                changePriceBtn.setDisable(false);
                changePriceBtn.setVisible(true);
                break;
            case CHAIN_MANAGER:
                confirmChangeBtn.setDisable(false);
                confirmChangeBtn.setVisible(true);
                showReportsBtn.setDisable(false);
                showReportsBtn.setVisible(true);
                break;
            default:
                System.out.println("Unknown Case");
                break;
        }
    }

    //Disabling all the buttons before enabling them according to the EmployeeType.
    private void disableUIElements(){
        showReportsBtn.setDisable(true);
        showReportsBtn.setVisible(false);

        changePriceBtn.setDisable(true);
        changePriceBtn.setVisible(false);

        showComplaintsBtn.setDisable(true);
        showComplaintsBtn.setVisible(false);

        changeContentBtn.setDisable(true);
        changeContentBtn.setVisible(false);

        confirmChangeBtn.setDisable(true);
        confirmChangeBtn.setVisible(false);

        stampBookletBtn.setDisable(true);
        stampBookletBtn.setVisible(false);

        complaintsListView.setVisible(false);
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
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) showReportsBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage("View Reports Button Clicked");
            message.setSourceFXML(EMPLOYEE_SCREEN);
            message.setEmployee(employee);
            client.moveScene(REPORTS_SCREEN,stage,message);
            EventBus.getDefault().unregister(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }

    public void setMessage(Message message) {
        this.localMessage = message;
    }
}
