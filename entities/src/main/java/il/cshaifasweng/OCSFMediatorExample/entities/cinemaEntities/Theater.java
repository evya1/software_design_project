package il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "theater")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int theaterNum;
    private int numOfSeats;
    private int availableSeats;

    @OneToMany
    private List<Seat> seatList;

    private int rowLength;

    @OneToMany(mappedBy = "theater", fetch = FetchType.LAZY, cascade = CascadeType.ALL) //Movie list
    private List<MovieSlot> movieTime;

    public Theater() {}

    public Theater(int numOfSeats, int availableSeats, List<MovieSlot> movieTime, List<Seat> seatList, int rowLength) {
        this.numOfSeats = numOfSeats;
        this.availableSeats = availableSeats;
        this.movieTime = movieTime;
        this.seatList = seatList;
        this.rowLength = rowLength;
    }

    public int getTheaterNum() {
        return theaterNum;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    public List<MovieSlot> getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(List<MovieSlot> movieTime) {
        this.movieTime = movieTime;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setRowLength(int rowLength) {
        this.rowLength = rowLength;
    }

    public int getRowLength() {
        return rowLength;
    }
}
