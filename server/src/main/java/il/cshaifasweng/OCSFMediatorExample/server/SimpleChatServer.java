package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Car;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static il.cshaifasweng.OCSFMediatorExample.server.SimpleServer.getSessionFactory;


public class SimpleChatServer
{
	
	private static SimpleServer server;
    private static Session session;

    private static void generateCars() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Car car = new Car("MOO- " + random.nextInt(), 100000, 2000 + random.nextInt(19));
            session.save(car);

            //*The call to session.flush() updates the DB immediately without ending the transaction.
            //*Recommended to do after an arbitrary unit of work.
            //*MANDATORY to do if you are saving a large amount of data -otherwise you may get cache errors.

            session.flush();
        }
    }

    private static List<Car> getAllCars() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        query.from(Car.class);
        List<Car> data = session.createQuery(query).getResultList();
        return data;
    }

    private static void printAllCars() throws Exception {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            System.out.print("Id: ");

            System.out.print(car.getId());
            System.out.print(", License plate: ");
            System.out.print(car.getLicensePlate());
            System.out.print(", Price: ");
            System.out.print(car.getPrice());
            System.out.print(", Year: ");
            System.out.print(car.getYear());
            System.out.print('\n');
        }
    }
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
        System.out.println("server is listening");
        server.listen();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            SessionFactory sessionFactory = getSessionFactory(password);
            session = sessionFactory.openSession();
            session.beginTransaction();
            generateCars();
            printAllCars();
            session.getTransaction().commit(); // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
        }

    }
}
