package il.cshaifasweng.OCSFMediatorExample.entities;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Seat;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Theater;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Employee;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;
import il.cshaifasweng.OCSFMediatorExample.entities.userRequests.Report;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.Month;
import java.util.List;
import java.time.Month;
import java.util.Map;


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

    public String getUsernamePassword() {
        return usernamePassword;
    }

    public void setUsernamePassword(String usernamePassword) {
        this.usernamePassword = usernamePassword;
    }

    private String usernamePassword;

    //Purchase related messages.
    private Customer customer;
    private Purchase purchase;
    private Booklet booklet;
    private PriceConstants prices;

    //Movie Ticket related messages
    private MovieTicket movieTicket;
    private List<Seat> chosenSeats;

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
    private List<InboxMessage> customerMessages;

    private List<Theater> theaters;

    //Complaint related
    private Complaint complaint;
    private String complaintTitle;
    private PurchaseType purchaseType;
    private List<Complaint> complaints;
    private boolean newContentFlag = false;

    // Reports Related
    private List<Report> reports;
    private boolean isSingleReport = false;
    private ReportSpanType reportSpanType;
    private int year;
    private Month month;
    private ReportType reportType;
    private String reportLabel;
    private String reportDetails;
    private Map<String, Double> dataForGraphs;
    private String serializedReportData;


    public List<Theater> getTheaters() {
        return theaters;
    }

    public void setTheaters(List<Theater> theaters) {
        this.theaters = theaters;
    }
    public List<Complaint> getComplaints() { return complaints; }

    public void setComplaints(List<Complaint> complaints) { this.complaints = complaints; }

    public MovieSlot getMovieSlot() { return movieSlot; }
    public String getCustomerID() {
        return customerID;
    }

    public void setMovieSlot(MovieSlot movieSlot) { this.movieSlot = movieSlot; }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    public int getBranchID() { return branchID; }

    public void setBranchID(int branchID) { this.branchID = branchID; }

    public MovieLink getMovieLink() {return movieLink;}

    public void setMovieLink(MovieLink movieLink) {this.movieLink = movieLink;}

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
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

    public void setMovieTicket(MovieTicket ticket) {this.movieTicket = ticket;}

    public MovieTicket getMovieTicket() {return movieTicket;}

    public boolean isNewContentFlag() {
        return newContentFlag;
    }

    public void setNewContentFlag(boolean newContentFlag) {
        this.newContentFlag = newContentFlag;
    }

    public List<InboxMessage> getCustomerMessages() {
        return customerMessages;
    }

    public void setCustomerMessages(List<InboxMessage> customerMessages) {
        this.customerMessages = customerMessages;
    }

    public List<Seat> getChosenSeats() {return chosenSeats;}

    public void setChosenSeats(List<Seat> chosenSeats) {this.chosenSeats = chosenSeats;}

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
        isSingleReport = reports != null && reports.size() == 1;
    }

    public boolean isSingleReport() {
        return isSingleReport;
    }

    public ReportSpanType getReportSpanType() {
        return reportSpanType;
    }

    public void setReportSpanType(ReportSpanType reportSpanType) {
        this.reportSpanType = reportSpanType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getReportLabel() {
        return reportLabel;
    }

    public void setReportLabel(String reportLabel) {
        this.reportLabel = reportLabel;
    }

    public String getReportDetails() {
        return reportDetails;
    }

    public void setReportDetails(String reportDetails) {
        this.reportDetails = reportDetails;
    }

    public Map<String, Double> getDataForGraphs() {
        return dataForGraphs;
    }

    public void setDataForGraphs(Map<String, Double> dataForGraphs) {
        this.dataForGraphs = dataForGraphs;
    }

    public String getSerializedReportData() {
        return serializedReportData;
    }

    public void setSerializedReportData(String serializedReportData) {
        this.serializedReportData = serializedReportData;
    }
}
