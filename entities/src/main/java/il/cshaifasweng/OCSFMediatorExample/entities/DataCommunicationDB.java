package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Chain;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Seat;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieGenre;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.TypeOfMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType.*;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.COMPLAINT_STATUS_CLOSED;
import static il.cshaifasweng.OCSFMediatorExample.entities.userRequests.ReportOperationTypes.COMPLAINT_STATUS_OPEN;
import static java.time.Month.*;

public class DataCommunicationDB {
    //region Attributes
    private static Session session;
    private static String password;
    //endregion

    //region Hibernate Session Methods
    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DataCommunicationDB.password = password;
    }

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        DataCommunicationDB.session = session;
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
        configuration.addAnnotatedClass(Branch.class);
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Chain.class);
        configuration.addAnnotatedClass(Report.class);
        configuration.addAnnotatedClass(Purchase.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Booklet.class);
        configuration.addAnnotatedClass(MovieLink.class);
        configuration.addAnnotatedClass(MovieTicket.class);
        configuration.addAnnotatedClass(Complaint.class);
        configuration.addAnnotatedClass(PriceConstants.class);
        configuration.addAnnotatedClass(InboxMessage.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);

    }

    //endregion

    //region Helper Methods

    public static void printAllEntities() {
        // Print all Theaters
        List<Theater> theaters = session.createQuery("FROM Theater", Theater.class).list();
        for (Theater theater : theaters) {
            System.out.println(theater);

            // Print MovieSlots
            List<MovieSlot> movieSlots = theater.getSchedule();
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
            TypeOfMovie typeOfMovie = movie.getMovieType();
            if (typeOfMovie != null) {
                System.out.println("\tTypeOfMovie ID: " + typeOfMovie.getId() +
                        ", Upcoming: " + typeOfMovie.isUpcoming() +
                        ", Currently Running: " + typeOfMovie.isCurrentlyRunning() +
                        ", Purchasable: " + typeOfMovie.isPurchasable() +
                        ", Release Date: " + typeOfMovie.getReleaseDate());
            }
        }

        // Print all Branches
        List<Branch> branches = session.createQuery("FROM Branch", Branch.class).list();
        for (Branch branch : branches) {
            System.out.println(branch);
            branch.printBranchManagerDetails();

            // Print Theaters in Branch
            List<Theater> branchTheaters = branch.getTheaterList();
            for (Theater branchTheater : branchTheaters) {
                System.out.println("\tTheater ID: " + branchTheater.getId() +
                        ", Num of Seats: " + branchTheater.getNumOfSeats());
            }

            // Print Report
            Report report = branch.getReport();
            if (report != null) {
                System.out.println("\tReport ID: " + report.getId());
            }
        }

        // Print all Employees
        List<Employee> employees = session.createQuery("FROM Employee", Employee.class).list();
        for (Employee employee : employees) {
            System.out.println(employee);

            // Print Branch In Charge if Theater Manager
            if (employee.getEmployeeType() == BRANCH_MANAGER) {
                Branch branchInCharge = employee.getBranchInCharge();
                if (branchInCharge != null) {
                    System.out.println("\tBranch In Charge: " + branchInCharge.getBranchName());
                }
            }
        }

        // Print all Reports
        List<Report> reports = session.createQuery("FROM Report", Report.class).list();
        for (Report report : reports) {
            System.out.println(report);
        }

        System.out.println("Print all the Branch Movies");
        printBranchMovies(1);
    }

    public static void generateMovieList() throws Exception {
        try {
            session.beginTransaction();
            Random random = new Random();

            // Create 10 specific Movies
            Movie movie1 = new Movie("Inception", "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Cillian Murphy, Marion Cotillard, Michael Caine", null, "Christopher Nolan", "Inception is a sci-fi thriller where a team of dream thieves led by Leonardo DiCaprio try to plant an idea in a CEO's mind by infiltrating his dreams.", 148, new ArrayList<>(), null, MovieGenre.DRAMA, "האשליה");
            Movie movie2 = new Movie("The Matrix", "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Hugo Weaving", null, "Lana Wachowski, Lilly Wachowski", "A computer hacker learns the world he lives in is a simulation and joins a rebellion against the machines.", 136, new ArrayList<>(), null, MovieGenre.COMEDY, "המטריקס");
            Movie movie3 = new Movie("Interstellar", "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine", null, "Christopher Nolan", "A team of astronauts travel through a wormhole in search of a new home for humanity.", 169, new ArrayList<>(), null, MovieGenre.DOCUMENTARY, "בין כוכבים");
            Movie movie4 = new Movie("The Dark Knight", "Christian Bale, Heath Ledger, Aaron Eckhart, Maggie Gyllenhaal", null, "Christopher Nolan", "Batman faces a new challenge from the Joker, who descends Gotham City into chaos.", 152, new ArrayList<>(), null, MovieGenre.DOCUMENTARY, "האביר האפל");
            Movie movie5 = new Movie("Fight Club", "Brad Pitt, Edward Norton, Helena Bonham Carter, Meat Loaf", null, "David Fincher", "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into something much, much more.", 139, new ArrayList<>(), null, MovieGenre.ACTION, "מועדון קרב");
            Movie movie6 = new Movie("Pulp Fiction", " John Travolta, Samuel L. Jackson, Uma Thurman, Bruce Willis", null, "Quentin Tarantino", "A hit man with a philosophical bent, a boxer on the fix, and their wives weave a darkly comedic tapestry.", 154, new ArrayList<>(), null, MovieGenre.COMEDY, "ספרות זולה");
            Movie movie7 = new Movie("Forrest Gump", "Tom Hanks, Robin Wright, Gary Sinise, Sally Field", null, "Robert Zemeckis", "A simple man with a low IQ but a big heart runs through key events in American history.", 142, new ArrayList<>(), null, MovieGenre.ADVENTURE, "פורסט גאמפ");
            Movie movie8 = new Movie("The Shawshank Redemption", "Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler", null, "Frank Darabont", "A wrongly convicted man plans his escape from prison over a long period of time.", 142, new ArrayList<>(), null, MovieGenre.COMEDY, "חומות של תקווה");
            Movie movie9 = new Movie("Gladiator", "Russell Crowe, Joaquin Phoenix, Connie Nielsen, Richard Harris", null, "Ridley Scott", "A former Roman general becomes a reluctant gladiator seeking revenge for the murder of his family.", 155, new ArrayList<>(), null, MovieGenre.HORROR, "הגלדיאטור");
            Movie movie10 = new Movie("The Godfather", "Marlon Brando, Al Pacino, James Caan, Robert Duvall", null, "Francis Ford Coppola", "The aging patriarch of a powerful Italian-American crime family tries to maintain control of his empire.", 175, new ArrayList<>(), null, MovieGenre.DRAMA, "הסנדק");

            // Adding images to the movies
            movie1.setImage(readImage("MoviePictures/Inception.jpg"));
            movie2.setImage(readImage("MoviePictures/The_Matrix.jpg"));
            movie3.setImage(readImage("MoviePictures/Interstellar.jpg"));
            movie4.setImage(readImage("MoviePictures/The_Dark_Knight.jpg"));
            movie5.setImage(readImage("MoviePictures/Fight_Club.jpg"));
            movie6.setImage(readImage("MoviePictures/Pulp_Fiction.jpg"));
            movie7.setImage(readImage("MoviePictures/Forrest_Gump.jpg"));
            movie8.setImage(readImage("MoviePictures/Shawshank.jpg"));
            movie9.setImage(readImage("MoviePictures/Gladiator.jpg"));
            movie10.setImage(readImage("MoviePictures/Godfather.jpg"));

            List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10);

            // Save all movies
            for (Movie movie : movies) {
                session.save(movie);
            }

            // create employees and save them in the DB
            Employee e1 = new Employee("Josh","Burns","Burns@gmail.com", "Base",
                    "Password", false, null, BASE, null);
            Employee e2 = new Employee("Jimmy","Cash", "sherotlko7ot@gmail.com",
                    "Service","Password", false, null, SERVICE, null);
            Employee e3 = new Employee("Sherlott","Chance", "tochen@gmail.com", "1",
                    "Content", false, null, CONTENT_MANAGER, null);
            Employee e4 = new Employee("Moki","Shtut", "CHM@gmail.com",
                    "CEO","Password", false, null, CHAIN_MANAGER, null);
            List<Employee> employees = Arrays.asList(e1, e2, e3, e4);
            for (Employee employee : employees) {
                session.save(employee);
            }

            //customer
            Customer customer1 = new Customer("Yossi","Smith", "csm@gmail.com", "123456789",
                    new ArrayList<Purchase>(), null, new ArrayList<Complaint>(), new ArrayList<InboxMessage>());
            Customer customer2 = new Customer("David","Freeman", "free@gmail.com", "345345345",
                    new ArrayList<Purchase>(), null, new ArrayList<Complaint>(), new ArrayList<InboxMessage>());
            Customer customer3 = new Customer("Abigail","Fox", "cfox@gmail.com", "888888888",
                    new ArrayList<Purchase>(), null, new ArrayList<Complaint>(), new ArrayList<InboxMessage>());
            Customer customer4 = new Customer("John","Al pacino", "godfather@gmail.com", "000000001",
                    new ArrayList<Purchase>(), null, new ArrayList<Complaint>(), new ArrayList<InboxMessage>());
            List<Customer> customers = Arrays.asList(customer1, customer2, customer3, customer4);
            for (Customer customer : customers) {
                session.save(customer);
            }


            //complaints

            Complaint comp = new Complaint("bad booklet", "i dont want the booklet anymore, i've used 5 tickets out of 20",
                    LocalDateTime.now(), "Open", BOOKLET, "000000001", customer4, -1);
            Complaint comp1 = new Complaint("bad link", "the link is not working",
                    LocalDateTime.now(), "Open", MOVIE_LINK, "000000001", customer4, -1);
            Complaint comp2 = new Complaint("bad ticket", "i don't want the ticket anymore",
                    LocalDateTime.now(), "Open", MOVIE_TICKET, "000000001", customer4, -1);
            Complaint comp3 = new Complaint("not good movie", "the dark knight is not as i expected",
                    LocalDateTime.now(), "Open", BOOKLET, "000000001", customer4, -1);
            Complaint comp4 = new Complaint("bad booklet", "the booklet is not working and i cant use it",
                    LocalDateTime.now(), COMPLAINT_STATUS_CLOSED, BOOKLET, "888888888", customer3, -1);
            Complaint comp5 = new Complaint("link corrupt", "the stream stops in the middle ",
                    LocalDateTime.now(), COMPLAINT_STATUS_OPEN, BOOKLET, "888888888", customer3, -1);
            Complaint comp6 = new Complaint("link corrupt", "the stream stops in the middle ",
                    LocalDateTime.now(), COMPLAINT_STATUS_OPEN, BOOKLET, "345345345", customer2, -1);
            List<Complaint> complaints = Arrays.asList(comp, comp1, comp2, comp3, comp4, comp5, comp6);
            for (Complaint complaint : complaints) {
                session.save(complaint);
            }

            // prices
            PriceConstants prices = new PriceConstants(250, 40, 30);
            session.save(prices);

            // Create 3 Branches

            String[] branchNames = {"Johns Cinema", "General Bay Cinema", "Selection Cinema"};
            Movie[][] branchMovies = {
                    {movie1, movie2}, // Johns Cinema
                    {movie3, movie4}, // General Bay Cinema
                    {movie5, movie6}  // Selection Cinema
            };

            for (int i = 0; i < branchNames.length; i++) {
                Branch branch = new Branch();
                branch.setBranchName(branchNames[i]);

                // Create Branch Manager
                Employee branchManager = new Employee();
                branchManager.setEmployeeType(BRANCH_MANAGER);
                branchManager.setEmail("manager" + (i + 1) + "@branch.com");
                branchManager.setFirstName("Manager" + (i + 1));
                branchManager.setLastName("Branch " + (i + 1));
                branchManager.setUsername("bm" + (i + 1));
                branchManager.setPassword("" + (i + 1));
                session.save(branchManager);

                branch.setBranchManager(branchManager);
                branchManager.setBranchInCharge(branch);
                session.save(branch);

                // Create Theaters for each Branch
                List<Theater> theaters = new ArrayList<>();
                for (int j = 1; j <= 3; j++) {
                    Theater theater = new Theater(70, new ArrayList<>(), 10);
                    theater.setBranch(branch);
                    theaters.add(theater);
                    session.save(theater);

                    // Assign unique movies to each branch
                    Movie[] moviesForBranch = branchMovies[i];
                    for (Movie movie : moviesForBranch) {
                        List<MovieSlot> movieSlots = new ArrayList<>();
                        for (int l = 0; l < 10; l++) {
                            int year = random.nextInt(2) + 2024; // Year between 2024 and 2025
                            int month = random.nextInt(6) + 7; // Month between 7 and 12
                            int day = random.nextInt(28) + 1; // Ensuring valid day of month

                            MovieSlot movieSlot = new MovieSlot(movie, LocalDateTime.of(year, month, day, 10, 0),
                                    LocalDateTime.of(year, month, day, 12, 0), theater);
                            movieSlot.setBranch(branch); // Set the branch for the movie slot

                            List<Seat> seats = new ArrayList<>(); // Set seats list
                            for (int a = 0; a < 70; a++) {
                                Seat seat = new Seat();
                                seat.setTheater(theater);
                                seat.setMovieSlot(movieSlot);
                                seat.setSeatNum(a + 1);
                                session.save(seat);
                                seats.add(seat);
                            }
                            movieSlot.setSeatList(seats);
                            movieSlots.add(movieSlot);
                            session.save(movieSlot);
                        }
                        movie.setMovieScreeningTime(movieSlots);
                        session.save(movie);

                        theater.getSchedule().addAll(movieSlots);
                    }
                    session.save(theater);
                }
                branch.setTheaterList(theaters);
                session.save(branch);
            }

            // Assign TypeOfMovie to each movie
            for (Movie movie : movies) {
                boolean inTheaterRandom, viewPackageRandom;
                boolean upcomingRandom = random.nextBoolean();
                if (upcomingRandom) {
                    inTheaterRandom = false;
                    viewPackageRandom = false;
                } else {
                    inTheaterRandom = true;
                    viewPackageRandom = random.nextBoolean();
                }
                TypeOfMovie typeOfMovie = new TypeOfMovie(upcomingRandom, inTheaterRandom, viewPackageRandom);
                session.save(typeOfMovie);
                session.flush();

                // Associate TypeOfMovie with Movie
                movie.setMovieType(typeOfMovie);
                session.save(movie);
                session.flush();
            }

            // Create and assign movie tickets to customers
            MovieTicket ticket1 = new MovieTicket(session.get(Movie.class, 1), session.get(Branch.class, 1), "Inception", "Johns Cinema", 2, 1, 1, session.get(MovieSlot.class, 24));
            MovieTicket ticket2 = new MovieTicket(session.get(Movie.class, 2), session.get(Branch.class, 1), "The Matrix", "Johns Cinema", 1, 1, 1, session.get(MovieSlot.class, 11));
            MovieTicket ticket3 = new MovieTicket(session.get(Movie.class, 3), session.get(Branch.class, 2), "Interstellar", "General Bay Cinema", 5, 1, 1, session.get(MovieSlot.class, 90));
            MovieTicket ticket4 = new MovieTicket(session.get(Movie.class, 4), session.get(Branch.class, 2), "The Dark Knight", "General Bay Cinema", 4, 2, 1, session.get(MovieSlot.class, 76));
            MovieTicket ticket5 = new MovieTicket(session.get(Movie.class, 1), session.get(Branch.class, 1), "Inception", "Johns Cinema", 1, 2, 1, session.get(MovieSlot.class, 5));
            List<MovieTicket> tickets = Arrays.asList(ticket1, ticket2, ticket3, ticket4, ticket5);
            for (MovieTicket ticket : tickets) {
                session.save(ticket);
            }

            assignTicketToCustomer(session.get(Customer.class, customer1.getId()), ticket1, session);
            assignTicketToCustomer(session.get(Customer.class, customer2.getId()), ticket2, session);
            assignTicketToCustomer(session.get(Customer.class, customer3.getId()), ticket3, session);
            assignTicketToCustomer(session.get(Customer.class, customer4.getId()), ticket4, session);
            assignTicketToCustomer(session.get(Customer.class, customer1.getId()), ticket5, session);

            // Create and assign movie links to customers
            MovieLink link1 = new MovieLink(movie1, "Inception", "http://example.com/inception", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
            MovieLink link2 = new MovieLink(movie2, "The Matrix", "http://example.com/matrix", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
            MovieLink link3 = new MovieLink(movie3, "Interstellar", "http://example.com/interstellar", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
            MovieLink link4 = new MovieLink(movie4, "The Dark Knight", "http://example.com/dark_knight", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
            MovieLink link5 = new MovieLink(movie4, "The Dark Knight", "http://example.com/dark_knight", LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
            link1.setActive();
            link2.setActive();
            link3.setActive();
            link4.setActive();
            link5.setInvalid();

            List<MovieLink> movieLinks = Arrays.asList(link1, link2, link3, link4, link5);
            for (MovieLink movieLink : movieLinks) {
                session.save(movieLink);
            }
            assignLinkToCustomer(session.get(Customer.class, customer1.getId()), link5, session);
            assignLinkToCustomer(session.get(Customer.class, customer1.getId()), link1, session);
            assignLinkToCustomer(session.get(Customer.class, customer2.getId()), link2, session);
            assignLinkToCustomer(session.get(Customer.class, customer3.getId()), link3, session);
            assignLinkToCustomer(session.get(Customer.class, customer4.getId()), link4, session);



            //create and assign Booklets
            Booklet booklet1 = new Booklet();
            Booklet booklet2 = new Booklet();
            Booklet booklet3 = new Booklet();
            Booklet booklet4 = new Booklet();
            List<Booklet> booklets = Arrays.asList(booklet1, booklet2, booklet3, booklet4);
            for (Booklet booklet : booklets) {
                session.save(booklet);
            }
            assignBookletToCustomer(session.get(Customer.class, customer1.getId()), booklet1, session);
            assignBookletToCustomer(session.get(Customer.class, customer2.getId()), booklet2, session);
            assignBookletToCustomer(session.get(Customer.class, customer3.getId()), booklet3, session);
            assignBookletToCustomer(session.get(Customer.class, customer4.getId()), booklet4, session);


            //create Inbox Messages
            InboxMessage in1 = new InboxMessage();
            in1.setMessageTitle("Ticket Purchased Successfully");
            in1.setMessageContent("Here's your ticket info");
            in1.setCustomer(customer1);
            session.save(in1);
            customer1.getInboxMessages().add(in1);
            session.save(customer1);
            session.flush();


            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }

    public static void createAndAssignMovieTicketsToCustomers() throws Exception {
        try {
            session.beginTransaction();
            Random random = new Random();

            // Create movie tickets for branch id 1 ("Johns Cinema")
            MovieTicket ticket1 = new MovieTicket(session.get(Movie.class, 1), session.get(Branch.class, 1), "Inception", "Johns Cinema", 2, 1, 1, session.get(MovieSlot.class, 24));
            MovieTicket ticket2 = new MovieTicket(session.get(Movie.class, 2), session.get(Branch.class, 1), "The Matrix", "Johns Cinema", 1, 1, 1, session.get(MovieSlot.class, 11));
            MovieTicket ticket5 = new MovieTicket(session.get(Movie.class, 1), session.get(Branch.class, 1), "Inception", "Johns Cinema", 1, 2, 1, session.get(MovieSlot.class, 5));

            // 3 additional tickets for "Johns Cinema" (branch id 1)
            MovieTicket ticket6 = new MovieTicket(session.get(Movie.class, 6), session.get(Branch.class, 1), "Pulp Fiction", "Johns Cinema", 3, 2, 1, session.get(MovieSlot.class, 35));
            MovieTicket ticket7 = new MovieTicket(session.get(Movie.class, 5), session.get(Branch.class, 1), "Fight Club", "Johns Cinema", 2, 2, 1, session.get(MovieSlot.class, 42));
            MovieTicket ticket8 = new MovieTicket(session.get(Movie.class, 7), session.get(Branch.class, 1), "Forrest Gump", "Johns Cinema", 4, 2, 1, session.get(MovieSlot.class, 55));

            // Create movie tickets for branch id 2 ("General Bay Cinema")
            MovieTicket ticket3 = new MovieTicket(session.get(Movie.class, 3), session.get(Branch.class, 2), "Interstellar", "General Bay Cinema", 5, 1, 1, session.get(MovieSlot.class, 90));
            MovieTicket ticket4 = new MovieTicket(session.get(Movie.class, 4), session.get(Branch.class, 2), "The Dark Knight", "General Bay Cinema", 4, 2, 1, session.get(MovieSlot.class, 76));

            // 3 additional tickets for "General Bay Cinema" (branch id 2)
            MovieTicket ticket9 = new MovieTicket(session.get(Movie.class, 8), session.get(Branch.class, 2), "The Shawshank Redemption", "General Bay Cinema", 1, 2, 1, session.get(MovieSlot.class, 101));
            MovieTicket ticket10 = new MovieTicket(session.get(Movie.class, 9), session.get(Branch.class, 2), "Gladiator", "General Bay Cinema", 3, 2, 1, session.get(MovieSlot.class, 115));
            MovieTicket ticket11 = new MovieTicket(session.get(Movie.class, 10), session.get(Branch.class, 2), "The Godfather", "General Bay Cinema", 5, 2, 1, session.get(MovieSlot.class, 120));

            // Save all tickets to the database
            List<MovieTicket> tickets = Arrays.asList(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6, ticket7, ticket8, ticket9, ticket10, ticket11);
            for (MovieTicket ticket : tickets) {
                session.save(ticket);
            }

            // Assign tickets to new or existing customers
            Customer newCustomer1 = new Customer("Sarah", "Connor", "sconnor@gmail.com", "987654321", new ArrayList<Purchase>(), null, new ArrayList<Complaint>(), new ArrayList<InboxMessage>());
            Customer newCustomer2 = new Customer("Kyle", "Reese", "kyle@gmail.com", "876543210", new ArrayList<Purchase>(), null, new ArrayList<Complaint>(), new ArrayList<InboxMessage>());

            session.save(newCustomer1);
            session.save(newCustomer2);

            // Assign tickets to customers
            assignTicketToCustomer(newCustomer1, ticket1, session);
            assignTicketToCustomer(newCustomer2, ticket2, session);
            assignTicketToCustomer(session.get(Customer.class, 3), ticket3, session);
            assignTicketToCustomer(session.get(Customer.class, 4), ticket4, session);
            assignTicketToCustomer(newCustomer1, ticket5, session);

            // Assign additional tickets to customers
            assignTicketToCustomer(newCustomer1, ticket6, session);
            assignTicketToCustomer(newCustomer2, ticket7, session);
            assignTicketToCustomer(session.get(Customer.class, 3), ticket8, session);
            assignTicketToCustomer(session.get(Customer.class, 4), ticket9, session);
            assignTicketToCustomer(newCustomer1, ticket10, session);
            assignTicketToCustomer(newCustomer2, ticket11, session);

            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }

    public static void printBranchMovies(int branchId) {
        List<Movie> movies = session.createQuery(
                        "SELECT DISTINCT ms.movie FROM MovieSlot ms " +
                                "JOIN ms.theater t " +
                                "JOIN t.branch b " +
                                "WHERE b.id = :branchId", Movie.class)
                .setParameter("branchId", branchId)
                .getResultList();
        System.out.println(movies.size());

        for (Movie movie : movies) {
            System.out.println("Movie ID: " + movie.getId() +
                    ", Name: " + movie.getMovieName() +
                    ", Main Cast: " + movie.getMainCast() +
                    ", Producer: " + movie.getProducer() +
                    ", Description: " + movie.getMovieDescription() +
                    ", Duration: " + movie.getMovieDuration());
        }
    }

    public static byte[] readImage(String imagePath) throws IOException {
        InputStream inputStream = DataCommunicationDB.class.getClassLoader().getResourceAsStream(imagePath);
        if (inputStream == null) {
            throw new IOException("File not found: " + imagePath);
        }
        return inputStream.readAllBytes();
    }

    private static void copyMovieDetails(Movie target, Movie source) {
        target.setMovieName(source.getMovieName());
        target.setHebrewMovieName(source.getHebrewMovieName());
        target.setMainCast(source.getMainCast());
        target.setImage(source.getImage());
        target.setProducer(source.getProducer());
        target.setMovieDescription(source.getMovieDescription());
        target.setMovieDuration(source.getMovieDuration());
        target.setMovieGenre(source.getMovieGenre());
        target.setMovieType(source.getMovieType());
    }

    //endregion

    //region Public Getters
    public static List<MovieSlot> getScreeningTimesByMovie(Session session, int movieId) {
        return session.createQuery(
                        "SELECT ms FROM MovieSlot ms " +
                                "WHERE ms.movie.id = :movieId", MovieSlot.class)
                .setParameter("movieId", movieId)
                .getResultList();
    }

    public static PriceConstants getPrices() {
        List<PriceConstants> prices = null;
        try {
            session.beginTransaction();
            prices = session.createQuery("FROM PriceConstants ", PriceConstants.class).list();
            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
        return prices.get(0);
    }

    public static Movie getMovieByID(int movieID) {
        return session.get(Movie.class, movieID);
    }

    public static MovieSlot getMovieSlotByID(int movieSlotID) {
        return session.get(MovieSlot.class, movieSlotID);
    }

    public static Theater getTheaterByID(int theaterID) {
        return session.get(Theater.class, theaterID);
    }

    public static Branch getBranchByID(int branchID) {
        return session.get(Branch.class, branchID);
    }

    public static TypeOfMovie getTypeOfMovieByID(int movieTypeID) {
        return session.get(TypeOfMovie.class, movieTypeID);
    }

    public static Seat getSeatByID(int seatID) {
        return session.get(Seat.class, seatID);
    }

    public static List<Branch> getBranches() {
        List<Branch> branches = null;
        try {
            session.beginTransaction();

            branches = session.createQuery("FROM Branch", Branch.class).list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
        return branches;
    }

    public static List<MovieSlot> getMovieSlotsByBranch(int branchId) {
        Transaction transaction = null;
        List<MovieSlot> movieSlots = null;
        try {
            transaction = session.beginTransaction();
            movieSlots = session.createQuery(
                            "FROM MovieSlot ms WHERE ms.branch.id = :branchId", MovieSlot.class)
                    .setParameter("branchId", branchId)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return movieSlots;
    }

    public static List<MovieSlot> getMovieSlotsByBranchIDAndMovieID(int movieID, int branchID) {
        Transaction transaction = null;
        List<MovieSlot> movieSlots = null;
        try {
            transaction = session.beginTransaction();
            movieSlots = session.createQuery(
                            "FROM MovieSlot ms WHERE ms.movie.id = :movie_Id AND ms.branch.id = :branch_Id", MovieSlot.class)
                    .setParameter("movie_Id", movieID)
                    .setParameter("branch_Id", branchID)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();  // Log the exception for debugging
        }
        return movieSlots;
    }

    public static List<Branch> getBranchesByMovieID(int movieID) {
        Transaction transaction = null;
        List<Branch> branches = null;
        try {
            transaction = session.beginTransaction();
            branches = session.createQuery(
                            "SELECT DISTINCT ms.branch FROM MovieSlot ms WHERE ms.movie.id = :movie_Id", Branch.class)
                    .setParameter("movie_Id", movieID)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();  // Log the exception for debugging
        }
        return branches;
    }

    public static List<Movie> getMoviesByBranch(int branchId) {
        Transaction transaction = null;
        List<Movie> movies = null;
        try {
            transaction = session.beginTransaction();
            movies = session.createQuery(
                            "SELECT DISTINCT ms.movie FROM MovieSlot ms WHERE ms.branch.id = :branchId", Movie.class)
                    .setParameter("branchId", branchId)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return movies;
    }

    // Method to get Customer by personal ID
    public static Customer getCustomerByPersonalID(Session session, String personalID) {
        Query<Customer> query = session.createQuery("FROM Customer WHERE personalID = :personalID", Customer.class);
        query.setParameter("personalID", personalID);
        return query.uniqueResult();
    }

    public static List<InboxMessage> getInboxMessagesByCustomerId(int customerId) {
        //Assuming transaction was already started.
        String hql = "FROM InboxMessage WHERE customer.id = :customerId";
        return session.createQuery(hql, InboxMessage.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public static Purchase getPurchaseByID(int id) {
        Query<Purchase> query = session.createQuery("FROM Purchase WHERE id = :id", Purchase.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    public static List<Employee> getAllEmployees() {
        //Assume Transaction is already open
        return (List<Employee>) session.createQuery("FROM Employee").list();
    }

    public static List<Theater> getAllTheaters() {
        return (List<Theater>) session.createQuery("From Theater").list();
    }

    public static List<Employee> getAllBranchManagersEmployees() {
        //Assume transaction is already open.
        String hql = "FROM Employee WHERE employeeType = :employeeType";
        Query query = session.createQuery(hql);
        query.setParameter("employeeType", BRANCH_MANAGER);
        return query.list();
    }
    //endregion

    //region Public Delete and Update methods
    public static void updateMovieDetails(Movie source) {
        Transaction transaction = null;
        try {
            if (session == null) {
                throw new IllegalStateException("Session has not been initialized.");
            }

            transaction = session.beginTransaction();
            Movie movie = getMovieByID(source.getId());

            if (movie == null) {
                System.out.println("LOG: Movie with ID " + source.getId() + " not found.");
                return;
            } else {
                System.out.println("LOG: Movie with ID " + source.getId() + " was found.");
            }

            // Copy details from source to the managed entity
            copyMovieDetails(movie, source);

            // Merge the source movie with the managed movie
            session.merge(movie);

            // Commit the transaction to save the changes
            transaction.commit();
        } catch (Exception exception) {

            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            throw exception;
        }
    }

    public static void deleteMovieById(int movieId) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Movie movie = session.get(Movie.class, movieId);
            if (movie != null) {
                List<MovieSlot> slotsToDelete = getScreeningTimesByMovie(session,movie.getId());
                for (MovieSlot slot : slotsToDelete) {
                    try {
                        int slotID = slot.getId();
                        slot = getMovieSlotByID(slotID);

                        if (slot == null) {
                            System.err.println("MovieSlot with ID " + slotID + " does not exist.");
                            session.getTransaction().rollback();
                            return;
                        }

                        // Delete all related seats
                        List<Seat> seats = slot.getSeatList();
                        for (Seat seat : seats) {
                            session.remove(seat);
                        }
                        slot.getSeatList().clear();

                        // Detach the MovieSlot from Movie
                        Movie movieToDetach = slot.getMovie();
                        if (movieToDetach != null) {
                            movieToDetach.getMovieScreeningTime().remove(slot);
                            slot.setMovie(null);
                        } else {
                            System.out.println("No associated movie found for this MovieSlot.");
                        }

                        // Detach the MovieSlot from Theater
                        Theater theater = slot.getTheater();
                        if (theater != null) {
                            theater.getSchedule().remove(slot);
                            slot.setTheater(null);
                        } else {
                            System.out.println("No associated theater found for this MovieSlot.");
                        }

                        // Detach the MovieSlot from Branch
                        Branch branch = slot.getBranch();
                        if (branch != null) {
                            slot.setBranch(null);
                        } else {
                            System.out.println("No associated branch found for this MovieSlot.");
                        }

                        // Finally, delete the MovieSlot
                        session.remove(session.contains(slot) ? slot : session.merge(slot));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                movie = session.get(Movie.class, movieId);
                session.delete(movie);
                System.out.println("Movie with ID " + movieId + " was deleted successfully.");
            } else {
                System.out.println("Movie with ID " + movieId + " not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void updateEmployeeData(Employee updateEmployee) {
        //Assume Transaction is already open
        Employee currentEmployee = session.get(Employee.class, updateEmployee.getId());

        currentEmployee.setFirstName(updateEmployee.getFirstName());
        currentEmployee.setLastName(updateEmployee.getLastName());
        currentEmployee.setEmail(updateEmployee.getEmail());
        currentEmployee.setPassword(updateEmployee.getPassword());
        currentEmployee.setUsername(updateEmployee.getUsername());
        currentEmployee.setBranch(updateEmployee.getBranch());
        currentEmployee.setBranchInCharge(updateEmployee.getBranchInCharge());
        currentEmployee.setEmployeeType(updateEmployee.getEmployeeType());
        currentEmployee.setBranch(updateEmployee.getBranch());
        session.getTransaction().commit();
    }

    public static void deleteEmployee(Employee employeeToDelete) {
        Employee delete = session.get(Employee.class, employeeToDelete.getId());
        if (delete != null) {
            // Check if the employee is referenced as a branch manager
            if (delete.getBranchInCharge() != null) {
                Branch branch = delete.getBranchInCharge();
                if (branch.getBranchManager() != null && branch.getBranchManager().getId() == delete.getId()) {
                    branch.setBranchManager(null);
                    session.saveOrUpdate(branch); // Ensure the update is flushed to the database
                }
            }

            // Attempt to delete the employee after removing all references
            session.delete(delete);
            session.getTransaction().commit();
        }
    }

    public static void updateMovieSlot(MovieSlot slot) {
        try {
            session.beginTransaction();
            MovieSlot slotToUpdate = getMovieSlotByID(slot.getId());

            slotToUpdate.setBranch(slot.getBranch());
            slotToUpdate.setTheater(slot.getTheater());
            slotToUpdate.setStartDateTime(slot.getStartDateTime());
            slotToUpdate.setEndDateTime(slot.getEndDateTime());
            session.saveOrUpdate(slotToUpdate);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            e.printStackTrace();
        }
    }

    public static void removeSlotFromMovie(MovieSlot slot) {
        try {
            session.beginTransaction();
            Movie movie = getMovieByID(slot.getMovie().getId());
            movie.getMovieScreeningTime().remove(slot);
            session.update(movie);
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            e.printStackTrace();
        }
    }

    public static void deleteMovieSlot(MovieSlot slot) {
        try {
            session.beginTransaction();

            int slotID = slot.getId();
            slot = getMovieSlotByID(slotID);

            if (slot == null) {
                System.err.println("MovieSlot with ID " + slotID + " does not exist.");
                session.getTransaction().rollback();
                return;
            }

            // Delete all related seats
            List<Seat> seats = slot.getSeatList();
            for (Seat seat : seats) {
                session.remove(seat);
            }
            slot.getSeatList().clear();

            // Detach the MovieSlot from Movie
            Movie movie = slot.getMovie();
            if (movie != null) {
                movie.getMovieScreeningTime().remove(slot);
                slot.setMovie(null);
            } else {
                System.out.println("No associated movie found for this MovieSlot.");
            }

            // Detach the MovieSlot from Theater
            Theater theater = slot.getTheater();
            if (theater != null) {
                theater.getSchedule().remove(slot);
                slot.setTheater(null);
            } else {
                System.out.println("No associated theater found for this MovieSlot.");
            }

            // Detach the MovieSlot from Branch
            Branch branch = slot.getBranch();
            if (branch != null) {
                slot.setBranch(null);
            } else {
                System.out.println("No associated branch found for this MovieSlot.");
            }

            // Finally, delete the MovieSlot
            session.remove(session.contains(slot) ? slot : session.merge(slot));

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            e.printStackTrace();
        }
    }


    //endregion

    //region Public Creation Methods
    public static void addMovieSlotToMovie(int movieID, MovieSlot slot) {

        try {
            session.beginTransaction();
            Movie movie = getMovieByID(movieID);

            if (movie.getMovieScreeningTime() == null) {
                movie.setMovieScreeningTime(new ArrayList<>());
            }
            movie.getMovieScreeningTime().add(slot);
            session.update(movie);
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            e.printStackTrace();
        }

    }


    public static void addMessageToCustomers() {
        session.beginTransaction();
        try {
            List<Customer> customers = session.createQuery(
                    "SELECT c FROM Customer c " +
                            "JOIN c.purchases p " +
                            "JOIN p.purchasedBooklet b " +
                            "WHERE p.purchaseType = il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType.BOOKLET " +
                            "AND b.numOfEntries > 0",
                    Customer.class
            ).getResultList();

            //Going over the customers and adding messages to their inbox.
            for (Customer customer : customers) {
                InboxMessage newContent = new InboxMessage();
                newContent.setMessageContent("New movie was added to the theaters");
                newContent.setMessageTitle("New Movie");
                newContent.setCustomer(customer);
                session.save(newContent);
                customer.getInboxMessages().add(newContent);
                session.update(customer);
                session.flush();  // Synchronize session state with the database
                session.clear();  // Clear the session cache to avoid issues in the next iteration
            }

            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }


    public static void createMovieSlot(MovieSlot slot) {
        try {
            session.beginTransaction();

            // Create seats and associate them with the MovieSlot
            List<Seat> seats = new ArrayList<>();
            for (int i = 0; i < 70; i++) {
                Seat seat = new Seat(false, slot.getTheater());
                seat.setMovieSlot(slot);  // Set the MovieSlot reference in Seat
                seat.setSeatNum(i + 1);   //70 seats
                seats.add(seat);
            }

            // Associate the seat list with the MovieSlot
            Movie movie = getMovieByID(slot.getMovie().getId());
            Theater theater = getTheaterByID(slot.getTheaterId());
            Branch branch = getBranchByID(slot.getBranchId());
            slot.setMovie(movie);
            slot.setSeatList(seats);
            slot.setBranch(branch);
            slot.setTheater(theater);

            // Save the MovieSlot (cascading will save the seats as well)
            session.save(slot);

            session.flush();
            session.getTransaction().commit();
            System.out.println("Movie Slot was created successfully. Movie Slot ID is " + slot.getId());
        } catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }


    public static void createNewMovie(Movie movieToAdd) {
        try {
            session.beginTransaction();

            Movie movie = new Movie();
            copyMovieDetails(movie, movieToAdd);

            session.save(movie);
            session.getTransaction().commit();
            System.out.println("Movie was created successfully. Movie ID is " + movie.getId());
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }

    public static void createMockData() {
        SessionFactory sessionFactory = getSessionFactory(password);
        session = sessionFactory.openSession();
        session.beginTransaction();

        List<Branch> branches = createAllBranches();
        List<Theater> theaters = createAllTheatersForBranches(branches);

        createSeatsForAllTheaters(theaters);
        createAdditionalEmployees(branches);
        createCustomersWithPurchases(theaters);

        session.getTransaction().commit();
    }

    public static void createEmployee(Employee newEmployee) {
        //Assume Transaction is already open
        session.save(newEmployee);
        session.getTransaction().commit();
    }

    public static void createNewBranch(Branch branch) {
        //assume transaction is already open.
        session.save(branch);
        session.getTransaction().commit();
    }


    //endregion

    //region Private Methods
    private static void addInboxMessageForMovie(Customer customer) {
        InboxMessage newContent = new InboxMessage();
        newContent.setMessageContent("New movie was added to the theaters");
        newContent.setMessageTitle("New Movie");
        newContent.setCustomer(customer);
        if (customer.getInboxMessages() == null) {
            List<InboxMessage> messages = new ArrayList<>();
            messages.add(newContent);
            customer.setInboxMessages(messages);
        } else {
            customer.getInboxMessages().add(newContent);
        }
    }

    private static Employee createEmployee(String firstName, String lastName, EmployeeType employeeType, String email, String username, String password) {
        Employee emp = createEmployee(firstName, lastName, employeeType, email, username);
        emp.setPassword(password);
        session.save(emp);
        return emp;
    }

    private static Employee createEmployee(String firstName, String lastName, EmployeeType employeeType, String email, String username) {
        Employee employee = new Employee();
        employee.setEmployeeType(employeeType);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setUsername(username);
        employee.setPassword("password");
        employee.setActive(false);

        session.save(employee);
        return employee;
    }

    private static MovieTicket createMovieTicket(Theater theater, String movieName, String dateTime) {
        MovieTicket ticket = new MovieTicket();
        ticket.setMovieName(movieName);
        ticket.setBranchName(theater.getBranch().getBranchName());
        ticket.setTheaterNum(theater.getId()); // Use getId() for theater number
        ticket.setSeatNum(1); // Assume seat 1 for simplicity
        ticket.setSeatRow(1);
//        ticket.setMovieSlot(new MovieSlot()); // Assume a new MovieSlot for simplicity
//        ticket.getMovieSlot().setStartDateTime(LocalDateTime.parse(dateTime));

        session.save(ticket);
        return ticket;
    }

    private static List<Branch> createAllBranches() {
        List<Branch> branches = new ArrayList<>();
        branches.add(createBranchWithManager("Main Branch", "Jane", "Smith", "jane.smith@example.com", "janesmith"));
        branches.add(createBranchWithManager("Second Branch", "Bob", "White", "bob.white@example.com", "bobwhite"));
        branches.add(createBranchWithManager("Third Branch", "Alice", "Green", "alice.green@example.com", "alicegreen"));
        branches.add(createBranchWithManager("Fourth Branch", "Chris", "Brown", "chris.brown@example.com", "chrisbrown"));
        branches.add(createBranchWithManager("Fifth Branch", "Emma", "Blue", "emma.blue@example.com", "emmablue"));
        return branches;
    }

    private static List<Theater> createAllTheatersForBranches(List<Branch> branches) {
        List<Theater> theaters = new ArrayList<>();
        theaters.addAll(createTheatersForBranch(branches.get(0), 3)); // Main Branch: 3 theaters
        theaters.addAll(createTheatersForBranch(branches.get(1), 4)); // Second Branch: 4 theaters
        theaters.addAll(createTheatersForBranch(branches.get(2), 7)); // Third Branch: 7 theaters
        theaters.addAll(createTheatersForBranch(branches.get(3), 4)); // Fourth Branch: 4 theaters
        theaters.addAll(createTheatersForBranch(branches.get(4), 11)); // Fifth Branch: 11 theaters
        return theaters;
    }

    private static List<Theater> createTheatersForBranch(Branch branch, int numTheaters) {
        List<Theater> theaters = new ArrayList<>();
        for (int i = 0; i < numTheaters; i++) {
            theaters.add(createTheaterForBranch(branch));
        }
        return theaters;
    }

    private static void createSeatsForAllTheaters(List<Theater> theaters) {
        for (Theater theater : theaters) {
            createSeatsForTheater(theater);
        }
    }

    private static Branch createBranchWithManager(String branchName, String firstName, String lastName, String email, String username) {
        Employee branchManager = createEmployee(firstName, lastName, BRANCH_MANAGER, email, username, "1");

        Branch branch = new Branch();
        branch.setBranchName(branchName);
        branch.setBranchManager(branchManager);
        branchManager.setBranchInCharge(branch);

        branch.setTheaterList(new ArrayList<>());

        session.save(branchManager);
        session.save(branch);
        return branch;
    }

    private static Theater createTheaterForBranch(Branch branch) {
        Theater theater = new Theater(70, new ArrayList<>(), 10);
        theater.setBranch(branch);
        session.save(theater);
        return theater;
    }

    private static void createSeatsForTheater(Theater theater) {
        for (int i = 1; i <= 100; i++) {
            Seat seat = new Seat(i, false, theater); // Default taken state is false
            session.save(seat);
        }
    }

    private static void createAdditionalEmployees(List<Branch> branches) {
        addEmployeeToBranch(branches.get(0), "Joe", "Black", SERVICE, "joe.black@example.com", "JB");
        addEmployeeToBranch(branches.get(1), "Anna", "Blue", CONTENT_MANAGER, "anna.blue@example.com", "AB");
        addEmployeeToBranch(branches.get(2), "Mark", "Red", CHAIN_MANAGER, "mark.red@example.com", "MR");
        addEmployeeToBranch(branches.get(3), "Nina", "Yellow", BRANCH_MANAGER, "nina.yellow@example.com", "NY");
    }

    private static void addEmployeeToBranch(Branch branch, String firstName, String lastName, EmployeeType employeeType, String email, String username) {
        Employee employee = createEmployee(firstName, lastName, employeeType, email, username, "1");
        branch.addEmployee(employee);
        session.save(employee);
    }

    private static void createCustomersWithPurchases(List<Theater> theaters) {
        List<Customer> customers = createCustomers();
        assignTicketsToCustomers(customers, theaters);
    }

    private static Customer createNewCustomer(String firstName, String lastName, String email, String personalID) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPersonalID(personalID);
        customer.setPurchases(new ArrayList<>()); // Initialize empty purchases list
        customer.setComplaints(new ArrayList<>()); // Initialize empty complaints list

        session.save(customer);
        return customer;
    }

    private static List<Customer> createCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(createNewCustomer("Charlie", "Red", "charlie.red@example.com", "123456700"));
        customers.add(createNewCustomer("Diana", "Yellow", "diana.yellow@example.com", "987654321"));
        customers.add(createNewCustomer("Eve", "Green", "eve.green@example.com", "456789123"));
        customers.add(createNewCustomer("Frank", "Blue", "frank.blue@example.com", "654321987"));
        customers.add(createNewCustomer("Grace", "Brown", "grace.brown@example.com", "321654987"));
        return customers;
    }

    private static void assignTicketsToCustomers(List<Customer> customers, List<Theater> theaters) {
        assignTicketsToCustomer(customers.get(0), theaters.get(0), theaters.get(1), theaters.get(2));
        assignTicketsToCustomer(customers.get(1), theaters.get(3), theaters.get(4), theaters.get(5));
        assignTicketsToCustomer(customers.get(2), theaters.get(6), theaters.get(7), theaters.get(8));
        assignTicketsToCustomer(customers.get(3), theaters.get(9), theaters.get(10), theaters.get(11));
        assignTicketsToCustomer(customers.get(4), theaters.get(12), theaters.get(13), theaters.get(14));
    }

    private static void assignTicketsToCustomer(Customer customer, Theater theater1, Theater theater2, Theater theater3) {
        assignTicketToCustomer(customer, createMovieTicket(theater1, "Inception", "2024-08-01T20:00"), session);
        assignTicketToCustomer(customer, createMovieTicket(theater2, "Interstellar", "2024-08-02T20:00"), session);
        assignTicketToCustomer(customer, createMovieTicket(theater3, "The Dark Knight", "2024-08-03T20:00"), session);
    }

    private static void assignTicketToCustomer(Customer customer, MovieTicket ticket, Session session) {
        Purchase purchase = new Purchase();
        purchase.setCustomerPID(customer.getPersonalID());
        purchase.setPurchasedMovieTicket(ticket);
        purchase.setPurchaseType(MOVIE_TICKET);
        purchase.SetPrice(session.get(PriceConstants.class, 1));
        purchase.setPriceByItem(purchase.getPurchaseType());
        purchase.setDateOfPurchase(LocalDateTime.now());
        purchase.setBranch(ticket.getBranch());
        purchase.setCustomer(customer);
        customer.addPurchase(purchase);
        session.save(purchase);
        session.save(customer);
    }

    private static void assignLinkToCustomer(Customer customer, MovieLink link, Session session) {
        link.setCustomer_id(customer.getId());
        Purchase purchase = new Purchase();
        purchase.setCustomerPID(customer.getPersonalID());
        purchase.setPurchasedMovieLink(link);
        purchase.setPurchaseType(MOVIE_LINK);
        purchase.SetPrice(session.get(PriceConstants.class, 1));
        purchase.setPriceByItem(purchase.getPurchaseType());
        purchase.setDateOfPurchase(LocalDateTime.now());
        purchase.setCustomer(customer);
        customer.addPurchase(purchase);


        InboxMessage inboxMessage = new InboxMessage();
        inboxMessage.setCustomer(customer);
        inboxMessage.setMessageTitle("New purchase");
        inboxMessage.setMessageContent("New Movie Package purchased. We'll notify you before activating the link.");
        session.save(inboxMessage);

        if (LocalDateTime.now().isAfter(link.getCreationTime())){
            InboxMessage inboxMessage1 = new InboxMessage();
            inboxMessage1.setCustomer(customer);
            inboxMessage1.setMessageTitle("Movie link has been activated");
            inboxMessage1.setMessageContent("The Link \n" + link.getMovieLink() + "\nHas been activated.");
            session.save(inboxMessage1);
        }

        if (LocalDateTime.now().isAfter(link.getExpirationTime())){
            InboxMessage inboxMessage2 = new InboxMessage();
            inboxMessage2.setCustomer(customer);
            inboxMessage2.setMessageTitle("Movie Link Expired");
            inboxMessage2.setMessageContent("The Link \n" + link.getMovieLink() + "\nHas expired.");
            session.save(inboxMessage2);
        }


        session.save(purchase);
    }

    private static void assignBookletToCustomer(Customer customer, Booklet booklet, Session session) {
        Purchase purchase = new Purchase();
        purchase.setPurchasedBooklet(booklet);
        purchase.setPurchaseType(BOOKLET);
        purchase.setDateOfPurchase(LocalDateTime.now());
        purchase.setCustomerPID(customer.getPersonalID());
        purchase.setCustomer(customer);
        customer.addPurchase(purchase);
        session.save(purchase);
    }
    //endregion

    public static void main(String[] args) {
    }

    public static Month getQuarterStartMonth(Month month) {
        switch (month) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                return JANUARY;
            case APRIL:
            case MAY:
            case JUNE:
                return APRIL;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                return JULY;
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                return OCTOBER;
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }

    public void persistReport(Report report) {
        Session session = getSession();
        try {
            session.beginTransaction();

            // If the branch is null (for example, for Chain Managers), set a placeholder branch
            if (report.getBranch() == null) {
                Branch placeholderBranch = new Branch();
                placeholderBranch.setId(-1);  // Assign -1 to indicate no specific branch
                report.setBranch(placeholderBranch);
            }

            session.merge(report);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    // Method to fetch a report by its ID
    public Report findReportById(Long reportId) {
        return getSession().get(Report.class, reportId);
    }

    public void modifyReport(Report report) {
        Session session = getSession();
        session.beginTransaction();
        session.update(report);
        session.getTransaction().commit();
    }

    public void removeReport(Report report) {
        Session session = getSession();
        session.beginTransaction();
        session.delete(report);
        session.getTransaction().commit();
    }

    public List<Report> retrieveReportsForBranchAndMonth(Month month, RequestData requestData) {
        Session session = ensureSession();
        Transaction transaction = null;
        List<Report> reports;
        try {
            transaction = startTransaction(session);

            reports = executeReportQuery(session, month, requestData);

            commitTransaction(transaction);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            throw e;  // Re-throw the exception to be handled further up
        }

        return reports;
    }

    public List<Complaint> retrieveComplaintsByBranchAndMonth(RequestData requestData) {
        Session session = ensureSession();
        Transaction transaction = null;
        List<Complaint> complaints;
        try {
            transaction = startTransaction(session);

            // First query: Fetch complaints based on purchaseType and month
            complaints = executeComplaintQuery(session, requestData);

            // Fallback query: If no complaints found, fetch all complaints
            if (complaints.isEmpty())
                complaints = fetchAllComplaints(session);

            commitTransaction(transaction);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            throw e;
        }

        return complaints;
    }

    public List<Purchase> retrieveAllPurchasesByBranchAndMonth(RequestData requestData) {
        Session session = ensureSession();
        Transaction transaction = null;
        List<Purchase> purchases;
        try {
            transaction = startTransaction(session);
            PurchaseType purchaseType = requestData.purchaseType();

            if (purchaseType == ALL_TYPES) {
                purchases = retrievePurchasesForAllTypes(session, requestData);
            } else {
                purchases = executePurchaseQueryForSpecificType(session, requestData, purchaseType);
            }

            commitTransaction(transaction);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            throw e;
        }

        return purchases;
    }

    private List<Purchase> retrievePurchasesForAllTypes(Session session, RequestData requestData) {
        List<Purchase> purchases = new ArrayList<>();

        for (PurchaseType type : PurchaseType.values()) {
            if (type != ALL_TYPES) {
                purchases.addAll(executePurchaseQueryForSpecificType(session, requestData, type));
            }
        }

        return purchases;
    }

    private List<Purchase> executePurchaseQueryForSpecificType(Session session, RequestData requestData, PurchaseType purchaseType) {
        // Define the base SQL query
        String sql = "SELECT * FROM purchase WHERE purchase_type = :purchaseType AND MONTH(dateOfPurchase) = :month";

        // Check if the query needs to filter by branch based on employee type
        boolean isBranchManager = requestData.employee() != null &&
                requestData.employee().getEmployeeType() == EmployeeType.BRANCH_MANAGER;

        // Only branch managers should filter by branch ID
        if (isBranchManager) {
            sql += " AND branch_id = :branchId";
        }

        int monthOrdinal = requestData.month().getValue();

        // Create the query and set parameters
        var query = session.createNativeQuery(sql, Purchase.class)
                .setParameter("purchaseType", purchaseType.name())
                .setParameter("month", monthOrdinal);

        // Apply branch filtering only for branch managers
        if (isBranchManager && requestData.branch() != null) {
            System.out.println("Branch Manager - Applying branch filtering for branch ID: " + requestData.branch().getId());
            query.setParameter("branchId", requestData.branch().getId());
        } else if (isBranchManager && requestData.branch() == null) {
            System.out.println("Branch Manager - No branch assigned, skipping filtering.");
        } else {
            System.out.println("No branch filtering required for Chain Manager or other employee type.");
        }

        return query.getResultList();
    }

    private List<Complaint> executeComplaintQuery(Session session, RequestData requestData) {
        // Define the base SQL query to filter complaints by purchaseType and month
        String sql = "SELECT * FROM complaints WHERE purchaseType = :purchaseType AND MONTH(dateOfComplaint) = :month";

        int monthOrdinal = requestData.month().getValue();
        int purchaseTypeOrdinal = requestData.purchaseType().ordinal();  // Get the ordinal value for the enum

        var query = session.createNativeQuery(sql, Complaint.class)
                .setParameter("purchaseType", purchaseTypeOrdinal)  // Set the ordinal value for purchaseType
                .setParameter("month", monthOrdinal);

        return query.getResultList();
    }

    private List<Complaint> fetchAllComplaints(Session session) {
        // Fallback query: Fetch all complaints if the first query returns an empty result
        String sql = "SELECT * FROM complaints";

        return session.createNativeQuery(sql, Complaint.class).getResultList();
    }

    private Session ensureSession() {
        Session session = getSession();
        if (session == null || !session.isOpen()) {
            session = getSessionFactory(DataCommunicationDB.getPassword()).openSession();
            setSession(session);  // Ensure this session is reused later
        }
        return session;
    }

    private Transaction startTransaction(Session session) {
        return session.beginTransaction();
    }

    private void commitTransaction(Transaction transaction) {
        if (transaction != null && !transaction.getRollbackOnly()) {
            transaction.commit();
        }
    }

    private void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    private List<Report> executeReportQuery(Session session, Month month, RequestData requestData) {
        String sql = "SELECT * FROM Report JOIN report_data ON Report.id = report_data.report_id " +
                    "WHERE branch_id = :branchId AND month = :month AND purchaseType = :purchaseType";
//        "SELECT * FROM Report JOIN report_data ON Report.id = report_data.report_id WHERE branch_id = :branchId AND month = :month\n"

        String purchaseTypeName = requestData.purchaseType().name();    // Get the string name for the enum
        List<Report> resultList = session.createNativeQuery(sql, Report.class)
                .setParameter("branchId", requestData.branch().getId())
                .setParameter("month", month.name())
                .setParameter("purchaseType", purchaseTypeName)
                .getResultList();
        System.out.println(resultList);
        return resultList;
    }
}