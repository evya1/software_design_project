package il.cshaifasweng.OCSFMediatorExample.client.MovieCatalog;

import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class MovieGridController {

    private static Movie movie;

    @FXML
    private ImageView movieImageView;

    @FXML
    private Button movieNameBtn;

    public void setData(Movie movie) {
        this.movie = movie;
        movieNameBtn.setText(movie.getMovieName());
        movieImageView.setImage(new Image(new ByteArrayInputStream(movie.getImage())));
    }

}

