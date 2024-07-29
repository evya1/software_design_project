package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "prices")
public class PriceConstants implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    //     boolean is_confirmed;
//
//    public void setIsConfirmed(boolean is_confirmed) {
//        PriceConstants.is_confirmed = is_confirmed;
//    }
//
//    public boolean getIsConfirmed() {
//        return is_confirmed;
//    }
    private double bookletPrice;
    private double movieTicketPrice;
    private double movieLinkPrice;

    private double newBookletPrice = -1;
    private double newMovieTicketPrice = -1;
    private double newMovieLinkPrice = -1;

    public PriceConstants(double B, double T, double L) {
        bookletPrice = B;
        movieTicketPrice = T;
        movieLinkPrice = L;
    }

    public PriceConstants() {
    }

    public double getNewBookletPrice() {
        return newBookletPrice;
    }

    public void setNewBookletPrice(double newBookletPrice) {
        this.newBookletPrice = newBookletPrice;
    }

    public double getNewMovieTicketPrice() {
        return newMovieTicketPrice;
    }

    public void setNewMovieTicketPrice(double newMovieTicketPrice) {
        this.newMovieTicketPrice = newMovieTicketPrice;
    }

    public double getNewMovieLinkPrice() {
        return newMovieLinkPrice;
    }

    public void setNewMovieLinkPrice(double newMovieLinkPrice) {
        this.newMovieLinkPrice = newMovieLinkPrice;
    }

    public double getBookletPrice() {
        return bookletPrice;
    }

    public void setBookletPrice(double bP) {
        bookletPrice = bP;
    }

    public double getMovieTicketPrice() {
        return movieTicketPrice;
    }

    public void setMovieTicketPrice(double MTP) {
        movieTicketPrice = MTP;
    }

    public double getMovieLinkPrice() {
        return movieLinkPrice;
    }

    public void setMovieLinkPrice(double MLP) {
        movieLinkPrice = MLP;
    }

}
