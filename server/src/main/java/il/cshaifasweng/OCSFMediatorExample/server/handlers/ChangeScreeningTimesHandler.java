package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ChangeScreeningTimesHandler implements RequestHandler {
    private static Session session;

    @Override
    public void handle(MessageObject message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            // the client should put in the object of the message a new movie such that the fields
            // take the same fields of the movie we want to change screening times
            // apart from the field of the List of the movieSlot to be changed with the new time slots.
            Movie movie = (Movie) message.getObject();
            for (MovieSlot current : movie.getMovieScreeningTime()) {
                System.out.println(current.getStartDateTime());
                DataCommunicationDB.modifyMovieSlotStartTime(current.getId(), current.getStartDateTime());
                DataCommunicationDB.modifyMovieSlotEndTime(current.getId(), current.getEndDateTime());
            }
            MessageObject answer = new MessageObject("screening times of the movie updated");
            client.sendToClient(answer);
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
