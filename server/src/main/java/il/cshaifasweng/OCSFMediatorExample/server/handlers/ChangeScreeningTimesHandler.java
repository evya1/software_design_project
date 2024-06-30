package il.cshaifasweng.OCSFMediatorExample.server.handlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageObject;
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
            MovieSlot movieSlot = (MovieSlot) message.getObject();
            DataCommunicationDB.modifyMovieSlotStartTime(movieSlot.getId(), movieSlot.getStartDateTime());
            DataCommunicationDB.modifyMovieSlotEndTime(movieSlot.getId(), movieSlot.getEndDateTime());

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
