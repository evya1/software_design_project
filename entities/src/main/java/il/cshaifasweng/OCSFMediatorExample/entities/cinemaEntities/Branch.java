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

    public Branch(String branchName) {
        this.theaterList = new ArrayList<>();
        this.employees = new ArrayList<>();
        setBranchName(branchName);
    }

    public Branch(String branchName, Employee branchManager) {
        this(branchName);
        setBranchManager(branchManager);
    }

    public Branch(String branchName, Employee branchManager, List<Theater> theaterList) {
        this(branchName, branchManager);
        setTheaterList(theaterList);
    }
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
            branchManager.setBranchInCharge(this);
        } else {
            this.branchManager = null;  // Explicitly set to null if conditions are not met
        }
    }

    @Override
    public String toString(){
        return branchName;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setBranch(this);
    }

    public void printBranchManagerDetails() {
        if (branchManager != null) {
            System.out.println("\tBranch Manager: " + branchManager.getFirstName() + " " + branchManager.getLastName() +
                    ", Email: " + branchManager.getEmail() +
                    ", Username: " + branchManager.getUsername());
        } else {
            System.out.println("\tNo Branch Manager assigned.");
        }
    }
}
