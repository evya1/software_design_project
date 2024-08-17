package il.cshaifasweng.OCSFMediatorExample.client.Reports;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType;

import static il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.*;

public class ReportsScreenConstants {

    // Report Types
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_REPORT_TYPE = String.valueOf(ReportType.ALL_REPORT_TYPE);
    public static final String DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_REPORT_TYPE = "Select Report Type";

    // Report Spans Types
    public static final String DAILY_REPORT = Daily + " Report";
    public static final String MONTHLY_REPORT = Monthly + " Report";
    public static final String QUARTERLY_REPORT = Quarterly + " Report";
    public static final String YEARLY_REPORT = Yearly + " Report";
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_SUPPORTED_SPAN = MONTHLY_REPORT;
    public static final String DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_SUPPORTED_SPAN = "Select Report Span";

    // Branches
    public static final String ALL_BRANCHES = ReportOperationTypes.ALL_BRANCHES;
    public static final String BRANCH_A = "Branch A";
    public static final String BRANCH_B = "Branch B";
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_BRANCH = ALL_BRANCHES;
    public static final String DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_BRANCH = "Select a Branch or all Branches";

    // Products
    public static final String PURCHASABLE_PRODUCT_A = MOVIE_LINK.toString();
    public static final String PURCHASABLE_PRODUCT_B = MOVIE_TICKET.toString();
    public static final String PURCHASABLE_PRODUCT_C = BOOKLET.toString();
    public static final String ALL_PURCHASABLE_PRODUCTS = "All Products";
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_PURCHASABLE = ALL_BRANCHES;

    // Employee Types
    public static final String BRANCH_MANAGER = EmployeeType.BRANCH_MANAGER.toString();
    public static final String CHAIN_MANAGER = EmployeeType.CHAIN_MANAGER.toString();
}
