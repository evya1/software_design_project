package il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "theater")
public class Theater implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int theaterNum;
    private int numOfSeats;
    private int availableSeats;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seatList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private int rowLength;

    @OneToMany(mappedBy = "theater", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<MovieSlot> schedule;

    public Theater() {}

    public Theater(int numOfSeats, int availableSeats, List<MovieSlot> schedule, List<Seat> seatList, int rowLength) {
        setNumOfSeats(numOfSeats);
        setAvailableSeats(availableSeats);
        setSchedule(schedule);
        setSeatList(seatList);
        setRowLength(rowLength);
    }

    public Theater(int numOfSeats, int availableSeats, List<MovieSlot> schedule, List<Seat> seatList, int rowLength, Branch branch) {
        this(numOfSeats,availableSeats,schedule,seatList,rowLength);
        setBranch(branch);
    }

    // Getters and Setters
    public int getId() {
        return theaterNum;
    }

    public void setTheaterNum(int theaterNum) {
        this.theaterNum = theaterNum;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public int getRowLength() {
        return rowLength;
    }

    public void setRowLength(int rowLength) {
        this.rowLength = rowLength;
    }

    public List<MovieSlot> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<MovieSlot> schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "" + theaterNum;
    }
}
