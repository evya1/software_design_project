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
            MovieSlot mv = new MovieSlot();

            int movieSlotId = message.getMovieID();
            for (MovieSlot movieSlot : movie.getMovieScreeningTime()) {
                int movieId = movieSlot.getId();
                if (movieSlotId == movieId) {
                    LocalDateTime movieStartTime = movieSlot.getStartDateTime();
                    LocalDateTime movieEndTime = movieSlot.getEndDateTime();
                    Branch branch = movieSlot.getBranch();

                    Theater theater = movieSlot.getTheater();
                    System.out.println(theater.getTheaterNum());
                    DataCommunicationDB.modifyMovieSlotStartTime(movieSlotId, movieStartTime);
                    DataCommunicationDB.modifyMovieSlotEndTime(movieSlotId, movieEndTime);
                    if (message.getFlag())
                        DataCommunicationDB.modifyMovieSlotBranch(movieSlotId, branch);
                    else {
                        session.close();
                        session = sessionFactory.openSession();
                        DataCommunicationDB.setSession(session);
                    }
                    //DataCommunicationDB.modifyMovieSlotTheater(movieSlotId, null);
                    DataCommunicationDB.modifyMovieSlotTheater(movieSlotId, theater);
                    for (MovieSlot mvs : movie.getMovieScreeningTime()) {
                        if (mvs.getId() == movieSlotId) {
                            System.out.println(mvs.getTheater().getTheaterNum());
                        }
                    }
                    session.close();
                    session = sessionFactory.openSession();
                    DataCommunicationDB.setSession(session);

                    session.beginTransaction();
                    theater.getSchedule().add(movieSlot);
                    //movieSlot.getTheater().getSchedule().add(movieSlot);
                    session.update(theater);
                    session.flush();
                    session.getTransaction().commit();
                }
            }
            Message answer = new Message();
            answer.setMessage("screening times of the movie updated");
            answer.setData(GET_BRANCHES);
            List<Branch> branches = DataCommunicationDB.getBranches();
            for (Branch branch : branches) {
                for (Theater theater : branch.getTheaterList()){
                    theater.getTheaterNum();
                    theater.getSchedule();
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
