package il.cshaifasweng.OCSFMediatorExample.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Example for the MovieSlot usage:
//Movie movie1 = new Movie("Inception", "Leonardo DiCaprio", null, "Christopher Nolan", "A mind-bending thriller", 148, null);
//MovieSlot movieSlot1 = new MovieSlot(movie1, LocalDateTime.of(2024, 9, 25, 10, 0), LocalDateTime.of(2024, 9, 25, 12, 30));

@Entity
@Table(name = "movieslot")
public class MovieSlot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String movieTitle;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    public MovieSlot() {}

    public MovieSlot(Movie movie, LocalDateTime startDateTime, LocalDateTime endDateTime, Theater theater) {
        this.movie = movie;
        this.movieTitle = movie.getMovieName();
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.theater = theater;
    }

    public int getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        this.movieTitle = movie.getMovieName();
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "MovieSlot{" +
                "movieTitle='" + movieTitle + '\'' +
                ", startDateTime=" + startDateTime.format(formatter) +
                ", endDateTime=" + endDateTime.format(formatter) +
                '}';
    }
}
