package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType;

import java.util.function.Function;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.FETCH_LAST_QUARTER_REPORT;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.ALL_REPORT_TYPE;

public class CombinedQuarterlyReportStrategy extends QuarterlyReportStrategy<Object> {

    @Override
    protected ReportType getReportType() {
        return ALL_REPORT_TYPE;  // Combined Report Type for quarterly reports
    }

    @Override
    protected String getReportOperationType() {
        return FETCH_LAST_QUARTER_REPORT;  // Example operation type for quarterly reports
    }

    @Override
    protected Function<Object, PurchaseType> getPurchaseTypeExtractor() {
        return item -> null;  // Handle generically for both purchases and complaints
    }

    @Override
    protected Class<Object> getGenericClass() {
        return Object.class;  // Generalized for mixed types
    }
}
