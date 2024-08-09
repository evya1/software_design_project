package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.Scanner;

public class SimpleChatServer {

    private static SimpleServer server;
    private static Session session;
    private static int port;

    private static String promptForDatabasePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your database password: \n");
        return scanner.nextLine();
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

        while (true) {
            try {
                String password = promptForDatabasePassword();
                SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(password);
                session = sessionFactory.openSession();

                DataCommunicationDB.setSession(session);
                DataCommunicationDB.setPassword(password);

                // If we get here, the password was correct
                break;

            } catch (HibernateException exception) {
                System.err.println("Incorrect password. Please try again.");
            }
        }

        try {
//             DataCommunicationDB.generateMovieList();
//             DataCommunicationDB.createMockData();
            DataCommunicationDB.printAllEntities();

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
