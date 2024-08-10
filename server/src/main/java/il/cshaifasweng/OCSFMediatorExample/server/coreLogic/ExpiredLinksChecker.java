package il.cshaifasweng.OCSFMediatorExample.server.coreLogic;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.MovieLink;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.InboxMessage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class ExpiredLinksChecker implements Runnable {
    private final SessionFactory sessionFactory;

    public ExpiredLinksChecker(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run() {
        while (true) {
            Session session = sessionFactory.openSession();
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                String hql = "FROM MovieLink WHERE expirationTime < :currentTime";
                List<MovieLink> expiredLinks = session.createQuery(hql, MovieLink.class)
                        .setParameter("currentTime", LocalDateTime.now())
                        .list();

                for (MovieLink movieLink : expiredLinks) {
                    if (movieLink.isActive()) {
                        movieLink.setInactive();
                        session.update(movieLink);

                        int customerID = movieLink.getCustomer_id();
                        Customer customer = session.get(Customer.class, customerID);

                        InboxMessage inboxMessage = new InboxMessage();
                        inboxMessage.setCustomer(customer);
                        inboxMessage.setMessageTitle("Movie Link Expired");
                        inboxMessage.setMessageContent("The Link \n" + movieLink.getMovieLink() + "\nHas expired.");
                        session.save(inboxMessage);

                        // Add the message to the customer's inbox messages
                        customer.getInboxMessages().add(inboxMessage);

                        session.update(customer);
                    }
                }

                // Flush and clear session after processing all movie links
                session.flush();
                session.clear();

                tx.commit();

                //This will erase all expired links every 30 seconds
                Thread.sleep(1000);
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }
}
