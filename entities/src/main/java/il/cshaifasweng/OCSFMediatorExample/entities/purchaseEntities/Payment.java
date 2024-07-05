package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentID;

    private String cardNumber;
    private LocalDate expiryDate;
    private String cvv;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Customer customer;

    public Payment(){}

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getPaymentID() {
        return paymentID;
    }
}
