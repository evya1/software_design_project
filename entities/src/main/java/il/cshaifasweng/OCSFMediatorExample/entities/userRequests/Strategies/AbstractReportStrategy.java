package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.*;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * AbstractReportStrategy is an abstract class that provides a template for generating reports based on specific
 * strategies. Subclasses are required to define the specific report type, span type, operation type, and how to
 * extract purchase types from the generic items they handle.
 *
 * @param <T> the type of items (e.g., Purchase, Complaint) that this strategy will process to generate reports.
 */
public abstract class AbstractReportStrategy<T> implements ReportStrategy {

    /**
     * Gets the specific report type handled by the strategy (e.g., Sales, Complaints).
     *
     * @return the report type for this strategy.
     */
    protected abstract ReportType getReportType();

    /**
     * Gets the report span type (e.g., Daily, Monthly, Quarterly) for the reports generated by this strategy.
     *
     * @return the report span type for this strategy.
     */
    protected abstract ReportSpanType getReportSpanType();

    /**
     * Gets the operation type for this strategy, used for identifying the specific report operation (e.g.,
     * fetching monthly reports, fetching quarterly reports).
     *
     * @return the report operation type as a string.
     */
    protected abstract String getReportOperationType();

    /**
     * Provides a function that extracts the purchase type from the generic items processed by this strategy.
     *
     * @return a function that maps an item of type T to a PurchaseType.
     */
    protected abstract Function<T, PurchaseType> getPurchaseTypeExtractor();

    /**
     * Gets the class of the generic type T, which is used to safely cast and process items during report generation.
     *
     * @return the class of the generic type T.
     */
    protected abstract Class<T> getGenericClass();

    /**
     * Assembles a RequestData object for a specific employee, year, and month, using the details of the strategy.
     *
     * @param employee the employee requesting the report.
     * @param year     the year for which the report is generated.
     * @param month    the month for which the report is generated.
     * @return a RequestData object containing the necessary information for the report request.
     */
    @Override
    public RequestData assembleRequestData(Employee employee, int year, int month) {
        return MessageUtilForReports.buildBasicRequestData(
                getReportOperationType(),
                getReportType(),
                getReportSpanType(),
                employee,
                year,
                month,
                PurchaseType.ALL_TYPES  // Default to ALL_TYPES unless overridden in subclass
        );
    }

    /**
     * Generates a Report entity by processing the given list of items and aggregating the data into a format suitable
     * for the report's dataForGraphs field. The report is populated with branch, month, year, and span type.
     *
     * @param items  the list of items to be processed (e.g., Purchases, Complaints).
     * @param branch the branch associated with the report.
     * @param month  the month for which the report is generated.
     * @return a Report entity containing the generated report data.
     */
    @Override
    public Report generateReport(List<?> items, Branch branch, Month month) {
        // Safely filter and cast items to the generic type T
        List<T> typedItems = items.stream()
                .filter(item -> getGenericClass().isInstance(item))
                .map(item -> (T) item)
                .toList();

        return new Report.Builder()
                .withBranch(branch)
                .withMonth(month)
                .withYear(2024)  // Example year, could be parameterized
                .withReportSpanType(getReportSpanType())
                .withDataForGraphs(extractDataForGraphs(typedItems))
                .build();
    }

    /**
     * Extracts data for graphs by processing items of type T. This method aggregates the data into a map where
     * the keys are labels (e.g., purchase types), and the values are the corresponding data points (e.g., total sales).
     *
     * @param items the list of items of type T to be processed.
     * @return a map representing the aggregated data for graphs.
     */
    protected Map<String, Double> extractDataForGraphs(List<T> items) {
        Map<String, Double> dataForGraphs = new HashMap<>();
        Function<T, PurchaseType> purchaseTypeExtractor = getPurchaseTypeExtractor();

        for (T item : items) {
            PurchaseType purchaseType = purchaseTypeExtractor.apply(item);
            String key = (purchaseType != null) ? purchaseType.name() : "Unknown";
            dataForGraphs.merge(key, 1.0, Double::sum);
        }

        return dataForGraphs;
    }
}
