//package il.cshaifasweng.OCSFMediatorExample.client.Reports;
//
//import il.cshaifasweng.OCSFMediatorExample.client.JavaFXInitializer;
//import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
//import il.cshaifasweng.OCSFMediatorExample.entities.Message;
//import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.scene.control.Button;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class ReportsScreenControllerTest {
//
//    private ReportsScreenController reportsScreenController;
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
//                reportsScreenController = new ReportsScreenController();
//                mockClient = mock(SimpleClient.class);
//                mockEmployee = mock(Employee.class);
//
//                reportsScreenController.setClient(mockClient);
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
//    void testInitialize_whenLocalMessageIsSet_setsEmployee() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.runLater(() -> {
//            try {
//                Message mockMessage = mock(Message.class);
//                when(mockMessage.getEmployee()).thenReturn(mockEmployee);
//                reportsScreenController.setLocalMessage(mockMessage);
//
//                reportsScreenController.initialize(null, null);
//
//                assertEquals(mockEmployee, reportsScreenController.getEmployee());
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
//    void testHandleBackAction_movesBackToPreviousScreen() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.runLater(() -> {
//            try {
//                Stage mockStage = mock(Stage.class);
//                Message mockMessage = mock(Message.class);
//                when(mockMessage.getSourceFXML()).thenReturn("PREVIOUS_SCREEN");
//                when(mockMessage.getEmployee()).thenReturn(mockEmployee);
//                reportsScreenController.setLocalMessage(mockMessage);
//
//                Button mockButton = mock(Button.class);
//                ActionEvent actionEvent = new ActionEvent(mockButton, null);
//
//                reportsScreenController.handleBackAction(actionEvent);
//
//                verify(mockClient, times(1)).moveScene(eq("PREVIOUS_SCREEN"), eq(mockStage), any(Message.class));
//            } finally {
//                latch.countDown();
//            }
//        });
//        if (!latch.await(5, TimeUnit.SECONDS)) {
//            throw new RuntimeException("Test did not complete in time");
//        }
//    }
//}
