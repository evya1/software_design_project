package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.RequestData;

import java.util.List;

public interface ReportStrategy {

    /**
     * Generates a report based on the provided data, branch, and month.
     * <p>
     * This method takes in a list of data items (e.g., purchases, complaints) and generates
     * a {@code Report} object. The specific logic for report generation will vary depending
     * on the implementation of the strategy.
     * </p>
     *
     * @return The generated {@code Report}.
     */
    Report generateReport(List<?> items, RequestData requestData);
}
