package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType;

import java.util.function.Function;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.FETCH_LAST_QUARTER_REPORT;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.REPORT_TYPE_A;

public class QuarterlySalesReportStrategy extends QuarterlyReportStrategy<Purchase> {

    @Override
    protected ReportType getReportType() {
        return REPORT_TYPE_A;  // Sales Report Type
    }

    @Override
    protected String getReportOperationType() {
        return FETCH_LAST_QUARTER_REPORT;
    }

    @Override
    protected Function<Purchase, PurchaseType> getPurchaseTypeExtractor() {
        return Purchase::getPurchaseType;
    }

    @Override
    protected Class<Purchase> getGenericClass() {
        return Purchase.class;
    }
}
