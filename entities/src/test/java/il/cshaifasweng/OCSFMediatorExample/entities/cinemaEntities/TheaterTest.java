package il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.EmployeeType;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheaterTest {

    private Theater theater;
    private List<Seat> seats;
    private List<MovieSlot> movieSlots;
    private Branch branch;
    private TestReporter testReporter;
    private TestInfo testInfo;

    @BeforeEach
    public void setUp(TestReporter testReporter, TestInfo testInfo) {
        this.testReporter = testReporter;
        this.testInfo = testInfo;
        seats = new ArrayList<>();
        movieSlots = new ArrayList<>();
        branch = createBranch("InitialBranch", "initial@branch.com", "Initial", "Manager");
        theater = new Theater(100, 50, movieSlots, seats, 10);
        theater.setBranch(branch);
    }

    @AfterEach
    public void tearDown() {
        theater = null;
        seats = null;
        movieSlots = null;
        branch = null;
    }

    private void printTestInfo(String message) {
        String testName = testInfo.getDisplayName();
        testReporter.publishEntry(testName + ": " + message);
        System.out.println(testName + ": " + message);
    }

    private Branch createBranch(String branchName, String email, String firstName, String lastName) {
        Branch newBranch = new Branch();
        newBranch.setBranchName(branchName);

        Employee manager = new Employee();
        manager.setEmployeeType(EmployeeType.BRANCH_MANAGER);
        manager.setEmail(email);
        manager.setFirstName(firstName);
        manager.setLastName(lastName);
        manager.setUsername(firstName.toLowerCase() + lastName.toLowerCase());
        manager.setPassword("password");

        newBranch.setBranchManager(manager);
        manager.setBranchInCharge(newBranch);
        newBranch.setTheaterList(new ArrayList<>());

        return newBranch;
    }

    @Test
    public void testGetTheaterNum() {
        int theaterNum = 1;
        theater.setTheaterNum(theaterNum);
        assertEquals(theaterNum, theater.getId());

        printTestInfo("Theater number set and retrieved successfully");
    }

    @Test
    public void testSetTheaterNum() {
        int theaterNum = 2;
        theater.setTheaterNum(theaterNum);
        assertEquals(theaterNum, theater.getId());

        printTestInfo("Theater number set successfully");
    }

    @Test
    public void testGetNumOfSeats() {
        assertEquals(100, theater.getNumOfSeats());

        printTestInfo("Number of seats retrieved successfully");
    }

    @Test
    public void testSetNumOfSeats() {
        int numOfSeats = 200;
        theater.setNumOfSeats(numOfSeats);
        assertEquals(numOfSeats, theater.getNumOfSeats());

        printTestInfo("Number of seats set successfully");
    }

    @Test
    public void testGetAvailableSeats() {
        assertEquals(50, theater.getAvailableSeats());

        printTestInfo("Available seats retrieved successfully");
    }

    @Test
    public void testSetAvailableSeats() {
        int availableSeats = 75;
        theater.setAvailableSeats(availableSeats);
        assertEquals(availableSeats, theater.getAvailableSeats());

        printTestInfo("Available seats set successfully");
    }

    @Test
    public void testGetSeatList() {
        assertEquals(seats, theater.getSeatList());

        printTestInfo("Seat list retrieved successfully");
    }

    @Test
    public void testSetSeatList() {
        List<Seat> newSeatList = new ArrayList<>();
        theater.setSeatList(newSeatList);
        assertEquals(newSeatList, theater.getSeatList());

        printTestInfo("Seat list set successfully");
    }

    @Test
    public void testGetBranch() {
        assertEquals(branch, theater.getBranch());

        printTestInfo("Branch retrieved successfully");
    }

    @Test
    public void testSetBranch() {
        Branch newBranch = createBranch("Julius", "manager@branch.com", "Manager", "Branch");

        printTestInfo("Setting branch: " + newBranch.getBranchName() + " with manager: " + newBranch.getBranchManager().getFirstName() + " " + newBranch.getBranchManager().getLastName());

        theater.setBranch(newBranch);

        assertEquals(newBranch, theater.getBranch());

        printTestInfo("Branch set successfully. Branch name: " + theater.getBranch().getBranchName());
    }

    @Test
    public void testGetRowLength() {
        assertEquals(10, theater.getRowLength());

        printTestInfo("Row length retrieved successfully");
    }

    @Test
    public void testSetRowLength() {
        int rowLength = 15;
        theater.setRowLength(rowLength);
        assertEquals(rowLength, theater.getRowLength());

        printTestInfo("Row length set successfully");
    }

    @Test
    public void testGetMovieTime() {
        assertEquals(movieSlots, theater.getSchedule());

        printTestInfo("Movie time retrieved successfully");
    }

    @Test
    public void testSetMovieTime() {
        List<MovieSlot> newMovieTime = new ArrayList<>();
        theater.setSchedule(newMovieTime);
        assertEquals(newMovieTime, theater.getSchedule());

        printTestInfo("Movie time set successfully");
    }
}
