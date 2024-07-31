package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class AdminPanelController implements ClientDependent{

    @FXML
    public Button homeScreenBtn;
    @FXML
    public Button newEmployeeBtn;
    @FXML
    public Button newBranchBtn;
    @FXML
    public Button newTheaterBtn;
    @FXML
    public ComboBox branchComboBox;
    
    public TextField firstNameEmployee;
    public TextField lastNameEmployee;
    public TextField emailEmployee;
    public TextField usernameEmployee;
    public TextField passwordEmployee;
    public ComboBox<EmployeeType> employeeTypeComboBox;
    public Button createEmployeeBtn;

    private SimpleClient client;
    private Message localMessage;


    @FXML
    private TitledPane newBranchAccordion;

    @FXML
    private TitledPane newEmployeeAccordion;

    @FXML
    private TitledPane newTheaterAccordion;


    @FXML
    private void initialize(){
        newBranchAccordion.setDisable(true);
        newEmployeeAccordion.setDisable(true);
        newTheaterAccordion.setDisable(true);

        //Initialize Employee Combobox
        employeeTypeComboBox.getItems().addAll(EmployeeType.BASE, EmployeeType.SERVICE, EmployeeType.CHAIN_MANAGER, EmployeeType.CONTENT_MANAGER, EmployeeType.THEATER_MANAGER);
        
    }

    @FXML
    void homeScreen(ActionEvent event) {

    }



    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }

    @FXML
    public void newEmployeeAction(ActionEvent actionEvent) {
        newEmployeeAccordion.setExpanded(true);
        newEmployeeAccordion.setDisable(false);
    }

    @FXML
    public void newBranchAction(ActionEvent actionEvent) {
    }

    @FXML
    public void newTheaterAction(ActionEvent actionEvent) {
    }

    @FXML
    public void selectBranch(ActionEvent actionEvent) {
    }

    public void selectEmployeeType(ActionEvent actionEvent) {
    }
}
