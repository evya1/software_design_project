package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;


public class NewPurchaseHandler implements RequestHandler{

    private static Session session;

    @Override
    public void handle(Message message, ConnectionToClient client) {// CHECKS WITH DB ID - IF EXISTS RETURN ID  (SEARCH BY PERSONAL ID), CREATE NEW CUSTOMER IF NOT EXIST, ADD BOOKLET INSTANCE TO  HIS PURCHASES
        if (message.getData() == "Booklet") {
            try {
                System.out.println("New purchase request received");
            } catch (Exception e) {
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
}
