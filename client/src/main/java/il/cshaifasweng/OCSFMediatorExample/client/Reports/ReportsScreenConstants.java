package il.cshaifasweng.OCSFMediatorExample.client.Reports;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType;

import static il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.ALL_REPORT_TYPE;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.REPORT_TYPE_A;

public class ReportsScreenConstants {

    // Report Types
    // Constants related to the selection of report types in the UI, providing default values and prompts.
    public static final String DEFAULT_SELECTION_OPTION_VALUE_FOR_REPORT_TYPE = REPORT_TYPE_A.getValue();
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
    public static final String ALL_PURCHASABLE_PRODUCTS = ALL_TYPES.toString();
    public static final PurchaseType DEFAULT_SELECTION_OPTION_VALUE_FOR_PURCHASABLE = ALL_TYPES;
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

    public static final double DEFAULT_PRODUCT_COUNT = 1.0;

    // Chart Colors
    // Constants representing the CSS classes for different product types in the chart.
    public static final String CHART_CSS_CLASS_PRODUCT_A = "default-color0";
    public static final String CHART_CSS_CLASS_PRODUCT_B = "default-color1";
    public static final String CHART_CSS_CLASS_PRODUCT_C = "default-color2";
    public static final String CHART_CSS_CLASS_COMPLAINTS = "default-color3";
    public static final String CHART_CSS_CLASS_DEFAULT_PRODUCT = "default-product";

    public static final String REPORTS_STYLE_PATH = "il/cshaifasweng/OCSFMediatorExample/client/reportsScreen/reportsStyle.css";

    // Constants for table column names (generic)
    public static final String COLUMN_A_TEXT = "Product";
    public static final String COLUMN_B_TEXT = "Report";
    public static final String COLUMN_C_TEXT = "Branch";
    public static final String COLUMN_D_TEXT = "Amount";

    // Constants representing property fields for the TableView columns (binding data fields from the Report entity)
    public static final String REPORT_TABLE_COLUMN_A_PROPERTY_FIELD_PRODUCT_TYPE = "label";
    public static final String REPORT_TABLE_COLUMN_B_PROPERTY_FIELD_REPORT_TYPE = "reportType";
    public static final String REPORT_TABLE_COLUMN_C_PROPERTY_FIELD_BRANCH = "branch";
    public static final String REPORT_TABLE_COLUMN_D_PROPERTY_FIELD_AMOUNT = "amount";

    public static final String NEW_PURCHASE_MESSAGE = "New Purchase";
    public static final int DEFAULT_PURCHASED_AMOUNT_FOR_BOOKLET_AND_MOVIE_LINK = 1;

    public static final String PURCHASE_SERIES_NAME = "Purchases";
    public static final String COMPLAINT_SERIES_NAME = "Complaints";
    public static final String DATE_FORMAT_PATTERN = "MM-dd";

    // Chart label style constants
    public static final String CHART_LABEL_CSS_CLASS = "chart-data-label";

    // Label offsets for positioning above bars
    public static final double LABEL_HORIZONTAL_OFFSET_RATIO = 0.5; // Center horizontally
    public static final double LABEL_VERTICAL_OFFSET_RATIO = 0.5; // Move vertically by this ratio

    // Chart label layout ratios (for centering labels correctly)
    public static final double LABEL_CENTERING_X_RATIO = 2.0; // Used to calculate the center horizontally
    public static final double LABEL_CENTERING_Y_RATIO = 0.5; // Vertical positioning adjustment

    // Chart label font size
    public static final String CHART_LABEL_FONT_SIZE = "12"; // Font size for labels
}
