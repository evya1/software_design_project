package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_link")
public class MovieLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Movie movie;

    private String movieName;
    private String movieLink;
    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;


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
}
