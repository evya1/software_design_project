package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "inbox_messages")
public class InboxMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String messageContent;
    private String messageTitle;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public InboxMessage() {}

    public InboxMessage(String messageTitle, String messageContent) {
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
    }

    public int getId() {return id;}

    public String getMessageContent(){return messageContent;}

    public String getMessageTitle(){return messageTitle;}

    public Customer getCustomer(){return customer;}

    public void setMessageContent(String messageContent){this.messageContent = messageContent;}

    public void setMessageTitle(String messageTitle){this.messageTitle = messageTitle;}

    public void setCustomer(Customer customer){this.customer = customer;}

}
