package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies.*;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.Monthly;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.Quarterly;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.REPORT_TYPE_A;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.REPORT_TYPE_B;

public class ReportService {
    private static ReportService instance;
    private final DataCommunicationDB db;
    private final Map<ReportType, Map<ReportSpanType, ReportStrategy>> strategyRegistry;

    private ReportService(DataCommunicationDB db) {
        this.db = db;
        this.strategyRegistry = new HashMap<>();
    }

    public static synchronized ReportService getInstance() {
        if (instance == null) {
            DataCommunicationDB db = new DataCommunicationDB();
            instance = new ReportService(db);

            // Register strategies for specific report types
            instance.registerStrategy(REPORT_TYPE_A, Monthly, new MonthlySalesReportStrategy());
            instance.registerStrategy(REPORT_TYPE_A, Quarterly, new QuarterlySalesReportStrategy());
            instance.registerStrategy(REPORT_TYPE_B, Monthly, new MonthlyComplaintsReportStrategy());
            instance.registerStrategy(REPORT_TYPE_B, Quarterly, new QuarterlyComplaintsReportStrategy());

            // Register combined strategies for ALL_REPORT_TYPE
            instance.registerStrategy(ReportType.ALL_REPORT_TYPE, Monthly, new CombinedMonthlyReportStrategy());
            instance.registerStrategy(ReportType.ALL_REPORT_TYPE, Quarterly, new CombinedQuarterlyReportStrategy());
        }
        return instance;
    }

    // Method to register strategies
    public void registerStrategy(ReportType reportType, ReportSpanType spanType, ReportStrategy strategy) {
        strategyRegistry
                .computeIfAbsent(reportType, k -> new HashMap<>())
                .put(spanType, strategy);
    }

    public Report generateReport(ReportType reportType, ReportSpanType spanType, Branch branch, Month month, PurchaseType purchaseType) {
        ReportStrategy strategy = getStrategy(reportType, spanType);
        List<?> dataItems = gatherReportData(reportType, branch, month, purchaseType);
        return strategy.generateReport(dataItems, branch, month);
    }

    private ReportStrategy getStrategy(ReportType reportType, ReportSpanType spanType) {
        Map<ReportSpanType, ReportStrategy> strategiesBySpan = strategyRegistry.get(reportType);
        if (strategiesBySpan != null) {
            ReportStrategy strategy = strategiesBySpan.get(spanType);
            if (strategy != null) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("No strategy found for report type: " + reportType + " and span type: " + spanType);
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
        Report report = strategy.generateReport(dataItems, branch, month); // I need to add "purchaseType" param later

        // Save the generated report to the database
        db.persistReport(report);

        return report;
    }

    public List<Report> retrieveReportsByBranchAndMonth(Branch branch, Month month, PurchaseType purchaseType, ReportType reportType) {
        List<Report> retrievedReports = db.retrieveReportsForBranchAndMonth(branch, month);

        // If no existing retrievedReports, generate new ones
        if (retrievedReports.isEmpty()) {
            Report newReport = createAndPersistReport(reportType, ReportSpanType.Monthly, branch, month, purchaseType);
            retrievedReports.add(newReport);
        }

        return retrievedReports;
    }
}
