package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class BranchTheaterHandler implements RequestHandler {
    Session session;

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            Message answer = new Message();
            List<Branch> branches;
            answer.setMessage(BRANCH_THEATER_INFORMATION);

            switch (message.getData()){

                case GET_BRANCHES:
                    answer.setData(GET_BRANCHES);
                    branches = DataCommunicationDB.getBranches();
                    for (Branch branch : branches) {
                        for (Theater theater : branch.getTheaterList()){
                            theater.getId();
                            theater.getSchedule();
                        }
                    }
                    answer.setBranches(branches);
                    client.sendToClient(answer);
                    break;
                case GET_BRANCHES_BY_MOVIE_ID:
                    answer.setData(GET_BRANCHES_BY_MOVIE_ID);
                    branches = DataCommunicationDB.getBranchesByMovieID(message.getMovieID());
                    answer.setBranches(branches);
                    client.sendToClient(answer);
                    break;
                case GET_MOVIE_SLOT_BY_MOVIE_ID_AND_BRANCH_ID:
                    answer.setData(GET_MOVIE_SLOT_BY_MOVIE_ID_AND_BRANCH_ID);
                    List<MovieSlot> slots = DataCommunicationDB
                            .getMovieSlotsByBranchIDAndMovieID(message.getMovieID(),message.getBranchID());
                    answer.setMovieSlots(slots);
                    client.sendToClient(answer);
                    break;
                case CREATE_NEW_BRANCH:
                    answer.setData(CREATE_NEW_BRANCH);
                    session.beginTransaction();
                    DataCommunicationDB.setSession(session);
                    DataCommunicationDB.createNewBranch(message.getBranch());
                    client.sendToClient(answer);
                case GET_ALL_THEATERS:
                    answer.setData(GET_ALL_THEATERS);
                    session.beginTransaction();
                    DataCommunicationDB.setSession(session);
                    answer.setTheaters(DataCommunicationDB.getAllTheaters());
                    client.sendToClient(answer);
                default:
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
