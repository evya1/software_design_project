package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;

public class ComplaintSubmissionHandler implements RequestHandler {

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {

        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        DataCommunicationDB.setSession(session);

        try {
            // All requests are to be within the try block -- START HERE
            Message answer = new Message();
            answer.setMessage("New Complaint");
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                Complaint complaint = new Complaint();
                complaint.setComplaintStatus("Open");
                complaint.setComplaintTitle(message.getComplaintTitle());
                complaint.setComplaintContent(message.getData());
                complaint.setPurchaseType(message.getPurchaseType());
                complaint.setDateOfComplaint(LocalDateTime.now());

                Customer customer = message.getCustomer();
                Customer existingCustomer = DataCommunicationDB.getCustomerByPersonalID(session, customer.getPersonalID());

                if (existingCustomer != null) {
                    System.out.println("Customer exists with ID: " + existingCustomer.getId());
                    complaint.setCustomer(existingCustomer);
                    complaint.setCustomerPId(existingCustomer.getPersonalID());
                    //TODO: AFTER UPDATING CUSTOMER ENTITY MAKE SURE IT WORKS:
                    //existingCustomer.getComplaints().add(complaint);
                    session.save(complaint);
                    session.update(existingCustomer);
                    System.out.println("Customer's complaints have been updated successfully");
                } else {
                    System.out.println("Customer does not exist. Creating new customer.");
                    complaint.setCustomer(customer);
                    complaint.setCustomerPId(customer.getPersonalID());
                    complaint.setDateOfComplaint(LocalDateTime.now());
                    //TODO: AFTER UPDATING CUSTOMER ENTITY MAKE SURE IT WORKS:
                    //customer.getComplaints().add(complaint);
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