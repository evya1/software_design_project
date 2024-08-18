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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private int rowLength;

    @OneToMany(mappedBy = "theater", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<MovieSlot> schedule;

    public Theater() {}

    public Theater(int numOfSeats, List<MovieSlot> schedule, int rowLength) {
        setNumOfSeats(numOfSeats);
        setSchedule(schedule);
        setRowLength(rowLength);
    }

    public Theater(int numOfSeats, List<MovieSlot> schedule, int rowLength, Branch branch) {
        this(numOfSeats,schedule,rowLength);
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
