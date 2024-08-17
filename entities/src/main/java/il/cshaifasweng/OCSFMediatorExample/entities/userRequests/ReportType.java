package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

public enum ReportType {
    REPORT_TYPE_A("Sells Report"),
    REPORT_TYPE_B("Complaints Report"),
    ALL_REPORT_TYPE("All Reports");
    private final String value;

    ReportType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}