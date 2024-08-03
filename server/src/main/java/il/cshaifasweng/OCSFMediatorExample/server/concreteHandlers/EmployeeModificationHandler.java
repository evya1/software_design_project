package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB.deleteEmployee;
import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class EmployeeModificationHandler implements RequestHandler {
    private Session session;
    private SimpleServer server;

    public EmployeeModificationHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        try(session)
        {
            Employee employee;
            Message answer = new Message();
            session.beginTransaction();
            DataCommunicationDB.setSession(session);

            switch(message.getData()){
                case CREATE_EMPLOYEE:
                    DataCommunicationDB.createEmployee(message.getEmployee());
                    answer.setMessage(EMPLOYEE_INFORMATION);
                    answer.setData(EMPLOYEE_CREATED);
                    client.sendToClient(answer);
                    break;
                case UPDATE_EMPLOYEE:
                    DataCommunicationDB.updateEmployeeData(message.getEmployee());
                    answer.setMessage(message.getMessage());
                    answer.setData(message.getData());
                    client.sendToClient(answer);
                    break;
                case DELETE_EMPLOYEE:
                    DataCommunicationDB.deleteEmployee(message.getEmployee());
                    break;

                case GET_ALL_EMPLOYEES:
                    List<Employee> employees = DataCommunicationDB.getAllEmployees();
                    answer.setMessage(message.getMessage());
                    answer.setData(message.getData());
                    answer.setEmployeeList(employees);
                    client.sendToClient(answer);
                    break;
                case GET_ALL_BRANCH_MANAGERS:
                    List<Employee> branchManagers = DataCommunicationDB.getAllBranchManagersEmployees();
                    answer.setMessage(message.getMessage());
                    answer.setData(message.getData());
                    answer.setEmployeeList(branchManagers);
                    client.sendToClient(answer);
                    break;
                default:
                    System.out.println("Unrecognized data");

            }
        }
        catch(Exception e)
        {
            System.out.println("An error occured while handling the request");
            e.printStackTrace();
            if (session.getTransaction() != null)
                session.getTransaction().rollback();  // Ensure rollback on error

        }
        finally {
            session.close();
        }
    }
}
