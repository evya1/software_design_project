package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.InboxMessage;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.GET_COMPLAINT_REQUEST;
import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.GET_CUSTOMER_INFO;

public class ComplaintSubmissionHandler implements RequestHandler {
    private SimpleServer server;

    public ComplaintSubmissionHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {

        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        DataCommunicationDB.setSession(session);

        try {
            // All requests are to be within the try block -- START HERE
            Message answer = new Message();
            answer.setMessage(GET_COMPLAINT_REQUEST);
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                Complaint complaint = new Complaint();
                complaint.setComplaintStatus("Open");
                complaint.setComplaintTitle(message.getComplaintTitle());
                complaint.setComplaintContent(message.getData());
                complaint.setPurchaseType(message.getPurchaseType());
                complaint.setDateOfComplaint(LocalDateTime.now());
                complaint.setBranch(message.getBranch());

                InboxMessage inboxMessage = new InboxMessage();
                inboxMessage.setMessageTitle("New Complaint");
                inboxMessage.setMessageContent("Your complaint has been submitted. You will receive an answer within 24 hours.");

                Customer customer = message.getCustomer();
                Customer existingCustomer = DataCommunicationDB.getCustomerByPersonalID(session, customer.getPersonalID());

                if (existingCustomer != null) {
                    System.out.println("Customer exists with ID: " + existingCustomer.getId());
                    complaint.setCustomer(existingCustomer);
                    complaint.setCustomerPId(existingCustomer.getPersonalID());
                    inboxMessage.setCustomer(existingCustomer);
                    existingCustomer.getInboxMessages().add(inboxMessage);
                    //TODO: AFTER UPDATING CUSTOMER ENTITY MAKE SURE IT WORKS:
                    existingCustomer.getComplaints().add(complaint);
                    session.save(inboxMessage);
                    session.save(complaint);
                    session.update(existingCustomer);
                    System.out.println("Customer's complaints have been updated successfully");
                } else {
                    System.out.println("Customer does not exist. Creating new customer.");
                    complaint.setCustomer(customer);
                    complaint.setCustomerPId(customer.getPersonalID());
                    complaint.setDateOfComplaint(LocalDateTime.now());
                    inboxMessage.setCustomer(customer);
                    customer.getInboxMessages().add(inboxMessage);
                    //TODO: AFTER UPDATING CUSTOMER ENTITY MAKE SURE IT WORKS:
                    customer.getComplaints().add(complaint);
                    session.save(customer); // Save customer first to generate ID
                    session.save(complaint); // Then save purchase to link it to customer
                    System.out.println("Successfully created new customer.");
                }
                message.setComplaint(complaint);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                System.err.println("An error occurred");
                e.printStackTrace();
            }
            answer.setComplaint(message.getComplaint());
            client.sendToClient(answer);

            Customer cust = DataCommunicationDB.getCustomerByPersonalID(session,message.getCustomer().getPersonalID());
            for (Complaint c : cust.getComplaints()) {
                System.out.println(c.getComplaintTitle());
            }
            answer.setMessage(GET_CUSTOMER_INFO);
            answer.setData("update the customers screen");
            answer.setCustomer(cust);
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
