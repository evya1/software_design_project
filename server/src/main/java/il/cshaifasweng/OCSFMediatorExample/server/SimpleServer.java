package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.util.ArrayList;

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

import static il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB.*;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private static Session session;

	public SimpleServer(int port) {
		super(port);
		
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		msgOb message = (msgOb) msg;
		//Message message = (Message) msg;
		String request = message.getMsg();
		try {
			//we got an empty message, so we will send back an error message with the error details.
			if (request.isBlank()){
				message.setMsg("Error! we got an empty message");
				client.sendToClient(message);
				System.out.println("Sent empty message");
			}
			//we got a request to change submitters IDs with the updated IDs at the end of the string, so we save
			// the IDs at data field in Message entity and send back to all subscribed clients a request to update
			//their IDs text fields. An example of use of observer design pattern.
			//message format: "change submitters IDs: 123456789, 987654321"
			/*else if(request.startsWith("change submitters IDs:")){
				message.setData(request.substring(23));
				message.setMessage("update submitters IDs");
				sendToAllClients(message);
			}
			//we got a request to add a new client as a subscriber.
			else if (request.equals("add client")){
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
				message.setMessage("client added successfully");
				client.sendToClient(message);
			}
			//we got a message from client requesting to echo Hello, so we will send back to client Hello world!
			else if(request.startsWith("echo Hello")){
				message.setMessage("Hello World!");
				client.sendToClient(message);
			}*/
			else if(request.startsWith("show all movies")){
				try {
					SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
					session = sessionFactory.openSession();
					List<Movie> movies = session.createQuery("FROM Movie", Movie.class).list();
					msgOb answer = new msgOb("showing all movies",movies);
					client.sendToClient(answer);
				} catch (Exception e) {
					System.err.println("An error occured");
					e.printStackTrace();
				} finally {
					assert session != null;
					session.close();
				}
			}
			else if (request.startsWith("change screening times of the movie")){
				try{
					SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
					session = sessionFactory.openSession();
					// the client should put in the object of the message a new movie such that the fields
					// take the same fields of the movie we want to change screening times
					// apart from the field of the List of the movieSlot to be changed with the new time slots.
					Movie movie = (Movie) message.getObject();
					for (MovieSlot current : movie.getMovieScreeningTime()) {
						DataCommunicationDB.modifyMovieSlotStartTime(current.getId(), current.getStartDateTime());
						DataCommunicationDB.modifyMovieSlotEndTime(current.getId(), current.getEndDateTime());
					}
					msgOb answer = new msgOb("screening times of the movie updated");
					client.sendToClient(answer);
				} catch (Exception e) {
					System.err.println("An error occured");
					e.printStackTrace();
				} finally {
					assert session != null;
					session.close();
				}
			}
			else if (request.equals("update movies list")) {
				try {
					SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
					session = sessionFactory.openSession();
					// to be continued
				} catch (Exception e) {
					System.err.println("An error occured");
					e.printStackTrace();
				} finally {
					assert session != null;
					session.close();
				}
			}
			else if (request.startsWith("")){
				//add code here to multiply 2 numbers received in the message and send result back to client
				//(use substring method as shown above)
				//message format: "multiply n*m"
			}else{
				//add code here to send received message to all clients.
				//The string we received in the message is the message we will send back to all clients subscribed.
				//Example:
					// message received: "Good morning"
					// message sent: "Good morning"
				//see code for changing submitters IDs for help
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void sendToAllClients(Message message) {
		try {
			for (SubscribedClient SubscribedClient : SubscribersList) {
				SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
