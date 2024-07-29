package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PriceConstants;

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

    @Column(name = "customer_pid", nullable = true)
    private String customerPID;

    private double price = 0.0;
    @OneToOne
    @JoinColumn(name = "price_id", nullable = true)
    private PriceConstants priceConstants;

    @OneToOne
    @JoinColumn(name = "booklet_id", nullable = true)
    private Booklet purchasedBooklet;

    @OneToOne
    @JoinColumn(name = "movie_link_id", nullable = true)
    private MovieLink purchasedMovieLink;

    @OneToOne
    @JoinColumn(name = "movie_ticket_id", nullable = true)
    private MovieTicket purchasedMovieTicket;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = true)
    private Branch branch;

    public int getId() { return id; }

    public PurchaseType getPurchaseType() { return purchaseType; }

    public void setPurchaseType(PurchaseType purchaseType) { this.purchaseType = purchaseType; }

    public LocalDateTime getDateOfPurchase() { return dateOfPurchase; }

    public void setDateOfPurchase(LocalDateTime dateOfPurchase) { this.dateOfPurchase = dateOfPurchase; }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public Booklet getPurchasedBooklet() {return purchasedBooklet;}

    public void setPurchasedBooklet(Booklet purchasedBooklet) {this.purchasedBooklet = purchasedBooklet;}

    public MovieLink getPurchasedMovieLink() {return purchasedMovieLink;}

    public void setPurchasedMovieLink(MovieLink purchasedMovieLink) {this.purchasedMovieLink = purchasedMovieLink;}

    public MovieTicket getPurchasedMovieTicket() {return purchasedMovieTicket;}

    public void setPurchasedMovieTicket(MovieTicket purchasedMovieTicket) {this.purchasedMovieTicket = purchasedMovieTicket;}

    public String getCustomerPID() {return customerPID;}

    public void setCustomerPID(String customerPID) {this.customerPID = customerPID;}

    public double getPrice() {return price;}

    private void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public void SetPrice(PriceConstants price) {
        priceConstants = price;
    }

    public void setPriceByItem(PurchaseType purchasedItem) {
        this.purchaseType = purchasedItem;
        switch (purchasedItem) {
            case BOOKLET:
                this.setPrice(priceConstants.getBookletPrice());
                break;
            case MOVIE_LINK:
                this.setPrice(priceConstants.getMovieLinkPrice());
                break;
            case MOVIE_TICKET:
                this.setPrice(priceConstants.getMovieTicketPrice());
                break;
        }
    }

    public Branch getBranch() { return branch; }

    public void setBranch(Branch branch) { this.branch = branch; }
}
