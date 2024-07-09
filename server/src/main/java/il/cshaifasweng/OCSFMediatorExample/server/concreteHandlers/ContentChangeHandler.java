package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class ContentChangeHandler implements RequestHandler {
    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {

        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        DataCommunicationDB.setSession(session);

        try {
            //ALL REQUESTS ARE TO BE WITHIN THE TRY  --   START HERE
            Message answer = new Message();

            //Checking if the request is to create a new movie.
            if(message.getData().equals("New Movie")){
                Movie movie = message.getSpecificMovie();
                DataCommunicationDB.createNewMovie(movie);
                System.out.println("New movie was created successfully");
                answer.setMessage("New Movie was Created");
                client.sendToClient(answer);
            }

            if(message.getData().equals("Update Movie")){
                Movie movie = message.getSpecificMovie();
                DataCommunicationDB.updateMovieDetails(movie);
                System.out.println("Movie was updated successfully");
                answer.setMessage("Movie was Updated");
                client.sendToClient(answer);
            }
            //ALL REQUESTS ARE TO BE WITHIN THE TRY -- END HERE
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
