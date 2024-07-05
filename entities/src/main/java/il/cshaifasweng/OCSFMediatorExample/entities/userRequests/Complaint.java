package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "complaint")
public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String complaintTitle;
    private String complaintContent;
    private String complaintDate;
    private String complaintStatus;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

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
}
