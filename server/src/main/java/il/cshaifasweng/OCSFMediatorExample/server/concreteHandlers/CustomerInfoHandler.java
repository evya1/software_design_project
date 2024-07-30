package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.GET_CUSTOMER_ID;

public class CustomerInfoHandler implements RequestHandler {
    Session session;

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
                    client.sendToClient(answer); //If there was a null then the customer doesn't exist show a popup.
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("An error occured while handling the request");
            e.printStackTrace();
        }
    }
}
