package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_ticket")
public class MovieTicket implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Movie movie;

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Branch branch;

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    MovieSlot movieSlot = new MovieSlot();

    private String movieName;
    private String branchName;
    private int theaterNum;
    private int seatNum;
    private int seatRow;
    private int seatID;


    public MovieTicket(Movie movie, Branch branch, String movieName, String branchName, int theaterNum, int seatNum, int seatRow, MovieSlot movieSlot) {
        this.id = getId();
        this.branch = branch;
        this.movieName = movieName;
        this.branchName = branchName;
        this.theaterNum = theaterNum;
        this.seatNum = seatNum;
        this.seatRow = seatRow;
        this.movieSlot = movieSlot;
    }

    public MovieTicket(){}

    public int getTheaterNum() {
        return theaterNum;
    }

    public void setTheaterNum(int theaterNum) {
        this.theaterNum = theaterNum;
    }

    public void setSeatID (int seatID){
        this.seatID = seatID;
    }

    public int getSeatID(){
        return this.seatID;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public int getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public MovieSlot getMovieSlot() {
        return movieSlot;
    }

    public void setMovieSlot(MovieSlot movieSlot) {
        this.movieSlot = movieSlot;
    }



}
