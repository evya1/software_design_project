package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.ClientRequests.*;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.addTextInputListener;
import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.changeControlBorderColor;

public class AdminPanelController implements ClientDependent {

    //region SideBar buttons
    @FXML
    public Button homeScreenBtn;
    @FXML
    public Button newEmployeeBtn;
    @FXML
    public Button newBranchBtn;
    @FXML
    public Button newTheaterBtn;
    //endregion

    @FXML
    public Label notificationLabel;

    //region Employee Admin
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab modifyEmployeeTab;
    @FXML
    public Tab newEmployeeTab;

    @FXML
    public Button createEmployeeBtn;
    @FXML
    public Button createBranchBtn;

    @FXML
    public CheckComboBox<Employee> branchManagerCCB;

    @FXML
    private TitledPane newEmployeeAccordion;
    @FXML
    private TitledPane newBranchAccordion;
    @FXML
    private TitledPane newTheaterAccordion;

    @FXML
    public ComboBox<Branch> branchComboBox;
    @FXML
    public ComboBox<EmployeeType> employeeTypeComboBox;
    @FXML
    public ComboBox<Branch> branchWorkerCombobox;

    @FXML
    public TextField firstNameEmployee;
    @FXML
    public TextField lastNameEmployee;
    @FXML
    public TextField emailEmployee;
    @FXML
    public TextField usernameEmployee;
    @FXML
    public TextField passwordEmployee;
    @FXML
    public TextField branchNameTF;

    @FXML
    public TableView<Employee> employeeTableView;
    @FXML
    public TableView<Branch> branchTableView;
    @FXML
    public TableColumn<Employee, Integer> idCol;
    @FXML
    public TableColumn<Employee, String> firstNameCol;
    @FXML
    public TableColumn<Employee, String> usernameCol;
    @FXML
    public TableColumn<Employee, String> lastNameCol;
    @FXML
    public TableColumn<Employee, String> emailCol;
    @FXML
    public TableColumn<Employee, String> passwordCol;
    @FXML
    public TableColumn<Employee, EmployeeType> employeeTypeCol;
    @FXML
    public TableColumn<Employee, Branch> branchManagerCol;
    @FXML
    public TableColumn<Employee, Branch> branchCol;

    @FXML
    public TableColumn<Branch,Integer> branchIDCol;
    @FXML
    public TableColumn<Branch, String> branchNameCol;
    @FXML
    public TableColumn<Branch,List<Employee>> branchManagersCol;
    @FXML
    public TableColumn<Branch,List<Employee>> employeesCol;
    @FXML
    public TableColumn<Branch,List<Theater>> theatersCol;

    @FXML
    private Label branchManagerLabel;
    //endregion

    //region Branch Admin

    //endregion

    //region Local Attributes
    private SimpleClient client;
    private Message localMessage;
    private List<Branch> branches;
    private List<Employee> employees;

    //endregion

    @FXML
    private void initialize() {
        // Sending a message to get the current list of Branches.
        Message request = new Message();
        request.setMessage(BRANCH_THEATER_INFORMATION);
        request.setData(GET_BRANCHES);
        client.sendMessage(request);

        // Adding listeners to the fields to check if they're empty
        addTextInputListener(firstNameEmployee, null);
        addTextInputListener(lastNameEmployee, null);
        addTextInputListener(emailEmployee, null);
        addTextInputListener(usernameEmployee, null);
        addTextInputListener(passwordEmployee, null);
        addTextInputListener(employeeTypeComboBox, null);
        addTextInputListener(branchComboBox, null);
        addTextInputListener(branchWorkerCombobox, null);

        // Initialize GUI components
        newBranchAccordion.setDisable(true);
        newEmployeeAccordion.setDisable(true);
        newTheaterAccordion.setDisable(true);
        branchManagerLabel.setVisible(false);
        branchComboBox.setVisible(false);
        branchComboBox.setDisable(true);
        EventBus.getDefault().register(this);

        // Initialize Employee ComboBox
        employeeTypeComboBox.getItems().addAll(EmployeeType.values());

        // Initialize the Table columns
        branchManagerCol.setCellValueFactory(new PropertyValueFactory<>("branchInCharge"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        employeeTypeCol.setCellValueFactory(new PropertyValueFactory<>("employeeType"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

        // Setting the fields to be editable.
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        employeeTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(EmployeeType.values())));

        // Use the generic method for handling commit events
        firstNameCol.setOnEditCommit(this::handleEditCommit);
        lastNameCol.setOnEditCommit(this::handleEditCommit);
        usernameCol.setOnEditCommit(this::handleEditCommit);
        emailCol.setOnEditCommit(this::handleEditCommit);
        passwordCol.setOnEditCommit(this::handleEditCommit);
        employeeTypeCol.setOnEditCommit(this::handleEditCommit);

        // Adding right-click context menu for row deletion
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem editItem = new MenuItem("Edit");
        MenuItem forceDisconnect = new MenuItem("Disconnect");
        contextMenu.getItems().addAll(deleteItem, editItem, forceDisconnect);

        // Set context menu on each row, and only show for non-empty rows
        employeeTableView.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });

        // Handle the delete action
        deleteItem.setOnAction(event -> {
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                deleteEmployee(selectedEmployee);
            }
        });

        // Handle the edit action (toggle editable state)
        editItem.setOnAction(event -> {
            boolean isEditable = employeeTableView.isEditable();
            employeeTableView.setEditable(!isEditable);
            editItem.setText(isEditable ? "Edit" : "Disable Edit");
        });

        //reset an employee activity in the system.
        forceDisconnect.setOnAction(event ->{
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if(selectedEmployee != null && selectedEmployee.isActive()){
                Message message = new Message();
                message.setMessage(EMPLOYEE_INFORMATION);
                message.setData(RESET_EMPLOYEE_ACTIVITY);
                message.setEmployee(selectedEmployee);
                client.sendMessage(message);
            }
        });
    }

    //region Helper Methods


    // Show label information for 5 seconds.
    private void pauseTransitionLabelUpdate(String string){
        notificationLabel.setText(string);

        // Create a pause transition of 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(5));

        // Set what to do after the pause
        pause.setOnFinished(event -> notificationLabel.setText(""));

        // Start the pause
        pause.play();
    }

    // Method to create a new Employee object from the input fields
    private Employee getEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName(firstNameEmployee.getText());
        newEmployee.setLastName(lastNameEmployee.getText());
        newEmployee.setEmail(emailEmployee.getText());
        newEmployee.setUsername(usernameEmployee.getText());
        newEmployee.setPassword(passwordEmployee.getText());
        newEmployee.setEmployeeType(employeeTypeComboBox.getValue());
        newEmployee.setBranch(branchWorkerCombobox.getValue());
        if (employeeTypeComboBox.getValue().equals(EmployeeType.BRANCH_MANAGER)) {
            newEmployee.setBranchInCharge(branchComboBox.getValue());
        }
        return newEmployee;
    }

    // Method to delete an employee
    private void deleteEmployee(Employee employee) {
        // Logic to delete employee, e.g., sending a message to the server
        Message request = new Message();
        request.setMessage(EMPLOYEE_INFORMATION);
        request.setData(DELETE_EMPLOYEE);
        request.setEmployee(employee);
        client.sendMessage(request);

        // Optionally remove from the TableView directly
        employeeTableView.getItems().remove(employee);
    }

    // Method to handle edit commit events
    private void handleEditCommit(TableColumn.CellEditEvent<Employee, ?> event) {
        Employee employee = event.getTableView().getItems().get(event.getTablePosition().getRow());

        // Update the employee's property based on the edited column
        if (event.getTableColumn() == firstNameCol) {
            employee.setFirstName((String) event.getNewValue());
        } else if (event.getTableColumn() == lastNameCol) {
            employee.setLastName((String) event.getNewValue());
        } else if (event.getTableColumn() == usernameCol) {
            employee.setUsername((String) event.getNewValue());
        } else if (event.getTableColumn() == emailCol) {
            employee.setEmail((String) event.getNewValue());
        } else if (event.getTableColumn() == passwordCol) {
            employee.setPassword((String) event.getNewValue());
        } else if (event.getTableColumn() == employeeTypeCol) {
            employee.setEmployeeType((EmployeeType) event.getNewValue());
        } else if (event.getTableColumn() == branchManagerCol) {
            employee.setBranchInCharge((Branch) event.getNewValue());
        } else if (event.getTableColumn() == branchCol) {
            employee.setBranch((Branch) event.getNewValue());
        }
        System.out.println("Branch worker is " + employee.getBranch() + " Branch In Charge " + employee.getBranchInCharge());
        updateEmployeeOnServer(employee);
    }

    // Method to update an employee on the server
    private void updateEmployeeOnServer(Employee employee) {
        Message request = new Message();
        request.setMessage(EMPLOYEE_INFORMATION);
        request.setData(UPDATE_EMPLOYEE);
        request.setEmployee(employee);
        client.sendMessage(request);
    }

    // Method to clear all input fields
    private void clearFields() {
        firstNameEmployee.setText("");
        lastNameEmployee.setText("");
        usernameEmployee.setText("");
        emailEmployee.setText("");
        passwordEmployee.setText("");
        branchWorkerCombobox.getSelectionModel().clearSelection();
        employeeTypeComboBox.getSelectionModel().clearSelection();
        branchComboBox.getSelectionModel().clearSelection();
        branchManagerLabel.setVisible(false);
        branchComboBox.setVisible(false);
    }

    //endregion

    //region GUI methods

    @FXML
    public void homeScreen(ActionEvent event) {
        // Implement home screen action
    }

    // Method to handle messages from the server
    @Subscribe
    public void handleMessageFromServer(MessageEvent event) {
        Message msgFromServer = event.getMessage();
        if (msgFromServer.getMessage().equals(BRANCH_THEATER_INFORMATION) && msgFromServer.getData().equals(GET_BRANCHES)) {
            Platform.runLater(() -> {
                branches = msgFromServer.getBranches();
                branchWorkerCombobox.getItems().addAll(branches);
                branchManagerCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(branches)));
                branchManagerCol.setOnEditCommit(this::handleEditCommit);
                branchCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(branches)));
                branchCol.setOnEditCommit(this::handleEditCommit);
            });
        }
        if (msgFromServer.getMessage().equals(GET_EMPLOYEES) && msgFromServer.getData().equals(GET_ALL_EMPLOYEES)) {
            Platform.runLater(() -> {
                employees = msgFromServer.getEmployeeList();
                if (employees != null && !employees.isEmpty()) {
                    employeeTableView.getItems().clear();
                    employeeTableView.getItems().addAll(employees);
                    employeeTableView.refresh();
                } else {
                    SimpleClient.showAlert(Alert.AlertType.INFORMATION, "No Employees", "There are no employees to display.");
                }
            });
        }
        if (msgFromServer.getMessage().equals(EMPLOYEE_INFORMATION)) {
            switch (msgFromServer.getData()) {
                case EMPLOYEE_CREATED -> Platform.runLater(() -> {
                    clearFields();
                    SimpleClient.showAlert(Alert.AlertType.INFORMATION, "Employee Created", "Employee has been created.");
                });
                case UPDATE_EMPLOYEE -> Platform.runLater(()->
                        pauseTransitionLabelUpdate("Employee Information Updated Successfully."));
                case RESET_EMPLOYEE_ACTIVITY -> Platform.runLater(()->
                        pauseTransitionLabelUpdate("Employee Activity Reset Successfully."));
                case GET_ALL_BRANCH_MANAGERS -> Platform.runLater(()->
                    branchManagerCCB.getItems().addAll(msgFromServer.getEmployeeList()));
                default -> System.out.println("Unknown EMPLOYEE_INFORMATION SUB CATEGORY");
            }
        }
    }

    @FXML
    public void newEmployeeAction(ActionEvent actionEvent) {
        newEmployeeAccordion.setExpanded(true);
        newEmployeeAccordion.setDisable(false);
        newBranchAccordion.setDisable(true);
        newBranchAccordion.setExpanded(false);
    }

    @FXML
    public void newBranchAction(ActionEvent actionEvent) {
        newEmployeeAccordion.setExpanded(false);
        newEmployeeAccordion.setDisable(true);
        newBranchAccordion.setDisable(false);
        newBranchAccordion.setExpanded(true);
        Message message = new Message();
        message.setMessage(EMPLOYEE_INFORMATION);
        message.setData(GET_ALL_BRANCH_MANAGERS);
        client.sendMessage(message);
    }

    @FXML
    public void newTheaterAction(ActionEvent actionEvent) {
        // Implement new theater action
    }

    @FXML
    public void selectBranch(ActionEvent actionEvent) {
        changeControlBorderColor(branchComboBox, null);
    }

    @FXML
    public void selectEmployeeType(ActionEvent actionEvent) {
        EmployeeType selected = employeeTypeComboBox.getValue();
        if (EmployeeType.BRANCH_MANAGER == selected) {
            branchComboBox.getItems().addAll(branches);
            branchComboBox.setVisible(true);
            branchComboBox.setDisable(false);
            branchManagerLabel.setVisible(true);
        } else {
            branchComboBox.getItems().clear();
            branchComboBox.setVisible(false);
            branchComboBox.setDisable(true);
            branchManagerLabel.setVisible(false);
        }
        changeControlBorderColor(employeeTypeComboBox, null);
    }

    @FXML
    public void createEmployeeAction(ActionEvent actionEvent) {
        boolean createEmployee = true;

        //region Checking if one of the fields are empty.
        if (firstNameEmployee.getText().isEmpty()) {
            changeControlBorderColor(firstNameEmployee, "Red");
            createEmployee = false;
        }
        if (lastNameEmployee.getText().isEmpty()) {
            changeControlBorderColor(lastNameEmployee, "red");
            createEmployee = false;
        }
        if (emailEmployee.getText().isEmpty()) {
            changeControlBorderColor(emailEmployee, "Red");
            createEmployee = false;
        }
        if (usernameEmployee.getText().isEmpty()) {
            changeControlBorderColor(usernameEmployee, "Red");
            createEmployee = false;
        }
        if (passwordEmployee.getText().isEmpty()) {
            changeControlBorderColor(passwordEmployee, "Red");
            createEmployee = false;
        }
        if (employeeTypeComboBox.getValue() == null) {
            changeControlBorderColor(employeeTypeComboBox, "Red");
            createEmployee = false;
        }
        if (branchWorkerCombobox.getValue() == null) {
            changeControlBorderColor(branchWorkerCombobox, "Red");
        }
        if (branchComboBox.getValue() == null && branchComboBox.isVisible()) {
            if (employeeTypeComboBox.getValue().equals(EmployeeType.BRANCH_MANAGER)) {
                changeControlBorderColor(branchComboBox, "Red");
                createEmployee = false;
            }
        }
        //endregion

        if (createEmployee) {
            Employee newEmployee = getEmployee();
            Message request = new Message();
            request.setMessage(EMPLOYEE_INFORMATION);
            request.setData(CREATE_EMPLOYEE);
            request.setEmployee(newEmployee);
            client.sendMessage(request);
        }
    }

    @FXML
    public void selectedModify(Event event) {
        // Check if the event's source tab is selected
        if (modifyEmployeeTab.isSelected()) {
            Message request = new Message();
            request.setMessage(GET_EMPLOYEES);
            request.setData(GET_ALL_EMPLOYEES);
            client.sendMessage(request);
        }
    }

    @FXML
    public void selectedNew(Event event) {
    }

    @FXML
    public void workerBranchAction(ActionEvent actionEvent) {
        changeControlBorderColor(branchWorkerCombobox, null);
    }
    @FXML
    public void createBranchAction(ActionEvent event) {
    }

    //endregion

    //region Interface Methods
    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }
    //endregion
}
