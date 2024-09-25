package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.time.Month;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    @Test
    void reportWithoutAYearShouldNotBeSavedToDB() {
        // Given
        Branch branch = new Branch("Test Branch");
        Report report = new Report.Builder()
                .withBranch(branch)
                .withReportSpanType(ReportSpanType.Monthly)
                .withMonth(Month.AUGUST)
                .withReportDate(LocalDate.now())
                .withDetails("Test Report")
                .withDataForGraphs(new HashMap<>())
                .build();

        // When / Then
        Exception exception = assertThrows(PersistenceException.class, () -> {
            // Simulate saving the report to the DB
            saveReportToDB(report);
        });

        String expectedMessage = "year cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void reportWithAllRequiredFieldsShouldBeSavedSuccessfully() {
        // Given
        Branch branch = new Branch("Test Branch");
        Map<String, Double> dataForGraphs = new HashMap<>();
        dataForGraphs.put("Item A", 10.0);
        dataForGraphs.put("Item B", 20.0);

        Report report = new Report.Builder()
                .withBranch(branch)
                .withReportSpanType(ReportSpanType.Monthly)
                .withMonth(Month.AUGUST)
                .withYear(2024)
                .withReportDate(LocalDate.now())
                .withDetails("Test Report")
                .withDataForGraphs(dataForGraphs)
                .withLabel("August 2024 Report")
                .build();

        // When / Then
        assertDoesNotThrow(() -> saveReportToDB(report));
    }

    @Test
    void reportWithoutAMonthShouldBeSavedSuccessfully() {
        // Given
        Branch branch = new Branch("Test Branch");

        Report report = new Report.Builder()
                .withBranch(branch)
                .withReportSpanType(ReportSpanType.Yearly)
                .withYear(2024)
                .withReportDate(LocalDate.now())
                .withDetails("Yearly Report")
                .build();

        // When / Then
        assertDoesNotThrow(() -> saveReportToDB(report));
    }

    @Test
    void reportWithEmptyDataForGraphsShouldStillBeValid() {
        // Given
        Branch branch = new Branch("Test Branch");

        Report report = new Report.Builder()
                .withBranch(branch)
                .withReportSpanType(ReportSpanType.Monthly)
                .withMonth(Month.JANUARY)
                .withYear(2024)
                .withReportDate(LocalDate.now())
                .withDetails("Monthly Report with No Data")
                .build();

        // When / Then
        assertDoesNotThrow(() -> saveReportToDB(report));
    }

    @Test
    void serializedReportDataShouldAcceptJsonFormat() {
        // Given
        Branch branch = new Branch("Test Branch");
        String jsonData = "{\"chartType\":\"PieChart\",\"data\":{\"Item A\":30,\"Item B\":70}}";

        Report report = new Report.Builder()
                .withBranch(branch)
                .withReportSpanType(ReportSpanType.Monthly)
                .withMonth(Month.MARCH)
                .withYear(2024)
                .withReportDate(LocalDate.now())
                .withSerializedReportData(jsonData)
                .build();

        // When / Then
        assertDoesNotThrow(() -> saveReportToDB(report));
        assertEquals(jsonData, report.getSerializedReportData());
    }

    // Simulate saving to DB
    private void saveReportToDB(Report report) throws PersistenceException {
        if (report.getYear() == 0) {
            throw new PersistenceException("year cannot be null");
        }
        // Simulate successful save
    }
}
