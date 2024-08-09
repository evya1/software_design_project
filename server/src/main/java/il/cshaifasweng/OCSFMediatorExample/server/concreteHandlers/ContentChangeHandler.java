package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.xml.crypto.Data;
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
                    if(movie.getMovieType().isCurrentlyRunning()){
                        DataCommunicationDB.addMessageToCustomers();
                    }
                    answer.setData("New Movie");
                    client.sendToClient(answer);
                    break;

                case UPDATE_MOVIE_REQUEST:
                    movie = message.getSpecificMovie();
                    DataCommunicationDB.updateMovieDetails(movie);
                    if(message.isNewContentFlag()){
                        DataCommunicationDB.addMessageToCustomers();
                    }
                    System.out.println("Movie was updated successfully");
                    answer.setData("Movie Updated");
                    client.sendToClient(answer);
                    break;

                case DELETE_MOVIE_BY_MOVIE_ID:
                    DataCommunicationDB.deleteMovieById(message.getMovieID());
                    System.out.println("Movie was deleted successfully");
                    answer.setData("Movie Deleted");
                    client.sendToClient(answer);

                case NEW_MOVIE_SLOT:
                    DataCommunicationDB.createMovieSlot(message.getMovieSlot());
                    DataCommunicationDB.createMovieSlotForMovieID(message.getSpecificMovie().getId(), message.getMovieSlot());
                    System.out.println("New movie slot was created successfully");
                    answer.setData(NEW_MOVIE_SLOT);
                    client.sendToClient(answer);
                    //TODO: Send to all clients?
                    break;

                case UPDATE_MOVIE_SLOT:
                    DataCommunicationDB.updateMovieSlot(message.getMovieSlot());
                    System.out.println("Movie slot was updated successfully");
                    answer.setData(UPDATE_MOVIE_SLOT);
                    client.sendToClient(answer);
                    break;
                case DELETE_MOVIE_SLOT:
                    DataCommunicationDB.removeSlotFromMovie(message.getMovieSlot());
                    DataCommunicationDB.deleteMovieSlot(message.getMovieSlot());
                    System.out.println("Movie slot was removed successfully");
                    answer.setData(DELETE_MOVIE_SLOT);
                    client.sendToClient(answer);
                    //TODO: send to all clients?
                    break;
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
