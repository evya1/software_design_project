package il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities;

import javax.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatNum;

    private boolean taken;

    @ManyToOne
    private Theater theater;

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
}
