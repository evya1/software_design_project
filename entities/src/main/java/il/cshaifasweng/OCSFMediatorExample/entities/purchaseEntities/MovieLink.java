package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_link")
public class MovieLink implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Movie movie;

    private int customer_id;


    private String movieName;
    private String movieLink;
    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;
    private boolean isValid = true;
    private boolean isActive = false;
    private boolean isNotified = false;



    public MovieLink() {}

    public MovieLink(Movie movie, String movieName, String movieLink, LocalDateTime creationTime, LocalDateTime expirationTime) {
        this.movieName = movieName;
        this.movieLink = movieLink;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
    }


    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieLink() {
        return movieLink;
    }

    public void setMovieLink(String movieLink) {
        this.movieLink = movieLink;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getId() {
        return id;
    }

    public boolean isValid(){return this.isValid;}

    public void setValid() {this.isValid = true;}

    public void setInvalid() {this.isValid = false;}

    public boolean isActive() {
        return isActive;
    }

    public void setActive() {
        isActive = true;
    }

    public void setInactive() {
        isActive = false;
    }

    public void setCustomer_id(int customer_id) {this.customer_id = customer_id;}

    public int getCustomer_id() {return customer_id;}

    public boolean isNotified() {return isNotified;}

    public void setNotified() {isNotified = true;}
}