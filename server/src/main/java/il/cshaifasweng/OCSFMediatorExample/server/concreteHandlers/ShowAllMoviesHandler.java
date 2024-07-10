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

public class ShowAllMoviesHandler implements RequestHandler {
    private static Session session;
    private SimpleServer server;

    public ShowAllMoviesHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(Message message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();

            String hql = "FROM Movie";
            Query query = session.createQuery(hql);
            List<Movie> movies = query.list();
            System.out.println("LOG: Server side - the number of movies is : " + movies.size());

            Message answer = new Message();
            answer.setMessage("show all movies");
            answer.setMovies(movies);
            server.sendToAllClients(answer);

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
