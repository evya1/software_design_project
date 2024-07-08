package il.cshaifasweng.OCSFMediatorExample.entities.movieDetails;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String movieName;
    private String hebrewMovieName;
    private String mainCast;
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    private String producer;
    private String movieDescription;
    private int movieDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "movie_genre")
    private MovieGenre movieGenre;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<MovieSlot> movieScreeningTime;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TypeOfMovie movieType;

    public Movie() {}

    public Movie(String movieName, String mainCast, byte[] image, String producer, String movieDescription,
                 int movieDuration, List<MovieSlot> movieScreeningTime, TypeOfMovie upcomingMovies, MovieGenre movieGenre, String hebrewMovieName) {
        this.movieName = movieName;
        this.mainCast = mainCast;
        this.image = image;
        this.producer = producer;
        this.movieDescription = movieDescription;
        this.movieDuration = movieDuration;
        this.movieScreeningTime = movieScreeningTime;
        this.movieType = upcomingMovies;
        this.movieGenre = movieGenre;
        this.hebrewMovieName = hebrewMovieName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getHebrewMovieName() {
        return hebrewMovieName;
    }

    public void setHebrewMovieName(String hebrewMovieName) {
        this.hebrewMovieName = hebrewMovieName;
    }

    public String getMainCast() {
        return mainCast;
    }

    public void setMainCast(String mainCast) {
        this.mainCast = mainCast;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public int getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(int movieDuration) {
        this.movieDuration = movieDuration;
    }

    public MovieGenre getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(MovieGenre movieGenre) {
        this.movieGenre = movieGenre;
    }

    public List<MovieSlot> getMovieScreeningTime() {
        return movieScreeningTime;
    }

    public void setMovieScreeningTime(List<MovieSlot> movieScreeningTime) {
        this.movieScreeningTime = movieScreeningTime;
    }

    public TypeOfMovie getMovieType() {
        return movieType;
    }

    public void setMovieType(TypeOfMovie movieType) {
        this.movieType = movieType;
    }
}
