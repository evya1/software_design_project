package il.cshaifasweng.OCSFMediatorExample.entities.movieDetails;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table (name = "type_of_movie")


//If typeOfMovie bool true/false - Upcoming , set a release date.
//If typeOfMovie bool true/false - Currently in theaters.
//If typeOfMovie bool true/false - Purchasable Link.

public class TypeOfMovie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean upcoming;
    private boolean currentlyRunning;
    private boolean purchasable;
    private LocalDateTime releaseDate;

    @OneToOne
    private Movie movie;

    public TypeOfMovie() {}

    public TypeOfMovie(boolean upcoming, boolean currentlyRunning, boolean purchasable) {
        this.upcoming = upcoming;
        this.currentlyRunning = currentlyRunning;
        this.purchasable = purchasable;
    }

    public LocalDateTime getReleaseDate() { //condition use of getter in App!
        return this.releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public boolean isUpcoming() {
        return upcoming;
    }

    public void setUpcoming(boolean upcoming) {
        this.upcoming = upcoming;
    }

    public boolean isCurrentlyRunning() {
        return currentlyRunning;
    }

    public void setCurrentlyRunning(boolean currentlyRunning) {
        this.currentlyRunning = currentlyRunning;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}


