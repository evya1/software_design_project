package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Booklet;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.MovieLink;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



public class Message implements Serializable {

    private int id;
    private LocalDateTime timeStamp;
    private String message;
    private String data;
    private Customer customer;
    private Purchase purchase;
    private String sourceFXML;
    private Movie specificMovie;
    private List<Movie> movies;
    private List<MovieSlot> movieSlots;
    private Booklet booklet;
    private MovieLink movieLink;

    public MovieLink getMovieLink() {return movieLink;}

    public void setMovieLink(MovieLink movieLink) {this.movieLink = movieLink;}

    public void setBooklet(Booklet booklet) {this.booklet = booklet;}

    public Booklet getBooklet() {return booklet;}

    public Purchase getPurchase() {return purchase;}

    public void setPurchase(Purchase purchase) {this.purchase = purchase;}

    public Customer getCustomer() {return customer;}

    public void setCustomer(Customer customer) {this.customer = customer;}

    public String getSourceFXML() {return sourceFXML;}

    public void setSourceFXML(String sourceFXML) {this.sourceFXML = sourceFXML;}

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
