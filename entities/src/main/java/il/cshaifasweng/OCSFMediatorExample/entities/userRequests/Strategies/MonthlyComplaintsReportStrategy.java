package il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType;

import java.util.function.Function;

public class MonthlyComplaintsReportStrategy extends MonthlyReportStrategy<Complaint> {

    @Override
    protected ReportType getReportType() {
        return ReportType.REPORT_TYPE_B;  // Complaints Report Type
    }

    @Override
    protected String getReportOperationType() {
        return ReportOperationTypes.FETCH_MONTHLY_REPORTS;
    }

    @Override
    protected Function<Complaint, PurchaseType> getPurchaseTypeExtractor() {
        return Complaint::getPurchaseType;  // Use the purchase type linked to the complaint
    }

    @Override
    protected Class<Complaint> getGenericClass() {
        return Complaint.class;
    }
}
