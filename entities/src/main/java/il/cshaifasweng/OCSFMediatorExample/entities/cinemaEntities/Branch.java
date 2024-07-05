package il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table (name = "branch")

public class Branch implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String branchName;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Theater> theaterList;

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Report report;

    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Employee branchManager;


    public Branch() {}

    public List<Theater> getTheaterList() {
        return theaterList;
    }

    public void setTheaterList(List<Theater> theaterList) {
        this.theaterList = theaterList;
    }

    public int getId() {
        return id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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
        if(branchManager.getEmployeeType() == EmployeeType.THEATER_MANAGER){
            this.branchManager = branchManager;
        }
    }
}
