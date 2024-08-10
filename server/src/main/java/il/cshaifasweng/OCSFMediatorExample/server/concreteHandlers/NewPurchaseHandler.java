package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.InboxMessage;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

public class NewPurchaseHandler implements RequestHandler {

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        DataCommunicationDB.setSession(session);

        try {
            // All requests are to be within the try block -- START HERE
            Message answer = new Message();
            answer.setMessage("New Purchase");
            PriceConstants price = DataCommunicationDB.getPrices();
            // Checking if the request is to create a new purchase.
            if ("New Booklet".equals(message.getMessage().toString())) {
                handleNewPurchase(message, PurchaseType.BOOKLET, session,price);
            } else if ("New Movielink".equals(message.getMessage().toString())) {
                handleNewPurchase(message, PurchaseType.MOVIE_LINK, session,price);

            } else if ("New Movie Ticket".equals(message.getMessage().toString())) {
                handleNewPurchase(message, PurchaseType.MOVIE_TICKET, session,price);
            }

            answer.setData(message.getPurchase().getPurchaseType().toString());
            answer.setPurchase(message.getPurchase());
            client.sendToClient(answer);
            // All requests are to be within the try block -- END HERE
        } catch (Exception e) {
            System.err.println("An error occurred");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void handleNewPurchase(Message message, PurchaseType purchaseType, Session session, PriceConstants price) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Purchase purchase = new Purchase();
            purchase.SetPrice(price);
            purchase.setPurchaseType(purchaseType);
            purchase.setDateOfPurchase(LocalDateTime.now());
            purchase.setPriceByItem(purchaseType);

            InboxMessage inboxMessage = new InboxMessage();
            inboxMessage = setInboxMessage(inboxMessage, purchaseType, purchase);

            Customer customer = message.getCustomer();
            Customer existingCustomer = DataCommunicationDB.getCustomerByPersonalID(session, customer.getPersonalID());

            if (existingCustomer != null) {
                System.out.println("Customer exists with ID: " + existingCustomer.getId());
                purchase.setCustomer(existingCustomer);
                purchase.setCustomerPID(existingCustomer.getPersonalID());
                purchase.setDateOfPurchase(LocalDateTime.now());
                existingCustomer.getPurchases().add(purchase);
                inboxMessage.setCustomer(existingCustomer);
                existingCustomer.getInboxMessages().add(inboxMessage);
                session.save(inboxMessage);
                session.save(purchase);
                session.update(existingCustomer);
                System.out.println("Customer's purchases have been updated successfully");
            } else {
                System.out.println("Customer does not exist. Creating new customer.");
                purchase.setCustomer(customer);
                purchase.setCustomerPID(customer.getPersonalID());
                purchase.setDateOfPurchase(LocalDateTime.now());
                customer.getPurchases().add(purchase);
                inboxMessage.setCustomer(customer);
                customer.getInboxMessages().add(inboxMessage);
                session.save(inboxMessage);
                session.save(customer); // Save customer first to generate ID
                session.save(purchase); // Then save purchase to link it to customer
                System.out.println("Successfully created new customer.");
            }
            session.flush();

            message.setPurchase(purchase);
            setPurchaseEntity(purchase, purchaseType, session, message);
            session.update(purchase); // Update the purchase with the correct entity

            // Log for checking IDs
            if (purchaseType == PurchaseType.BOOKLET) {
                System.out.println("New booklet created with ID: " + purchase.getPurchasedBooklet().getId());
            } else if (purchaseType == PurchaseType.MOVIE_LINK) {
                System.out.println("New movie link created with ID: " + purchase.getPurchasedMovieLink().getId());
            } else if (purchaseType == PurchaseType.MOVIE_TICKET) {
                System.out.println("New movie ticket created with ID: " + purchase.getPurchasedMovieTicket().getId());
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("An error occurred");
            e.printStackTrace();
        }
    }

    private void setPurchaseEntity(Purchase purchase, PurchaseType purchaseType, Session session, Message message) {
        switch (purchaseType) {
            case BOOKLET:
                System.out.println("Booklet Purchase Call Received");
                Booklet booklet = new Booklet();
                session.save(booklet);
                purchase.setPurchasedBooklet(booklet);
                break;
            case MOVIE_LINK:
                MovieLink movieLink = new MovieLink();
                LocalDateTime currentTime = LocalDateTime.now();
                String movieName = message.getSpecificMovie().getMovieName();

                //Generating movie link, securely making a random string for the link and making sure there are no duplicates
                SecureRandom random = new SecureRandom();
                String hql = "FROM MovieLink";
                List<MovieLink> movieLinks = session.createQuery(hql, MovieLink.class).list();
                int flag = 0;
                String randomString = "";
                String newLink = "";

                while (true) {
                    //Making a secure random string of hexadecimal string
                    byte[] bytes = new byte[16 / 2];
                    random.nextBytes(bytes);
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : bytes) {
                        hexString.append(String.format("%02x", b));
                    }
                    randomString = hexString.toString();

                    newLink = "http://example.com/" + movieName + "/" + randomString;

                    for (MovieLink link : movieLinks) {
                        System.out.println("MovieLink ID: " + link.getId() + ", Link: " + link.getMovieLink());
                        if (link.getMovieLink() == newLink) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag==0){
                        break;
                    }
                    flag = 0;
                }

                movieLink.setCreationTime(currentTime);
                //movieLink.setExpirationTime(currentTime.plusDays(1));
                movieLink.setExpirationTime(currentTime.plusMinutes(1));
                movieLink.setCustomer_id(purchase.getCustomer().getId());
                movieLink.setMovieName(movieName);
                movieLink.setMovieLink(newLink);
                Movie movie = session.get(Movie.class, message.getSpecificMovie().getId());
                movieLink.setMovie(movie);

                session.save(movieLink);
                purchase.setPurchasedMovieLink(movieLink);
                break;

            case MOVIE_TICKET:
                MovieTicket movieTicket = new MovieTicket();
                session.save(movieTicket);
                purchase.setPurchasedMovieTicket(movieTicket);
                break;
            default:
                throw new IllegalArgumentException("Unknown purchase type: " + purchaseType);
        }
    }

    public InboxMessage setInboxMessage (InboxMessage inboxMessage, PurchaseType purchaseType, Purchase purchase){
        inboxMessage.setMessageTitle("New purchase");
        switch (purchaseType) {
            case BOOKLET:
                inboxMessage.setMessageContent("New Booklet purchased with 20 entries.");
                break;
            case MOVIE_LINK:
                inboxMessage.setMessageContent("New Movie Package purchased. We'll notify you before activating the link.");
                break;
            case MOVIE_TICKET:
                inboxMessage.setMessageContent("New Movie Ticket Purchased for " + purchase.getPurchasedMovieTicket().getMovieName());
                break;
        }
        return inboxMessage;
    }
}
