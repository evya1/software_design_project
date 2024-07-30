package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.GET_BRANCHES;

public class ChangeScreeningTimesHandler implements RequestHandler {
    private static Session session;

    private void changeScreeningTimes(MovieSlot mv, int movieSlotId) {
        session.beginTransaction();

        MovieSlot movieSl = session.get(MovieSlot.class, movieSlotId);
        movieSl.setMovieTitle(mv.getMovieTitle());
        movieSl.setBranch(mv.getBranch());
        movieSl.setTheater(mv.getTheater());
        movieSl.setStartDateTime(mv.getStartDateTime());
        movieSl.setEndDateTime(mv.getEndDateTime());

        Theater thea = session.get(Theater.class,movieSl.getTheater().getTheaterNum());
        thea.getSchedule().add(movieSl);

        session.getTransaction().commit();
    }

    private void addNewScreeningTime(Message message, int movieSlotId) {
        session.beginTransaction();

        MovieSlot movieSl = new MovieSlot();
        System.out.println(movieSl.getId());
        MovieSlot mv = message.getMovieSlot();
        Movie movie = session.get(Movie.class, message.getSpecificMovie().getId());
        movieSl.setMovie(movie);
        movieSl.setMovieTitle(mv.getMovieTitle());
        movieSl.setBranch(mv.getBranch());
        movieSl.setTheater(mv.getTheater());
        movieSl.setStartDateTime(mv.getStartDateTime());
        movieSl.setEndDateTime(mv.getEndDateTime());

        Theater thea = session.get(Theater.class,movieSl.getTheater().getTheaterNum());
        thea.getSchedule().add(movieSl);

        movie.getMovieScreeningTime().add(movieSl);

        session.getTransaction().commit();
    }

    @Override
    public void handle(Message message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            Movie movie = message.getSpecificMovie();
            MovieSlot mv = null;

            int movieSlotId = message.getMovieID();
            for (MovieSlot movieSlot : movie.getMovieScreeningTime()) {
                int movieId = movieSlot.getId();
                if (movieSlotId == movieId) mv = movieSlot;
            }
            if (movieSlotId == -1) addNewScreeningTime(message, movieSlotId);
            else changeScreeningTimes(mv, movieSlotId);

            Message answer = new Message();
            answer.setMessage("screening times of the movie updated/added");
            answer.setData(GET_BRANCHES);
            List<Branch> branches = DataCommunicationDB.getBranches();
            for (Branch branch : branches) {
                for (Theater th : branch.getTheaterList()) {
                    th.getTheaterNum();
                    th.getSchedule();
                }
            }
            answer.setBranches(branches);
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
