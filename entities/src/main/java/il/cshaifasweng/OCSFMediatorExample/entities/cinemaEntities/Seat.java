package il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "seats")
public class Seat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatNum;

    private boolean taken;

    @ManyToOne
    private Theater theater;

    @ManyToOne
    @JoinColumn(name = "movieslot_id")
    private MovieSlot movieSlot;

    public Seat() {
        setTaken(false); // Default value
    }

    public Seat(boolean taken, Theater theater) {
        setTaken(taken);
        setTheater(theater);
    }

    public Seat(int seatNum, boolean taken, Theater theater) {
        this(taken, theater);
        setSeatNum(seatNum);
    }

    // Getters and Setters
    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }


    public MovieSlot getMovieSlot() {
        return movieSlot;
    }

    public void setMovieSlot(MovieSlot movieslot) {
        this.movieSlot = movieslot;
    }
}
