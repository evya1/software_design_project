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

    @Override
    public void handle(Message message, ConnectionToClient client) {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            // the client should put in the object of the message a new movie such that the fields
            // take the same fields of the movie we want to change screening times
            // apart from the field of the List of the movieSlot to be changed with the new time slots.
            Movie movie = message.getSpecificMovie();
            MovieSlot mv = null;
            Theater theater = null;

            int movieSlotId = message.getMovieID();
            for (MovieSlot movieSlot : movie.getMovieScreeningTime()) {
                int movieId = movieSlot.getId();
                if (movieSlotId == movieId) {
//                    LocalDateTime movieStartTime = movieSlot.getStartDateTime();
//                    LocalDateTime movieEndTime = movieSlot.getEndDateTime();
//                    Branch branch = movieSlot.getBranch();
                    theater = movieSlot.getTheater();
                    mv = movieSlot;
//                    DataCommunicationDB.modifyMovieSlotStartTime(movieSlotId, movieStartTime);
//                    DataCommunicationDB.modifyMovieSlotEndTime(movieSlotId, movieEndTime);
//                    DataCommunicationDB.modifyMovieSlotTheater(movieSlotId, null);
//                    DataCommunicationDB.modifyMovieSlotTheater(movieSlotId, theater);


                }
            }
            session.beginTransaction();
            MovieSlot movieSl = DataCommunicationDB.getMovieSlotByID(movieSlotId);
            movieSl.setMovieTitle(mv.getMovieTitle());
            movieSl.setBranch(mv.getBranch());
            movieSl.setTheater(mv.getTheater());
            movieSl.setStartDateTime(mv.getStartDateTime());
            movieSl.setEndDateTime(mv.getEndDateTime());
            movieSl.setMovie(mv.getMovie());

            theater.getSchedule().add(mv);
            Theater thea = DataCommunicationDB.getTheaterByID(movieSl.getTheater().getTheaterNum());
            thea.setSchedule(theater.getSchedule());

            session.merge(movieSl);
            //session.merge(thea);
            session.flush();
            session.getTransaction().commit();
            Message answer = new Message();
            answer.setMessage("screening times of the movie updated");
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
