package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.util.List;


@Entity
@Table (name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String movieName;
    private String mainCast;
    private byte[] image;
    private String producer;
    private String movieDescription;
    private int movieDuration;
    @OneToMany
    private List<MovieSlot> movieScreeningTime; //true to prototype only!
    @OneToOne
    private TypeOfMovie upcomingMovies;


    public Movie(){}

    public Movie(String movieName, String mainCast, byte[] image, String producer, String movieDescription,
                 int movieDuration, List<MovieSlot> movieScreeningTime, TypeOfMovie upcomingMovies) {
        this.movieName = movieName;
        this.mainCast = mainCast;
        this.image = image;
        this.producer = producer;
        this.movieDescription = movieDescription;
        this.movieDuration = movieDuration;
        this.movieScreeningTime = movieScreeningTime;
        this.upcomingMovies = upcomingMovies;
    }

    public int getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
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

    public List <MovieSlot> getMovieScreeningTime() {
        return movieScreeningTime;
    }

    public void setMovieScreeningTime(List <MovieSlot> movieScreeningTime) {
        this.movieScreeningTime = movieScreeningTime;
    }

    public TypeOfMovie getUpcomingMovies() {
        return upcomingMovies;
    }

    public void setUpcomingMovies(TypeOfMovie upcomingMovies) {
        this.upcomingMovies = upcomingMovies;
    }
}