package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;
import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Booklet;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


public class NewPurchaseHandler implements RequestHandler{

    private static Session session;

    @Override
    public void handle(Message message, ConnectionToClient client) {// CHECKS WITH DB ID - IF EXISTS RETURN ID  (SEARCH BY PERSONAL ID), CREATE NEW CUSTOMER IF NOT EXIST, ADD BOOKLET INSTANCE TO  HIS PURCHASES
        if (message.getMessage().equals("New Booklet")) {
            Session session = null;
            Transaction tx = null;
            try {
                System.out.println("New purchase request received, Initializing check-ups with DB");
                session = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword()).openSession();
                tx = session.beginTransaction();

                Customer customer = (Customer) message.getCustomer();
                Booklet booklet = (Booklet) message.getBooklet();
                Customer existingCustomer = getCustomerByPersonalID(session, customer.getPersonalID());

                //NEED TO WORK ON PURCHASE AND CUSTOMER ENTITIES, THEN GENERATE CUSTOMER IN DB
                if (existingCustomer != null) {
                    System.out.println("Customer exists with ID: " + existingCustomer.getId());
                    booklet.setCustomer(existingCustomer);
                    session.save(booklet);
                    System.out.println("Existing Customer PID:" + existingCustomer.getPersonalID() + " booklet id:" + booklet.getId() + "fetching customerPID from booklet:" + booklet.getCustomer().getPersonalID());//check
                } else {
                    System.out.println("Customer does not exist. Creating new customer.");
                    session.save(customer);
                    booklet.setCustomer(customer);
                    session.save(booklet);
                    System.out.println("new customer PID:" + customer.getPersonalID() + " booklet id:" + booklet.getId() + " fetching customer PID from booklet" + booklet.getCustomer().getPersonalID()); //check
                }
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                System.err.println("An error occurred");
                e.printStackTrace();
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        //if(message.getData() == "Movie Ticket"){}  SHIRA

        //if(message.getData() == "Movie Link"){}
    }
    private Customer getCustomerByPersonalID(Session session, String personalID) {
        Query<Customer> query = session.createQuery("FROM Customer WHERE personalID = :personalID", Customer.class);
        query.setParameter("personalID", personalID);
        return query.uniqueResult();
    }
}
