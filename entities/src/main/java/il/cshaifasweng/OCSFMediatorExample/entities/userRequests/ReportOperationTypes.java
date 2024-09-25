package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

public final class ReportOperationTypes {

    // Request Constants
    public static final String REPORT_DATA_RESPONSE = "Report Data Response";
    public static final String FETCH_ALL_REPORTS = "Fetch All Reports"; // Request to fetch all reports
    public static final String FETCH_SINGLE_REPORT = "Fetch Single Report"; // Request to fetch a specific report
    public static final String FETCH_EMPLOYEE_REPORTS = "Fetch Employee-Specific Reports"; // Request to fetch reports associated with a specific employee
    public static final String FETCH_BRANCH_REPORTS = "Fetch Branch Reports"; // Request to fetch reports related to a specific branch
    public static final String FETCH_DAILY_REPORTS = "Fetch Daily Reports";
    public static final String FETCH_MONTHLY_REPORTS = "Fetch Monthly Reports";
    public static final String FETCH_LAST_QUARTER_REPORT = "Fetch Last Quarterly Report"; // Request to fetch reports for a specific month
    public static final String FETCH_YEARLY_REPORTS = "Fetch Yearly Reports"; // Request to fetch reports for a specific year
    public static final String GENERATE_REPORT = "Generate Report"; // Request to generate a new report
    public static final String DELETE_REPORT = "Delete Report"; // Request to delete a specific report


    // Response Constants
    public static final String REPORT_GENERATION_SUCCESS = "Report Generation Success"; // Response indicating successful report generation
    public static final String REPORT_GENERATION_FAILURE = "Report Generation Failure"; // Response indicating failure in report generation
    public static final String REPORT_DELETION_SUCCESS = "Report Deletion Success"; // Response indicating successful report deletion
    public static final String REPORT_DELETION_FAILURE = "Report Deletion Failure"; // Response indicating failure in report deletion
    public static final String NO_REPORTS_FOUND = "No Reports Found"; // Response indicating that no reports were found for the given request
    public static final String INVALID_REPORT_REQUEST = "Invalid Report Request"; // Response indicating that the request for a report was invalid or malformed

    // General Operation Constants
    public static final String REPORT_UPDATE_NOTIFICATION = "Report Update Notification"; // Notification for when a report has been updated or modified
    public static final String REPORT_VIEW_ACCESS_GRANTED = "Report View Access Granted"; // Notification for when an employee is granted access to view a report
    public static final String REPORT_VIEW_ACCESS_DENIED = "Report View Access Denied"; // Notification for when an employee is denied access to view a report

    public static final String INVALID_LABEL = "Invalid Label";
    public static final String ALL_BRANCHES = "All Branches";

    public static final String COMPLAINT_STATUS_OPEN = "Open";
    public static final String COMPLAINT_STATUS_CLOSED = "Closed";
    public static final String PURCHASE_TYPE_UNKNOWN = "Unknown";

    // Prevent instantiation
    private ReportOperationTypes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

