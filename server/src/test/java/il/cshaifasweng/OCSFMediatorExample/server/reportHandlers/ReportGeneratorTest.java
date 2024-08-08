package il.cshaifasweng.OCSFMediatorExample.server.reportHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Chain;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Month;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportGeneratorTest {

    private ReportGenerator reportGenerator;
    private Branch testBranch;
    private Chain testChain;
    private LocalDate testDate;
    private Month testMonth;

    @BeforeEach
    void setUp() {
        testBranch = new Branch();
        testBranch.setBranchName("Test Branch");

        testChain = new Chain();
        testChain.setName("Test Chain");
        testChain.setBranches(new ArrayList<>());
        testChain.getBranches().add(testBranch);

        reportGenerator = mock(ReportGenerator.class);

        testDate = LocalDate.of(2024, 8, 1);
        testMonth = Month.AUGUST;
    }

    @Test
    void generateDailyPurchaseReport_shouldRetrieveDailyPurchasesForBranch() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateDailyPurchaseReport(testBranch, testDate)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateDailyPurchaseReport(testBranch, testDate);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch."));
    }

    @Test
    void generateDailyPurchaseReport_withPurchaseType_shouldRetrieveSpecificType() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateDailyPurchaseReport(testBranch, testDate, PurchaseType.BOOKLET)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateDailyPurchaseReport(testBranch, testDate, PurchaseType.BOOKLET);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> {
            assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch.");
            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
        });
    }

    @Test
    void generateMonthlyPurchaseReport_shouldRetrieveMonthlyPurchasesForBranch() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateMonthlyPurchaseReport(testBranch, testMonth)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateMonthlyPurchaseReport(testBranch, testMonth);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch."));
    }

    @Test
    void generateMonthlyPurchaseReport_withPurchaseType_shouldRetrieveSpecificType() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateMonthlyPurchaseReport(testBranch, testMonth, PurchaseType.BOOKLET)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateMonthlyPurchaseReport(testBranch, testMonth, PurchaseType.BOOKLET);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> {
            assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch.");
            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
        });
    }

    @Test
    void generateDailyComplaintReport_shouldRetrieveDailyComplaintsForBranch() {
        List<Complaint> mockComplaints = Collections.singletonList(createMockComplaint());
        when(reportGenerator.generateDailyComplaintReport(testBranch, testDate)).thenReturn(mockComplaints);

        List<Complaint> complaints = reportGenerator.generateDailyComplaintReport(testBranch, testDate);
        assertNotNull(complaints, "The complaint list should not be null.");
        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
        complaints.forEach(complaint -> assertEquals(testBranch, complaint.getBranch(), "The branch should match the test branch."));
    }

    @Test
    void generateMonthlyComplaintReport_shouldRetrieveMonthlyComplaintsForBranch() {
        List<Complaint> mockComplaints = Collections.singletonList(createMockComplaint());
        when(reportGenerator.generateMonthlyComplaintReport(testBranch, testMonth)).thenReturn(mockComplaints);

        List<Complaint> complaints = reportGenerator.generateMonthlyComplaintReport(testBranch, testMonth);
        assertNotNull(complaints, "The complaint list should not be null.");
        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
        complaints.forEach(complaint -> assertEquals(testBranch, complaint.getBranch(), "The branch should match the test branch."));
    }

    @Test
    void generateDailyChainPurchaseReport_shouldRetrieveDailyPurchasesForChain() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateDailyChainPurchaseReport(testChain, testDate)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateDailyChainPurchaseReport(testChain, testDate);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain."));
    }

    @Test
    void generateDailyChainPurchaseReport_withPurchaseType_shouldRetrieveSpecificTypeForChain() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateDailyChainPurchaseReport(testChain, testDate, PurchaseType.BOOKLET)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateDailyChainPurchaseReport(testChain, testDate, PurchaseType.BOOKLET);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> {
            assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain.");
            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
        });
    }

    @Test
    void generateMonthlyChainPurchaseReport_shouldRetrieveMonthlyPurchasesForChain() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateMonthlyChainPurchaseReport(testChain, testMonth)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateMonthlyChainPurchaseReport(testChain, testMonth);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain."));
    }

    @Test
    void generateMonthlyChainPurchaseReport_withPurchaseType_shouldRetrieveSpecificTypeForChain() {
        List<Purchase> mockPurchases = Collections.singletonList(createMockPurchase());
        when(reportGenerator.generateMonthlyChainPurchaseReport(testChain, testMonth, PurchaseType.BOOKLET)).thenReturn(mockPurchases);

        List<Purchase> purchases = reportGenerator.generateMonthlyChainPurchaseReport(testChain, testMonth, PurchaseType.BOOKLET);
        assertNotNull(purchases, "The purchase list should not be null.");
        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
        purchases.forEach(purchase -> {
            assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain.");
            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
        });
    }

    @Test
    void generateDailyChainComplaintReport_shouldRetrieveDailyComplaintsForChain() {
        List<Complaint> mockComplaints = Collections.singletonList(createMockComplaint());
        when(reportGenerator.generateDailyChainComplaintReport(testChain, testDate)).thenReturn(mockComplaints);

        List<Complaint> complaints = reportGenerator.generateDailyChainComplaintReport(testChain, testDate);
        assertNotNull(complaints, "The complaint list should not be null.");
        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
        complaints.forEach(complaint -> assertTrue(testChain.getBranches().contains(complaint.getBranch()), "The complaint branch should be part of the test chain."));
    }

    @Test
    void generateMonthlyChainComplaintReport_shouldRetrieveMonthlyComplaintsForChain() {
        List<Complaint> mockComplaints = Collections.singletonList(createMockComplaint());
        when(reportGenerator.generateMonthlyChainComplaintReport(testChain, testMonth)).thenReturn(mockComplaints);

        List<Complaint> complaints = reportGenerator.generateMonthlyChainComplaintReport(testChain, testMonth);
        assertNotNull(complaints, "The complaint list should not be null.");
        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
        complaints.forEach(complaint -> assertTrue(testChain.getBranches().contains(complaint.getBranch()), "The complaint branch should be part of the test chain."));
    }

    private Purchase createMockPurchase() {
        Purchase purchase = new Purchase();
        purchase.setBranch(testBranch);
        purchase.setPurchaseType(PurchaseType.BOOKLET);
        purchase.setDateOfPurchase(LocalDateTime.of(testDate, LocalDateTime.now().toLocalTime()));
        return purchase;
    }

    private Complaint createMockComplaint() {
        Complaint complaint = new Complaint();
        complaint.setBranch(testBranch);
        complaint.setDateOfComplaint(LocalDateTime.of(testDate, LocalDateTime.now().toLocalTime()));
        complaint.setComplaintStatus("Closed");
        return complaint;
    }
}
