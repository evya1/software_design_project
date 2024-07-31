package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class ComplaintsHandler implements RequestHandler {
    private static Session session;
    private SimpleServer server;

    public ComplaintsHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        try {
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
            session = sessionFactory.openSession();
            DataCommunicationDB.setSession(session);
            Message answer = new Message();
            answer.setMessage("Complaints");
            if (message.getData().equals("show all complaints")){
                String hql = "FROM Complaint where complaintStatus = :status ORDER BY dateOfComplaint asc";
                Query query = session.createQuery(hql);
                query.setParameter("status", "open");
                List<Complaint> complaints = query.list();
                for (Complaint complaint : complaints) {
                    complaint.getCustomer().getFirstName();
                    complaint.getCustomer().getLastName();
                    complaint.getCustomer().getEmail();
                }
                System.out.println("LOG: Server side - the number of movies is : " + complaints.size());
                answer.setComplaints(complaints);
                answer.setData("show all complaints");
                server.sendToAllClients(answer);
            } else if (message.getData().equals("change complaint status")) {
                session.beginTransaction();
                Complaint complaint = session.get(Complaint.class, message.getComplaint().getId());
                complaint.setComplaintStatus(message.getComplaint().getComplaintStatus());
                session.getTransaction().commit();
                answer.setData("change complaint status");
                server.sendToAllClients(answer);
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
