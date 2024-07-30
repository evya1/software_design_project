package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Complaint;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



public class Message implements Serializable {

    //Generic Attributes
    private int id;

    //Request time.
    private LocalDateTime timeStamp;

    //Will be used for the main request to navigate the handler in the Server.
    private String message;

    //Will be used for sub request once inside the handler.
    private String data;

    private String sourceFXML;

    //Specific Attributes.
    private List<Employee> employeeList;
    private Employee employee;

    //Purchase related messages.
    private Customer customer;
    private Purchase purchase;
    private Booklet booklet;
    private PriceConstants prices;



    private String customerID;

    //Movie Related messages
    private Movie specificMovie;
    private List<Movie> movies;
    private List<MovieSlot> movieSlots;
    private MovieLink movieLink;
    private int movieID;
    private MovieSlot movieSlot;

    //Branch or Theater related
    private int branchID;
    private List<Branch> branches;
    private Branch branch;

    //Complaint related
    private Complaint complaint;
    private String complaintTitle;
    private PurchaseType purchaseType;



    public MovieSlot getMovieSlot() {return movieSlot;}
    public String getCustomerID() {
        return customerID;
    }

    public void setMovieSlot(MovieSlot movieSlot) {this.movieSlot = movieSlot;}

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public MovieLink getMovieLink() {return movieLink;}

    public void setMovieLink(MovieLink movieLink) {this.movieLink = movieLink;}

    public void setBooklet(Booklet booklet) {this.booklet = booklet;}

    public Booklet getBooklet() {return booklet;}

    public Purchase getPurchase() {return purchase;}

    public void setPurchase(Purchase purchase) {this.purchase = purchase;}

    public Customer getCustomer() {return customer;}

    public void setCustomer(Customer customer) {this.customer = customer;}

    public String getSourceFXML() {return sourceFXML;}

    public void setSourceFXML(String sourceFXML) {this.sourceFXML = sourceFXML;}

    public Message() {
        super();
    }

    public Movie getSpecificMovie() {
        return specificMovie;
    }

    public void setSpecificMovie(Movie specificMovie) {
        this.specificMovie = specificMovie;
    }

    public List<MovieSlot> getMovieSlots() {
        return movieSlots;
    }

    public void setMovieSlots(List<MovieSlot> movieSlots) {
        this.movieSlots = movieSlots;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public Complaint getComplaint() {return complaint;}

    public void setComplaint(Complaint complaint) {this.complaint = complaint;}

    public PurchaseType getPurchaseType() {return purchaseType;}

    public void setPurchaseType(PurchaseType purchaseType) {this.purchaseType = purchaseType;}

    public String getComplaintTitle() {return complaintTitle;}

    public void setComplaintTitle(String complaintTitle) {this.complaintTitle = complaintTitle;}

    public Employee getEmployee() {return employee;}

    public void setEmployee(Employee employee) {this.employee = employee;}

    public List<Employee> getEmployeeList() {return employeeList;}

    public void setEmployeeList(List<Employee> employeeList) {this.employeeList = employeeList;}

    public void setPrices(PriceConstants prices) {this.prices = prices;}

    public PriceConstants getPrices() {return prices;}

}
