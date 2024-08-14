//package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;
//
//import il.cshaifasweng.OCSFMediatorExample.client.JavaFXInitializer;
//import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
//import il.cshaifasweng.OCSFMediatorExample.entities.Message;
//import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
//import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.REPORTS_SCREEN;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//class EmployeeControllerTest {
//
//    private EmployeeController employeeController;
//    private SimpleClient mockClient;
//    private Employee mockEmployee;
//
//    @BeforeAll
//    static void initToolkit() {
//        JavaFXInitializer.initToolkit();
//    }
//
//    @BeforeEach
//    void setUp() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.runLater(() -> {
//            try {
//                employeeController = new EmployeeController();
//                mockClient = mock(SimpleClient.class);
//                mockEmployee = mock(Employee.class);
//
//                Button realButton = new Button();
//                VBox root = new VBox(realButton);
//                Scene realScene = new Scene(root);
//                Stage realStage = new Stage();
//                realStage.setScene(realScene);
//
//                employeeController.setLogINBtn(new Button());
//                employeeController.setLogOUTBtn(new Button());
//                employeeController.setHomeScreenBtn(new Button());
//                employeeController.setShowReportsBtn(realButton);
//
//                employeeController.setClient(mockClient);
//            } finally {
//                latch.countDown();
//            }
//        });
//        if (!latch.await(5, TimeUnit.SECONDS)) {
//            throw new RuntimeException("Setup did not complete in time");
//        }
//    }
//
//    @Test
//    void testCheckEmployeeLoginStatus_whenEmployeeIsActive_updatesUIAndDisablesHomeScreen() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.runLater(() -> {
//            try {
//                when(mockEmployee.isActive()).thenReturn(true);
//                when(mockEmployee.getEmployeeType()).thenReturn(EmployeeType.BRANCH_MANAGER);
//
//                Message message = new Message();
//                message.setEmployee(mockEmployee);
//
//                employeeController.setMessage(message);
//
//                assertFalse(employeeController.getLogINBtn().isVisible());
//                assertTrue(employeeController.getLogOUTBtn().isVisible());
//                assertTrue(employeeController.getHomeScreenBtn().isDisable());
//            } finally {
//                latch.countDown();
//            }
//        });
//        if (!latch.await(5, TimeUnit.SECONDS)) {
//            throw new RuntimeException("Test did not complete in time");
//        }
//    }
//
//    @Test
//    void testCheckEmployeeLoginStatus_whenEmployeeIsInactive_doesNotUpdateUI() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.runLater(() -> {
//            try {
//                when(mockEmployee.isActive()).thenReturn(false);
//
//                Message message = new Message();
//                message.setEmployee(mockEmployee);
//
//                employeeController.setMessage(message);
//
//                assertTrue(employeeController.getLogINBtn().isVisible());
//                assertFalse(employeeController.getLogOUTBtn().isVisible());
//                assertFalse(employeeController.getHomeScreenBtn().isDisable());
//            } finally {
//                latch.countDown();
//            }
//        });
//        if (!latch.await(5, TimeUnit.SECONDS)) {
//            throw new RuntimeException("Test did not complete in time");
//        }
//    }
//
//    @Test
//    void testShowReports_whenEmployeeIsSet_movesToReportScreen() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.runLater(() -> {
//            try {
//                Stage mockStage = mock(Stage.class);
//                Scene mockScene = mock(Scene.class);
//                Button mockButton = mock(Button.class);
//
//                when(mockButton.getScene()).thenReturn(mockScene);
//                when(mockScene.getWindow()).thenReturn(mockStage);
//
//                employeeController.setShowReportsBtn(mockButton);
//
//                when(mockEmployee.isActive()).thenReturn(true);
//                Message message = new Message();
//                message.setEmployee(mockEmployee);
//
//                employeeController.setMessage(message);
//                employeeController.showReports(new ActionEvent(mockButton, null));
//
//                verify(mockClient, times(1)).moveScene(eq(REPORTS_SCREEN), eq(mockStage), any(Message.class));
//            } finally {
//                latch.countDown();
//            }
//        });
//        if (!latch.await(5, TimeUnit.SECONDS)) {
//            throw new RuntimeException("Test did not complete in time");
//        }
//    }
//}
