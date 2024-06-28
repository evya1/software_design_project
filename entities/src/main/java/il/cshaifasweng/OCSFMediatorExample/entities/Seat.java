package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;


@Entity
@Table (name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatNum;
    private boolean taken;
    @ManyToOne
    private Theater theater;

    public Seat() {}

    public Seat(boolean taken, Theater theater) {
        this.taken = false;
        this.theater = theater;
    }

    public int getSeatNum() {
        return seatNum;
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


