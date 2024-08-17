package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies.*;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.Monthly;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.Quarterly;

public class ReportService {
    private final DataCommunicationDB db;
    private final MonthlyReportStrategy monthlyReportStrategy;
    private final QuarterlySalesReportStrategy quarterlySalesReportStrategy;

    // Constructor for dependency injection
    public ReportService(DataCommunicationDB db,
                         MonthlyReportStrategy monthlyReportStrategy,
                         QuarterlySalesReportStrategy quarterlySalesReportStrategy) {
        this.db = db;
        this.monthlyReportStrategy = monthlyReportStrategy;
        this.quarterlySalesReportStrategy = quarterlySalesReportStrategy;
    }

    public Report generateReport(ReportType reportType, ReportSpanType spanType, Branch branch, Month month, PurchaseType purchaseType) {
        ReportStrategy strategy = getStrategy(reportType, spanType);
        List<?> dataItems = gatherReportData(reportType, branch, month, purchaseType);
        return strategy.generateReport(dataItems, branch, month);
    }

    private ReportStrategy getStrategy(ReportType reportType, ReportSpanType spanType) {
        if (spanType == Monthly) {
            switch (reportType) {
                case REPORT_TYPE_A:  // Monthly Sales Report
                    return monthlyReportStrategy;
                case REPORT_TYPE_B:  // Monthly Complaints Report
                    return new MonthlyComplaintsReportStrategy();
            }
        } else if (spanType == Quarterly) {
            switch (reportType) {
                case REPORT_TYPE_A:  // Quarterly Sales Report
                    return quarterlySalesReportStrategy;
                case REPORT_TYPE_B:  // Quarterly Complaints Report
                    return new QuarterlyComplaintsReportStrategy();
            }
        }
        throw new IllegalArgumentException("Unknown report type or span: " + reportType + ", " + spanType);
    }

    private List<Object> gatherReportData(ReportType reportType, Branch branch, Month month, PurchaseType purchaseType) {
        List<Object> combinedResults = new ArrayList<>();

        switch (reportType) {
            case REPORT_TYPE_A:  // Fetch purchases
                combinedResults.addAll(db.retrieveAllPurchasesByBranchAndMonth(branch, month, purchaseType));
                break;
            case REPORT_TYPE_B:  // Fetch complaints
                combinedResults.addAll(db.retrieveComplaintsByBranchAndMonth(branch, month));
                break;
            case ALL_REPORT_TYPE:  // Fetch both purchases and complaints
                combinedResults.addAll(db.retrieveAllPurchasesByBranchAndMonth(branch, month, purchaseType));
                combinedResults.addAll(db.retrieveComplaintsByBranchAndMonth(branch, month));
                break;
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
        return combinedResults;
    }

    public Report createAndPersistReport(ReportType reportType, ReportSpanType spanType, Branch branch, Month month, PurchaseType purchaseType) {
        ReportStrategy strategy = getStrategy(reportType, spanType);
        List<?> dataItems = gatherReportData(reportType, branch, month, purchaseType);
        Report report = strategy.generateReport(dataItems, branch, month);

        // Save the generated report to the database
        db.persistReport(report);

        return report;
    }

    public List<Report> retrieveReportsByBranchAndMonth(Branch branch, Month month) {
        return db.retrieveReportsForBranchAndMonth(branch, month);
    }
}
