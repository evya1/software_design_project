package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType;

import java.util.function.Function;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.FETCH_MONTHLY_REPORTS;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.ALL_REPORT_TYPE;

public class CombinedMonthlyReportStrategy extends MonthlyReportStrategy<Object> {

    @Override
    protected ReportType getReportType() {
        return ALL_REPORT_TYPE;  // Combined Report Type
    }

    @Override
    protected String getReportOperationType() {
        return FETCH_MONTHLY_REPORTS;  // Example operation type
    }

    @Override
    protected Function<Object, PurchaseType> getPurchaseTypeExtractor() {
        return item -> null;  // Default implementation, modify as needed
    }

    @Override
    protected Class<Object> getGenericClass() {
        return Object.class;  // Generalized for mixed types
    }
}

