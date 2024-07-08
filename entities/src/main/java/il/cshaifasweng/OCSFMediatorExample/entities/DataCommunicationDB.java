package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Chain;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Seat;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieGenre;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.TypeOfMovie;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.function.Consumer;

public class DataCommunicationDB
{
    //Attributes
    private static Session session;
    private static String password;

    public static String getPassword() { return password; }
    public static void setPassword(String password) { DataCommunicationDB.password = password; }
    //Session Methods
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

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);

    }

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
            TypeOfMovie typeOfMovie = movie.getMovieType();
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

        // Print all Branches
        List<Branch> branches = session.createQuery("FROM Branch", Branch.class).list();
        for (Branch branch : branches) {
            System.out.println("Branch ID: " + branch.getId() +
                    ", Branch Name: " + branch.getBranchName());

            // Print Branch Manager
            Employee branchManager = branch.getBranchManager();
            if (branchManager != null) {
                System.out.println("\tBranch Manager: " + branchManager.getFirstName() + " " + branchManager.getLastName() +
                        ", Email: " + branchManager.getEmail() +
                        ", Username: " + branchManager.getUsername());
            }

            // Print Theaters in Branch
            List<Theater> branchTheaters = branch.getTheaterList();
            for (Theater branchTheater : branchTheaters) {
                System.out.println("\tTheater ID: " + branchTheater.getTheaterNum() +
                        ", Num of Seats: " + branchTheater.getNumOfSeats() +
                        ", Available Seats: " + branchTheater.getAvailableSeats());
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
            System.out.println("Employee ID: " + employee.getId() +
                    ", First Name: " + employee.getFirstName() +
                    ", Last Name: " + employee.getLastName() +
                    ", Email: " + employee.getEmail() +
                    ", Username: " + employee.getUsername() +
                    ", Employee Type: " + employee.getEmployeeType());

            // Print Branch In Charge if Theater Manager
            if (employee.getEmployeeType() == EmployeeType.THEATER_MANAGER) {
                Branch branchInCharge = employee.getBranchInCharge();
                if (branchInCharge != null) {
                    System.out.println("\tBranch In Charge: " + branchInCharge.getBranchName());
                }
            }
        }

        // Print all Chains
        List<Chain> chains = session.createQuery("FROM Chain", Chain.class).list();
        for (Chain chain : chains) {
            System.out.println("Chain ID: " + chain.getId());

            // Print Branches in Chain
            List<Branch> chainBranches = chain.getBranches();
            if (chainBranches != null) {
                for (Branch chainBranch : chainBranches) {
                    System.out.println("\tBranch ID: " + chainBranch.getId() +
                            ", Branch Name: " + chainBranch.getBranchName());
                }
            } else {
                System.out.println("\tNo Branches in this Chain.");
            }

            // Print Chain Manager
            Employee chainManager = chain.getChainManager();
            if (chainManager != null) {
                System.out.println("\tChain Manager: " + chainManager.getFirstName() + " " + chainManager.getLastName() +
                        ", Email: " + chainManager.getEmail() +
                        ", Username: " + chainManager.getUsername());
            }
        }

        // Print all Reports
        List<Report> reports = session.createQuery("FROM Report", Report.class).list();
        for (Report report : reports) {
            System.out.println("Report ID: " + report.getId());

            // Print Branch associated with Report
            Branch branch = report.getBranch();
            if (branch != null) {
                System.out.println("\tBranch: " + branch.getBranchName());
            }
        }

        System.out.println("Print all the Branch Movies");
        printBranchMovies(1);
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

    //Previous command to use with the previous data entities  -----DO NOT REMOVE-----
    public static void generateMovieList2() throws Exception {
        try {
            session.beginTransaction();
            Random random = new Random();

            Employee employee = new Employee();
            employee.setEmployeeType(EmployeeType.THEATER_MANAGER);
            employee.setEmail("Something@c.com");
            employee.setFirstName("Shimi");
            employee.setLastName("Tavori");
            employee.setUsername("Sodk");
            employee.setPassword("129090m");
            session.save(employee);
            session.flush();

            Branch branch = new Branch();
            branch.setBranchName("Julius");
            branch.setBranchManager(employee);
            employee.setBranchInCharge(branch);
            List<Theater> theaters = new ArrayList<>();
            branch.setTheaterList(theaters);
            session.save(branch);
            session.flush();


            // Create a Theater
            Theater theater = new Theater(100, 100, new ArrayList<>(), new ArrayList<>(), 10);
            session.save(theater);
            session.flush();

            // Create 10 specific Movies
            Movie movie1 = new Movie("Inception", "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Cillian Murphy, Marion Cotillard, Michael Caine", null, "Christopher Nolan", "Inception is a sci-fi thriller where a team of dream thieves led by Leonardo DiCaprio try to plant an idea in a CEO's mind by infiltrating his dreams.", 148, new ArrayList<>(), null, MovieGenre.DRAMA,"האשליה");
            Movie movie2 = new Movie("The Matrix", "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Hugo Weaving", null, "Lana Wachowski, Lilly Wachowski", "A computer hacker learns the world he lives in is a simulation and joins a rebellion against the machines.", 136, new ArrayList<>(), null,MovieGenre.COMEDY, "המטריקס");
            Movie movie3 = new Movie("Interstellar", "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine", null, "Christopher Nolan", "A team of astronauts travel through a wormhole in search of a new home for humanity.", 169, new ArrayList<>(), null,MovieGenre.DOCUMENTARY,"בין כוכבים");
            Movie movie4 = new Movie("The Dark Knight", "Christian Bale, Heath Ledger, Aaron Eckhart, Maggie Gyllenhaal", null, "Christopher Nolan", "Batman faces a new challenge from the Joker, who descends Gotham City into chaos.", 152, new ArrayList<>(), null,MovieGenre.DOCUMENTARY, "האביר האפל");
            Movie movie5 = new Movie("Fight Club", "Brad Pitt, Edward Norton, Helena Bonham Carter, Meat Loaf", null, "David Fincher", "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into something much, much more.", 139, new ArrayList<>(), null,MovieGenre.ACTION,"מועדון קרב");
            Movie movie6 = new Movie("Pulp Fiction", " John Travolta, Samuel L. Jackson, Uma Thurman, Bruce Willis", null, "Quentin Tarantino", "A hit man with a philosophical bent, a boxer on the fix, and their wives weave a darkly comedic tapestry.", 154, new ArrayList<>(), null,MovieGenre.COMEDY, "ספרות זולה");
            Movie movie7 = new Movie("Forrest Gump", "Tom Hanks, Robin Wright, Gary Sinise, Sally Field", null, "Robert Zemeckis", "A simple man with a low IQ but a big heart runs through key events in American history.", 142, new ArrayList<>(), null,MovieGenre.ADVENTURE,"פורסט גאמפ");
            Movie movie8 = new Movie("The Shawshank Redemption", "Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler", null, "Frank Darabont", "A wrongly convicted man plans his escape from prison over a long period of time.", 142, new ArrayList<>(), null,MovieGenre.COMEDY,"חומות של תקווה");
            Movie movie9 = new Movie("Gladiator", "Russell Crowe, Joaquin Phoenix, Connie Nielsen, Richard Harris", null, "Ridley Scott", "A former Roman general becomes a reluctant gladiator seeking revenge for the murder of his family.", 155, new ArrayList<>(), null,MovieGenre.HORROR, "הגלדיאטור");
            Movie movie10 = new Movie("The Godfather", "Marlon Brando, Al Pacino, James Caan, Robert Duvall", null, "Francis Ford Coppola", "The aging patriarch of a powerful Italian-American crime family tries to maintain control of his empire.", 175, new ArrayList<>(), null, MovieGenre.DRAMA,"הסנדק");

            List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10);

            for (Movie movie : movies) {
                session.save(movie);
                session.flush();

                // Create TypeOfMovie
                boolean inTheaterRandom, viewPackageRandom;
                boolean upcomingRandom = random.nextBoolean();
                if(upcomingRandom) {
                    inTheaterRandom = false;
                    viewPackageRandom = false;
                }
                else {
                    inTheaterRandom = true;
                    viewPackageRandom =random.nextBoolean();
                }
                TypeOfMovie typeOfMovie = new TypeOfMovie(upcomingRandom, inTheaterRandom, viewPackageRandom);
                session.save(typeOfMovie);
                session.flush();

                // Associate TypeOfMovie with Movie
                movie.setMovieType(typeOfMovie);
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

            for( Movie movie : movies){
                session.save(movie);
            }
            session.flush();

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

            branch.getTheaterList().add(theater);
            session.save(theater);

            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }

    public static void generateMovieList() throws Exception {
        try {
            session.beginTransaction();
            Random random = new Random();

            // Create Chain
            Chain chain = new Chain();
            session.save(chain);

            // Create 10 specific Movies
            Movie movie1 = new Movie("Inception", "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Cillian Murphy, Marion Cotillard, Michael Caine", null, "Christopher Nolan", "Inception is a sci-fi thriller where a team of dream thieves led by Leonardo DiCaprio try to plant an idea in a CEO's mind by infiltrating his dreams.", 148, new ArrayList<>(), null, MovieGenre.DRAMA,"האשליה");
            Movie movie2 = new Movie("The Matrix", "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Hugo Weaving", null, "Lana Wachowski, Lilly Wachowski", "A computer hacker learns the world he lives in is a simulation and joins a rebellion against the machines.", 136, new ArrayList<>(), null,MovieGenre.COMEDY, "המטריקס");
            Movie movie3 = new Movie("Interstellar", "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine", null, "Christopher Nolan", "A team of astronauts travel through a wormhole in search of a new home for humanity.", 169, new ArrayList<>(), null,MovieGenre.DOCUMENTARY,"בין כוכבים");
            Movie movie4 = new Movie("The Dark Knight", "Christian Bale, Heath Ledger, Aaron Eckhart, Maggie Gyllenhaal", null, "Christopher Nolan", "Batman faces a new challenge from the Joker, who descends Gotham City into chaos.", 152, new ArrayList<>(), null,MovieGenre.DOCUMENTARY, "האביר האפל");
            Movie movie5 = new Movie("Fight Club", "Brad Pitt, Edward Norton, Helena Bonham Carter, Meat Loaf", null, "David Fincher", "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into something much, much more.", 139, new ArrayList<>(), null,MovieGenre.ACTION,"מועדון קרב");
            Movie movie6 = new Movie("Pulp Fiction", " John Travolta, Samuel L. Jackson, Uma Thurman, Bruce Willis", null, "Quentin Tarantino", "A hit man with a philosophical bent, a boxer on the fix, and their wives weave a darkly comedic tapestry.", 154, new ArrayList<>(), null,MovieGenre.COMEDY, "ספרות זולה");
            Movie movie7 = new Movie("Forrest Gump", "Tom Hanks, Robin Wright, Gary Sinise, Sally Field", null, "Robert Zemeckis", "A simple man with a low IQ but a big heart runs through key events in American history.", 142, new ArrayList<>(), null,MovieGenre.ADVENTURE,"פורסט גאמפ");
            Movie movie8 = new Movie("The Shawshank Redemption", "Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler", null, "Frank Darabont", "A wrongly convicted man plans his escape from prison over a long period of time.", 142, new ArrayList<>(), null,MovieGenre.COMEDY,"חומות של תקווה");
            Movie movie9 = new Movie("Gladiator", "Russell Crowe, Joaquin Phoenix, Connie Nielsen, Richard Harris", null, "Ridley Scott", "A former Roman general becomes a reluctant gladiator seeking revenge for the murder of his family.", 155, new ArrayList<>(), null,MovieGenre.HORROR, "הגלדיאטור");
            Movie movie10 = new Movie("The Godfather", "Marlon Brando, Al Pacino, James Caan, Robert Duvall", null, "Francis Ford Coppola", "The aging patriarch of a powerful Italian-American crime family tries to maintain control of his empire.", 175, new ArrayList<>(), null, MovieGenre.DRAMA,"הסנדק");

            List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10);

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

            for (Movie movie : movies) {
                session.save(movie);
            }



            // Create 3 Branches
            List<Branch> branchesList = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                Branch branch = new Branch();
                branch.setBranchName("Branch " + i);
                branch.setChain(chain);

                // Create Branch Manager
                Employee branchManager = new Employee();
                branchManager.setEmployeeType(EmployeeType.THEATER_MANAGER);
                branchManager.setEmail("manager" + i + "@branch.com");
                branchManager.setFirstName("Manager" + i);
                branchManager.setLastName("Branch" + i);
                branchManager.setUsername("manager" + i);
                branchManager.setPassword("password" + i);
                session.save(branchManager);

                branch.setBranchManager(branchManager);
                branchManager.setBranchInCharge(branch);
                session.save(branch);

                branchesList.add(branch);
                chain.setBranches(branchesList);
                session.save(chain);

                // Create 2 Theaters for each Branch
                List<Theater> theaters = new ArrayList<>();
                for (int j = 1; j <= 2; j++) {
                    Theater theater = new Theater(100, 100, new ArrayList<>(), new ArrayList<>(), 10);
                    theater.setBranch(branch);
                    theaters.add(theater);
                    session.save(theater);

                    // Create MovieSlots for each Theater
                    for (Movie movie : movies) {
                        List<MovieSlot> movieSlots = new ArrayList<>();
                        for (int k = 0; k < 10; k++) {
                            int year = random.nextInt(2) + 2024; // Year between 2024 and 2025
                            int month = random.nextInt(6) + 7; // Month between 7 and 12
                            int day = random.nextInt(28) + 1; // Ensuring valid day of month

                            MovieSlot movieSlot = new MovieSlot(movie, LocalDateTime.of(year, month, day, 10, 0),
                                    LocalDateTime.of(year, month, day, 12, 0), theater);
                            movieSlots.add(movieSlot);
                            session.save(movieSlot);
                        }
                        movie.setMovieScreeningTime(movieSlots);
                        session.save(movie);

                        theater.getMovieTime().addAll(movieSlots);
                        // Create TypeOfMovie
                        boolean inTheaterRandom, viewPackageRandom;
                        boolean upcomingRandom = random.nextBoolean();
                        if(upcomingRandom) {
                            inTheaterRandom = false;
                            viewPackageRandom = false;
                        }
                        else {
                            inTheaterRandom = true;
                            viewPackageRandom =random.nextBoolean();
                        }
                        TypeOfMovie typeOfMovie = new TypeOfMovie(upcomingRandom, inTheaterRandom, viewPackageRandom);
                        session.save(typeOfMovie);
                        session.flush();

                        // Associate TypeOfMovie with Movie
                        movie.setMovieType(typeOfMovie);
                        session.save(movie);
                        session.flush();
                    }

                    // Create Seats for Theater
                    List<Seat> seats = new ArrayList<>();
                    for (int k = 0; k < theater.getNumOfSeats(); k++) {
                        Seat seat = new Seat(false, theater);
                        seats.add(seat);
                        session.save(seat);
                    }
                    theater.setSeatList(seats);
                    session.save(theater);
                }
                branch.setTheaterList(theaters);
                session.save(branch);
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

    public static List<MovieSlot> getScreeningTimesByMovie(Session session, int movieId) {
        return session.createQuery(
                        "SELECT ms FROM MovieSlot ms " +
                                "WHERE ms.movie.id = :movieId", MovieSlot.class)
                .setParameter("movieId", movieId)
                .getResultList();
    }

    public static byte[] readImage(String imagePath) throws IOException {
        InputStream inputStream = DataCommunicationDB.class.getClassLoader().getResourceAsStream(imagePath);
        if (inputStream == null) {
            throw new IOException("File not found: " + imagePath);
        }
        return inputStream.readAllBytes();
    }


    /******************* GETTERS **************************/
    public static Movie getMovieByID(int movieID){
        return session.get(Movie.class, movieID);
    }
    public static MovieSlot getMovieSlotByID(int movieSlotID){
        return session.get(MovieSlot.class, movieSlotID);
    }
    public static Theater getTheaterByID(int theaterID){
        return session.get(Theater.class, theaterID);
    }
    public static TypeOfMovie getTypeOfMovieByID(int movieTypeID){
        return session.get(TypeOfMovie.class, movieTypeID);
    }
    public static Seat getSeatByID(int seatID){
        return session.get(Seat.class, seatID);
    }



    /*************** Movie ENTITY SETTERS********************/

    //Generic method that accepts lambda function to modify a specific field in Movie entity.
    public static void updateMovieField(int movieId, Consumer<Movie> updater) {
        try {
            session.beginTransaction();

            // Retrieve the movie from the database
            Movie movie = getMovieByID(movieId);
            if (movie == null) {
                System.out.println("Movie with ID " + movieId + " not found.");
                return;
            }

            // Apply the updater function to modify the movie
            updater.accept(movie);

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

    //Update Name field by movieID
    public static void modifyMovieName(int movieID, String newMovieName){
        updateMovieField(movieID,movie -> movie.setMovieName(newMovieName));
    }

    //Update main cast field by movieID
    public static void modifyMovieMainCast(int movieID, String newMainCast){
        updateMovieField(movieID,movie -> movie.setMainCast(newMainCast));
    }

    //Update producer by movieID
    public static void modifyMovieProducer(int movieId, String newProducer) {
        updateMovieField(movieId, movie -> movie.setProducer(newProducer));
    }

    //Update movie description field by movieID
    public static void modifyMovieDescription(int movieId, String newMovieDescription) {
        updateMovieField(movieId, movie -> movie.setMovieDescription(newMovieDescription));
    }

    //Update movie duration field by movieID
    public static void modifyMovieDuration(int movieId, int newMovieDuration) {
        updateMovieField(movieId, movie -> movie.setMovieDuration(newMovieDuration));
    }


    /****************** TypeOfMovie ENTITY SETTERS *******************/

    //Generic method that accepts lambda function to modify a specific field in TypeOfMovie entity.
    public static void updateTypeOfMovieField(int typeId, Consumer<TypeOfMovie> updater) {
        try {
            session.beginTransaction();

            // Retrieve the TypeOfMovie from the database
            TypeOfMovie typeOfMovie = getTypeOfMovieByID(typeId);
            if (typeOfMovie == null) {
                System.out.println("TypeOfMovie with ID " + typeId + " not found.");
                return;
            }

            // Apply the updater function to modify the TypeOfMovie
            updater.accept(typeOfMovie);

            // Save the updated TypeOfMovie back to the database
            session.update(typeOfMovie);
            session.getTransaction().commit();
            System.out.println("TypeOfMovie with ID " + typeId + " has been updated.");
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }

    //Update upcoming field by typeID
    public static void modifyTypeOfMovieUpcoming(int typeId, boolean upcoming) {
        updateTypeOfMovieField(typeId, typeOfMovie -> typeOfMovie.setUpcoming(upcoming));
    }

    //Update currently in theaters field by typeID
    public static void modifyTypeOfMovieCurrentlyRunning(int typeId, boolean currentlyRunning) {
        updateTypeOfMovieField(typeId, typeOfMovie -> typeOfMovie.setCurrentlyRunning(currentlyRunning));
    }

    //Update purchasable field by typeID
    public static void modifyTypeOfMoviePurchasable(int typeId, boolean purchasable) {
        updateTypeOfMovieField(typeId, typeOfMovie -> typeOfMovie.setPurchasable(purchasable));
    }

    //Update release date field by typeID
    public static void modifyTypeOfMovieReleaseDate(int typeId, LocalDateTime releaseDate) {
        updateTypeOfMovieField(typeId, typeOfMovie -> typeOfMovie.setReleaseDate(releaseDate));
    }


    /*************** MovieSlot ENTITY SETTERS********************/

    //Generic method that accepts lambda function to modify a specific field in MovieSlot entity.
    public static void updateMovieSlotField(int slotId, Consumer<MovieSlot> updater) {
        try {
            session.beginTransaction();

            // Retrieve the MovieSlot from the database
            MovieSlot movieSlot = getMovieSlotByID(slotId);
            if (movieSlot == null) {
                System.out.println("MovieSlot with ID " + slotId + " not found.");
                return;
            }

            // Apply the updater function to modify the MovieSlot
            updater.accept(movieSlot);
            // Save the updated MovieSlot back to the database
            session.update(movieSlot);
            session.flush();

            session.getTransaction().commit();
            System.out.println("MovieSlot with ID " + slotId + " has been updated.");
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        }
    }

    //Update start time field by slotID
    public static void modifyMovieSlotStartTime(int slotId, LocalDateTime startTime) {
        updateMovieSlotField(slotId, movieSlot -> movieSlot.setStartDateTime(startTime));
    }

    //Update end time field by slotID
    public static void modifyMovieSlotEndTime(int slotId, LocalDateTime endTime) {
        updateMovieSlotField(slotId, movieSlot -> movieSlot.setEndDateTime(endTime));
    }


    public static void main( String[] args ) {

    }
}