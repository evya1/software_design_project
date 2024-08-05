package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class UpdatePurchaseHandler implements RequestHandler {

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        try (Session session = sessionFactory.openSession()) {
            DataCommunicationDB.setSession(session);
            Message answer = new Message();
            Customer customer;
            session.beginTransaction();
            try {
                switch (message.getData()) {
                    case "UPDATE PURCHASE":
                        answer.setMessage(message.getMessage());
                        answer.setData(message.getData());
                        customer = DataCommunicationDB.getCustomerByPersonalID(session, message.getCustomerID());
                        Purchase updatedPurchase = message.getPurchase();
                        Purchase purchase = customer.getPurchases().stream()
                                .filter(p -> p.getId() == updatedPurchase.getId())
                                .findFirst()
                                .orElse(null);

                        if (purchase != null) {
                            // Update the purchase's cancelled status
                            purchase.setCancelled(updatedPurchase.isCancelled());

                            // Save the updated purchase
                            session.update(purchase);
                            session.getTransaction().commit();
                            session.flush(); // Ensure changes are flushed to the database

                            answer.setPurchase(purchase);
                            client.sendToClient(answer);
                        } else {
                            answer.setMessage("PURCHASE NOT FOUND");
                            client.sendToClient(answer);
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.out.println("An error occurred while handling the request");
                e.printStackTrace();
                answer.setMessage("ERROR");
                client.sendToClient(answer);
            }
        }
    }
}
