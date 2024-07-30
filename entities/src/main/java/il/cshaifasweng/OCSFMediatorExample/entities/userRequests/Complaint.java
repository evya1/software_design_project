package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table (name = "complaints")
public class Complaint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String complaintTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String complaintContent;

    private LocalDateTime dateOfComplaint;
    private String complaintStatus;
    private PurchaseType purchaseType = null;
    private String customerPId;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintContent() {
        return complaintContent;
    }

    public void setComplaintContent(String complaintContent) {
        this.complaintContent = complaintContent;
    }

    public LocalDateTime getDateOfComplaint() { return dateOfComplaint; }

    public void setDateOfComplaint(LocalDateTime dateOfComplaint) { this.dateOfComplaint = dateOfComplaint; }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public PurchaseType getPurchaseType() {return purchaseType;}

    public void setPurchaseType(PurchaseType purchaseType) {this.purchaseType = purchaseType;}

    public String getCustomerPId() {return customerPId;}

    public void setCustomerPId(String customerPId) {this.customerPId = customerPId;}

}
