package il.cshaifasweng.OCSFMediatorExample.entities.userEntities;

import com.sun.xml.bind.v2.TODO;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Booklet;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Payment;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String personalID;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Purchase> purchases = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;


    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String personalID, List<Purchase> purchases, Payment payment, List<Complaint> complaints) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalID = personalID;
        this.purchases = purchases;
        this.payment = payment;
        this.complaints = complaints;
    }

    public Customer() {}

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getPersonalID() { return personalID; }

    public void setPersonalID(String personalId) { this.personalID = personalId; }

    public List<Purchase> getPurchases() {return purchases;}

    public void setPurchases(List<Purchase> purchases) {this.purchases = purchases;}

    public void addPurchase(Purchase purchase) {this.purchases.add(purchase);}

    public List<Complaint> getComplaints() {return complaints;}

    public void setComplaints(List<Complaint> complaints) {this.complaints = complaints;}
}
