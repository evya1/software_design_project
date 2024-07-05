package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "purchase")

public class Purchase implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_type")
    private PurchaseType purchaseType;
}