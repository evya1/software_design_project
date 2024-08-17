package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportDataTest {

    @Test
    void reportDataShouldBeCreatedSuccessfully() {
        // Given
        Report report = new Report();
        ReportData reportData = new ReportData("Item A", 50.0, report);

        // When / Then
        assertEquals("Item A", reportData.getLabel());
        assertEquals(50.0, reportData.getValue());
        assertEquals(report, reportData.getReport());
    }

    @Test
    void reportDataWithoutLabelShouldThrowException() {
        // Given
        Report report = new Report();

        // When / Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ReportData(null, 50.0, report);
        });

        String expectedMessage = "Label and value cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void reportDataWithoutValueShouldThrowException() {
        // Given
        Report report = new Report();

        // When / Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ReportData("Item A", null, report);
        });

        String expectedMessage = "Label and value cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void reportDataShouldBeLinkedToReport() {
        // Given
        Report report = new Report();
        ReportData reportData = new ReportData("Item A", 50.0, report);

        // When / Then
        assertEquals(report, reportData.getReport());
    }
}
