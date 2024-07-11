package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "booklet")
public class Booklet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int numOfEntries = 20;


    public void useEntry(){
        this.numOfEntries = numOfEntries--;
    }

    public int getNumOfEntries() {return numOfEntries;}

    public void setNumOfEntries(int numOfEntries) {
        this.numOfEntries = numOfEntries;
    }

    public int getId() {
        return id;
    }

}
