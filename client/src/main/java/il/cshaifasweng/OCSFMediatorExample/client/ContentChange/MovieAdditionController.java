package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieGenre;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.TypeOfMovie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class MovieAdditionController implements ClientDependent {

    private Movie movie;
    private TypeOfMovie movieType;
    private SimpleClient client;
    private Message localMessage;
    private final String NEW_MOVIE_TEXT_REQUEST = "new movie";

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
    private Button homeScreenBtn; // Value injected by FXMLLoader

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
    public void initialize() {
        if(localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            movie = new Movie();
            movieType = new TypeOfMovie();
            movie.setMovieType(movieType);
            chooseGenreComboBox.getItems().addAll(MovieGenre.values());
        }
        //Loading an existing movie.
        else{
            movie = localMessage.getSpecificMovie();

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

            //Populating Combobox
            chooseGenreComboBox.setValue(movie.getMovieGenre());

            //Loading movie image
            if (movie.getImage() != null) {
                InputStream imageStream = new ByteArrayInputStream(movie.getImage());
                Image image = new Image(imageStream);
                movieImageView.setImage(image);
            }
        }
    }


    @FXML
    void checkComingSoon(ActionEvent event) {
        if(soonCheckBox.isSelected()) {
            //Disabling the other checkboxes
            inTheatersCheckBox.setSelected(false);
            inTheatersCheckBox.setDisable(true);
            packageCheckBox.setSelected(false);
            packageCheckBox.setDisable(true);

            //Setting up the correct movieType details.
            movie.getMovieType().setUpcoming(true);
            movie.getMovieType().setCurrentlyRunning(false);
            movie.getMovieType().setPurchasable(false);
        }
        else{
            inTheatersCheckBox.setDisable(false);
            packageCheckBox.setDisable(false);
            movie.getMovieType().setUpcoming(false);
        }
    }

    @FXML
    void checkInTheaters(ActionEvent event) {
        if(inTheatersCheckBox.isSelected()) {
            movie.getMovieType().setCurrentlyRunning(true);
        }
        else {
            movie.getMovieType().setCurrentlyRunning(false);
        }
    }

    @FXML
    void checkViewingPackage(ActionEvent event) {
        if(packageCheckBox.isSelected()) {
            movie.getMovieType().setPurchasable(true);
        }
        else{
            movie.getMovieType().setPurchasable(false);
        }
    }

    @FXML
    void backToHomeScreen(ActionEvent event) {

    }

    @FXML
    void chooseGenre(ActionEvent event) {
        MovieGenre genreSelection = chooseGenreComboBox.getValue();
        movie.setMovieGenre(genreSelection);
    }

    @FXML
    void previewImage(ActionEvent event) {
        //Loading movie image from local path given by the user or from Movie.
        if(localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            if(!filePathTextField.getText().isEmpty()){
                try {
                    File file = new File(filePathTextField.getText());
                    Image image = new Image(file.toURI().toString());
                    movieImageView.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                    SimpleClient.showAlert(Alert.AlertType.ERROR,"File path","File location is incorrect");
                }
            }
            else{
                SimpleClient.showAlert(Alert.AlertType.ERROR,"File path","The file path is empty.");
            }

        }
        else{
            if (movie.getImage() != null) {
                InputStream imageStream = new ByteArrayInputStream(movie.getImage());
                Image image = new Image(imageStream);
                movieImageView.setImage(image);
            }
        }
    }


    @FXML
    void submitMovie(ActionEvent event) {

        //Checking empty fields.
        boolean description = descriptionTextArea.getText().isEmpty();
        boolean hebrewTitle = hebrewTitleTextField.getText().isEmpty();
        boolean englishTitle = englishTitleTextField.getText().isEmpty();
        boolean cast = castTextField.getText().isEmpty();
        boolean producer = producerTextField.getText().isEmpty();
        boolean imagePath = filePathTextField.getText().isEmpty();
        boolean genre = chooseGenreComboBox.getValue() != null;
        boolean soon = soonCheckBox.isSelected();
        boolean inTheater = inTheatersCheckBox.isSelected();
        boolean viewingPackage = packageCheckBox.isSelected();
        boolean selection = soon || inTheater || viewingPackage;

        //Checking that all the fields are populated.
        if(description || hebrewTitle || englishTitle || cast || producer || genre || !selection){

            //If it's a movie loaded then the image path should not be populated.
            //If its not a new movie then require image path.
            if(!localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST) && imagePath)
            {
                SimpleClient.showAlert(Alert.AlertType.INFORMATION,"Empty fields","There are one or more empty fields, please populate them before continuing");
            }
        }
        else{

            if(localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)){
                //TODO: Complete the section
            }
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
