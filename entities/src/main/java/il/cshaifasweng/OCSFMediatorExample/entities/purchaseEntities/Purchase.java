package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase")
public class Purchase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_type")
    private PurchaseType purchaseType;
    private LocalDateTime dateOfPurchase;
    private String customerPID;
    private double amount;

    @OneToOne
    private Booklet purchasedBooklet;

    @OneToOne
    private MovieLink purchasedMovieLink;

    @OneToOne
    private MovieTicket purchasedMovieTicket;

    @ManyToOne
    private Customer customer;

    public int getId() { return id; }

    public PurchaseType getPurchaseType() { return purchaseType; }

    public void setPurchaseType(PurchaseType purchaseType) { this.purchaseType = purchaseType; }

    public LocalDateTime getDateOfPurchase() { return dateOfPurchase; }

    public void setDateOfPurchase(LocalDateTime dateOfPurchase) { this.dateOfPurchase = dateOfPurchase; }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public Booklet getPurchasedBooklet() {return purchasedBooklet;}

    public void setPurchasedBooklet(Booklet puchasedBooklet) {this.purchasedBooklet = puchasedBooklet;}

    public MovieLink getPurchasedMovieLink() {return purchasedMovieLink;}

    public void setPurchasedMovieLink(MovieLink purchasedMovieLink) {this.purchasedMovieLink = purchasedMovieLink;}

    public MovieTicket getPurchasedMovieTicket() {return purchasedMovieTicket;}

    public void setPurchasedMovieTicket(MovieTicket purchasedMovieTicket) {this.purchasedMovieTicket = purchasedMovieTicket;}

    public String getCustomerPID() {return customerPID;}

    public void setCustomerPID(String customerPID) {this.customerPID = customerPID;}

    public double getAmount() {return amount;}

    public void setAmount(double amount) {this.amount = amount;}

    public void setAmountByItem(PurchaseType purchasedItem){
        switch(purchasedItem){
            case BOOKLET:
                this.setAmount(127.5);
                break;
            case MOVIE_LINK:
                this.setAmount(27.5);
                break;
            case MOVIE_TICKET:
                this.setAmount(29.5);
                break;
        }
    }

}

