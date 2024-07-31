package il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "branch")
public class Branch implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String branchName;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "chain_id")
//    private Chain chain;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "branch")
    @Fetch(FetchMode.SUBSELECT)
    private List<Theater> theaterList;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Report report;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Employee branchManager;

    @OneToMany(mappedBy = "branch", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

    public Branch() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

//    public Chain getChain() {
//        return chain;
//    }
//
//    public void setChain(Chain chain) {
//        this.chain = chain;
//    }

    public List<Theater> getTheaterList() {
        return theaterList;
    }

    public void setTheaterList(List<Theater> theaterList) {
        this.theaterList = theaterList;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Employee getBranchManager() {
        return branchManager;
    }

    public void setBranchManager(Employee branchManager) {
        if (branchManager != null && branchManager.getEmployeeType() == EmployeeType.BRANCH_MANAGER) {
            this.branchManager = branchManager;
        } else {
            this.branchManager = null;  // Explicitly set to null if conditions are not met
        }
    }

    @Override
    public String toString(){
        return branchName;
    }
}
