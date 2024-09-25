package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Strategies.*;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.Monthly;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.Quarterly;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.*;

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
            instance.registerStrategy(ALL_REPORT_TYPE, Monthly, new CombinedMonthlyReportStrategy());
            instance.registerStrategy(ALL_REPORT_TYPE, Quarterly, new CombinedQuarterlyReportStrategy());
        }
        return instance;
    }

    // Method to register strategies
    public void registerStrategy(ReportType reportType, ReportSpanType spanType, ReportStrategy strategy) {
        strategyRegistry
                .computeIfAbsent(reportType, k -> new HashMap<>())
                .put(spanType, strategy);
    }

    public Report generateReport(RequestData requestData) {
        ReportStrategy strategy = getStrategy(requestData);
        List<?> dataItems = gatherReportData(requestData);
        return strategy.generateReport(dataItems, requestData);
    }

    private ReportStrategy getStrategy(RequestData requestData) {
        ReportType reportType = requestData.reportType();
        ReportSpanType reportSpanType = requestData.reportSpanType();
        Map<ReportSpanType, ReportStrategy> strategiesBySpan = strategyRegistry.get(reportType);
        if (strategiesBySpan != null) {
            ReportStrategy strategy = strategiesBySpan.get(reportSpanType);
            if (strategy != null) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("No strategy found for report type: " + reportType + " and span type: " + reportSpanType);
    }

    private List<Object> gatherReportData(RequestData requestData) {
        ReportType reportType = requestData.reportType();
        List<Object> combinedResults = new ArrayList<>();

        switch (reportType) {
            case REPORT_TYPE_A:  // Fetch purchases
                combinedResults.addAll(db.retrieveAllPurchasesByBranchAndMonth(requestData));
                break;
            case REPORT_TYPE_B:  // Fetch complaints
                combinedResults.addAll(db.retrieveComplaintsByBranchAndMonth(requestData));
                break;
            case ALL_REPORT_TYPE:  // Fetch both purchases and complaints
                combinedResults.addAll(db.retrieveAllPurchasesByBranchAndMonth(requestData));
                combinedResults.addAll(db.retrieveComplaintsByBranchAndMonth(requestData));
                break;
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportType);
        }
        return combinedResults;
    }

    public Report createAndPersistReport(RequestData requestData) {
        // Retrieve the appropriate strategy
        ReportStrategy strategy = getStrategy(requestData);
        // Gather data items based on the report details
        List<?> dataItems = gatherReportData(requestData);

        // Generate the report
        Report report = strategy.generateReport(dataItems, requestData);

        // Persist the report to the database
        db.persistReport(report);

        return report;
    }

    public List<Report> retrieveReportsByBranchAndMonth(Month month, RequestData requestData) {

        List<Report> retrievedReports = new ArrayList<>();

        Report newReport = createAndPersistReport(requestData);
        retrievedReports.add(newReport);

        return retrievedReports;
    }

    public List<Complaint> retrieveAllComplaints() {
        return db.fetchAllComplaints();
    }

    public List<Purchase> retrieveAllPurchases() {
        return db.fetchAllPurchases();
    }
}
