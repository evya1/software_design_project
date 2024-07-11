package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class ContentChangeHandler implements RequestHandler {

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {

        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        DataCommunicationDB.setSession(session);

        try {
            // All requests are to be within the try block -- START HERE
            Message answer = new Message();
            answer.setMessage("Content Change");
            Movie movie;

            switch(message.getData()){
                case NEW_MOVIE_REQUEST:
                    movie = message.getSpecificMovie();
                    DataCommunicationDB.createNewMovie(movie);
                    System.out.println("New movie was created successfully");
                    answer.setData("New Movie");
                    client.sendToClient(answer);
                    break;
                case UPDATE_MOVIE_REQUEST:
                    movie = message.getSpecificMovie();
                    DataCommunicationDB.updateMovieDetails(movie);
                    System.out.println("Movie was updated successfully");
                    answer.setData("Movie Updated");
                    client.sendToClient(answer);
                    break;
                case DELETE_MOVIE_BY_MOVIE_ID:
                    DataCommunicationDB.deleteMovieById(message.getMovieID());
                    System.out.println("Movie was deleted successfully");
                    answer.setData("Movie Deleted");
                    client.sendToClient(answer);
                default:

            }
            // All requests are to be within the try block -- END HERE
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
