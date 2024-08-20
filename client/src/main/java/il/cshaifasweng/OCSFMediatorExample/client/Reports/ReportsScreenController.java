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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;
import static il.cshaifasweng.OCSFMediatorExample.client.Reports.ReportsScreenConstants.ALL_BRANCHES;
import static il.cshaifasweng.OCSFMediatorExample.client.Reports.ReportsScreenConstants.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType.BRANCH_MANAGER;
import static il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType.CHAIN_MANAGER;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportSpanType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportType.*;
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
    public ComboBox branchSelectionComboBox;
    @FXML
    public ComboBox reportTypeSelectionComboBox;
    @FXML
    public ComboBox supportedSpanSelectionComboBox;
    @FXML
    public ComboBox productSelectionComboBox;
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

        Message localMessage = getLocalMessage();

        if (localMessage != null) {
            setEmployee(localMessage.getEmployee());
            setChartContext(chartContext);

            // Default fetch for Monthly Purchases of Booklet type
            fetchDefaultMonthlyBookletPurchases();
        }
    }

    private void fetchDefaultMonthlyBookletPurchases() {
        if (requestHandler == null) {
            System.err.println("Request Handler is not initialized!\n");
            return;
        }

        // Fetch for Monthly Purchases of "Booklet" type
        ReportSpanType spanType = ReportSpanType.Monthly;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        RequestData requestData = MessageUtilForReports.buildRequestData(
                FETCH_MONTHLY_REPORTS,
                REPORT_TYPE_A,
                spanType,
                PurchaseType.BOOKLET,
                employee,
                year,
                month
        );

        requestHandler.sendRequest(requestData);
    }

    @FXML
    void requestReportData() {
        if (requestHandler == null) {
            System.err.println("Request Handler is not initialized!\n");
            return;
        }
        // Assuming `employee` and other necessary fields are available
        ReportSpanType spanType = Monthly;
        int year = LocalDate.now().getYear();  // Example of using the current year
        int month = LocalDate.now().getMonthValue();  // Example of using the current month

        RequestData requestData = MessageUtilForReports.buildRequestData(
                FETCH_MONTHLY_REPORTS,
                ReportType.ALL_REPORT_TYPE,  // Example report type, change as needed
                spanType,
                ALL_TYPES,  // Assuming fetching all purchase types
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
     * Handles the action event to display a BarChart in the UI.
     * <p>
     * This method retrieves the context description based on the current chart context (e.g., branch name or all locations)
     * and uses the ChartFactory to create, configure, and display the BarChart in the specified BorderPane.
     * </p>
     *
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     */
    @FXML
    public void handleShowBarChart(ActionEvent actionEvent) {
        String contextDescription = chartFactory.getContextDescription(chartContext);
        chartFactory.prepareAndDisplayBarChart(contextDescription, chartBorderPane);
    }

    /**
     * Handles the action event to display a PieChart in the UI.
     * <p>
     * This method retrieves the context description based on the current chart context (e.g., branch name or all locations)
     * and uses the ChartFactory to create, configure, and display the PieChart in the specified BorderPane.
     * </p>
     *
     * @param actionEvent The action event triggered by user interaction, such as clicking a button.
     */
    @FXML
    public void handleShowPieChart(ActionEvent actionEvent) {
        String contextDescription = chartFactory.getContextDescription(chartContext);
        chartFactory.prepareAndDisplayPieChart(contextDescription, chartBorderPane);
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
//        EventBus.getDefault().post(new RefreshChartDataEvent());

        Node node = chartBorderPane.getCenter();
        if (node instanceof PieChart pc) {

            PieChart.Data second = pc.getData().get(2);
            second.setPieValue(5000);   // Product C

        } else if (node instanceof BarChart bc) {
            //            BarChart.
        }
    }

    /**
     * Receives events when chart data needs to be updated and refreshes the chart data.
     * <p>
     * This method listens for {@link RefreshChartDataEvent} and refreshes the data in the `ChartFactory`.
     * The updated data will automatically be reflected in the displayed charts.
     * </p>
     *
     * @param event The event that triggers the chart data refresh.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshChartDataEvent(RefreshChartDataEvent event) {
        if (!event.isEmpty()) {
            updateChartWithData(event.getReports());
        } else {
            System.out.println("No reports available for the selected criteria.");
        }
    }

    @FXML
    @Subscribe(threadMode = ThreadMode.MAIN)
    private void updateChartWithData(List<Report> reports) {
        // Pass the reports data to ChartFactory
        chartFactory.onReportDataReceived(new ReportDataReceivedEvent(reports, false, employee, employee.getBranch()));

        // Update the UI with the new data using ChartFactory
        String contextDescription = chartFactory.getContextDescription(chartContext);
        chartFactory.prepareAndDisplayBarChart(contextDescription, chartBorderPane);
        chartFactory.prepareAndDisplayPieChart(contextDescription, chartBorderPane);
    }

    public void onReportDataReceived(ReportDataReceivedEvent event) {
        EventBus.getDefault().post(new ChartDataUpdatedEvent(chartContext, true, // Or false, depending on logic
                event.getReports().get(0), "DataKeyExample", // Update with actual data key
                chartBorderPane));
    }

    /**
     * Handles events when the chart data is updated.
     * <p>
     * This method listens for {@link ChartDataUpdatedEvent} and triggers the update of the charts in the UI.
     * </p>
     *
     * @param event The event that triggers the UI update.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
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
        if (requestHandler == null) {
            System.err.println("Request Handler is not initialized!\n");
            return;
        }

        // Assuming `employee` and other necessary fields are available
        ReportSpanType spanType = Quarterly;
        int year = LocalDate.now().getYear();  // Example of using the current year
        int month = LocalDate.now().getMonthValue();  // Example of using the current month

        RequestData requestData = MessageUtilForReports.buildRequestData(
                FETCH_LAST_QUARTER_REPORT,
                ReportType.ALL_REPORT_TYPE,  // Example report type, change as needed
                spanType,
                ALL_TYPES,  // Assuming fetching all purchase types
                employee,
                year,
                month
        );

        requestHandler.sendRequest(requestData);
    }

    /**
     * Subscribes to `MessageEvent` events posted on the EventBus and processes the received messages.
     * This method handles the response containing report data by updating the `TableView` in the UI.
     * If no reports are found, an informational alert is displayed to the user.
     *
     * @param event The `MessageEvent` containing the message with report data received from the server.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dataReceived(MessageEvent event) {
        Message message = event.getMessage();
        if (REPORT_DATA_RESPONSE.equals(message.getMessage())) {
            Platform.runLater(() -> {
                List<Report> reports = message.getReports();
                if (reports != null && !reports.isEmpty()) {
                    EventBus.getDefault().post(new ReportDataReceivedEvent(reports, false, employee, employee.getBranch()));
                } else {
                    SimpleClient.showAlert(INFORMATION, "No Reports", "There are no reports available.");
                }
            });
        }
    }

    @FXML
    void chooseBranch(ActionEvent event) {
        // Logic to filter or apply selection based on selected branch
        String selectedBranch = branchSelectionComboBox.getValue().toString();
        System.out.println("Selected branch: " + selectedBranch);
        // Add your logic to handle the selected branch
    }

    @FXML
    void chooseSupportedSpan(ActionEvent event) {
        // Logic to filter or apply selection based on report type
        String selectedReportType = supportedSpanSelectionComboBox.getValue().toString();
        System.out.println("Selected report type: " + selectedReportType);
        // Add your logic to handle the selected report type
    }

    @FXML
    void chooseReportType(ActionEvent event) {
        // Logic to filter or apply selection based on report span
        String selectedReportSpan = reportTypeSelectionComboBox.getValue().toString();
        System.out.println("Selected report span: " + selectedReportSpan);
        // Add your logic to handle the selected report span
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
        resetComboBoxPromptAndValue(reportTypeSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_REPORT_TYPE, DEFAULT_SELECTION_OPTION_VALUE_FOR_REPORT_TYPE);
        resetComboBoxPromptAndValue(branchSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_BRANCH, DEFAULT_SELECTION_OPTION_VALUE_FOR_BRANCH);
        resetComboBoxPromptAndValue(supportedSpanSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_SUPPORTED_SPAN, DEFAULT_SELECTION_OPTION_VALUE_FOR_SUPPORTED_SPAN);
        hideDatePicker(); // Reset the date picker
    }

    @FXML
    public void chooseProduct(ActionEvent actionEvent) {
    }

    @FXML
    void getReport(ActionEvent event) {
        if (requestHandler == null) {
            System.err.println("Request Handler is not initialized!\n");
            return;
        }

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
        initializeBranchSelectionComboBoxBasedOnEmployeeType();
        initializeDatePicker();
        setUpDatePickerVisibilityBasedOnSpanSelection();
    }

    private void setUpDatePickerVisibilityBasedOnSpanSelection() {
        supportedSpanSelectionComboBox.setOnAction(event -> {
            String selectedReportSpan = supportedSpanSelectionComboBox.getValue().toString();
            if (ReportsScreenConstants.DAILY_REPORT.equals(selectedReportSpan)) {
                showDatePicker();
            } else {
                hideDatePicker();
            }
        });
    }

    private void initializeReportTypeSelectionComboBox() {
        reportTypeSelectionComboBox.getItems().addAll(ReportType.REPORT_TYPE_A, ReportType.REPORT_TYPE_B, ReportType.ALL_REPORT_TYPE);
        resetComboBoxPromptAndValue(reportTypeSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_REPORT_TYPE, ReportType.ALL_REPORT_TYPE);
    }

    private void initializeSupportedSpanSelectionComboBox() {
        supportedSpanSelectionComboBox.getItems().addAll(DAILY_REPORT, MONTHLY_REPORT, QUARTERLY_REPORT, YEARLY_REPORT);
        resetComboBoxPromptAndValue(supportedSpanSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_SUPPORTED_SPAN, DEFAULT_SELECTION_OPTION_VALUE_FOR_SUPPORTED_SPAN);
    }

    private void initializeBranchSelectionComboBox() {
        branchSelectionComboBox.getItems().addAll(ALL_BRANCHES, BRANCH_A, BRANCH_B);
        resetComboBoxPromptAndValue(branchSelectionComboBox, DEFAULT_SELECTION_OPTION_PROMPT_TEXT_FOR_BRANCH, DEFAULT_SELECTION_OPTION_VALUE_FOR_BRANCH);
    }

    private void initializeBranchSelectionComboBoxBasedOnEmployeeType() {
        if (employee != null && employee.getEmployeeType() == CHAIN_MANAGER) {
            ObservableList comboBoxItems = branchSelectionComboBox.getItems();
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
                    // Apply custom logic for date styling
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
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
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
        this.requestHandler = new ReportsRequestHandler(client);
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
        if (ReportsScreenConstants.DAILY_REPORT.equals(reportSpan)) {
            return Daily;
        } else if (ReportsScreenConstants.MONTHLY_REPORT.equals(reportSpan)) {
            return Monthly;
        } else if (ReportsScreenConstants.QUARTERLY_REPORT.equals(reportSpan)) {
            return Quarterly;
        } else if (ReportsScreenConstants.YEARLY_REPORT.equals(reportSpan)) {
            return ReportSpanType.Yearly;
        } else {
            return Monthly; // Default fallback
        }
    }

    private PurchaseType getPurchasableTypes(ReportType reportType) {
        if (REPORT_TYPE_A.getValue().equals(reportType)) {
            return MOVIE_TICKET;
        } else if (REPORT_TYPE_B.getValue().equals(reportType)) {
            return BOOKLET;
        } else if (ALL_REPORT_TYPE.getValue().equals(reportType)) {
            return ALL_TYPES;
        } else {
            return ALL_TYPES; // Default fallback
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
        return getSelectedValue(branchSelectionComboBox).toString();
    }

    private LocalDate getSelectedDate() {
        return datePicker.getValue();  // Use the DatePicker's getValue() method directly
    }

    private ReportSpanType getSelectedSpanType() {
        String span = getSelectedValue(supportedSpanSelectionComboBox).toString();
        return getTimeSpan(span);
    }

    private int getSelectedYear(LocalDate selectedDate) {
        return (selectedDate != null) ? selectedDate.getYear() : LocalDate.now().getYear();
    }

    private int getSelectedMonth(LocalDate selectedDate) {
        return (selectedDate != null) ? selectedDate.getMonthValue() : LocalDate.now().getMonthValue();
    }

    private ReportType getSelectedReportType() {
        return (ReportType) reportTypeSelectionComboBox.getValue();
    }

}
