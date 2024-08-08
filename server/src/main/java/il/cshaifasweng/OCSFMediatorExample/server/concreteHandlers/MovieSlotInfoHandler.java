package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.GET_MOVIE_SLOT_BY_MOVIE_ID;
import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.MOVIE_SLOT_INFORMATION;

public class MovieSlotInfoHandler implements RequestHandler {
    private static Session session;

    @Override
    public void handle(Message message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            //Loading the movie Entity
            Movie movie = session.get(Movie.class, message.getSpecificMovie().getId());

            List<MovieSlot> screeningTimes = movie.getMovieScreeningTime();
            screeningTimes.size();
            for (MovieSlot movieSlot : screeningTimes) {
                if (movieSlot.getTheater() != null && movieSlot.getBranch() != null) {
                movieSlot.getTheater().getId();
                movieSlot.getBranch().getBranchName();
                }
            }
            transaction.commit();

            Message answer = new Message();
            answer.setMovieSlots(screeningTimes);
            answer.setSpecificMovie(movie);
            answer.setMessage(MOVIE_SLOT_INFORMATION);
            answer.setData(GET_MOVIE_SLOT_BY_MOVIE_ID);
            client.sendToClient(answer);

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
