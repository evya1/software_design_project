package il.cshaifasweng.OCSFMediatorExample.client.Reports;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;
import static il.cshaifasweng.OCSFMediatorExample.client.Reports.ReportsScreenConstants.ALL_BRANCHES;
import static il.cshaifasweng.OCSFMediatorExample.client.Reports.ReportsScreenConstants.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType.BRANCH_MANAGER;
import static il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType.CHAIN_MANAGER;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.*;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class ReportsScreenController implements ClientDependent, Initializable, AutoCloseable {

    private final ChartFactory chartFactory = new ChartFactory();
    @FXML
    public Button ExitBtn;
    @FXML
    public Button newBtn;
    @FXML
    public MenuItem closeBtn;
    @FXML
    public BorderPane chartBorderPane;
    @FXML
    public MenuItem showBarChartMenuItem;
    @FXML
    public MenuItem showPieChartMenuItem;
    @FXML
    public MenuItem updateRefreshBtn;
    @FXML
    public Button getReportBtn;
    @FXML
    public Button clearFiltersBtn;
    @FXML
    public DatePicker datePicker;
    @FXML
    public ComboBox<String> branchSelectionComboBox;
    @FXML
    public ComboBox<ReportType> reportTypeSelectionComboBox;
    @FXML
    public ComboBox<String> supportedSpanSelectionComboBox;
    @FXML
    public ComboBox<PurchaseType> purchaseTypeSelectionComboBox;
    private ReportsRequestHandler requestHandler;
    private Message localMessage;
    private SimpleClient client;
    private Employee employee;
    private String previousScreen;
    /**
     * Represents the context or scope for generating the chart.
     * This field can be used to determine the specific data or branch context
     * that will influence the content and title of the chart.
     * For example, it might represent a specific branch name,
     * a data filter criterion, or any other relevant context.
     */
    private Object chartContext;
    @FXML
    private TableView<Report> reportTableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);

        // Initialize UI components
        initializeComponents();

        // Set the chartBorderPane in the ChartFactory instance
        chartFactory.setChartBorderPane(chartBorderPane);

        // Display the PieChart by default on screen
        String contextDescription = chartFactory.getContextDescription(chartContext);
        chartFactory.prepareAndDisplayPieChart(contextDescription, chartBorderPane);

        Message localMessage = getLocalMessage();

        if (localMessage != null) {
            setEmployee(localMessage.getEmployee());
            setChartContext(chartContext);

            // Default fetch for Monthly Purchases of Booklet type
            fetchDefault();
        }
    }

    private void fetchDefault() {
        if (isNotInitialized(requestHandler, ERROR_MESSAGE_REQUEST_HANDLER_NOT_INITIALIZED)) return;

        Pair<Integer, Integer> yearAndMonth = getCurrentYearAndMonth();
        int year = yearAndMonth.getKey();
        int month = yearAndMonth.getValue();

        RequestData requestData = MessageUtilForReports.buildRequestData(
                FETCH_MONTHLY_REPORTS,
                REPORT_TYPE_A,
                Monthly,
                ALL_TYPES,
                employee,
                year,
                month
        );

        requestHandler.sendRequest(requestData);
    }

    @FXML
    void handleNewBtn(ActionEvent actionEvent) {
        requestReportData(actionEvent);
    }

    /**
     * Handles the action event to display a chart (either BarChart or PieChart) in the UI.
     * <p>
     * This method retrieves the context description based on the current chart context (e.g., branch name or all locations)
     * and uses the ChartFactory to create, configure, and display the chart in the specified BorderPane.
     * The chart type (BarChart or PieChart) is determined by the chartType parameter.
     * </p>
     *
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     * @param chartType   The type of chart to be displayed (e.g., "BarChart" or "PieChart").
     */
    private void handleShowChart(ActionEvent actionEvent, String chartType) {
        String contextDescription = chartFactory.getContextDescription(chartContext);
        if (BAR_CHART_TYPE.equals(chartType)) {
            chartFactory.prepareAndDisplayBarChart(contextDescription, chartBorderPane);
        } else if (PIE_CHART_TYPE.equals(chartType)) {
            chartFactory.prepareAndDisplayPieChart(contextDescription, chartBorderPane);
        }
    }

    /**
     * Handles the action event to display a BarChart in the UI.
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     */
    @FXML
    public void handleShowBarChart(ActionEvent actionEvent) {
        handleShowChart(actionEvent, BAR_CHART_TYPE);
    }

    /**
     * Handles the action event to display a PieChart in the UI.
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     */
    @FXML
    public void handleShowPieChart(ActionEvent actionEvent) {
        handleShowChart(actionEvent, PIE_CHART_TYPE);
    }

    @FXML
    public void handleCloseBtn(ActionEvent actionEvent) {
    }

    /**
     * Handles the action event to refresh chart data.
     * <p>
     * This method triggers a refresh of the chart data in the ChartFactory. The refreshed data
     * will automatically update any charts currently displayed in the UI.
     * </p>
     *
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     */
    @FXML
    public void handleUpdateData(ActionEvent actionEvent) {
        Node node = chartBorderPane.getCenter();

        if (node instanceof PieChart pieChart) {
            ObservableList<PieChart.Data> pieChartData = pieChart.getData();

            if (pieChartData.size() > 2) {
                PieChart.Data second = pieChartData.get(2);
                second.setPieValue(5000);   // Update the value for Product C
            }

        } else if (node instanceof BarChart<?, ?> barChart) {
            // Logic for handling BarChart updates should go here
        }
    }

    @Subscribe
    public void onRefreshChartDataEvent(RefreshChartDataEvent event) {
        if (!event.isEmpty()) {
            updateChartWithData(event.getReports());
        } else {
            System.out.println("No reports available for the selected criteria.");
        }
    }

    @Subscribe
    public void updateChartWithData(List<Report> reports) {
        Platform.runLater(() -> {
            if (reports != null && !reports.isEmpty()) {
                logReceivedData(reports);

                chartFactory.onReportDataReceived(new ReportDataReceivedEvent(reports, false, employee, employee.getBranch()));
                String contextDescription = chartFactory.getContextDescription(chartContext);

                // Display the PieChart with the received data
                chartFactory.prepareAndDisplayPieChart(contextDescription, chartBorderPane);
            } else {
                System.out.println("No data to display in chart.");
            }
        });
    }

    @Subscribe
    public void dataReceived(MessageEvent event) {
        Message message = event.getMessage();
        String messageContent = message.getMessage();
        System.out.println("ReportsScreenController: dataReceived: Message received: " + messageContent);

        Platform.runLater(() -> {
            List<Report> reports = message.getReports();
            if (reports != null && !reports.isEmpty()) {
                logReceivedData(reports);
                updateChartWithData(reports);
            } else {
                System.out.println("ReportsScreenController: dataReceived: No reports received.");
                SimpleClient.showAlert(INFORMATION, "No Reports", messageContent);
            }
        });
    }

    /**
     * Handles events when the chart data is updated.
     * <p>
     * This method listens for {@link ChartDataUpdatedEvent} and triggers the update of the charts in the UI.
     * </p>
     *
     * @param event The event that triggers the UI update.
     */
    @Subscribe
    public void onChartDataUpdatedEvent(ChartDataUpdatedEvent event) {
        String contextDescription = chartFactory.getContextDescription(event.getContextParameter());
        chartFactory.prepareAndDisplayBarChart(contextDescription, event.getChartBorderPane());
        chartFactory.prepareAndDisplayPieChart(contextDescription, event.getChartBorderPane());
    }

    /**
     * Handles the action event triggered by the user to request report data from the server.
     * This method uses the `ReportsRequestHandler` to send a request to fetch all reports.
     * If the request handler is not initialized, an error message is printed.
     *
     * @param event The `ActionEvent` triggered by the user's interaction (e.g., button click).
     */
    @FXML
    void requestReportData(ActionEvent event) {
        if (isNotInitialized(requestHandler, ERROR_MESSAGE_REQUEST_HANDLER_NOT_INITIALIZED)) return;

        Pair<Integer, Integer> yearAndMonth = getCurrentYearAndMonth();
        int year = yearAndMonth.getKey();
        int month = yearAndMonth.getValue();

        RequestData requestData = MessageUtilForReports.buildRequestData(
                FETCH_LAST_QUARTER_REPORT,
                ReportType.ALL_REPORT_TYPE,  // Example report type, change as needed
                Quarterly,
                ALL_TYPES,  // Assuming fetching all purchase types
                employee,
                year,
                month
        );

        requestHandler.sendRequest(requestData);
    }

    @FXML
    void chooseBranch(ActionEvent event) {
        handleSelectionChange(branchSelectionComboBox, "branch");
    }

    @FXML
    void chooseSupportedSpan(ActionEvent event) {
        handleSelectionChange(supportedSpanSelectionComboBox, "supported span");
    }

    @FXML
    void chooseReportType(ActionEvent event) {
        handleSelectionChange(reportTypeSelectionComboBox, "report type");
    }

    @FXML
    public void choosePurchaseType(ActionEvent actionEvent) {
        handleSelectionChange(purchaseTypeSelectionComboBox, "product");
        requestReportData(actionEvent); // Optionally trigger report request based on selected product
    }

    @FXML
    private <T> void handleSelectionChange(ComboBox<T> comboBox, String description) {
        T selectedValue = comboBox.getValue();
        if (selectedValue != null) {
            System.out.println("Selected " + description + ": " + selectedValue);
            // Add your logic to handle the selected value, e.g., filtering reports or triggering events
        }
    }

    @FXML
    void dateFilterPicker(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            System.out.println("Selected date: " + selectedDate);
            // Add your logic to filter reports by the selected date
        }
    }

    @FXML
    void clearFilters(ActionEvent event) {
        resetComboBoxPromptAndValue(reportTypeSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_REPORT_TYPE, ReportType.ALL_REPORT_TYPE);
        resetComboBoxPromptAndValue(branchSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_BRANCH, DEFAULT_SELECTION_OPTION_VALUE_FOR_BRANCH);
        resetComboBoxPromptAndValue(supportedSpanSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_SUPPORTED_SPAN, DEFAULT_SELECTION_OPTION_VALUE_FOR_SUPPORTED_SPAN);
        resetComboBoxPromptAndValue(purchaseTypeSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_PURCHASABLE, PurchaseType.ALL_TYPES);
        hideDatePicker();
    }

    @FXML
    void getReport(ActionEvent event) {
        if (isNotInitialized(requestHandler, ERROR_MESSAGE_REQUEST_HANDLER_NOT_INITIALIZED)) return;

        // Extracted local variables
        ReportType typeOfReport = getSelectedReportType();
        String branch = getSelectedBranch();
        LocalDate selectedDate = getSelectedDate();
        ReportSpanType spanType = getSelectedSpanType();
        PurchaseType purchaseType = getPurchasableTypes(typeOfReport);
        int year = getSelectedYear(selectedDate);
        int month = getSelectedMonth(selectedDate);

        // Build RequestData using MessageUtilForReports
        RequestData requestData = MessageUtilForReports.buildRequestData(
                getOperationType(branch, spanType),
                typeOfReport,
                spanType,
                purchaseType,
                employee,
                year,
                month
        );

        // Send the request
        requestHandler.sendRequest(requestData);

        System.out.println("Report request sent with type: " + typeOfReport + ", span: " + spanType + ", branch: " + branch + ", date: " + selectedDate);
    }

    private void showDatePicker() {
        datePicker.setVisible(true);
        datePicker.setEditable(true);
    }

    private void hideDatePicker() {
        datePicker.setVisible(false);
        datePicker.setEditable(false);
        datePicker.setValue(null); // Reset the selected date
    }

    private void initializeComponents() {
        initializeReportTypeSelectionComboBox();
        initializeSupportedSpanSelectionComboBox();
        initializeBranchSelectionComboBox();
        initializePurchasesTypesSelectionComboBox();
        initializeBranchSelectionComboBoxBasedOnEmployeeType();
        initializeDatePicker();
        setUpDatePickerVisibilityBasedOnSpanSelection();
    }

    private void setUpDatePickerVisibilityBasedOnSpanSelection() {
        supportedSpanSelectionComboBox.setOnAction(event -> {
            String selectedReportSpan = supportedSpanSelectionComboBox.getValue();
            if (ReportsScreenConstants.DAILY_REPORT.equals(selectedReportSpan)) {
                showDatePicker();
            } else {
                hideDatePicker();
            }
        });
    }

    private void initializeReportTypeSelectionComboBox() {
        // Populate ComboBox with ReportType enum values directly
        reportTypeSelectionComboBox.getItems().addAll(ReportType.values());
        // Set the default selection to ALL_REPORT_TYPE
        resetComboBoxPromptAndValue(reportTypeSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_REPORT_TYPE, ALL_REPORT_TYPE);
    }

    private void initializeSupportedSpanSelectionComboBox() {
        supportedSpanSelectionComboBox.getItems().addAll(DAILY_REPORT, MONTHLY_REPORT, QUARTERLY_REPORT, YEARLY_REPORT);
        resetComboBoxPromptAndValue(supportedSpanSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_SUPPORTED_SPAN, DEFAULT_SELECTION_OPTION_VALUE_FOR_SUPPORTED_SPAN);
    }

    private void initializeBranchSelectionComboBox() {
        branchSelectionComboBox.getItems().addAll(ALL_BRANCHES, BRANCH_A, BRANCH_B);
        resetComboBoxPromptAndValue(branchSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_BRANCH, DEFAULT_SELECTION_OPTION_VALUE_FOR_BRANCH);
    }

    private void initializePurchasesTypesSelectionComboBox() {
        purchaseTypeSelectionComboBox.getItems().addAll(PurchaseType.values());
        resetComboBoxPromptAndValue(branchSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_PURCHASABLE, DEFAULT_SELECTION_OPTION_VALUE_FOR_PURCHASABLE);
    }

    private void initializeBranchSelectionComboBoxBasedOnEmployeeType() {
        if (employee != null && employee.getEmployeeType() == CHAIN_MANAGER) {
            ObservableList<String> comboBoxItems = branchSelectionComboBox.getItems();
            comboBoxItems.clear();
            List<String> branchNames = new ArrayList<>();
            branchNames.add(employee.getBranchInCharge().getBranchName());
            branchSelectionComboBox.setVisible(true);
            resetComboBoxPromptAndValue(branchSelectionComboBox,
                    DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_BRANCH,
                    branchNames.getFirst());
        } else {
            branchSelectionComboBox.setVisible(false);
        }
    }

    private void initializeDatePicker() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && !empty) {
                    // Example: Highlight weekends with a different background color
                    if (date.getDayOfWeek() == SATURDAY || date.getDayOfWeek() == SUNDAY) {
                        setStyle("-fx-background-color: lightgray;");
                    }
                }
            }
        });
        datePicker.setVisible(false);
        datePicker.setEditable(false);
    }

    private <T> void resetComboBoxPromptAndValue(ComboBox<T> comboBox, String promptText, T value) {
        comboBox.setPromptText(promptText);
        comboBox.setValue(value);
    }

    @FXML
    public void handleBackAction(ActionEvent actionEvent) {
        if (isNotInitialized(client, ERROR_MESSAGE_CLIENT_NOT_INITIALIZED)) return;
        try {
            if (actionEvent.getSource() instanceof Node) { // Check if the event source can be cast to a Node
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Message message = new Message();
                message.setMessage("Back to " + previousScreen.replace("_SCREEN", "").replace("_", " ").toLowerCase() + " screen");
                message.setSourceFXML(REPORTS_SCREEN);
                message.setEmployee(getLocalMessage().getEmployee());
                EventBus.getDefault().unregister(this);
                client.moveScene(previousScreen, stage, message);
            } else {
                System.err.println("Action event source is not a Node, cannot retrieve the stage.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * Sets the `SimpleClient` instance to be used by this controller and initializes
     * the `ReportsRequestHandler` with the provided client. This ensures that the controller
     * can send requests and handle responses through the request handler.
     *
     * @param client The `SimpleClient` instance to be associated with this controller.
     */
    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
        // Use the singleton instance and initialize it with the client
        this.requestHandler = ReportsRequestHandler.getInstance();
        this.requestHandler.initialize(client);
    }

    @Override
    public void setMessage(Message message) {
        setLocalMessage(message);
        this.previousScreen = message.getSourceFXML();  // Store the previous screen's FXML path
        Message localMessage = getLocalMessage();
        if (localMessage != null) {
            setEmployee(localMessage.getEmployee());
        }
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Message getLocalMessage() {
        return localMessage;
    }

    public void setLocalMessage(Message localMessage) {
        this.localMessage = localMessage;
    }

    public Object getChartContext() {
        return chartContext;
    }

    /**
     * Sets the chart context based on the employee's role within the organization.
     * <p>
     * If the employee is a Branch Manager, the context is set to the employee's branch.
     * If the employee is a Chain Manager, the context is set to represent all branches.
     * If the employee is null or has a different role, the context is set to null.
     * </p>
     *
     * @param chartContext The initial context parameter (can be null or an object).
     *                     This value will be overridden based on the employee's role.
     */
    public void setChartContext(Object chartContext) {
        if (employee != null) {
            EmployeeType type = employee.getEmployeeType();
            if (type == BRANCH_MANAGER) {
                this.chartContext = employee.getBranch();
            } else if (type == CHAIN_MANAGER) {
                this.chartContext = CHAIN_MANAGER;
            }
        } else {
            this.chartContext = null;
        }
    }

    private ReportSpanType getTimeSpan(String reportSpan) {
        if (DAILY_REPORT.equals(reportSpan)) {
            return Daily;
        } else if (MONTHLY_REPORT.equals(reportSpan)) {
            return Monthly;
        } else if (QUARTERLY_REPORT.equals(reportSpan)) {
            return Quarterly;
        } else if (YEARLY_REPORT.equals(reportSpan)) {
            return Yearly;
        } else {
            return DEFAULT_SUPPORTED_SPAN_TYPE_FALLBACK;
        }
    }

    private PurchaseType getPurchasableTypes(ReportType reportType) {
        if (REPORT_TYPE_A.equals(reportType)) {
            return MOVIE_TICKET;
        } else if (REPORT_TYPE_B.equals(reportType)) {
            return BOOKLET;
        } else if (ALL_REPORT_TYPE.equals(reportType)) {
            return ALL_TYPES;
        } else {
            return DEFAULT_PURCHASABLE_TYPE_FALLBACK;
        }
    }

    private String getOperationType(String branch, ReportSpanType spanType) {
        if (ALL_BRANCHES.equals(branch)) {
            if (spanType == Daily) {
                return FETCH_ALL_REPORTS;
            } else if (spanType == Monthly) {
                return FETCH_MONTHLY_REPORTS;
            } else if (spanType == Quarterly) {
                return FETCH_LAST_QUARTER_REPORT;
            } else if (spanType == Yearly) {
                return FETCH_YEARLY_REPORTS;
            } else {
                return INVALID_LABEL;
            }
        } else {
            return FETCH_BRANCH_REPORTS;
        }
    }

    // Extracted helper methods
    private <T> T getSelectedValue(ComboBox<T> comboBox) {
        return comboBox.getValue();
    }

    private String getSelectedBranch() {
        return getSelectedValue(branchSelectionComboBox);
    }

    private LocalDate getSelectedDate() {
        return datePicker.getValue();  // Use the DatePicker's getValue() method directly
    }

    private ReportSpanType getSelectedSpanType() {
        String span = getSelectedValue(supportedSpanSelectionComboBox);
        return getTimeSpan(span);
    }

    private int getSelectedDatePart(LocalDate selectedDate, Function<LocalDate, Integer> datePartExtractor, int defaultValue) {
        return (selectedDate != null) ? datePartExtractor.apply(selectedDate) : defaultValue;
    }

    private int getSelectedDateValue(LocalDate selectedDate, Function<LocalDate, Integer> datePartExtractor) {
        return (selectedDate != null) ? datePartExtractor.apply(selectedDate) : datePartExtractor.apply(LocalDate.now());
    }

    private int getSelectedYear(LocalDate selectedDate) {
        return getSelectedDateValue(selectedDate, LocalDate::getYear);
    }

    private int getSelectedMonth(LocalDate selectedDate) {
        return getSelectedDateValue(selectedDate, LocalDate::getMonthValue);
    }

    private ReportType getSelectedReportType() {
        ReportType selectedReportType = reportTypeSelectionComboBox.getValue();
        System.out.println("Selected Report Type: " + selectedReportType);
        return selectedReportType;
    }

    private boolean isNotInitialized(Object handler, String errorMessage) {
        if (handler == null) {
            System.err.println(errorMessage + "\n");
            return true;
        }
        return false;
    }

    private Pair<Integer, Integer> getCurrentYearAndMonth() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        return new Pair<>(year, month);
    }

    private void logReceivedData(List<Report> reports) {
        System.out.println("Received Reports Data:");
        for (Report report : reports) {
            System.out.println("Report ID: " + report.getId());
            System.out.println("Branch: " + (report.getBranch() != null ? report.getBranch().getBranchName() : "N/A"));
            System.out.println("Report Type: " + report.getReportType());
            System.out.println("Report Data:");
            report.getDataForGraphs().forEach((key, value) -> {
                System.out.println(key + ": " + value);
            });
        }
    }

}
