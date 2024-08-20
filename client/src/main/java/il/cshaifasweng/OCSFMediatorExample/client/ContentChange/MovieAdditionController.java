package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieGenre;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.TypeOfMovie;
import il.cshaifasweng.OCSFMediatorExample.client.Utility.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;
import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.*;

//TODO: need to clear the comboboxes when the Branch is changed.

public class MovieAdditionController implements ClientDependent {

    //region Private attributes
    private static final String clientNotInit = "Client is not initialized!\n";
    private static final Logger logger = LoggerFactory.getLogger(MovieAdditionController.class);

    private List<Movie> movies;
    private List<MovieSlot> slots;
    private Movie movie;
    private TypeOfMovie movieType;
    private SimpleClient client;
    private Message localMessage;
    private List<Branch> branches;
    private PauseTransition pause;
    private boolean flag = false;

    private final String errorColor = "red";
    private final String normalColor = "black";

    //endregion

    //region FXML Attributes
    @FXML
    private AnchorPane movieToDoChangeOn;

    @FXML
    private Button browseBtn;
    @FXML
    private Button submitNewBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button previewImageButton;
    @FXML
    private Button submitMovieBtn;
    @FXML
    private Button newMovieBtn;
    @FXML
    private Button deleteMovieBtn;
    @FXML
    private Button backBtn1;
    @FXML
    private CheckBox inTheatersCheckBox;
    @FXML
    private CheckBox packageCheckBox;
    @FXML
    private CheckBox soonCheckBox;

    @FXML
    private ComboBox<Branch> branchModifyComboBox;
    @FXML
    private ComboBox<Theater> theaterModifyComboBox;
    @FXML
    private ComboBox<MovieGenre> chooseGenreComboBox;
    @FXML
    private ComboBox<Movie> chooseMovieComboBox;

    @FXML
    private DatePicker datePickerScreening;

    @FXML
    private Label informationLabel;

    @FXML
    private Tab ModifyScreeningTimeTab;

    @FXML
    private Tab modifyMovieTab;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<MovieSlot> screeningTableView;
    @FXML
    private TableColumn<MovieSlot, Branch> branchNameCol;
    @FXML
    private TableColumn<MovieSlot, Theater> theaterNumCol;
    @FXML
    private TableColumn<MovieSlot, LocalDateTime> dateCol;
    @FXML
    private TableColumn<MovieSlot, LocalDateTime> startHourCol;
    @FXML
    private TableColumn<MovieSlot, LocalDateTime> endHourCol;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField hourTextField;
    @FXML
    private TextField movieDurationTextField;
    @FXML
    private TextField castTextField;
    @FXML
    private TextField englishTitleTextField;
    @FXML
    private TextField filePathTextField;
    @FXML
    private TextField hebrewTitleTextField;
    @FXML
    private TextField producerTextField;

    @FXML
    private ImageView movieImageView;

    @FXML
    private Tooltip hourToolTip;

    //endregion


    @FXML
    public void initialize() {

        //region ContextMenu for the Screening Table

        // Adding right-click context menu for row deletion and modification
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Edit");

        contextMenu.getItems().addAll(deleteItem, editItem);

        // Set context menu on each row, and only show for non-empty rows
        screeningTableView.setRowFactory(tv -> {
            TableRow<MovieSlot> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });

        // Handle the delete action
        deleteItem.setOnAction(event -> {
            MovieSlot selectedMovieSlot = screeningTableView.getSelectionModel().getSelectedItem();
            if (selectedMovieSlot != null) {
                deleteMovieSlot(selectedMovieSlot);
            }
        });

        //Changing the displayed text in context menu depending on the previous value.
        editItem.setOnAction(actionEvent -> {
            boolean isEditable = screeningTableView.isEditable();
            screeningTableView.setEditable(!isEditable);
            editItem.setText(isEditable ? "Edit" : "Disable Edit");
        });

        //endregion

        EventBus.getDefault().register(this);
        chooseGenreComboBox.getItems().addAll(MovieGenre.values());

        if (localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            chooseMovieComboBox.setVisible(false);
            backBtn1.setVisible(true);
            backBtn1.setDisable(false);
            movieToDoChangeOn.setVisible(true);
            deleteMovieBtn.setVisible(false);
            deleteMovieBtn.setDisable(true);
            movie = new Movie();
            movieType = new TypeOfMovie();
            movie.setMovieType(movieType);
            addTextInputListener(descriptionTextArea, null);
            addTextInputListener(englishTitleTextField, null);
            addTextInputListener(filePathTextField, null);
            addTextInputListener(hebrewTitleTextField, null);
            addTextInputListener(producerTextField, null);
            addTextInputListener(castTextField, null);
            addTextInputListener(movieDurationTextField, null);
            ModifyScreeningTimeTab.setDisable(true);
        }
        //Loading an existing movie.
        else {
            filePathTextField.setDisable(false);
            browseBtn.setDisable(false);
            previewImageButton.setDisable(false);
            chooseMovieComboBox.setVisible(true);
            backBtn1.setVisible(false);
            backBtn1.setDisable(true);
            movieToDoChangeOn.setVisible(false);
            deleteMovieBtn.setVisible(true);
            deleteMovieBtn.setDisable(false);
            submitMovieBtn.setText("Update Movie");
        }
        addTextInputListener(hourTextField,null);
        addTextInputListener(branchModifyComboBox,null);
        addTextInputListener(datePickerScreening,null);
        addTextInputListener(theaterModifyComboBox, null);

        Message message = new Message();
        message.setMessage(MOVIE_REQUEST);
        message.setData(SHOW_ALL_MOVIES);
        client.sendMessage(message);
        message.setMessage(BRANCH_THEATER_INFORMATION);
        message.setData(GET_BRANCHES);
        client.sendMessage(message);
    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        if (event != null) {
            Message message = event.getMessage();
            if (message.getData().equals(SHOW_ALL_MOVIES)) {
                Platform.runLater(() -> {
                    movies = message.getMovies();
                    chooseMovieComboBox.getItems().addAll(movies);
                });
            }
            if (message.getData().equals(GET_BRANCHES)) {
                Platform.runLater(() -> {
                    this.branches = message.getBranches();
                    branchModifyComboBox.getItems().addAll(branches);
                    setupTableColumns(); // Re-setup columns after getting branches
                });
            }
            if (message.getData().equals(GET_MOVIE_SLOT_BY_MOVIE_ID)) {
                Platform.runLater(()->{
                    slots = message.getMovieSlots();
                });
                Platform.runLater(()->{
                    setupTableColumns();
                    screeningTableView.getItems().clear();
                    screeningTableView.getItems().addAll(slots);
                    screeningTableView.refresh();
                });
            }
        }
    }


    //region Helper Methods

    private void requestMovieSlotUpdate(){
        Message message = new Message();
        message.setMessage(MOVIE_SLOT_INFORMATION);
        message.setData(GET_MOVIE_SLOT_BY_MOVIE_ID);
        message.setSpecificMovie(movie);
        client.sendMessage(message);
    }
    private void deleteMovieSlot(MovieSlot slot){

        //Sending a message to the server to delete the requested Movie Slot.
        Message msg = new Message();
        msg.setMessage(CONTENT_CHANGE);
        msg.setData(DELETE_MOVIE_SLOT);
        msg.setMovieSlot(slot);
        client.sendMessage(msg);

        //Deleting the selected Movie Slot from the table.
        screeningTableView.getItems().remove(slot);
        screeningTableView.refresh();
        requestMovieSlotUpdate();
    }

    // Show label information for 5 seconds.
    private void pauseTransitionLabelUpdate(String string) {
        informationLabel.setText(string);

        // Create a pause transition of 5 seconds
        if (pause != null) {
            pause.setOnFinished(null);
            pause.stop();
        }

        pause = new PauseTransition(Duration.seconds(5));

        // Set what to do after the pause
        pause.setOnFinished(event -> informationLabel.setText(""));

        // Start the pause
        pause.play();
    }

    private void setupTableColumns() {
        //Setting a new message that will be sent to the server changing the MovieSlot details.
        Message message = new Message();
        message.setMessage(CONTENT_CHANGE);
        message.setData(UPDATE_MOVIE_SLOT);
        message.setSpecificMovie(movie);

        //Initializing the Table columns with the Branches and information from the server.

        //region branchNameCol
        branchNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBranch()));
        branchNameCol.setCellFactory(column -> {
            ComboBoxTableCell<MovieSlot, Branch> cell = new ComboBoxTableCell<>(FXCollections.observableArrayList(branches));
            cell.setComboBoxEditable(true);
            return cell;
        });
        branchNameCol.setOnEditCommit(event -> {
            MovieSlot movieSlot = event.getRowValue();
            Branch newBranch = event.getNewValue();
            movieSlot.setBranch(newBranch);

            // Update the theater column to reflect the new branch
            theaterNumCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(newBranch.getTheaterList())));
            theaterNumCol.setEditable(true); // Make the theater column editable
            movieSlot.setTheater(null); // Reset the theater selection when the branch changes
            screeningTableView.refresh();
            pauseTransitionLabelUpdate("Please set a Theater to save the changes.");
        });
        //endregion

        //region theaterNumCol
        theaterNumCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTheater()));
        theaterNumCol.setCellFactory(column -> {
            ComboBoxTableCell<MovieSlot, Theater> cell = new ComboBoxTableCell<>(FXCollections.observableArrayList());
            cell.setComboBoxEditable(true);
            return cell;
        });
        theaterNumCol.setOnEditCommit(event -> {
            MovieSlot movieSlot = event.getRowValue();
            movieSlot.setTheater(event.getNewValue());
            message.setMovieSlot(movieSlot);
            client.sendMessage(message);
            theaterNumCol.setEditable(false); // Initially set this column to be non-editable
        });
        theaterNumCol.setEditable(false); // Initially set this column to be non-editable

        //endregion

        //region dateCol
        dateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartDateTime()));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDateTime>() {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDateTime dateTime) {
                return dateTime != null ? dateTime.format(formatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                try {
                    LocalDate date = LocalDate.parse(string, formatter);
                    return LocalDateTime.of(date, LocalTime.MIN); // Use existing time component
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        }));
        dateCol.setOnEditCommit(event -> {
            MovieSlot movieSlot = event.getRowValue();
            LocalDateTime newDateTime = event.getNewValue();
            if (newDateTime != null) {
                LocalDate currentDate = newDateTime.toLocalDate();
                LocalTime currentTime = movieSlot.getStartDateTime().toLocalTime();
                if (currentDate.isBefore(LocalDate.now())) {
                    pauseTransitionLabelUpdate("The date entered cannot be before today.");
                    screeningTableView.refresh();
                }
                else{
                    movieSlot.setStartDateTime(LocalDateTime.of(currentDate, currentTime));
                    movieSlot.setEndDateTime(LocalDateTime.of(currentDate, movieSlot.getEndDateTime().toLocalTime()));
                    message.setMovieSlot(movieSlot);
                    client.sendMessage(message);
                }
            } else {
                pauseTransitionLabelUpdate("Invalid date format. Use dd-MM-yyyy.");
                screeningTableView.refresh();
            }
        });
        //endregion

        //region startHourCol
        startHourCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartDateTime()));
        startHourCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDateTime>() {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalDateTime dateTime) {
                return dateTime != null ? dateTime.format(formatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                try {
                    LocalTime time = LocalTime.parse(string, formatter);
                    return LocalDateTime.of(LocalDate.MIN, time); // Use existing date component
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        }));
        startHourCol.setOnEditCommit(event -> {
            MovieSlot movieSlot = event.getRowValue();
            LocalDateTime newDateTime = event.getNewValue();
            if (newDateTime != null) {
                LocalDate currentDate = movieSlot.getStartDateTime().toLocalDate();
                LocalTime newTime = newDateTime.toLocalTime();
                movieSlot.setStartDateTime(LocalDateTime.of(currentDate, newTime));
                message.setMovieSlot(movieSlot);
                client.sendMessage(message);
            } else {
                pauseTransitionLabelUpdate("Invalid time format. Use HH:mm.");
                screeningTableView.refresh();
            }
        });
        //endregion

        //region endHourCol
        endHourCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndDateTime()));
        endHourCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDateTime>() {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalDateTime dateTime) {
                return dateTime != null ? dateTime.format(formatter) : "";
            }

            @Override
            public LocalDateTime fromString(String string) {
                try {
                    LocalTime time = LocalTime.parse(string, formatter);
                    return LocalDateTime.of(LocalDate.MIN, time); // Use existing date component
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        }));
        endHourCol.setOnEditCommit(event -> {
            MovieSlot movieSlot = event.getRowValue();
            LocalDateTime newDateTime = event.getNewValue();
            if (newDateTime != null) {
                LocalDate currentDate = movieSlot.getEndDateTime().toLocalDate();
                LocalTime newTime = newDateTime.toLocalTime();
                movieSlot.setEndDateTime(LocalDateTime.of(currentDate, newTime));
                message.setMovieSlot(movieSlot);
                client.sendMessage(message);
            } else {
                pauseTransitionLabelUpdate("Invalid time format. Use HH:mm.");
                screeningTableView.refresh();
            }
        });
        //endregion

    }

    private void showTooltip(Control control, Tooltip tooltip) {
        // Calculate the position of the tooltip
        double x = control.getScene().getWindow().getX() + control.getLocalToSceneTransform().getTx() + control.getWidth() / 2;
        double y = control.getScene().getWindow().getY() + control.getLocalToSceneTransform().getTy() + control.getHeight();

        // Show the tooltip
        tooltip.show(control, x, y);

        // Create a PauseTransition to hide the tooltip after 2 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();
    }

    private void loadNewImage(){
        try {
            File file = new File(filePathTextField.getText());
            Image image = new Image(file.toURI().toString());
            movieImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            SimpleClient.showAlert(Alert.AlertType.ERROR, "File path", "File location is incorrect");
        }
    }

    private void copyMovieDetails(boolean newMovie) throws IOException {
        try {
            movie.setHebrewMovieName(hebrewTitleTextField.getText());
            movie.setMovieName(englishTitleTextField.getText());
            movie.setMovieDescription(descriptionTextArea.getText());
            movie.setMainCast(castTextField.getText());
            movie.setMovieDuration(Integer.parseInt(movieDurationTextField.getText()));
            movie.setProducer(producerTextField.getText());
            movie.setMovieGenre(chooseGenreComboBox.getValue());
            movie.getMovieType().setUpcoming(soonCheckBox.isSelected());
            movie.getMovieType().setCurrentlyRunning(inTheatersCheckBox.isSelected());
            movie.getMovieType().setPurchasable(packageCheckBox.isSelected());

            //If it's a new movie or if there was a new image loaded for the movie during edit.
            if (newMovie || !filePathTextField.getText().isEmpty()) {
                movie.setImage(Files.readAllBytes(Paths.get(filePathTextField.getText())));
            }
        }catch (NumberFormatException NumberFormatException) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Movie Duration error", "Please enter a valid integer");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Checking the fields and setting them accordingly.
    private boolean checkFields() {
        boolean flag = false;
        for (Movie mov : movies) {
            if (mov.getMovieName() != null && mov.getMovieName().equalsIgnoreCase(englishTitleTextField.getText().trim()))
                return true;
        }
        if (hebrewTitleTextField.getText().isEmpty()) {
            changeControlBorderColor(hebrewTitleTextField, errorColor);
            flag = true;
        }
        if (englishTitleTextField.getText().isEmpty()) {
            changeControlBorderColor(englishTitleTextField, errorColor);
            flag = true;
        }
        if (producerTextField.getText().isEmpty()) {
            changeControlBorderColor(producerTextField, errorColor);
            flag = true;
        }
        if (castTextField.getText().isEmpty()) {
            changeControlBorderColor(castTextField, errorColor);
            flag = true;
        }
        if (movieDurationTextField.getText().isEmpty()) {
            changeControlBorderColor(movieDurationTextField, errorColor);
        }
        if (filePathTextField.getText().isEmpty() && localMessage.getMessage().equals("new movie")) {
            changeControlBorderColor(filePathTextField, errorColor);
            flag = true;
        }
        if (chooseGenreComboBox.getValue() == null) {
            changeControlBorderColor(chooseGenreComboBox, errorColor);
            flag = true;
        }
        if (!inTheatersCheckBox.isSelected() && !packageCheckBox.isSelected() && !soonCheckBox.isSelected()) {
            setCheckBoxesColor(errorColor);
            flag = true;
        }

        return flag;
    }

    private void setCheckBoxesColor(String color) {
        changeControlTextColor(soonCheckBox, color);
        changeControlTextColor(inTheatersCheckBox, color);
        changeControlTextColor(packageCheckBox, color);
    }

    //Resets the fields color to be null - the transparent border by default.
    private void resetFieldsColor() {
        changeControlBorderColor(descriptionTextArea, null);
        changeControlBorderColor(hebrewTitleTextField, null);
        changeControlBorderColor(englishTitleTextField, null);
        changeControlBorderColor(castTextField, null);
        changeControlBorderColor(producerTextField, null);
        changeControlBorderColor(chooseGenreComboBox, null);
        changeControlTextColor(soonCheckBox, normalColor);
        changeControlTextColor(inTheatersCheckBox, normalColor);
        changeControlTextColor(packageCheckBox, normalColor);
        changeControlTextColor(movieDurationTextField, normalColor);
    }


    //endregion

    //region FXML Gui methods
    @FXML
    void previewImage(ActionEvent event) {
        //Loading movie image from local path given by the user or from Movie.
        if (localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            if (!filePathTextField.getText().isEmpty()) {
               loadNewImage();
            } else {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "File path", "The file path is empty.");
            }

        } else {
            if(!filePathTextField.getText().isEmpty()){
                loadNewImage();
            }
            else{
                if (movie.getImage() != null) {
                    InputStream imageStream = new ByteArrayInputStream(movie.getImage());
                    Image image = new Image(imageStream);
                    movieImageView.setImage(image);
                }
            }
        }
    }

    @FXML
    void submitMovie(ActionEvent event) throws IOException {
        resetFieldsColor();

        //Checking if the requested is a New movie and checking the fields accordingly.
        if (localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            if (!checkFields()) {
                try {
                    copyMovieDetails(true);
                    Message message = new Message();
                    message.setMessage(CONTENT_CHANGE);
                    message.setData("New Movie");
                    message.setSpecificMovie(movie);
                    client.sendMessage(message);
                    System.out.println("Movie ID: " + movie.getId() +
                            ", Name: " + movie.getMovieName() + "Hebrew: " + movie.getHebrewMovieName() +
                            ", Main Cast: " + movie.getMainCast() +
                            ", Producer: " + movie.getProducer() +
                            ", Description: " + movie.getMovieDescription() +
                            ", Duration: " + movie.getMovieDuration());

                    Stage stage = (Stage) newMovieBtn.getScene().getWindow();
                    message = new Message();
                    message.setMessage(NEW_MOVIE_TEXT_REQUEST);
                    message.setEmployee(localMessage.getEmployee());
                    logger.info("Moving scene");
                    EventBus.getDefault().unregister(this);
                    client.moveScene(ADD_EDIT_MOVIE, stage, message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // If its a new movie setup the fields.
        } else {
            if (movie != null) {
                copyMovieDetails(false);
                Message message = new Message();
                message.setMessage(CONTENT_CHANGE);
                message.setData(UPDATE_MOVIE);
                message.setSpecificMovie(movie);
                if(!flag && movie.getMovieType().isCurrentlyRunning()) {
                    message.setNewContentFlag(true);
                    System.out.println("Entered");
                }
                flag = inTheatersCheckBox.isSelected();
                client.sendMessage(message);
            } else {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Updating Error", "you have not selected a movie");
            }
        }
    }

    @FXML
    void deleteMovie(ActionEvent event) {
        if (movie != null) {
            Dialogs.yesNoDialog("Are you sure you want to delete this movie?",
                    //Action to perform on YES
                    () -> {
                        Message message = new Message();
                        message.setMessage(CONTENT_CHANGE);
                        message.setData(DELETE_MOVIE);
                        logger.info("Movie ID: {} Was requested to be deleted.", movie.getId());
                        message.setMovieID(movie.getId());
                        client.sendMessage(message);
                        Stage stage = (Stage) deleteMovieBtn.getScene().getWindow();
                        message = new Message();
                        message.setMessage("back to change content screen");
                        message.setEmployee(localMessage.getEmployee());
                        logger.info("Moving scene");
                        EventBus.getDefault().unregister(this);
                        client.moveScene(ADD_EDIT_MOVIE, stage, message);
                    },
                    //Action to perform on NO
                    () -> {
                        return;
                    });
        } else {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Deletion Error", "you have not selected a movie");
        }
    }

    @FXML
    public void browseLocation(ActionEvent actionEvent) {
        changeControlBorderColor(filePathTextField, null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            filePathTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void createNewMovie(ActionEvent event) {
        if (client == null) {
            logger.error(clientNotInit);
            return;
        }
        try {
            Stage stage = (Stage) newMovieBtn.getScene().getWindow();
            Message message = new Message();
            message.setMessage(NEW_MOVIE_TEXT_REQUEST);
            message.setEmployee(localMessage.getEmployee());
            logger.info("Moving scene");
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void branchModifyChangeAction(ActionEvent event) {
        changeControlBorderColor(branchModifyComboBox, null);
        Branch selectedBranch = branchModifyComboBox.getSelectionModel().getSelectedItem();
        if(selectedBranch != null){
            theaterModifyComboBox.getItems().clear();
            theaterModifyComboBox.getItems().addAll(selectedBranch.getTheaterList());
        }
    }

    @FXML
    public void theaterModifyAction(ActionEvent event) {
        changeControlBorderColor(theaterModifyComboBox, null);
    }

    @FXML
    public void submitNewAction(ActionEvent event) {

        //Checking the fields if they're set correctly.
        boolean hasError = false;
        if(branchModifyComboBox.getValue() == null){
            changeControlBorderColor(branchModifyComboBox, errorColor);
            hasError = true;
        }
        if(theaterModifyComboBox.getValue() == null) {
            changeControlBorderColor(theaterModifyComboBox, errorColor);
            hasError = true;
        }
        if(datePickerScreening.getValue() == null){
            changeControlBorderColor(datePickerScreening, errorColor);
            hasError = true;
        }
        if(hourTextField.getText().isEmpty()){
            changeControlBorderColor(hourTextField, errorColor);
            hasError = true;
        }
        // Regular expression to match HH:MM format
        String regex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
        if(!hourTextField.getText().matches(regex)){
            changeControlBorderColor(hourTextField,errorColor);
            showTooltip(hourTextField,hourToolTip);
            hasError = true;
        }

        //If the fields are set correctly, verify the date and send a new MovieSlot message to server.
        if(!hasError){
            try{
                LocalDate date = datePickerScreening.getValue();
                LocalTime time = LocalTime.parse(hourTextField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalDateTime dateTime = LocalDateTime.of(date,time);

                if(date.isBefore(LocalDate.now())){
                    String prevText = hourToolTip.getText();
                    hourToolTip.setText("Please pick a date after today.");
                    showTooltip(datePickerScreening,hourToolTip);
                    hourToolTip.setText(prevText);
                    return;
                }

                MovieSlot slot = new MovieSlot();
                slot.setMovie(movie);
                slot.setBranch(branchModifyComboBox.getValue());
                slot.setMovieTitle(movie.getMovieName());
                slot.setTheater(theaterModifyComboBox.getValue());
                slot.setStartDateTime(dateTime);
                slot.setEndDateTime(dateTime.plusHours(3).plusMinutes(30));
                Message message = new Message();
                message.setSpecificMovie(movie);
                message.setMessage(CONTENT_CHANGE);
                message.setData(NEW_MOVIE_SLOT);
                message.setMovieSlot(slot);
                client.sendMessage(message);
                System.out.println(slot);
                requestMovieSlotUpdate();

                //Clearing the values when a new screening submitted.
                branchModifyComboBox.getSelectionModel().clearSelection();
                theaterModifyComboBox.getSelectionModel().clearSelection();
                datePickerScreening.setValue(null);
                hourTextField.clear();
                hourTextField.setPromptText("Format HH:mm");

                //TODO: Add to the server side to send the timeslots for the movie to all clients.
            }
            catch(DateTimeParseException e){
                changeControlBorderColor(hourTextField,errorColor);
                showTooltip(hourTextField,hourToolTip);
            }

        }


    }

    @FXML
    void backBtnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn1.getScene().getWindow();
            Message message = new Message();
            message.setMessage("back to change content screen");
            message.setEmployee(localMessage.getEmployee());
            logger.info("Moving scene");
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void chooseMovie(ActionEvent event) {
        movie = chooseMovieComboBox.getValue();

        //Setting up checkboxes according to the movie given
        if(movie.getMovieType().isUpcoming()) {
            soonCheckBox.setDisable(false);
            soonCheckBox.setSelected(true);
            packageCheckBox.setDisable(true);
            inTheatersCheckBox.setDisable(true);
        }
        else{
            soonCheckBox.setDisable(true);
            soonCheckBox.setSelected(false);
            packageCheckBox.setDisable(false);
            inTheatersCheckBox.setDisable(false);
        }
        packageCheckBox.setSelected(movie.getMovieType().isPurchasable());
        inTheatersCheckBox.setSelected(movie.getMovieType().isCurrentlyRunning());
        flag = inTheatersCheckBox.isSelected();

        //Setting up text fields.
        castTextField.setText(movie.getMainCast());
        producerTextField.setText(movie.getProducer());
        descriptionTextArea.setText(movie.getMovieDescription());
        hebrewTitleTextField.setText(movie.getHebrewMovieName());
        englishTitleTextField.setText(movie.getMovieName());
        movieDurationTextField.setText(Integer.toString(movie.getMovieDuration()));

        //Populating Combobox
        chooseGenreComboBox.setValue(movie.getMovieGenre());

        //Loading movie image
        if (movie.getImage() != null) {
            InputStream imageStream = new ByteArrayInputStream(movie.getImage());
            Image image = new Image(imageStream);
            movieImageView.setImage(image);
        }

        requestMovieSlotUpdate();
        movieToDoChangeOn.setVisible(true);
    }

    @FXML
    void checkComingSoon(ActionEvent event) {
        if (soonCheckBox.isSelected()) {
            //Disabling the other checkboxes
            inTheatersCheckBox.setSelected(false);
            inTheatersCheckBox.setDisable(true);
            packageCheckBox.setSelected(false);
            packageCheckBox.setDisable(true);

            //Setting up the correct movieType details.
            movie.getMovieType().setUpcoming(true);
            movie.getMovieType().setCurrentlyRunning(false);
            movie.getMovieType().setPurchasable(false);
        } else {
            inTheatersCheckBox.setDisable(false);
            packageCheckBox.setDisable(false);
            movie.getMovieType().setUpcoming(false);
        }
        setCheckBoxesColor(normalColor);
    }

    @FXML
    void checkInTheaters(ActionEvent event) {
        if (inTheatersCheckBox.isSelected()) {
            movie.getMovieType().setCurrentlyRunning(true);
            soonCheckBox.setDisable(true);
            soonCheckBox.setSelected(false);
        } else {
            movie.getMovieType().setCurrentlyRunning(false);
            soonCheckBox.setDisable(false);
        }
        setCheckBoxesColor(normalColor);
    }

    @FXML
    void checkViewingPackage(ActionEvent event) {
        if (packageCheckBox.isSelected()) {
            movie.getMovieType().setPurchasable(true);
            soonCheckBox.setDisable(true);
            soonCheckBox.setSelected(false);
        } else {
            movie.getMovieType().setPurchasable(false);
            soonCheckBox.setDisable(false);
        }
        setCheckBoxesColor(normalColor);
    }

    @FXML
    void backToHomeScreen(ActionEvent event) {
        if (client == null) {
            logger.error(clientNotInit);
            return;
        }
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            logger.info("Moving scene");
            Message message = new Message();
            message.setSourceFXML(ADD_EDIT_MOVIE);
            message.setEmployee(localMessage.getEmployee());
            EventBus.getDefault().unregister(this);
            client.moveScene(EMPLOYEE_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void chooseGenre(ActionEvent event) {
        MovieGenre genreSelection = chooseGenreComboBox.getValue();
        movie.setMovieGenre(genreSelection);
        changeControlBorderColor(chooseGenreComboBox, null);
    }

    @FXML
    public void selectedDateAction(ActionEvent event) {
        changeControlBorderColor(datePickerScreening,null);
    }

    //endregion

    //region Interface Methods

    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }

    //endregion

}
