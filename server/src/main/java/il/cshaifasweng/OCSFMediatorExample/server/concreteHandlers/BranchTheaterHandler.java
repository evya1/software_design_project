package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

            switch (message.getData()){
                case GET_BRANCHES:
                    answer.setMessage(BRANCH_THEATER_INFORMATION);
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
