package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;
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
            DataCommunicationDB.setSession(session);
            DataCommunicationDB.setPassword(password);

//
//            System.out.print("The old screening is: " + DataCommunicationDB.getMovieByID(2).getScreeningTimeByID(12) + "\n");
//            LocalDateTime startTime = LocalDateTime.of(2025,10,12,14,30);
//            LocalDateTime endTime = LocalDateTime.of(2025,10,12,16,30);
//
//            DataCommunicationDB.modifyMovieSlotStartTime((DataCommunicationDB.getMovieByID(2).getScreeningTimeByID(12).getId()),startTime);
//
//            DataCommunicationDB.modifyMovieSlotEndTime((DataCommunicationDB.getMovieByID(2).getScreeningTimeByID(12).getId()),endTime);
//
//            System.out.print("The new screening is: " + DataCommunicationDB.getMovieByID(2).getScreeningTimeByID(12) + "\n");

            //System.out.print("Slot 13 " + DataCommunicationDB.getMovieSlotByID(13).getStartDateTime());
            //"2024-12-24 08:00:00.000000"


            DataCommunicationDB.generateMovieList();
            DataCommunicationDB.printAllEntities();

        } catch (Exception exception) {
            System.err.println("An error occured" + exception.getMessage());
            exception.printStackTrace();

        } finally {
            assert session != null;
            session.close();
        }

    }
}
