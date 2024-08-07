package il.cshaifasweng.OCSFMediatorExample.server.reportHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Chain;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Month;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * The ReportGenerator class is responsible for generating reports for Purchases and Complaints.
 * It supports generating reports for a specific branch or for the entire chain,
 * and for a specified month. Reports can also be filtered by purchase type.
 */
public class ReportGenerator {

    private final EntityManager entityManager;

    /**
     * Constructs a ReportGenerator with the given EntityManager.
     *
     * @param entityManager the EntityManager to be used for querying the database.
     */
    public ReportGenerator(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Generates a daily purchase report for the specified branch and date.
     *
     * @param branch the branch for which to generate the report.
     * @param date   the date for which to generate the report.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateDailyPurchaseReport(Branch branch, LocalDate date) {
        return generateDailyReport(branch, date, Purchase.class, "Purchase", "dateOfPurchase", null);
    }

    /**
     * Generates a daily purchase report for the specified branch, date, and purchase type.
     *
     * @param branch       the branch for which to generate the report.
     * @param date         the date for which to generate the report.
     * @param purchaseType the type of purchase to filter by.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateDailyPurchaseReport(Branch branch, LocalDate date, PurchaseType purchaseType) {
        return generateDailyReport(branch, date, Purchase.class, "Purchase", "dateOfPurchase", purchaseType);
    }

    /**
     * Generates a monthly purchase report for the specified branch and month.
     *
     * @param branch the branch for which to generate the report.
     * @param month  the month for which to generate the report.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateMonthlyPurchaseReport(Branch branch, Month month) {
        return generateMonthlyReport(branch, month, this::generateDailyPurchaseReport);
    }

    /**
     * Generates a monthly purchase report for the specified branch, month, and purchase type.
     *
     * @param branch       the branch for which to generate the report.
     * @param month        the month for which to generate the report.
     * @param purchaseType the type of purchase to filter by.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateMonthlyPurchaseReport(Branch branch, Month month, PurchaseType purchaseType) {
        return generateMonthlyReport(branch, month, (b, d) -> generateDailyPurchaseReport(b, d, purchaseType));
    }

//    /**
//     * For testing purposes
//     *
//     * @return a list of Complaint entities matching the criteria.
//     */
//    private List<Complaint> generateDailyComplaintReport(Branch branch, LocalDate date, String complaintStatus) {
//        return generateDailyComplaintReport(branch, date);
//    }

    /**
     * Generates a daily complaint report for the specified branch and date.
     *
     * @param branch the branch for which to generate the report.
     * @param date   the date for which to generate the report.
     * @return a list of Complaint entities matching the criteria.
     */
    public List<Complaint> generateDailyComplaintReport(Branch branch, LocalDate date) {
        return generateDailyReport(branch, date, Complaint.class, "Complaint", "complaintDate", null);
    }

    /**
     * Generates a monthly complaint report for the specified branch and month.
     *
     * @param branch the branch for which to generate the report.
     * @param month  the month for which to generate the report.
     * @return a list of Complaint entities matching the criteria.
     */
    public List<Complaint> generateMonthlyComplaintReport(Branch branch, Month month) {
        return generateMonthlyReport(branch, month, this::generateDailyComplaintReport);
    }

    /**
     * Generates a daily purchase report for the entire chain for the specified date.
     *
     * @param chain the chain for which to generate the report.
     * @param date  the date for which to generate the report.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateDailyChainPurchaseReport(Chain chain, LocalDate date) {
        List<Purchase> dailyReport = new ArrayList<>();
        for (Branch branch : chain.getBranches()) {
            dailyReport.addAll(generateDailyPurchaseReport(branch, date));
        }
        return dailyReport;
    }

    /**
     * Generates a daily purchase report for the entire chain, date, and purchase type.
     *
     * @param chain        the chain for which to generate the report.
     * @param date         the date for which to generate the report.
     * @param purchaseType the type of purchase to filter by.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateDailyChainPurchaseReport(Chain chain, LocalDate date, PurchaseType purchaseType) {
        List<Purchase> dailyReport = new ArrayList<>();
        for (Branch branch : chain.getBranches()) {
            dailyReport.addAll(generateDailyPurchaseReport(branch, date, purchaseType));
        }
        return dailyReport;
    }

    /**
     * Generates a monthly purchase report for the entire chain for the specified month.
     *
     * @param chain the chain for which to generate the report.
     * @param month the month for which to generate the report.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateMonthlyChainPurchaseReport(Chain chain, Month month) {
        return generateMonthlyChainReport(chain, month, this::generateDailyChainPurchaseReport);
    }

    /**
     * Generates a monthly purchase report for the entire chain, month, and purchase type.
     *
     * @param chain        the chain for which to generate the report.
     * @param month        the month for which to generate the report.
     * @param purchaseType the type of purchase to filter by.
     * @return a list of Purchase entities matching the criteria.
     */
    public List<Purchase> generateMonthlyChainPurchaseReport(Chain chain, Month month, PurchaseType purchaseType) {
        return generateMonthlyChainReport(chain, month, (c, d) -> generateDailyChainPurchaseReport(c, d, purchaseType));
    }

    /**
     * Generates a daily complaint report for the entire chain for the specified date.
     *
     * @param chain the chain for which to generate the report.
     * @param date  the date for which to generate the report.
     * @return a list of Complaint entities matching the criteria.
     */
    public List<Complaint> generateDailyChainComplaintReport(Chain chain, LocalDate date) {
        List<Complaint> dailyReport = new ArrayList<>();
        for (Branch branch : chain.getBranches()) {
            dailyReport.addAll(generateDailyComplaintReport(branch, date));
        }
        return dailyReport;
    }

    /**
     * Generates a monthly complaint report for the entire chain for the specified month.
     *
     * @param chain the chain for which to generate the report.
     * @param month the month for which to generate the report.
     * @return a list of Complaint entities matching the criteria.
     */
    public List<Complaint> generateMonthlyChainComplaintReport(Chain chain, Month month) {
        return generateMonthlyChainReport(chain, month, this::generateDailyChainComplaintReport);
    }

    /**
     * Generates a daily report for the specified branch, date, and entity type.
     *
     * @param branch       the branch for which to generate the report.
     * @param date         the date for which to generate the report.
     * @param entityClass  the class of the entity to retrieve.
     * @param entityName   the name of the entity to retrieve.
     * @param dateField    the date field to filter by.
     * @param purchaseType the type of purchase to filter by, if applicable.
     * @return a list of entities matching the criteria.
     */
    private <T> List<T> generateDailyReport(Branch branch, LocalDate date, Class<T> entityClass, String entityName, String dateField, PurchaseType purchaseType) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        String queryStr = String.format("SELECT e FROM %s e WHERE e.branch = :branch AND e.%s BETWEEN :startDate AND :endDate", entityName, dateField);
        if (purchaseType != null) {
            queryStr += " AND e.purchaseType = :purchaseType";
        }
        TypedQuery<T> query = entityManager.createQuery(queryStr, entityClass);

        query.setParameter("branch", branch);
        query.setParameter("startDate", date.atStartOfDay());
        query.setParameter("endDate", date.atTime(23, 59, 59));
        if (purchaseType != null) {
            query.setParameter("purchaseType", purchaseType);
        }

        List<T> results = query.getResultList();
        transaction.commit();
        return results;
    }

    /**
     * Generates a monthly report for the specified branch, month, and daily report generator.
     *
     * @param branch               the branch for which to generate the report.
     * @param month                the month for which to generate the report.
     * @param dailyReportGenerator the function to generate daily reports.
     * @return a list of entities matching the criteria.
     */
    private <T> List<T> generateMonthlyReport(Branch branch, Month month, DailyReportGenerator<T> dailyReportGenerator) {
        LocalDate startDate = getStartDate(month);
        LocalDate endDate = getEndDate(month);

        List<T> monthlyReport = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            monthlyReport.addAll(dailyReportGenerator.generateReport(branch, date));
        }

        return monthlyReport;
    }

    /**
     * Generates a monthly report for the entire chain, month, and daily chain report generator.
     *
     * @param chain                     the chain for which to generate the report.
     * @param month                     the month for which to generate the report.
     * @param dailyChainReportGenerator the function to generate daily reports for the chain.
     * @return a list of entities matching the criteria.
     */
    private <T> List<T> generateMonthlyChainReport(Chain chain, Month month, DailyChainReportGenerator<T> dailyChainReportGenerator) {
        LocalDate startDate = getStartDate(month);
        LocalDate endDate = getEndDate(month);

        List<T> monthlyReport = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            monthlyReport.addAll(dailyChainReportGenerator.generateReport(chain, date));
        }

        return monthlyReport;
    }

    /**
     * Returns the start date of the specified month.
     *
     * @param month the month for which to get the start date.
     * @return the start date of the month.
     */
    private LocalDate getStartDate(Month month) {
        return YearMonth.of(LocalDate.now().getYear(), month.ordinal() + 1).atDay(1);
    }

    /**
     * Returns the end date of the specified month.
     *
     * @param month the month for which to get the end date.
     * @return the end date of the month.
     */
    private LocalDate getEndDate(Month month) {
        return YearMonth.of(LocalDate.now().getYear(), month.ordinal() + 1).atEndOfMonth();
    }

    @FunctionalInterface
    private interface DailyReportGenerator<T> {
        List<T> generateReport(Branch branch, LocalDate date);
    }

    @FunctionalInterface
    private interface DailyChainReportGenerator<T> {
        List<T> generateReport(Chain chain, LocalDate date);
    }
}
