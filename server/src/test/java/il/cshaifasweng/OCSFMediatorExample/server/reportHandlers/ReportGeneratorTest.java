//package il.cshaifasweng.OCSFMediatorExample.server.reportHandlers;
//
//import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
//import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
//import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Chain;
//import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
//import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
//import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
//import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Month;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.junit.jupiter.api.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ReportGeneratorTest {
//
//    private Session session;
//    private ReportGenerator reportGenerator;
//    private Branch testBranch;
//    private Chain testChain;
//    private LocalDate testDate;
//    private Month testMonth;
//
//    @BeforeEach
//    void setUp() {
//        String password = "admin"; // Replace with your actual password
//        session = DataCommunicationDB.getSessionFactory(password).openSession();
//        DataCommunicationDB.setSession(session);
//        reportGenerator = new ReportGenerator(session);
//        testBranch = createTestBranch();
//        testChain = createTestChain(testBranch);
//        testDate = LocalDate.of(2024, 8, 1);
//        testMonth = Month.AUGUST;
//        populateTestData();
//    }
//
//    @AfterEach
//    void tearDown() {
//        clearTestData();
//        if (session != null) {
//            session.close();
//        }
//    }
//
//    @Test
//    void generateDailyPurchaseReport_shouldRetrieveDailyPurchasesForBranch() {
//        List<Purchase> purchases = reportGenerator.generateDailyPurchaseReport(testBranch, testDate);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch."));
//    }
//
//    @Test
//    void generateDailyPurchaseReport_withPurchaseType_shouldRetrieveSpecificType() {
//        List<Purchase> purchases = reportGenerator.generateDailyPurchaseReport(testBranch, testDate, PurchaseType.BOOKLET);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> {
//            assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch.");
//            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
//        });
//    }
//
//    @Test
//    void generateMonthlyPurchaseReport_shouldRetrieveMonthlyPurchasesForBranch() {
//        List<Purchase> purchases = reportGenerator.generateMonthlyPurchaseReport(testBranch, testMonth);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch."));
//    }
//
//    @Test
//    void generateMonthlyPurchaseReport_withPurchaseType_shouldRetrieveSpecificType() {
//        List<Purchase> purchases = reportGenerator.generateMonthlyPurchaseReport(testBranch, testMonth, PurchaseType.BOOKLET);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> {
//            assertEquals(testBranch, purchase.getBranch(), "The branch should match the test branch.");
//            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
//        });
//    }
//
//    @Test
//    void generateDailyComplaintReport_shouldRetrieveDailyComplaintsForBranch() {
//        List<Complaint> complaints = reportGenerator.generateDailyComplaintReport(testBranch, testDate);
//        assertNotNull(complaints, "The complaint list should not be null.");
//        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
//        complaints.forEach(complaint -> assertEquals(testBranch, complaint.getBranch(), "The branch should match the test branch."));
//    }
//
//    @Test
//    void generateMonthlyComplaintReport_shouldRetrieveMonthlyComplaintsForBranch() {
//        List<Complaint> complaints = reportGenerator.generateMonthlyComplaintReport(testBranch, testMonth);
//        assertNotNull(complaints, "The complaint list should not be null.");
//        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
//        complaints.forEach(complaint -> assertEquals(testBranch, complaint.getBranch(), "The branch should match the test branch."));
//    }
//
//    @Test
//    void generateDailyChainPurchaseReport_shouldRetrieveDailyPurchasesForChain() {
//        List<Purchase> purchases = reportGenerator.generateDailyChainPurchaseReport(testChain, testDate);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain."));
//    }
//
//    @Test
//    void generateDailyChainPurchaseReport_withPurchaseType_shouldRetrieveSpecificTypeForChain() {
//        List<Purchase> purchases = reportGenerator.generateDailyChainPurchaseReport(testChain, testDate, PurchaseType.BOOKLET);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> {
//            assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain.");
//            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
//        });
//    }
//
//    @Test
//    void generateMonthlyChainPurchaseReport_shouldRetrieveMonthlyPurchasesForChain() {
//        List<Purchase> purchases = reportGenerator.generateMonthlyChainPurchaseReport(testChain, testMonth);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain."));
//    }
//
//    @Test
//    void generateMonthlyChainPurchaseReport_withPurchaseType_shouldRetrieveSpecificTypeForChain() {
//        List<Purchase> purchases = reportGenerator.generateMonthlyChainPurchaseReport(testChain, testMonth, PurchaseType.BOOKLET);
//        assertNotNull(purchases, "The purchase list should not be null.");
//        assertFalse(purchases.isEmpty(), "The purchase list should not be empty.");
//        purchases.forEach(purchase -> {
//            assertTrue(testChain.getBranches().contains(purchase.getBranch()), "The purchase branch should be part of the test chain.");
//            assertEquals(PurchaseType.BOOKLET, purchase.getPurchaseType(), "The purchase type should be BOOKLET.");
//        });
//    }
//
//    @Test
//    void generateDailyChainComplaintReport_shouldRetrieveDailyComplaintsForChain() {
//        List<Complaint> complaints = reportGenerator.generateDailyChainComplaintReport(testChain, testDate);
//        assertNotNull(complaints, "The complaint list should not be null.");
//        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
//        complaints.forEach(complaint -> assertTrue(testChain.getBranches().contains(complaint.getBranch()), "The complaint branch should be part of the test chain."));
//    }
//
//    @Test
//    void generateMonthlyChainComplaintReport_shouldRetrieveMonthlyComplaintsForChain() {
//        List<Complaint> complaints = reportGenerator.generateMonthlyChainComplaintReport(testChain, testMonth);
//        assertNotNull(complaints, "The complaint list should not be null.");
//        assertFalse(complaints.isEmpty(), "The complaint list should not be empty.");
//        complaints.forEach(complaint -> assertTrue(testChain.getBranches().contains(complaint.getBranch()), "The complaint branch should be part of the test chain."));
//    }
//
//    /**
//     * Creates and persists a test branch in the database.
//     *
//     * @return the created Branch instance.
//     */
//    private Branch createTestBranch() {
//        Transaction transaction = session.beginTransaction();
//        Branch branch = new Branch();
//        branch.setBranchName("Test Branch");
//        session.save(branch);
//        transaction.commit();
//        return branch;
//    }
//
//    /**
//     * Creates and persists a test chain with the specified branch in the database.
//     *
//     * @param branch the branch to be added to the chain.
//     * @return the created Chain instance.
//     */
//    private Chain createTestChain(Branch branch) {
//        Transaction transaction = session.beginTransaction();
//        Chain chain = new Chain();
//        chain.setName("Test Chain");
//        chain.getBranches().add(branch);
//        session.save(chain);
//        transaction.commit();
//        return chain;
//    }
//
//    /**
//     * Populates the database with test data.
//     */
//    private void populateTestData() {
//        Transaction transaction = session.beginTransaction();
//
//        // Creating test purchases
//        for (int i = 0; i < 5; i++) {
//            Purchase purchase = new Purchase();
//            purchase.setBranch(testBranch);
//            purchase.setPurchaseType(PurchaseType.BOOKLET);
//            purchase.setDateOfPurchase(LocalDateTime.of(testDate, LocalDateTime.now().toLocalTime()));
//            session.save(purchase);
//        }
//
//        // Creating test complaints
//        for (int i = 0; i < 5; i++) {
//            Complaint complaint = new Complaint();
//            complaint.setBranch(testBranch);
//            complaint.setDateOfComplaint(LocalDateTime.of(testDate, LocalDateTime.now().toLocalTime()));
//            complaint.setComplaintStatus("Closed");
//            session.save(complaint);
//        }
//
//        transaction.commit();
//    }
//
//    /**
//     * Clears the test data from the database.
//     */
//    private void clearTestData() {
//        Transaction transaction = session.beginTransaction();
//        session.createQuery("DELETE FROM Purchase").executeUpdate();
//        session.createQuery("DELETE FROM Complaint").executeUpdate();
//        session.createQuery("DELETE FROM Branch").executeUpdate();
//        session.createQuery("DELETE FROM Chain").executeUpdate();
//        transaction.commit();
//    }
//}
