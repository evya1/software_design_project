package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class EmployeesListHandler implements RequestHandler {
    private static Session session;

    @Override
    public void handle(Message message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            String hql = "FROM Employee";
            Query query = session.createQuery(hql);
            List<Employee> employees = query.list();
            System.out.println("LOG: Server side - the number of employees is : " + employees.size());
            Message answer = new Message();
            answer.setMessage(GET_EMPLOYEES);
            if (message.getData().equals("check password")) {
                String[] usernamePass = message.getUsernamePassword().split("=");
                Employee employee = null;
                for (Employee emp : employees) {
                    if (usernamePass[0].equalsIgnoreCase(emp.getUsername().trim())
                            && usernamePass[1].equals(emp.getPassword().trim())) {
                        employee = emp;
                    }
                }
                answer.setData("password check");
                answer.setEmployee(employee);
                client.sendToClient(answer);
            } else if (message.getData().equals("set employee as active")) {
                session.beginTransaction();
                Employee employee = session.get(Employee.class, message.getEmployee().getId());
                employee.setActive(message.getEmployee().isActive());
                session.getTransaction().commit();
                answer.setData("employee is active");
                answer.setEmployee(employee);
                client.sendToClient(answer);
            }
        } catch (Exception e) {
            System.err.println("An error occurred");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
