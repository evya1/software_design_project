package il.cshaifasweng.OCSFMediatorExample.client.Reports;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType;

import static il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.ALL_REPORT_TYPE;

public class ReportsScreenConstants {

    // Report Types
    // Constants related to the selection of report types in the UI, providing default values and prompts.
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_REPORT_TYPE = ALL_REPORT_TYPE.getValue();
    public static final String DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_REPORT_TYPE = "Select Report Type";

    // Report Span Types
    // Constants representing different span types for reports (e.g., Daily, Monthly) and default options for UI selection.
    public static final String DAILY_REPORT = Daily + " Report";
    public static final String MONTHLY_REPORT = Monthly + " Report";
    public static final String QUARTERLY_REPORT = Quarterly + " Report";
    public static final String YEARLY_REPORT = Yearly + " Report";
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_SUPPORTED_SPAN = MONTHLY_REPORT;
    public static final String DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_SUPPORTED_SPAN = "Select Report Span";
    public static final ReportSpanType DEFAULT_SUPPORTED_SPAN_TYPE_FALLBACK = Monthly;

    // Branches
    // Constants related to branch selection for reports, including predefined branch names and default UI prompts.
    public static final String ALL_BRANCHES = ReportOperationTypes.ALL_BRANCHES;
    public static final String BRANCH_A = "Branch A";
    public static final String BRANCH_B = "Branch B";
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_BRANCH = ALL_BRANCHES;
    public static final String DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_BRANCH = "Select a Branch or all Branches";

    // Products
    // Constants related to the selection of purchasable product types in the UI, providing options for different products.
    public static final String PURCHASABLE_PRODUCT_A = MOVIE_LINK.toString();
    public static final String PURCHASABLE_PRODUCT_B = MOVIE_TICKET.toString();
    public static final String PURCHASABLE_PRODUCT_C = BOOKLET.toString();
    public static final String ALL_PURCHASABLE_PRODUCTS = "All Products";
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_PURCHASABLE = ALL_PURCHASABLE_PRODUCTS;
    public static final String DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_PURCHASABLE = "Select a Product type";

    // Employee Types
    // Constants for employee types such as Branch Manager and Chain Manager, used in the UI for employee role selection.
    public static final String BRANCH_MANAGER = EmployeeType.BRANCH_MANAGER.toString();
    public static final String CHAIN_MANAGER = EmployeeType.CHAIN_MANAGER.toString();

    // Default fallback for PurchaseType
    // Default value for PurchaseType selection, falling back to ALL_TYPES.
    public static final PurchaseType DEFAULT_PURCHASABLE_TYPE_FALLBACK = ALL_TYPES;

    // Error Messages
    // Displayed when specific objects like Request Handler or Client are not initialized.
    protected static final String ERROR_MESSAGE_REQUEST_HANDLER_NOT_INITIALIZED = "Request Handler is not initialized!";
    protected static final String ERROR_MESSAGE_CLIENT_NOT_INITIALIZED = "Client is not initialized!";

    // Chart Types
    // Constants representing different types of charts used in the UI (e.g., BarChart, PieChart).
    protected static final String BAR_CHART_TYPE = "BarChart";
    protected static final String PIE_CHART_TYPE = "PieChart";

}
