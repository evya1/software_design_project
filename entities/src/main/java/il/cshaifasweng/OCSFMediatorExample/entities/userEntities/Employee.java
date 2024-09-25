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

    // Many employees can belong to one branch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    // One employee can be in charge of one branch (if they are a manager)
    @OneToOne
    @JoinColumn(name = "branchInCharge_id", referencedColumnName = "id")
    private Branch branchInCharge;

    public Employee() {
    }

    public Employee(EmployeeType employeeType) {
        setEmployeeType(employeeType);
    }

    public Employee(String firstName, String lastName, String email, String username, String password, boolean active, Branch branchInCharge, EmployeeType employeeType, Branch workingBranch) {
        this(employeeType);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
        setBranchInCharge(branchInCharge);
        this.branch = workingBranch;
    }

    public Employee(EmployeeType employeeType, String firstName, String lastName, String email, String username, String password, boolean active, Branch branchInCharge, Branch branchWorker) {
        this(firstName, lastName, email, username, password, active, branchInCharge, employeeType, branchWorker);
    }

    // Factory method to create a new Employee
    public static Employee createEmployee(EmployeeType employeeType, String firstName, String lastName, String email, String username, String password, boolean active, Branch branchInCharge, Branch branchWorker) {
        return new Employee(employeeType, firstName, lastName, email, username, password, active, branchInCharge,branchWorker);
    }

    public int getId() {
        return id;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
        if (employeeType == EmployeeType.BRANCH_MANAGER) {
            this.branch = this.branchInCharge;
        }
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * Gets the branch in charge if the employee is a theater manager.
     * If the employee type is THEATER_MANAGER, this method returns the branch
     * the employee is in charge of. Otherwise, it returns null.
     *
     * @return the branch in charge or null if the employee is not a theater manager
     */
    public Branch getBranchInCharge() {
        if (employeeType == EmployeeType.BRANCH_MANAGER || employeeType == EmployeeType.CHAIN_MANAGER ) {
            return branchInCharge;
        }
        return null;
    }

    /**
     * Sets the branch in charge if the employee is a theater manager.
     * This method ensures that the branchInCharge attribute is set only if the
     * employee type is THEATER_MANAGER. Additionally, it sets the branch attribute
     * to the same branch.
     *
     * @param branchInCharge the branch to set as the branch in charge
     */
    public void setBranchInCharge(Branch branchInCharge) {
        if (employeeType == EmployeeType.CHAIN_MANAGER)
            setBranch(branchInCharge);
        if (employeeType == EmployeeType.BRANCH_MANAGER) {
            this.branchInCharge = branchInCharge;
            setBranch(branchInCharge);
        }
        else {
            this.branchInCharge = null;
        }
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " " +  getFirstName() + " " + getLastName();
    }
}
