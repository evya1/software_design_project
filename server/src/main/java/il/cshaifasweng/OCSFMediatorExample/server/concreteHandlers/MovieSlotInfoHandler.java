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

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class MovieSlotInfoHandler implements RequestHandler {
    private static Session session;

    @Override
    public void handle(Message message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Movie movie = null;
            List<MovieSlot> screeningTimes = null;

            if (message.getData().equals(GET_MOVIE_SLOT_BY_MOVIE_ID)) {
                //Loading the movie Entity
                movie = session.get(Movie.class, message.getSpecificMovie().getId());

                screeningTimes = movie.getMovieScreeningTime();
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
            }
            else if (message.getData().equals(GET_MOVIE_SLOT_BY_ID)) {
                MovieSlot movieSlot = session.get(MovieSlot.class, message.getMovieSlot().getId());
                Message answer = new Message();
                answer.setMovieSlot(movieSlot);
                answer.setMessage(MOVIE_SLOT_INFORMATION);
                answer.setData(GET_MOVIE_SLOT_BY_ID);
                client.sendToClient(answer);
            }



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
