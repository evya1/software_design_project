package il.cshaifasweng.OCSFMediatorExample.entities.userEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType;

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private boolean active;

    @OneToOne
    private Branch branchInCharge;

    // Constructors, getters, and setters
    public Employee() {}

    public Employee(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public int getId() {
        return id;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Branch getBranchInCharge() {
        if (employeeType == EmployeeType.THEATER_MANAGER) {
            return branchInCharge;
        }
        return null;
    }

    public void setBranchInCharge(Branch branchInCharge) {
        if (employeeType == EmployeeType.THEATER_MANAGER) {
            this.branchInCharge = branchInCharge;
        }
    }
}
