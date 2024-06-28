package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.io.IOException;
import java.util.Scanner;

public class SimpleChatServer
{
	
	private static SimpleServer server;
    private static Session session;

    public static void main(String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
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
