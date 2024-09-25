package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType;

import java.util.function.Function;

public abstract class QuarterlyReportStrategy<T> extends AbstractReportStrategy<T> {

    @Override
    protected ReportSpanType getReportSpanType() {
        return ReportSpanType.Quarterly;
    }

    @Override
    protected abstract ReportType getReportType();

    @Override
    protected abstract String getReportOperationType();

    @Override
    protected Function<T, PurchaseType> getPurchaseTypeExtractor() {
        return item -> null;
    }
}
