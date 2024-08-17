package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;

import java.util.List;

/**
 * The `ReportDataReceivedEvent` class encapsulates the data received from the server related to reports.
 * This class provides information about the reports, the employee who requested them, and the branch context.
 */
public class ReportDataReceivedEvent {

    private List<Report> reports;
    private boolean isSingleReport;
    private Employee employee;
    private EmployeeType employeeType;
    private Branch branch;
    private String branchName;

    /**
     * Constructs a new `ReportDataReceivedEvent` with the specified reports, employee, and branch information.
     * The constructor uses setter methods to initialize the fields and apply any necessary logic.
     *
     * @param reports       the list of reports received.
     * @param isSingleReport whether only one report is included in the list.
     * @param employee      the employee associated with the request.
     * @param branch        the branch associated with the request; can be null.
     */
    public ReportDataReceivedEvent(List<Report> reports, boolean isSingleReport, Employee employee, Branch branch) {
        setReports(reports);
        setSingleReport(isSingleReport);
        setEmployee(employee);
        setBranch(branch);
    }

    /**
     * Sets the employee associated with the event and determines the employee type.
     *
     * @param employee the employee to set.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (employee != null) {
            this.employeeType = employee.getEmployeeType();
        }
    }

    /**
     * Sets the branch associated with the event. If the employee is a chain manager,
     * the branch is set to null, and the branch name is set to "All Branches".
     *
     * @param branch the branch to set.
     */
    public void setBranch(Branch branch) {
        if (employeeType != null && employeeType.equals(EmployeeType.CHAIN_MANAGER)) {
            this.branch = null;
            this.branchName = "All Branches";
        } else {
            this.branch = branch;
            this.branchName = branch != null ? branch.getBranchName() : "Unknown";
        }
    }

    // Getters and setters for other fields (no additional logic)
    public List<Report> getReports() {
        return reports;
    }

    public boolean isSingleReport() {
        return isSingleReport;
    }

    public Employee getEmployee() {
        return employee;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void setSingleReport(boolean isSingleReport) {
        this.isSingleReport = isSingleReport;
    }
}
