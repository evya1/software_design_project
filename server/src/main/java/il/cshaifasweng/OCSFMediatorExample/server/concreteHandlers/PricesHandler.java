package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PriceConstants;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class PricesHandler implements RequestHandler {
    private static Session session;

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            Message answer = new Message();
            PriceConstants price = DataCommunicationDB.getPrices();
            System.out.println(price.getNewBookletPrice());
            System.out.println(price.getNewMovieLinkPrice());
            System.out.println(price.getNewMovieTicketPrice());
            System.out.println(price.getBookletPrice());
            System.out.println(price.getMovieTicketPrice());
            System.out.println(price.getMovieLinkPrice());
            switch (message.getData()) {
                case "show all prices":
                    answer.setMessage("prices information");
                    answer.setData("prices");
                    answer.setPrices(price);
                    client.sendToClient(answer);
                    break;
                case "change price":
                    session.beginTransaction();
                    price = message.getPrices();
                    System.out.println(price.getNewBookletPrice());
                    System.out.println(price.getNewMovieLinkPrice());
                    System.out.println(price.getNewMovieTicketPrice());
                    session.merge(price);
                    session.flush();
                    session.getTransaction().commit();
                    answer.setMessage("prices changed");
                    answer.setData("prices");
                    answer.setPrices(price);
                    client.sendToClient(answer);
                    break;
                case "confirm prices change":
                    session.beginTransaction();
                    price = message.getPrices();
                    System.out.println(price.getNewBookletPrice());
                    System.out.println(price.getNewMovieLinkPrice());
                    System.out.println(price.getNewMovieTicketPrice());
                    if (price.getNewBookletPrice() != -1) {
                        price.setBookletPrice(price.getNewBookletPrice());
                        price.setNewBookletPrice(-1);
                    }
                    if (price.getNewMovieLinkPrice() != -1) {
                        price.setMovieLinkPrice(price.getNewMovieLinkPrice());
                        price.setNewMovieLinkPrice(-1);
                    }
                    if (price.getNewMovieTicketPrice() != -1) {
                        price.setMovieTicketPrice(price.getNewMovieTicketPrice());
                        price.setNewMovieTicketPrice(-1);
                    }
                    session.merge(price);
                    session.flush();
                    session.getTransaction().commit();
                    answer.setMessage("prices change confirmed");
                    answer.setData("prices");
                    answer.setPrices(price);
                    client.sendToClient(answer);
                    break;

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
