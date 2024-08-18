package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Seat;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.InboxMessage;
import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestTypes.*;

public class NewPurchaseHandler implements RequestHandler {
    private SimpleServer server;
    private Message localMessage;
    private Session session;

    public NewPurchaseHandler(SimpleServer server) {
        this.server = server;
    }

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        session = sessionFactory.openSession();
        DataCommunicationDB.setSession(session);
        localMessage = message;

        try {
            // All requests are to be within the try block -- START HERE
            Message answer = new Message();
            answer.setMessage("New Purchase");
            PriceConstants price = DataCommunicationDB.getPrices();
            // Checking if the request is to create a new purchase.
            if ("New Booklet".equals(message.getMessage())) {
                handleNewPurchase(message, PurchaseType.BOOKLET, session, price);
            } else if ("New Movielink".equals(message.getMessage().toString())) {
                handleNewPurchase(message, PurchaseType.MOVIE_LINK, session, price);

            } else if ("New Movie Ticket".equals(message.getMessage().toString())) {
                if (message.getChosenSeats() == null || message.getChosenSeats().isEmpty()) {
                    // Handle the case where no seats are chosen
                    System.err.println("No seats were chosen!");
                    return;
                }
                int size = message.getChosenSeats().size();
                for(int i = 0 ; i < size; i++) {
                    System.out.println("The current num of seats are : " + i);
                    handleNewPurchase(message, PurchaseType.MOVIE_TICKET, session,price);
                }
            }

            answer.setData(message.getPurchase().getPurchaseType().toString());
            answer.setPurchase(message.getPurchase());
            client.sendToClient(answer);

            if("New Movie Ticket".equals(message.getMessage().toString())) {
                answer.setMovieSlot(session.get(MovieSlot.class, localMessage.getMovieSlot().getId()));
                answer.setMessage(NEW_TICKETS);
                server.sendToAllClients(answer);
            }
            
            Customer customer = DataCommunicationDB.getCustomerByPersonalID(session, message.getCustomer().getPersonalID());
            session.beginTransaction();

            for (Purchase purchase : customer.getPurchases())
                System.out.println(purchase.getId());
            session.update(customer);
            session.getTransaction().commit();
            answer.setCustomer(customer);
            answer.setData("update the customers screen");
            answer.setMessage(GET_CUSTOMER_INFO);
            server.sendToAllClients(answer);

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

            //Generic information for all types of purchases
            Purchase purchase = new Purchase();
            purchase.SetPrice(price);
            purchase.setPurchaseType(purchaseType);
            purchase.setDateOfPurchase(LocalDateTime.now());
            purchase.setPriceByItem(purchaseType);

            //Creating an inbox message for the customer
            InboxMessage inboxMessage = new InboxMessage();
            inboxMessage = setInboxMessage(inboxMessage, purchaseType, purchase);


            //Checking if the customer exist otherwise create a new one.
            Customer customer = message.getCustomer();
            Customer existingCustomer = DataCommunicationDB.getCustomerByPersonalID(session, customer.getPersonalID());

            if (existingCustomer != null) {
                //Customer Exists.
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
            System.out.println("Entering the PurchaseEntity");
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
                    if (flag == 0) {
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
                session.flush();
                System.out.println("Movie Ticket Purchase Call Received");
                MovieTicket movieTicket = new MovieTicket();
                movieTicket.setBranch(session.get(Branch.class,localMessage.getMovieSlot().getBranch().getId()));
                movieTicket.setMovie(session.get(Movie.class,localMessage.getSpecificMovie().getId()));
                movieTicket.setMovieSlot(session.get(MovieSlot.class,localMessage.getMovieSlot().getId()));
                movieTicket.setMovieName(localMessage.getSpecificMovie().getMovieName());
                movieTicket.setBranchName(localMessage.getMovieSlot().getBranch().getBranchName());
                movieTicket.setTheaterNum(localMessage.getMovieSlot().getTheaterId());

                if (!message.getChosenSeats().isEmpty()) {
                    movieTicket.setSeatNum(message.getChosenSeats().getFirst().getSeatNum());
                    int rowNumber = (message.getChosenSeats().getFirst().getSeatNum() - 1) / 10 + 1;
                    movieTicket.setSeatRow(rowNumber);
                    Seat currentSeat = session.get(Seat.class, message.getChosenSeats().getFirst().getId());
                    currentSeat.setTaken(true);
                    movieTicket.setSeatID(currentSeat.getId());
                    
                    //Updating the MovieSlot number of available seats
                    MovieSlot slotToUpdate = session.get(MovieSlot.class, localMessage.getMovieSlot().getId());
                    slotToUpdate.decreaseSeat();
                    message.getChosenSeats().removeFirst();
                    
                }

                session.save(movieTicket);
                System.out.println("The current movie ticket: " + movieTicket.getId());
                purchase.setPurchasedMovieTicket(movieTicket);

                System.out.println("this is the current purchase: " + purchase.getPurchasedMovieTicket().getId());
                break;
            default:
                throw new IllegalArgumentException("Unknown purchase type: " + purchaseType);
        }
    }

    public InboxMessage setInboxMessage(InboxMessage inboxMessage, PurchaseType purchaseType, Purchase purchase) {
        inboxMessage.setMessageTitle("New purchase");
        switch (purchaseType) {
            case BOOKLET:
                inboxMessage.setMessageContent("New Booklet purchased with 20 entries.");
                break;
            case MOVIE_LINK:
                inboxMessage.setMessageContent("New Movie Package purchased. We'll notify you before activating the link.");
                break;
            case MOVIE_TICKET:
                inboxMessage.setMessageContent("New Movie Ticket Purchased for " + localMessage.getSpecificMovie().getMovieName() + ". The Ticket information can be found at your personal area under 'Purchases'. ");
                break;
        }
        return inboxMessage;
    }
}
