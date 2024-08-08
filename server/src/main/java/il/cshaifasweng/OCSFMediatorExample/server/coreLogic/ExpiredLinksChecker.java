package il.cshaifasweng.OCSFMediatorExample.server.coreLogic;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.MovieLink;
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

                String hql = "FROM MovieLink WHERE expirationTime < :currentTime";
                List<MovieLink> expiredLinks = session.createQuery(hql, MovieLink.class)
                        .setParameter("currentTime", LocalDateTime.now())
                        .list();

                for (MovieLink link : expiredLinks) {
                    System.out.println("Expired MovieLink ID: " + link.getId() + ", Movie Name: " + link.getMovieName());
                    session.delete(link);
                }

                tx.commit();

                //This will erase all expired links every 30 seconds
                Thread.sleep(60000);
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
