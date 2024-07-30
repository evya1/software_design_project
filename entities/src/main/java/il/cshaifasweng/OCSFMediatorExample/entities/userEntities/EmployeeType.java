package il.cshaifasweng.OCSFMediatorExample.entities.userEntities;

public enum EmployeeType {
    BASE("Employee"),
    SERVICE("Customer Service"),
    THEATER_MANAGER("Theater Manager"),
    CHAIN_MANAGER("Chain Manager"),
    CONTENT_MANAGER("Content Manager");

    private final String description;
    EmployeeType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
