package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



public class Message implements Serializable {

    private int id;
    private LocalDateTime timeStamp;
    private String message;
    private String data;


    private Movie specificMovie;

    private List<Movie> movies;
    private List<MovieSlot> movieSlots;


    public Message() {
        super();
    }

    public Movie getSpecificMovie() {
        return specificMovie;
    }

    public void setSpecificMovie(Movie specificMovie) {
        this.specificMovie = specificMovie;
    }

    public List<MovieSlot> getMovieSlots() {
        return movieSlots;
    }

    public void setMovieSlots(List<MovieSlot> movieSlots) {
        this.movieSlots = movieSlots;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
