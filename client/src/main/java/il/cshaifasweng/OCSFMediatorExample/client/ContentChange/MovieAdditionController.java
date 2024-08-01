package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.*;
import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.*;


public class MovieAdditionController implements ClientDependent {

    private static final String clientNotInit = "Client is not initialized!\n";
    private static final Logger logger = LoggerFactory.getLogger(MovieAdditionController.class);


    private List<Movie> movies;
    private List<MovieSlot> slots;
    private Movie movie;
    private MovieSlot movieSlot;
    private TypeOfMovie movieType;
    private SimpleClient client;
    private Message localMessage;
    private List<Branch> branches;

    @FXML
    private Button browseBtn;

    @FXML
    private TextField movieDurationTextField;

    @FXML // fx:id="castTextField"
    private TextField castTextField; // Value injected by FXMLLoader

    @FXML // fx:id="chooseGenreComboBox"
    private ComboBox<MovieGenre> chooseGenreComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionTextArea"
    private TextArea descriptionTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="englishTitleTextField"
    private TextField englishTitleTextField; // Value injected by FXMLLoader

    @FXML // fx:id="filePathTextField"
    private TextField filePathTextField; // Value injected by FXMLLoader

    @FXML // fx:id="hebrewTitleTextField"
    private TextField hebrewTitleTextField; // Value injected by FXMLLoader

    @FXML // fx:id="homeScreenBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="inTheatersCheckBox"
    private CheckBox inTheatersCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="movieImageView"
    private ImageView movieImageView; // Value injected by FXMLLoader

    @FXML // fx:id="packageCheckBox"
    private CheckBox packageCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="previewImageButton"
    private Button previewImageButton; // Value injected by FXMLLoader

    @FXML // fx:id="producerTextField"
    private TextField producerTextField; // Value injected by FXMLLoader

    @FXML // fx:id="soonCheckBox"
    private CheckBox soonCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="submitMovieBtn"
    private Button submitMovieBtn; // Value injected by FXMLLoader

    @FXML
    private Button newMovieBtn;

    @FXML
    private Button deleteMovieBtn;

    @FXML
    private AnchorPane movieToDoChangeOn;

    @FXML
    private Button backBtn1;

    @FXML
    private ComboBox<String> chooseMovieComboBox;

    @FXML
    private Button addScreenTimeBtn;

    @FXML
    private ListView<String> screeningTimesListView;

    private final String errorColor = "red";
    private final String normalColor = "black";
    private final String borderDefault = null;


    @FXML
    public void initialize() {
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
            addTextInputListener(descriptionTextArea, borderDefault);
            addTextInputListener(englishTitleTextField, borderDefault);
            addTextInputListener(filePathTextField, borderDefault);
            addTextInputListener(hebrewTitleTextField, borderDefault);
            addTextInputListener(producerTextField, borderDefault);
            addTextInputListener(castTextField, borderDefault);
            addTextInputListener(movieDurationTextField, borderDefault);
        }
        //Loading an existing movie.
        else {
            filePathTextField.setDisable(true);
            browseBtn.setDisable(true);
            previewImageButton.setDisable(true);
            chooseMovieComboBox.setVisible(true);
            backBtn1.setVisible(false);
            backBtn1.setDisable(true);
            movieToDoChangeOn.setVisible(false);
            deleteMovieBtn.setVisible(true);
            deleteMovieBtn.setDisable(false);
            submitMovieBtn.setText("Update Movie");

        }
        Message message = new Message();
        message.setMessage(MOVIE_REQUEST);
        message.setData(SHOW_ALL_MOVIES);
        client.sendMessage(message);
        message.setMessage(BRANCH_THEATER_INFORMATION);
        message.setData(GET_BRANCHES);
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
                    List<String> moviesNames = new ArrayList<>();
                    for (Movie movie : movies) {
                        moviesNames.add(movie.getMovieName());
                    }

                    chooseMovieComboBox.setItems(FXCollections.observableArrayList(moviesNames));
                });
            }
            if (message.getData().equals(GET_BRANCHES)) {
                Platform.runLater(() -> {
                    this.branches = message.getBranches();
                });
            }
            if (message.getData().equals("time slots for specific movie")) {
                Platform.runLater(() -> {
                    this.slots = message.getMovieSlots();
                    this.movie = message.getSpecificMovie();
                    List<String> slotsNames = new ArrayList<>();
                    for (MovieSlot slot : slots) {
                        String cur = slot.getStartDateTime().toString() + ", in branch: " + slot.getBranch().getBranchName()
                                + ", at theater: " + slot.getTheater().getId();
                        slotsNames.add(cur);

                    }
                    ObservableList<String> stList = FXCollections.observableArrayList(slotsNames);
                    Collections.sort(stList);
                    screeningTimesListView.setItems(stList);

                });
            }
        }
    }

    @FXML
    void ModifyScreening(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                String selectedItem = screeningTimesListView.getSelectionModel().getSelectedItem();
                newScreeningTime(selectedItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void newScreeningTime(String selectedItem) {
        try {
            MovieSlot currentSlot = null;
            int slotIndex = -1;
            for (int i = 0; i < movie.getMovieScreeningTime().size(); i++) {
                String cur = movie.getMovieScreeningTime().get(i).getStartDateTime().toString() +
                        ", in branch: " + movie.getMovieScreeningTime().get(i).getBranch().getBranchName() +
                        ", at theater: " + movie.getMovieScreeningTime().get(i).getTheater().getId();

                if (cur.equals(selectedItem)) {
                    currentSlot = movie.getMovieScreeningTime().get(i);
                    slotIndex = i;
                }
            }

            if (selectedItem != null && currentSlot != null) {
                TextInputDialog dialog = new TextInputDialog(selectedItem.toString());
                dialog.setTitle("Modify Screening Time");
                dialog.setHeaderText("Modify the time in this format: dd/MM/yyyy HH:mm, " +
                        "in branch: branchName, at theater: theaterNum");
                dialog.setContentText("New time:");
                Optional<String> result = dialog.showAndWait();

                //Check if the result exists before proceeding.
                if (result.isPresent()) {
                    parse(result.get(), slotIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse(String result, int slotIndex) {
        try {
            LocalDateTime newStart = null;
            String brName = "";
            int theaterNum = 0;
            String[] split = result.split(",");
            String[] brArray = split[1].split(":");
            String[] theaterArray = split[2].split(":");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            newStart = LocalDateTime.parse(split[0], formatter);
            if (newStart.isBefore(LocalDateTime.now())) {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Unavailable Time", "The time entered cannot be before today.");
                return;
            }
            brName = brArray[1].strip();
            theaterNum = Integer.parseInt(theaterArray[1].strip());

            if (checkInput(newStart, brName, theaterNum)) {
                if (slotIndex != -1) sendChangedInput(newStart, brName, theaterNum, slotIndex);
                else sendNewInput(newStart, brName, theaterNum);
            }
        } catch (DateTimeParseException ParseException) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Time Error", "Please enter a valid time");
        } catch (NumberFormatException NumberFormatException) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Theater Number Error", "Please enter a valid number");
        } catch (ArrayIndexOutOfBoundsException ArrayIndexOutOfBoundsException) {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid content using the right format");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNewInput(LocalDateTime newStart, String brName, int theaterNum) {
        movieSlot = new MovieSlot();
        movieSlot.setMovie(movie);
        movieSlot.setMovieTitle(movie.getMovieName());
        movieSlot.setStartDateTime(newStart);
        movieSlot.setEndDateTime(newStart.plusMinutes(movie.getMovieDuration()));
        for (Branch branch : branches) {
            if (branch.getBranchName().equalsIgnoreCase(brName)) {
                movieSlot.setBranch(branch);
                for (Theater theater : branch.getTheaterList()) {
                    if (theater.getId() == theaterNum)
                        movieSlot.setTheater(theater);
                }
            }
        }

        Message message = new Message();
        message.setMessage(CHANGE_SCREEN_TIME);
        message.setSpecificMovie(movie);
        message.setMovieSlot(movieSlot);
        message.setMovieID(-1);
        client.sendMessage(message);

        screeningTimesListView.getItems().clear();
        message.setMessage(GET_MOVIE_SLOT_BY_MOVIE_ID);
        message.setSpecificMovie(movie);
        client.sendMessage(message);
    }

    private void sendChangedInput(LocalDateTime newStart, String brName, int theaterNum, int slotIndex) {
        try {
            LocalDateTime newEnd = newStart.plusMinutes(movie.getMovieDuration());

            for (int i = 0; i < movie.getMovieScreeningTime().get(slotIndex).getTheater().getSchedule().size(); i++) {
                if (movie.getMovieScreeningTime().get(slotIndex).getTheater().getSchedule().get(i).getStartDateTime() ==
                        movie.getMovieScreeningTime().get(slotIndex).getStartDateTime() &&
                        movie.getMovieScreeningTime().get(slotIndex).getTheater().getSchedule().get(i).getBranch().getBranchName()
                                == movie.getMovieScreeningTime().get(slotIndex).getBranch().getBranchName()) {
                    movie.getMovieScreeningTime().get(slotIndex).getTheater().getSchedule().remove(i);
                    break;
                }
            }

            movie.getMovieScreeningTime().get(slotIndex).setStartDateTime(newStart);
            movie.getMovieScreeningTime().get(slotIndex).setEndDateTime(newEnd);
            boolean flag = false;
            for (int i = 0; i < branches.size(); i++) {
                if (branches.get(i).getBranchName().equalsIgnoreCase(brName)
                        && !(movie.getMovieScreeningTime().get(slotIndex).getBranch().getBranchName().equalsIgnoreCase(brName))) {
                    flag = true;
                    movie.getMovieScreeningTime().get(slotIndex).setBranch(branches.get(i));
                    for (int j = 0; j < branches.get(i).getTheaterList().size(); j++) {
                        if (branches.get(i).getTheaterList().get(j).getId() == theaterNum) {
                            movie.getMovieScreeningTime().get(slotIndex).setTheater(branches.get(i).getTheaterList().get(j));
                        }
                    }
                }
            }
            if (!flag) {
                for (int i = 0; i < movie.getMovieScreeningTime().get(slotIndex).getBranch().getTheaterList().size(); i++) {
                    if (movie.getMovieScreeningTime().get(slotIndex).getBranch().getTheaterList().get(i).getId() == theaterNum) {
                        movie.getMovieScreeningTime().get(slotIndex).setTheater(movie.getMovieScreeningTime().get(slotIndex).getBranch().getTheaterList().get(i));
                        movie.getMovieScreeningTime().get(slotIndex).getTheater().getSchedule().add(movie.getMovieScreeningTime().get(slotIndex));
                    }
                }
            }

            Message message = new Message();
            message.setMessage(CHANGE_SCREEN_TIME);
            message.setSpecificMovie(movie);
            message.setMovieID(movie.getMovieScreeningTime().get(slotIndex).getId());
            client.sendMessage(message);

            screeningTimesListView.getItems().clear();
            message.setMessage(GET_MOVIE_SLOT_BY_MOVIE_ID);
            message.setSpecificMovie(movie);
            client.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkInput(LocalDateTime newStart, String brName, int theaterNum) {
        try {
            boolean theaterFound = false;
            boolean branchFound = false;
            for (Branch branch : branches) {
                if (branch.getBranchName().equalsIgnoreCase(brName)) {
                    branchFound = true;
                    for (Theater theater : branch.getTheaterList()) {
                        if (theater.getId() == theaterNum) {
                            theaterFound = true;
                            int i = 0;
                            for (MovieSlot slot : theater.getSchedule()) {
                                if (slot.getStartDateTime().equals(newStart) &&
                                        slot.getBranch().getBranchName().equalsIgnoreCase(brName) &&
                                        slot.getTheater().getId() == theaterNum) {
                                    SimpleClient.showAlert(Alert.AlertType.ERROR, "Time Error", "The time you entered is already there");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            if (!branchFound) {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Branch Name Error", "There is no branch with that name");
                return false;
            }
            if (!theaterFound) {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "Theater Number Error", "There is no theater with that number");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @FXML
    void addScreenTime(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Screening Time");
            dialog.setHeaderText("Add the time in this format dd/MM/yyyy HH:mm, " +
                    "in branch: branchName, at theater: theaterNum");
            dialog.setContentText("New time:");
            Optional<String> result = dialog.showAndWait();

            //Check if the result exists before proceeding.
            if (result.isPresent()) {
                parse(result.get(), -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backBtnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) backBtn1.getScene().getWindow();
            Message message = new Message();
            message.setMessage("back to change content screen");
            logger.info("Moving scene");
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void chooseMovie(ActionEvent event) {
        String movieName = chooseMovieComboBox.getValue();
        for (Movie mov : movies) {
            if (mov.getMovieName().equals(movieName)) {
                movie = mov;

                //Setting up checkboxes according to the movie given
                packageCheckBox.setSelected(movie.getMovieType().isPurchasable());
                soonCheckBox.setSelected(movie.getMovieType().isUpcoming());
                inTheatersCheckBox.setSelected(movie.getMovieType().isCurrentlyRunning());

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

                Message message = new Message();
                message.setMessage(GET_MOVIE_SLOT_BY_MOVIE_ID);
                message.setSpecificMovie(movie);
                client.sendMessage(message);

                movieToDoChangeOn.setVisible(true);
            }
        }
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
        } else {
            movie.getMovieType().setCurrentlyRunning(false);
        }
        setCheckBoxesColor(normalColor);
    }

    @FXML
    void checkViewingPackage(ActionEvent event) {
        if (packageCheckBox.isSelected()) {
            movie.getMovieType().setPurchasable(true);
        } else {
            movie.getMovieType().setPurchasable(false);
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
            message.setMessage("nfew movie");
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
        changeControlBorderColor(chooseGenreComboBox, borderDefault);
    }

    @FXML
    void previewImage(ActionEvent event) {
        //Loading movie image from local path given by the user or from Movie.
        if (localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            if (!filePathTextField.getText().isEmpty()) {
                try {
                    File file = new File(filePathTextField.getText());
                    Image image = new Image(file.toURI().toString());
                    movieImageView.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                    SimpleClient.showAlert(Alert.AlertType.ERROR, "File path", "File location is incorrect");
                }
            } else {
                SimpleClient.showAlert(Alert.AlertType.ERROR, "File path", "The file path is empty.");
            }

        } else {
            if (movie.getImage() != null) {
                InputStream imageStream = new ByteArrayInputStream(movie.getImage());
                Image image = new Image(imageStream);
                movieImageView.setImage(image);
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
                    },
                    //Action to perform on NO
                    () -> {
                        return;
                    });
        } else {
            SimpleClient.showAlert(Alert.AlertType.ERROR, "Deletion Error", "you have not selected a movie");
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
        changeControlBorderColor(descriptionTextArea, borderDefault);
        changeControlBorderColor(hebrewTitleTextField, borderDefault);
        changeControlBorderColor(englishTitleTextField, borderDefault);
        changeControlBorderColor(castTextField, borderDefault);
        changeControlBorderColor(producerTextField, borderDefault);
        changeControlBorderColor(chooseGenreComboBox, borderDefault);
        changeControlTextColor(soonCheckBox, normalColor);
        changeControlTextColor(inTheatersCheckBox, normalColor);
        changeControlTextColor(packageCheckBox, normalColor);
        changeControlTextColor(movieDurationTextField, normalColor);
    }

    @FXML
    public void browseLocation(ActionEvent actionEvent) {
        changeControlBorderColor(filePathTextField, borderDefault);
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
            logger.info("Moving scene");
            EventBus.getDefault().unregister(this);
            client.moveScene(ADD_EDIT_MOVIE, stage, message);

        } catch (Exception e) {
            e.printStackTrace();
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
