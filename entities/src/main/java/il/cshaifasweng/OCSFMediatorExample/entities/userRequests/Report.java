package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;

import javax.persistence.*;
import java.io.Serializable;


//TODO: Need to update this one.

@Entity
@Table (name = "report")
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Branch branch;
}
