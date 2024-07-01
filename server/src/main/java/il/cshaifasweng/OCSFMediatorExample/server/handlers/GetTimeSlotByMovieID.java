package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class GetTimeSlotByMovieID implements RequestHandler {
    private static Session session;

    @Override
    public void handle(MessageObject message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            //Loading the movie Entity
            Movie movie = session.get(Movie.class, ((Movie) message.getObject()).getId());

            List<MovieSlot> screeningTimes = movie.getMovieScreeningTime();
            screeningTimes.size();

            MessageObject answer = new MessageObject("time slots for specific movie", screeningTimes);
            client.sendToClient(answer);
            transaction.commit();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
