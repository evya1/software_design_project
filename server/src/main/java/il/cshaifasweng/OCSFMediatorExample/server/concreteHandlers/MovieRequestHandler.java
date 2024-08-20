package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;

import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB.getMovieSlotsByBranch;
import static il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB.getMoviesByBranch;
import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class MovieRequestHandler implements RequestHandler {
    private static Session session;
    private SimpleServer server;

    public MovieRequestHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(Message message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            Message answer = new Message();
            answer.setMessage(MOVIES_REQUEST);

            switch(message.getData()){
                case SHOW_ALL_MOVIES:
                    String hql = "FROM Movie";
                    Query query = session.createQuery(hql);
                    List<Movie> movies = query.list();
                    System.out.println("LOG: Server side - the number of movies is : " + movies.size());

                    answer.setData(SHOW_ALL_MOVIES);
                    answer.setMovies(movies);

                    //If there was an update to the content a show all movies request is being transmitted to the server.
                    //The server needs to send request to all the clients to update their movie view.
                    server.sendToAllClients(answer);
                    break;
                case GET_MOVIES_BY_BRANCH_ID:
                    int branchId = message.getBranchID();
                    answer.setData(GET_MOVIES_BY_BRANCH_ID);
                    answer.setMovies(getMoviesByBranch(branchId));
                    answer.setMovieSlots(getMovieSlotsByBranch(branchId));
                    //Specific client is requesting the movies by Branch ID and a specific reply is being produced.
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
