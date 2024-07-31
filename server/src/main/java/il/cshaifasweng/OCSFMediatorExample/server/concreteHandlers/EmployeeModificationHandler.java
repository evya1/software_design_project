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
            DataCommunicationDB.setSession(session);
            Employee employee;
            Message answer = new Message();
            session.beginTransaction();

            switch(message.getData()){
                case CREATE_EMPLOYEE:
                    employee = message.getEmployee();
                    session.save(employee);
                    session.getTransaction().commit();
                    answer.setMessage(EMPLOYEE_INFORMATION);
                    answer.setData(EMPLOYEE_CREATED);
                    client.sendToClient(answer);
                    break;
                case UPDATE_EMPLOYEE:
                    System.out.println("Entered Update Employee");
                    employee = (Employee) session.get(Employee.class, message.getEmployee().getId());
                    Employee newEmployeeInfo = message.getEmployee();
                    employee.setFirstName(newEmployeeInfo.getFirstName());
                    employee.setLastName(newEmployeeInfo.getLastName());
                    employee.setEmail(newEmployeeInfo.getEmail());
                    employee.setPassword(newEmployeeInfo.getPassword());
                    employee.setUsername(newEmployeeInfo.getUsername());
                    employee.setBranch(newEmployeeInfo.getBranch());
                    employee.setBranchInCharge(newEmployeeInfo.getBranchInCharge());
                    employee.setEmployeeType(newEmployeeInfo.getEmployeeType());
                    session.getTransaction().commit();
                    answer.setMessage(message.getMessage());
                    answer.setData(message.getData());
                    client.sendToClient(answer);
                    break;
                case DELETE_EMPLOYEE:
                    employee = session.get(Employee.class, message.getEmployee().getId());
                    if (employee != null) {
                        // Check if the employee is referenced as a branch manager
                        if (employee.getBranchInCharge() != null) {
                            Branch branch = employee.getBranchInCharge();
                            if (branch.getBranchManager() != null && branch.getBranchManager().getId() == employee.getId()) {
                                branch.setBranchManager(null);
                                session.saveOrUpdate(branch); // Ensure the update is flushed to the database
                            }
                        }

                        // Attempt to delete the employee after removing all references
                        session.delete(employee);
                        session.getTransaction().commit();
                    }
                        break;

                case GET_ALL_EMPLOYEES:
                    List<Employee> employees = session.createQuery("FROM Employee").list();
                    session.close();
                    answer.setMessage(message.getMessage());
                    answer.setData(message.getData());
                    answer.setEmployeeList(employees);
                    client.sendToClient(answer);
                    break;
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
