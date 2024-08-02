package il.cshaifasweng.OCSFMediatorExample.client.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.*;
import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javassist.Loader;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class CustomerController implements ClientDependent {

    private SimpleClient client;
    private Message localMessage;
    private boolean connectedFlag = false;
    private Customer localCustomer;

    @FXML
    private Button homeScreenBtn;

    @FXML
    private Accordion PurchasesAccordion;

    @FXML
    private Button loginLogoutBtn;

    @FXML
    private Button submitComplaintBtn;

    @FXML
    private Button viewComplaintsBtn;

    @FXML
    private Button viewPurchasesBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableColumn<Complaint, Integer> complaintIdCol;

    @FXML
    private TableColumn<Complaint, String> complaintTitleCol;

    @FXML
    private TableColumn<Complaint, String> complaintDescriptionCol;

    @FXML
    private TableColumn<Complaint, String> complaintStatusCol;

    @FXML
    private TableColumn<Complaint, LocalDateTime> dateOfComplaintCol;

    @FXML
    private TableColumn<Complaint, PurchaseType> typeOfPurchaseCol;

    @FXML
    private TableView<Complaint> tableViewComplaints;

    @FXML
    private TableView<Booklet> bookletTableView;

    @FXML
    private TableColumn<Booklet, Integer> idBookletCol;

    @FXML
    private TableColumn<Booklet, Integer> numOfEntriesBookletCol;

    @FXML
    private TableView<MovieTicket> movieTicketTableView;

    @FXML
    private TableColumn<MovieTicket, Integer> idNumMovieTicketCol;

    @FXML
    private TableColumn<MovieTicket, String> movieNameMovieTicketCol;

    @FXML
    private TableColumn<MovieTicket, MovieSlot> movieSlotMovieTicketCol;

    @FXML
    private TableColumn<MovieTicket, Integer> theaterNumMovieTicketCol;

    @FXML
    private TableColumn<MovieTicket, Integer> rowNumMovieTicketCol;

    @FXML
    private TableColumn<MovieTicket, Integer> seatNumMovieTicketCol;

    @FXML
    private TableColumn<MovieTicket, String> branchNameMovieTicketCol;

    @FXML
    private TitledPane movieTicketsTitlePane;

    @FXML
    private TitledPane bookletsTilePane;


    @FXML
    private TitledPane viewingPackageTilePane;

    @FXML
    private TableColumn<MovieLink, LocalDateTime> activationTimeMovieLinkCol;

    @FXML
    private TableColumn<MovieLink, LocalDateTime> expirationTimeMovieLinkCol;

    @FXML
    private TableColumn<MovieLink, Integer> idNumMovieLinkCol;

    @FXML
    private TableColumn<MovieLink, String> linkMovieLinkCol;

    @FXML
    private TableColumn<MovieLink, String> movieNameMovieLinkCol;

    @FXML
    private TableView<MovieLink> moviePackageTableView;





    @FXML
    void initialize() {
        loggedOutButtons();
        EventBus.getDefault().register(this);

        // Initialize the table values
        // Initialize the columns

        // region Complaint Columns
        complaintIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        complaintTitleCol.setCellValueFactory(new PropertyValueFactory<>("complaintTitle"));
        complaintDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("complaintContent"));
        complaintStatusCol.setCellValueFactory(new PropertyValueFactory<>("complaintStatus"));
        typeOfPurchaseCol.setCellValueFactory(new PropertyValueFactory<>("purchaseType"));
        // Setting up the date column with a custom format
        dateOfComplaintCol.setCellFactory(column -> new TableCell<Complaint, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    // Format the LocalDateTime
                    setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                }
            }
        });
        dateOfComplaintCol.setCellValueFactory(new PropertyValueFactory<>("dateOfComplaint"));
        // endregion

        // region Booklet Columns
        idBookletCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        numOfEntriesBookletCol.setCellValueFactory(new PropertyValueFactory<>("numOfEntries"));
        // endregion

        // region Movie Ticket Columns
        idNumMovieTicketCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        movieNameMovieTicketCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        movieSlotMovieTicketCol.setCellValueFactory(new PropertyValueFactory<>("movieSlot"));
        theaterNumMovieTicketCol.setCellValueFactory(new PropertyValueFactory<>("theaterNum"));
        rowNumMovieTicketCol.setCellValueFactory(new PropertyValueFactory<>("seatRow"));
        seatNumMovieTicketCol.setCellValueFactory(new PropertyValueFactory<>("seatNum"));
        branchNameMovieTicketCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        // endregion

        // region Movie Link Columns
        idNumMovieLinkCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        linkMovieLinkCol.setCellValueFactory(new PropertyValueFactory<>("movieLink"));
        movieNameMovieLinkCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        expirationTimeMovieLinkCol.setCellValueFactory(new PropertyValueFactory<>("expirationTime"));
        expirationTimeMovieLinkCol.setCellFactory(column -> new TableCell<MovieLink, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    // Format the LocalDateTime
                    setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                }
            }
        });
        activationTimeMovieLinkCol.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        activationTimeMovieLinkCol.setCellFactory(column -> new TableCell<MovieLink, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    // Format the LocalDateTime
                    setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                }
            }
        });
        // endregion

        // Initialize pane popups
        if (movieTicketsTitlePane != null) {
            addPopUpToTitledPane(movieTicketsTitlePane);
        }
        if (bookletsTilePane != null) {
            addPopUpToTitledPane(bookletsTilePane);
        }
        if (viewingPackageTilePane != null) {
            addPopUpToTitledPane(viewingPackageTilePane);
        }
    }


    @Subscribe
    public void dataReceived(MessageEvent event) {
        Platform.runLater(() -> {
            Message message = event.getMessage();
            String displayMessage = "Customer wasn't found";
            if (message.getCustomer() != null) {
                localCustomer = message.getCustomer();
                displayMessage = message.getCustomer().getFirstName() + " " + message.getCustomer().getLastName();
                welcomeLabel.setText("Welcome " + message.getCustomer().getFirstName() + " " + message.getCustomer().getLastName() + " Choose the information you wish to view from the side menu");
                loggedInButtons();
                SimpleClient.showAlert(Alert.AlertType.INFORMATION, "Customer connected", displayMessage);
            } else {
                connectedFlag = false;
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Wrong information", displayMessage);
            }
        });
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
            message.setSourceFXML(CUSTOMER_SCREEN);
            EventBus.getDefault().unregister(this);
            client.moveScene(COMPLAINT_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loginLogoutAction(ActionEvent event) {
        if (connectedFlag) {
            loggedOutButtons();
            SimpleClient.showAlert(Alert.AlertType.INFORMATION, "Logged out", "Logged out successfully.");

            // Perform logout
        } else {
            try {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Customer sign In");
                ButtonType signInButtonType = new ButtonType("Sign In", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(signInButtonType, ButtonType.CANCEL);
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                PasswordField id = new PasswordField();
                id.setPromptText("Enter your id");

                grid.add(new Label("Customer ID:"), 0, 1);
                grid.add(id, 1, 1);

                dialog.getDialogPane().setContent(grid);

                // Set the result converter for the dialog
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == signInButtonType) {
                        return id.getText();
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(customerID -> {
                    if (customerID != null && !customerID.isEmpty()) {
                        System.out.println(customerID);
                        Message message = new Message();
                        message.setMessage(GET_CUSTOMER_INFO);
                        message.setData(GET_CUSTOMER_ID);
                        message.setCustomerID(customerID);
                        client.sendMessage(message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loggedInButtons(){
        connectedFlag = true;
        loginLogoutBtn.setText("Logout");
        homeScreenBtn.setDisable(true);
        viewComplaintsBtn.setDisable(false);
        viewPurchasesBtn.setDisable(false);
        viewComplaintsBtn.setVisible(true);
        viewPurchasesBtn.setVisible(true);
    }

    private void addPopUpToTitledPane(TitledPane pane){
        pane.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                boolean items = false;
                if(pane == movieTicketsTitlePane){
                    items = movieTicketTableView.getItems().isEmpty();
                }
                else if(pane == bookletsTilePane) {
                    items = bookletTableView.getItems().isEmpty();
                }

                else if(pane == viewingPackageTilePane) {
                    items = moviePackageTableView.getItems().isEmpty();
                }

                if(items) {
                    SimpleClient.showAlert(Alert.AlertType.INFORMATION,"There are no " + pane.getText(),
                            "There are no " + pane.getText() + " for the customer");
                }
            }
        });
    }

    private void loggedOutButtons(){

        //Setting UI elements when logged off.
        connectedFlag = false;
        homeScreenBtn.setDisable(false);
        viewComplaintsBtn.setDisable(true);
        viewPurchasesBtn.setDisable(true);
        viewComplaintsBtn.setVisible(false);
        viewPurchasesBtn.setVisible(false);
        loginLogoutBtn.setText("Login");
        welcomeLabel.setText("Welcome to the customer panel, login to view your information");
        disableElements();

        //When loading a new customer clear the tables.
        bookletTableView.getItems().clear();
        movieTicketTableView.getItems().clear();
        moviePackageTableView.getItems().clear();

        //Checking if the panes are not null and then setting them to not expend.
        if(movieTicketsTitlePane != null) {
            movieTicketsTitlePane.setExpanded(false);
        }
        if(bookletsTilePane != null) {
            bookletsTilePane.setExpanded(false);
        }
        if(viewingPackageTilePane != null){
            viewingPackageTilePane.setExpanded(false);
        }

    }

    private void disableElements(){
        tableViewComplaints.setVisible(false);
        tableViewComplaints.setDisable(true);
        PurchasesAccordion.setVisible(false);
        PurchasesAccordion.setDisable(true);
    }
    @FXML
    void returnHomeAction(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) homeScreenBtn.getScene().getWindow();
            Message message = new Message();
            EventBus.getDefault().unregister(this);
            client.moveScene(PRIMARY_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void viewComplaintsAction(ActionEvent event) {
        disableElements();
        tableViewComplaints.setVisible(true);
        tableViewComplaints.setDisable(false);
        if (localCustomer == null) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "No Customer", "You must be logged in to view complaints.");
            return;
        }

        // Assuming complaints are fetched and stored in localCustomer object correctly
        if (localCustomer.getComplaints() != null && !localCustomer.getComplaints().isEmpty()) {
            tableViewComplaints.getItems().clear(); // Clear existing items
            tableViewComplaints.getItems().addAll(localCustomer.getComplaints()); // Add new items
            tableViewComplaints.refresh(); // Refresh the table view to display new data
        } else {
            SimpleClient.showAlert(Alert.AlertType.INFORMATION, "No Complaints", "There are no complaints to display.");
        }
    }


    @FXML
    void viewPurchasesAction(ActionEvent event) {
        // Disable and hide the complaints table
        tableViewComplaints.setDisable(true);
        tableViewComplaints.setVisible(false);

        // Enable and show the purchases accordion
        PurchasesAccordion.setVisible(true);
        PurchasesAccordion.setDisable(false);

        // Ensure the booklet table view is visible and enabled
        bookletTableView.setVisible(true);
        bookletTableView.setDisable(false);

        // Ensure the movie ticket table view is visible and enabled
        movieTicketTableView.setVisible(true);
        movieTicketTableView.setDisable(false);

        // Ensure the movie package table view is visible and enabled
        moviePackageTableView.setVisible(true);
        moviePackageTableView.setDisable(false);

        // Fetch and display the booklets from purchases
        if (localCustomer == null) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "No Customer", "You must be logged in to view purchases.");
            return;
        }

        // Assuming purchases are fetched and stored in the localCustomer object correctly
        if (localCustomer.getPurchases() != null && !localCustomer.getPurchases().isEmpty()) {
            // Extract booklets from purchases
            List<Booklet> booklets = localCustomer.getPurchases().stream()
                    .map(Purchase::getPurchasedBooklet)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!booklets.isEmpty()) {
                bookletTableView.getItems().clear(); // Clear existing items
                bookletTableView.getItems().addAll(booklets); // Add new items
                bookletTableView.refresh(); // Refresh the table view to display new data
            } else if (bookletsTilePane.isExpanded() && booklets.isEmpty()) {
                SimpleClient.showAlert(Alert.AlertType.INFORMATION, "No Booklets", "There are no booklets to display.");
            }

            // Extract movie tickets from purchases
            List<MovieTicket> movieTickets = localCustomer.getPurchases().stream()
                    .map(Purchase::getPurchasedMovieTicket)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!movieTickets.isEmpty()) {
                movieTicketTableView.getItems().clear(); // Clear existing items
                movieTicketTableView.getItems().addAll(movieTickets); // Add new items
                movieTicketTableView.refresh(); // Refresh the table view to display new data
            } else if (movieTicketsTitlePane.isExpanded() && movieTickets.isEmpty()) {
                SimpleClient.showAlert(Alert.AlertType.INFORMATION, "No Movie Tickets", "There are no movie tickets to display.");
            }

            // Extract movie links from purchases
            List<MovieLink> movieLinks = localCustomer.getPurchases().stream()
                    .map(Purchase::getPurchasedMovieLink)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!movieLinks.isEmpty()) {
                moviePackageTableView.getItems().clear(); // Clear existing items
                moviePackageTableView.getItems().addAll(movieLinks); // Add new items
                moviePackageTableView.refresh(); // Refresh the table view to display new data
            } else if (viewingPackageTilePane.isExpanded() && movieLinks.isEmpty()) {
                SimpleClient.showAlert(Alert.AlertType.INFORMATION, "No Movie Packages", "There are no movie packages to display.");
            }
        } else {
            SimpleClient.showAlert(Alert.AlertType.INFORMATION, "No Purchases", "There are no purchases to display.");
        }
    }



    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }
}
