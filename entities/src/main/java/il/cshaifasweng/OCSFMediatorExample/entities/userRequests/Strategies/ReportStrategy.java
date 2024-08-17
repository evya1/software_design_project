package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.RequestData;

import java.time.Month;
import java.util.List;

public interface ReportStrategy {

    /**
     * Assembles the {@code RequestData} needed for a report request.
     * <p>
     * This method constructs a {@code RequestData} object based on the provided employee details,
     * the year, and the month. The specific implementation will vary depending on the reporting
     * strategy (e.g., monthly report, quarterly report, sales report, etc.).
     * </p>
     *
     * @param employee The {@code Employee} who is requesting the report.
     * @param year The year for which the report is requested.
     * @param month The month for which the report is requested.
     * @return The assembled {@code RequestData} object containing the details of the report request.
     */
    RequestData assembleRequestData(Employee employee, int year, int month);

    /**
     * Generates a report based on the provided data, branch, and month.
     * <p>
     * This method takes in a list of data items (e.g., purchases, complaints) and generates
     * a {@code Report} object. The specific logic for report generation will vary depending
     * on the implementation of the strategy.
     * </p>
     *
     * @param items The data items (e.g., purchases, complaints) to be included in the report.
     * @param branch The branch for which the report is generated.
     * @param month The month for which the report is generated.
     * @return The generated {@code Report}.
     */
    Report generateReport(List<?> items, Branch branch, Month month);
}
