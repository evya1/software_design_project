//package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;
//
//import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
//import il.cshaifasweng.OCSFMediatorExample.client.GenericEvent;
//import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
//import il.cshaifasweng.OCSFMediatorExample.entities.Message;
//import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.Node;
//import javafx.scene.control.*;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.util.List;
//import java.util.Objects;
//
//public class CatalogController implements ClientDependent {
//    private static List<Movie> movies;
//    private SimpleClient client;
//    private Message localMessage;
//
//    @FXML
//    private Button backButton;
//
//    @FXML
//    private Button updateButton;
//
//    @FXML
//    private ListView<String> movieListView;
//
//    @FXML
//    private ComboBox<String> chooseTypeComboBox;
//
//    @FXML
//    private ScrollPane scrollPane;
//
//    public static void setMovies(List<Movie> movies) {
//        CatalogController.movies = movies;
//    }
//
//    @FXML
//    void initialize() {
//        chooseTypeComboBox.getItems().addAll("All Movies","Upcoming Movies", "In Theater", "Viewing Package");
//        // Call loadMovies here to ensure the ListView is populated when the scene is loaded
//        if (client != null) {
//            Message message = new Message();
//            message.setMessage("show all movies");
//            client.sendMessage(message);
//        }
//
//        EventBus.getDefault().register(this);
//        chooseTypeComboBox.setValue("All Movies");
//        loadMovies();
//    }
//
//    private void loadMovies() {
//        // Ensure movies list is initialized and populated
//        if (movies != null && !movies.isEmpty()) {
//            System.out.println("LOG: This is the number of movies given " + movies.size());
//            ObservableList<String> movieNames = FXCollections.observableArrayList();
//            for (Movie movie : movies) {
//                movieNames.add(movie.getMovieName());
//            }
//            movieListView.setItems(movieNames);
//        }
//    }
//
//    @Subscribe
//    public void dataRecieved(GenericEvent<List<Movie>> event) {
//        if (event.getData() != null && !event.getData().isEmpty() && event.getData().getFirst() instanceof Movie) {
//            Platform.runLater(()->{
//                CatalogController.movies = event.getData();
//                loadMovies();
//            });
//        }
//    }
//
//    @FXML
//    public void handleBackButtonAction(ActionEvent event) {
//        try {
//            EventBus.getDefault().unregister(this);
//            Stage stage = (Stage) backButton.getScene().getWindow();
//            client.moveScene("primary",stage, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void handleUpdateButtonAction(ActionEvent event) {
//
//    }
//
//    public void chosenMovie(MouseEvent mouseEvent) {
//        if (mouseEvent.getClickCount() == 2) {
//            String movieName = movieListView.getSelectionModel().getSelectedItem();
//            if (movieName != null) {
//                for (Movie movie : movies) {
//                    if (movie.getMovieName().equals(movieName)) {
//                        try {
//                            //MovieController.setMovie(movie);
//                            Node node = (Node) mouseEvent.getSource();
//                            Stage stage = (Stage) node.getScene().getWindow();
//                            EventBus.getDefault().unregister(this);
//                            client.moveScene("catalogM/Movie",stage,null);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } else {
//                System.out.println("No movie selected.");
//            }
//        }
//    }
//
//
//    public void chooseTypeAction(ActionEvent actionEvent) {
//        String selection = chooseTypeComboBox.getValue();
//        movieListView.getItems().clear();
//
//        if (movies != null && !movies.isEmpty()) {
//            ObservableList<String> movieNames = FXCollections.observableArrayList();
//            for (Movie movie : movies) {
//                if(movie.getMovieType().isPurchasable() && Objects.equals(selection, "Viewing Package"))
//                {
//                    movieNames.add(movie.getMovieName());
//                }
//                else if(movie.getMovieType().isCurrentlyRunning() && Objects.equals(selection, "In Theater"))
//                {
//                    movieNames.add(movie.getMovieName());
//                }
//                else if(movie.getMovieType().isUpcoming() && Objects.equals(selection, "Upcoming Movies")){
//                    movieNames.add(movie.getMovieName());
//                }
//                else if(Objects.equals(selection, "All Movies")){
//                    movieNames.add(movie.getMovieName());
//                }
//            }
//            movieListView.setItems(movieNames);
//        }
//    }
//
//    public void setClient(SimpleClient client) {
//        this.client = client;
//    }
//
//    @Override
//    public void setMessage(Message message) {
//        this.localMessage = message;
//    }
//
//}
//
//
