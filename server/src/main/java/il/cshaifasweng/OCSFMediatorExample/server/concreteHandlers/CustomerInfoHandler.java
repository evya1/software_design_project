package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Booklet;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.InboxMessage;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class CustomerInfoHandler implements RequestHandler {
    Session session;
    private SimpleServer server;

    public CustomerInfoHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();

        try (session) {
            DataCommunicationDB.setSession(session);
            Message answer = new Message();
            Customer customer;
            switch (message.getData()) {
                case GET_CUSTOMER_ID:
                    answer.setMessage(message.getMessage());
                    answer.setData(message.getData());
                    session.beginTransaction();
                    customer = DataCommunicationDB.getCustomerByPersonalID(session, message.getCustomerID());
                    answer.setCustomer(customer);
                    System.out.println(customer);
                    client.sendToClient(answer); //If there was a null then the customer doesn't exist show a popup.
                    break;
                case GET_CUSTOMER_MESSAGES:
                    System.out.println("Customer Messages Requested.");
                    answer.setMessage(GET_CUSTOMER_INFO);
                    answer.setData(GET_CUSTOMER_MESSAGES);
                    session.beginTransaction();
                    List<InboxMessage> messages = DataCommunicationDB.getInboxMessagesByCustomerId(message.getCustomer().getId());
                    System.out.println("The number of messages for the customer is "+ messages.size());
                    answer.setCustomerMessages(messages);
                    client.sendToClient(answer);
                    break;
                case "update Booklet":
                    session.beginTransaction();
                    Booklet booklet = session.get(Booklet.class, message.getBooklet().getId());
                    booklet.setNumOfEntries(message.getBooklet().getNumOfEntries());
                    Customer cust = session.get(Customer.class, message.getCustomer().getId());
                    session.update(cust);
                    session.getTransaction().commit();
                    answer.setMessage(message.getMessage());
                    answer.setData("update the customers screen");
                    answer.setCustomer(cust);
                    server.sendToAllClients(answer);
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("An error occured while handling the request");
            e.printStackTrace();
        }
    }
}
