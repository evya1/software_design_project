package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.Scanner;

public class SimpleChatServer {

    private static SimpleServer server;
    private static Session session;
    private static int port;

    public static void main(String[] args) throws IOException {
        port = (args.length > 0) ? Integer.parseInt(args[0]) : 3000;// we want the port to be dynamic,
                                                                // Example of usage "java -jar Server.jar 3005"
        server = new SimpleServer(port);
        System.out.println("server is listening");
        server.listen();

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter your password of database: \n");
            String password = scanner.nextLine();
            SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(password);
            session = sessionFactory.openSession();
            session.beginTransaction();

            DataCommunicationDB.setSession(session);
            DataCommunicationDB.setPassword(password);
            DataCommunicationDB.generateMovieList();
            DataCommunicationDB.printAllEntities();

            session.getTransaction().commit(); // Save everything.

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();

        } finally {
            assert session != null;
            session.close();
        }

    }
}
