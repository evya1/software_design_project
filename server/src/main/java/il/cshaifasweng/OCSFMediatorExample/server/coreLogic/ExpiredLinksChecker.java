package il.cshaifasweng.OCSFMediatorExample.server.coreLogic;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.MovieLink;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.InboxMessage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.List;


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

                String hql = "FROM MovieLink";
                List<MovieLink> movieLinks = session.createQuery(hql, MovieLink.class)
                        .list();

                for (MovieLink movieLink : movieLinks) {

                    if(movieLink.isValid()){
                        int customerID = movieLink.getCustomer_id();
                        Customer customer = session.get(Customer.class, customerID);

                        if (movieLink.isActive() && (movieLink.getExpirationTime().isBefore(LocalDateTime.now()))) {
                            movieLink.setInvalid();
                            movieLink.setInactive();
                            session.update(movieLink);

                            InboxMessage inboxMessage = new InboxMessage();
                            inboxMessage.setCustomer(customer);
                            inboxMessage.setMessageTitle("Movie Link Expired");
                            inboxMessage.setMessageContent("The Link \n" + movieLink.getMovieLink() + "\nHas expired.");
                            session.save(inboxMessage);

                            // Add the message to the customer's inbox messages
                            customer.getInboxMessages().add(inboxMessage);

                            session.update(customer);
                        }

                        else if (!movieLink.isActive() && movieLink.getCreationTime().isBefore(LocalDateTime.now()) && movieLink.getExpirationTime().isAfter(LocalDateTime.now())) {
                            movieLink.setActive();
                            session.update(movieLink);

                            InboxMessage inboxMessage = new InboxMessage();
                            inboxMessage.setCustomer(customer);
                            inboxMessage.setMessageTitle("Movie link has been activated");
                            inboxMessage.setMessageContent("The Link \n" + movieLink.getMovieLink() + "\nHas been activated.");
                            session.save(inboxMessage);

                        }
                    }
                }

                // Flush and clear session after processing all movie links
                session.flush();
                session.clear();

                tx.commit();

                //This will erase all expired links every 30 seconds
                Thread.sleep(30000);
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