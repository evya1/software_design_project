package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;

import java.time.Month;
import java.util.Map;

public record RequestData(
        String requestType,
        ReportType reportType,
        Employee employee,
        Branch branch,
        ReportSpanType reportSpanType,
        Month month,
        int year,
        PurchaseType purchaseType,
        String label,
        String details,
        Map<String, Double> dataForGraphs,
        String serializedReportData
) {}
