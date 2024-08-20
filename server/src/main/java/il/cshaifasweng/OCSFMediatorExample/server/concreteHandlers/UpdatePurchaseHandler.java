package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Seat;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Payment;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.InboxMessage;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.query.Query;
import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.PURCHASE_NOT_FOUND;
import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.UPDATE_PURCHASE;

public class UpdatePurchaseHandler implements RequestHandler {

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        Message answer = new Message();
        try {
            DataCommunicationDB.setSession(session);
            session.beginTransaction();

            if (message.getMessage().equals(UPDATE_PURCHASE)) {
                answer.setMessage(message.getMessage());
                Purchase updatePurchase = message.getPurchase();

                Purchase oldPurchase = DataCommunicationDB.getPurchaseByID(updatePurchase.getId());
                if (oldPurchase != null) {
                    // Update the purchase's cancelled status
                    oldPurchase.setCancelled(updatePurchase.isCancelled());

                    //releasing the purchased seat and updating the number of available seats in the theater
                    if (oldPurchase.getPurchaseType() == PurchaseType.MOVIE_TICKET) {
                        String hql = "FROM MovieSlot";
                        Query query = session.createQuery(hql);
                        List<MovieSlot> movieSlots = query.list();
                        for (MovieSlot movieSlot : movieSlots) {
                            if (movieSlot.getStartDateTime() == oldPurchase.getPurchasedMovieTicket().getSlot() &&
                                movieSlot.getBranch().getBranchName() == oldPurchase.getPurchasedMovieTicket().getBranchName()
                                && movieSlot.getTheater().getId() == oldPurchase.getPurchasedMovieTicket().getTheaterNum()
                                && movieSlot.getMovieTitle() == oldPurchase.getPurchasedMovieTicket().getMovieName()) {
                                movieSlot.increaseSeat();
                            }
                        }
                        Seat freeSeat = session.get(Seat.class, oldPurchase.getPurchasedMovieTicket().getSeatID());
                        freeSeat.setTaken(false);
                    }

                    // Adding message to customer's inbox
                    InboxMessage inboxMessage = new InboxMessage();
                    inboxMessage.setMessageTitle("Purchase Cancelled successfully");
                    inboxMessage.setMessageContent("Your Purchase has been cancelled successfully.");
                    inboxMessage.setCustomer(oldPurchase.getCustomer());
                    oldPurchase.getCustomer().getInboxMessages().add(inboxMessage);
                    // Save the Message and update customer
                    session.update(oldPurchase.getCustomer());
                    session.save(inboxMessage);
                    session.flush();

                    // Save the updated purchase
                    session.update(oldPurchase);
                    session.getTransaction().commit();
                    session.flush(); // Ensure changes are flushed to the database

                    answer.setPurchase(oldPurchase);
                    System.out.println(answer.getMessage());
                } else {
                    answer.setMessage(PURCHASE_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("An error occurred while handling the request");
            e.printStackTrace();
        } finally {
            client.sendToClient(answer);
        }

    }
}
