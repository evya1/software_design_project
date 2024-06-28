package il.cshaifasweng.OCSFMediatorExample.entities;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;

public class Initializer
{
    //Attributes
    private static Session session;


    //Session Methods
    public static Session getSession() {
        return session;
    }
    public static void setSession(Session session) {
        Initializer.session = session;
    }

    //Hibernate Method to start a session and declare all the entities classes being used throughout the program.
    public static SessionFactory getSessionFactory(String password) throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.password", password);

        //All Entities that are being used.
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Theater.class);
        configuration.addAnnotatedClass(Seat.class);
        configuration.addAnnotatedClass(MovieSlot.class);
        configuration.addAnnotatedClass(TypeOfMovie.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);

    }

    //Entities Methods
    public static void printAllEntities() {
        // Print all Theaters
        List<Theater> theaters = session.createQuery("FROM Theater", Theater.class).list();
        for (Theater theater : theaters) {
            System.out.println("Theater ID: " + theater.getTheaterNum() +
                    ", Num of Seats: " + theater.getNumOfSeats() +
                    ", Available Seats: " + theater.getAvailableSeats() +
                    ", Row Length: " + theater.getRowLength());

            // Print Seats
            List<Seat> seats = theater.getSeatList();
            for (Seat seat : seats) {
                System.out.println("\tSeat Number: " + seat.getSeatNum() +
                        ", Taken: " + seat.isTaken());
            }

            // Print MovieSlots
            List<MovieSlot> movieSlots = theater.getMovieTime();
            for (MovieSlot movieSlot : movieSlots) {
                System.out.println("\tMovieSlot ID: " + movieSlot.getId() +
                        ", Movie Title: " + movieSlot.getMovieTitle() +
                        ", Start Time: " + movieSlot.getStartDateTime() +
                        ", End Time: " + movieSlot.getEndDateTime());
            }
        }

        // Print all Movies
        List<Movie> movies = session.createQuery("FROM Movie", Movie.class).list();
        for (Movie movie : movies) {
            System.out.println("Movie ID: " + movie.getId() +
                    ", Name: " + movie.getMovieName() +
                    ", Main Cast: " + movie.getMainCast() +
                    ", Producer: " + movie.getProducer() +
                    ", Description: " + movie.getMovieDescription() +
                    ", Duration: " + movie.getMovieDuration());

            // Print TypeOfMovie
            TypeOfMovie typeOfMovie = movie.getUpcomingMovies();
            if (typeOfMovie != null) {
                System.out.println("\tTypeOfMovie ID: " + typeOfMovie.getId() +
                        ", Upcoming: " + typeOfMovie.isUpcoming() +
                        ", Currently Running: " + typeOfMovie.isCurrentlyRunning() +
                        ", Purchasable: " + typeOfMovie.isPurchasable() +
                        ", Release Date: " + typeOfMovie.getReleaseDate());
            }

            // Print MovieSlots
            List<MovieSlot> movieSlots = movie.getMovieScreeningTime();
            for (MovieSlot movieSlot : movieSlots) {
                System.out.println("\tMovieSlot ID: " + movieSlot.getId() +
                        ", Start Time: " + movieSlot.getStartDateTime() +
                        ", End Time: " + movieSlot.getEndDateTime() +
                        ", Theater ID: " + movieSlot.getTheater().getTheaterNum());
            }
        }
    }
    public static void generateMovieList()throws Exception{


        Random random = new Random();

        // Create a Theater
        Theater theater = new Theater(50, 50, new ArrayList<>(), new ArrayList<>(), 10);
        session.save(theater);
        session.flush();

        // Create a Movie
        Movie movie = new Movie("Inception", "Leonardo DiCaprio", null, "Christopher Nolan", "A mind-bending thriller", 148, new ArrayList<>(), null);


        session.flush();

        // Create TypeOfMovie
        TypeOfMovie typeOfMovie = new TypeOfMovie(false, true, true);
        session.save(typeOfMovie);
        session.flush();

        // Associate TypeOfMovie with Movie
        movie.setUpcomingMovies(typeOfMovie);
        session.save(movie);
        session.flush();

        List<MovieSlot> movieSlots = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            int year = random.nextInt(2024,2025);
            int month = random.nextInt(7,12);
            int day = random.nextInt(1,31);

            MovieSlot movieSlot = new MovieSlot(movie,LocalDateTime.of(year,month,day,10,0),
                    LocalDateTime.of(year,month,day,12,30),theater);
            movieSlots.add(movieSlot);
            session.save(movieSlot);
            session.flush();

        }

        // Create Seats
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < theater.getNumOfSeats(); i++) {
            Seat seat = new Seat(true, theater);
            seats.add(seat);
            session.save(seat);
            session.flush();
        }


        // Update Theater and Movie with MovieSlots and Seats
        theater.setSeatList(seats);
        theater.setMovieTime(movieSlots);
        movie.setMovieScreeningTime(movieSlots);
        session.save(theater);
        session.save(movie);
        session.flush();



    }
    public static void generateMovieList2() throws Exception {
        Random random = new Random();

        // Create a Theater
        Theater theater = new Theater(100, 100, new ArrayList<>(), new ArrayList<>(), 10);
        session.save(theater);
        session.flush();

        // Create 10 specific Movies
        Movie movie1 = new Movie("Inception", "Leonardo DiCaprio", null, "Christopher Nolan", "A mind-bending thriller", 148, new ArrayList<>(), null);
        Movie movie2 = new Movie("The Matrix", "Keanu Reeves", null, "Lana Wachowski, Lilly Wachowski", "A sci-fi action film", 136, new ArrayList<>(), null);
        Movie movie3 = new Movie("Interstellar", "Matthew McConaughey", null, "Christopher Nolan", "A journey through space", 169, new ArrayList<>(), null);
        Movie movie4 = new Movie("The Dark Knight", "Christian Bale", null, "Christopher Nolan", "A superhero film", 152, new ArrayList<>(), null);
        Movie movie5 = new Movie("Fight Club", "Brad Pitt", null, "David Fincher", "An underground fight club", 139, new ArrayList<>(), null);
        Movie movie6 = new Movie("Pulp Fiction", "John Travolta", null, "Quentin Tarantino", "A crime film", 154, new ArrayList<>(), null);
        Movie movie7 = new Movie("Forrest Gump", "Tom Hanks", null, "Robert Zemeckis", "A man's extraordinary life", 142, new ArrayList<>(), null);
        Movie movie8 = new Movie("The Shawshank Redemption", "Tim Robbins", null, "Frank Darabont", "A story of hope", 142, new ArrayList<>(), null);
        Movie movie9 = new Movie("Gladiator", "Russell Crowe", null, "Ridley Scott", "A Roman epic", 155, new ArrayList<>(), null);
        Movie movie10 = new Movie("The Godfather", "Marlon Brando", null, "Francis Ford Coppola", "A crime family saga", 175, new ArrayList<>(), null);

        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10);

        for (Movie movie : movies) {
            session.save(movie);
            session.flush();

            // Create TypeOfMovie
            TypeOfMovie typeOfMovie = new TypeOfMovie(false, true, true);
            session.save(typeOfMovie);
            session.flush();

            // Associate TypeOfMovie with Movie
            movie.setUpcomingMovies(typeOfMovie);
            session.save(movie);
            session.flush();

            // Create MovieSlots
            List<MovieSlot> movieSlots = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int year = random.nextInt(2) + 2024; // Year between 2024 and 2025
                int month = random.nextInt(6) + 7; // Month between 7 and 12
                int day = random.nextInt(28) + 1; // Ensuring valid day of month

                MovieSlot movieSlot = new MovieSlot(movie, LocalDateTime.of(year, month, day, 10, 0),
                        LocalDateTime.of(year, month, day, 12 , 0), theater);
                movieSlots.add(movieSlot);
                session.save(movieSlot);
                session.flush();
            }

            movie.setMovieScreeningTime(movieSlots);
            session.save(movie);
            session.flush();

            // Associate movieSlots with Theater
            theater.getMovieTime().addAll(movieSlots);
        }

        // Create Seats
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < theater.getNumOfSeats(); i++) {
            Seat seat = new Seat(false, theater);
            seats.add(seat);
            session.save(seat);
            session.flush();
        }

        // Update Theater with Seats
        theater.setSeatList(seats);
        session.save(theater);
        session.flush();
    }
    public static void printData() {
        List<Theater> theaters = session.createQuery("from Theater", Theater.class).list();
        for (Theater theater : theaters) {
            System.out.println("Theater ID: " + theater.getTheaterNum());
            System.out.println("Number of Seats: " + theater.getNumOfSeats());
            System.out.println("Available Seats: " + theater.getAvailableSeats());
            System.out.println("Seats:");
            for (Seat seat : theater.getSeatList()) {
                System.out.println("  Seat ID: " + seat.getSeatNum() + ", Available: " + !seat.isTaken());
            }

            System.out.println("Movie Slots:");
            for (MovieSlot slot : theater.getMovieTime()) {
                System.out.println("  Slot ID: " + slot.getId() + ", Movie: " + slot.getMovieTitle()
                        + ", Start Time: " + slot.getStartDateTime() + ", End Time: " + slot.getEndDateTime());
            }
            System.out.println();
        }

        List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
        for (Movie movie : movies) {
            System.out.println("Movie ID: " + movie.getId());
            System.out.println("Title: " + movie.getMovieName());
            System.out.println("Main Cast: " + movie.getMainCast());
            System.out.println("Producer: " + movie.getProducer());
            System.out.println("Description: " + movie.getMovieDescription());
            System.out.println("Duration: " + movie.getMovieDuration() + " minutes");

            if (movie.getUpcomingMovies() != null) {
                TypeOfMovie type = movie.getUpcomingMovies();
                System.out.println("Upcoming: " + type.isUpcoming() + ", Currently Running: " + type.isCurrentlyRunning() + ", Purchasable: " + type.isPurchasable());
            }

            System.out.println("Movie Slots:");
            for (MovieSlot slot : movie.getMovieScreeningTime()) {
                System.out.println("  Slot ID: " + slot.getId() + ", Start Time: " + slot.getStartDateTime()
                        + ", End Time: " + slot.getEndDateTime() + ", Theater ID: " + slot.getTheater().getTheaterNum());
            }
            System.out.println();
        }
    }

    //Work In Progress
    public static void modifyMovie(int movieId, String newMovieName, String newMainCast, String newProducer, String newMovieDescription, int newMovieDuration) {
        try {
            session.beginTransaction();

            // Retrieve the movie from the database
            Movie movie = session.get(Movie.class, movieId);
            if (movie == null) {
                System.out.println("Movie with ID " + movieId + " not found.");
                return;
            }

            // Modify the movie details
            movie.setMovieName(newMovieName);
            movie.setMainCast(newMainCast);
            movie.setProducer(newProducer);
            movie.setMovieDescription(newMovieDescription);
            movie.setMovieDuration(newMovieDuration);

            // Save the updated movie back to the database
            session.update(movie);
            session.getTransaction().commit();
            System.out.println("Movie with ID " + movieId + " has been updated.");
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }

    public static void main( String[] args ) {

    }
}