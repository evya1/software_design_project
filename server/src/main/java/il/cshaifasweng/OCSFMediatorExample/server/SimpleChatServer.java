package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.ExpiredLinksChecker;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class SimpleChatServer {

    private static SimpleServer server;
    private static Session session;
    private static int port;

    private static String promptForDatabasePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your database password: \n");
        String password = scanner.nextLine().trim(); // Trims any extra whitespace
        System.out.println("Password entered: [" + password + "]"); // Debug output
        return password;
    }

    /**
     * Sets the server port from command-line arguments or defaults to 3000.
     *
     * Usage: "java -jar Server.jar 3005" to set port 3005.
     *
     * @param args Command-line arguments. The first argument can be a port number.
     */
    private static void setPort(String[] args) {
        port = (args.length > 0) ? Integer.parseInt(args[0]) : 3000;
    }

    public static void main(String[] args) throws IOException {
        setPort(args);
        System.out.println("The new port is : " + port);
        server = new SimpleServer(port);
        System.out.println("Server is listening");
        server.listen();
        SessionFactory sessionFactory;
        while (true) {
            try {
                String password = promptForDatabasePassword();
                sessionFactory = DataCommunicationDB.getSessionFactory(password);
                session = sessionFactory.openSession();

                DataCommunicationDB.setSession(session);
                DataCommunicationDB.setPassword(password);

                // If we get here, the password was correct
                break;

            } catch (AnnotationException annotationException) {
                System.err.println("Annotation error: " + annotationException.getMessage());
                annotationException.printStackTrace();
            } catch (JDBCConnectionException jdbcConnectionException) {
                System.err.println("Database connection failed: " + jdbcConnectionException.getMessage());
                jdbcConnectionException.printStackTrace();
            } catch (SQLGrammarException sqlGrammarException) {
                System.err.println("SQL syntax error: " + sqlGrammarException.getMessage());
                sqlGrammarException.printStackTrace();
            } catch (ConstraintViolationException constraintViolationException) {
                System.err.println("Constraint violation: " + constraintViolationException.getMessage());
                constraintViolationException.printStackTrace();
            } catch (TransactionException transactionException) {
                System.err.println("Transaction error: " + transactionException.getMessage());
                transactionException.printStackTrace();
            } catch (HibernateException hibernateException) {
                // Generic HibernateException to catch other unhandled exceptions
                System.err.println("Hibernate error: " + hibernateException.getMessage());
                hibernateException.printStackTrace();
            }
        }


        try {
            DataCommunicationDB.generateMovieList();
            DataCommunicationDB.createAndAssignMovieTicketsToCustomers();
            DataCommunicationDB.generateMovieTicketsAndComplaints();
            DataCommunicationDB.printAllEntities();

            Thread expiredLinksCheckerThread = new Thread(new ExpiredLinksChecker(sessionFactory));
            expiredLinksCheckerThread.start();

        } catch (Exception exception) {
            System.err.println("An error occurred: " + exception.getMessage());
            exception.printStackTrace();

        } finally {
            if (session != null) {
                session.close();
            }
        }

    }


}
