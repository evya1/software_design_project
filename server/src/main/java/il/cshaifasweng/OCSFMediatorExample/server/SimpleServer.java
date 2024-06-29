package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private static Session session;

	public SimpleServer(int port) {
		super(port);

	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		MessageObject message = (MessageObject) msg;
		String request = message.getMsg();
		try {
			//we got an empty message, so we will send back an error message with the error details.
			if (request.isBlank()) {
				message.setMsg("Error! we got an empty message");
				client.sendToClient(message);
				System.out.println("Sent empty message");
			} else if (request.startsWith("show all movies")) {
				try {
					SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
					session = sessionFactory.openSession();
					List<Movie> movies = session.createQuery("FROM Movie", Movie.class).list();
					MessageObject answer = new MessageObject("showing all movies", movies);
					client.sendToClient(answer);
				} catch (Exception e) {
					System.err.println("An error occured");
					e.printStackTrace();
				} finally {
					assert session != null;
					session.close();
				}
			} else if (request.startsWith("change screening times of the movie")) {
				try {
					SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
					session = sessionFactory.openSession();
					DataCommunicationDB.setSession(session);
					// the client should put in the object of the message a new movieSlot such that the fields
					// take the same fields of the movieSlot we want to change screening times.
					MovieSlot movieSlot = (MovieSlot) message.getObject();
					DataCommunicationDB.modifyMovieSlotStartTime(movieSlot.getId(), movieSlot.getStartDateTime());
					DataCommunicationDB.modifyMovieSlotEndTime(movieSlot.getId(), movieSlot.getEndDateTime());

					MessageObject answer = new MessageObject("screening times of the movie updated");
					client.sendToClient(answer);
				} catch (Exception e) {
					System.err.println("An error occured");
					e.printStackTrace();
				} finally {
					assert session != null;
					session.close();
				}
			} else if (request.equals("update movies list")) {
				try {
					SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
					session = sessionFactory.openSession();
					List<Movie> movies = session.createQuery("FROM Movie", Movie.class).list();
					MessageObject answer = new MessageObject("all updated movies", movies);
					client.sendToClient(answer);
				} catch (Exception e) {
					System.err.println("An error occured");
					e.printStackTrace();
				} finally {
					assert session != null;
					session.close();
				}
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


