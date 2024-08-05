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

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.PURCHASE_NOT_FOUND;
import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.UPDATE_PURCHASE;

public class UpdatePurchaseHandler implements RequestHandler {

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        Message answer = new Message();
        try {
            DataCommunicationDB.setSession(session);
            session.beginTransaction();

            if (message.getMessage().equals(UPDATE_PURCHASE)) {
                answer.setMessage(message.getMessage());
                Purchase updatePurchase = message.getPurchase();

                Purchase oldPurchase = DataCommunicationDB.getPurchaseByID(updatePurchase.getId());
                if (oldPurchase != null) {
                    // Update the purchase's cancelled status
                    oldPurchase.setCancelled(updatePurchase.isCancelled());
                    // Save the updated purchase
                    session.update(oldPurchase);
                    session.getTransaction().commit();
                    session.flush(); // Ensure changes are flushed to the database

                    answer.setPurchase(oldPurchase);
                    System.out.println(answer.getMessage());
                } else {
                    answer.setMessage(PURCHASE_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("An error occurred while handling the request");
            e.printStackTrace();
        }
        finally {
            client.sendToClient(answer);
        }

    }
}
