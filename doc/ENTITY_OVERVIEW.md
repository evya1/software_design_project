
# Cinema Entities and Purchase Entities UML Diagram

## Table of Contents
- [UML Class Diagrams](#uml-class-diagrams)
  - [Cinema Entities](#cinema-entities)
  - [Purchase Entities](#purchase-entities)
- [Class Descriptions](#class-descriptions)
  - [Cinema Entities](#cinema-entities)
  - [Purchase Entities](#purchase-entities)
  - [Movie Details Entities](#movie-details-entities)
  - [User Entities](#user-entities)
  - [User Requests Entities](#user-requests-entities)

## UML Class Diagrams

### Cinema Entities
Below is the UML class diagram representing the cinema entities and their relationships:

```mermaid
classDiagram
  class Branch {
    int id
    String branchName
    List~Theater~ theaterList
    Report report
    Employee branchManager
    List~Employee~ employees
  }
  class Chain {
    int id
    String name
    List~Branch~ branches
    Employee chainManager
  }
  class Seat {
    int seatNum
    boolean taken
    Theater theater
  }
  class Theater {
    int theaterNum
    int numOfSeats
    int availableSeats
    List~Seat~ seatList
    Branch branch
    int rowLength
    List~MovieSlot~ movieTime
  }
  class Movie {
    int id
    String movieName
    String hebrewMovieName
    String mainCast
    byte[] image
    String producer
    String movieDescription
    int movieDuration
    MovieGenre movieGenre
    List~MovieSlot~ movieScreeningTime
    TypeOfMovie movieType
  }
  class MovieGenre {
    COMEDY
    ACTION
    HORROR
    DRAMA
    ADVENTURE
    DOCUMENTARY
  }
  class MovieSlot {
    int id
    Movie movie
    String movieTitle
    LocalDateTime startDateTime
    LocalDateTime endDateTime
    Theater theater
  }
  class TypeOfMovie {
    int id
    boolean upcoming
    boolean currentlyRunning
    boolean purchasable
    LocalDateTime releaseDate
    Movie movie
  }
  class Complaint {
    int id
    String complaintTitle
    String complaintContent
    String complaintDate
    String complaintStatus
    Customer customer
  }
  class Report {
    int id
    Branch branch
  }
  class Customer {
    int id
    String firstName
    String lastName
    String email
    String personalID
    List~Payment~ payments
    List~Complaint~ complaints
  }
  class Employee {
    int id
    EmployeeType employeeType
    String firstName
    String lastName
    String email
    String username
    String password
    boolean active
    Branch branchInCharge
  }
  class EmployeeType {
    BASE
    SERVICE
    THEATER_MANAGER
    CHAIN_MANAGER
    CONTENT_MANAGER
  }

  Chain "1" --o "many" Branch
  Branch "1" --o "many" Theater
  Branch "1" --o "many" Report
  Theater "1" --o "many" Seat
  MovieGenre "1" --o "many" Movie
  Movie "1" --o "many" MovieSlot
  EmployeeType "1" --o "many" Employee
  Customer "1" --o "many" Complaint
  Customer "1" --o "many" Payment
  Employee "1" --o "many" Report : writes
  MovieSlot "1" --o "1" Movie
  MovieSlot "1" --o "1" Theater
  TypeOfMovie "1" --o "1" Movie
  Complaint "1" --o "1" Customer
  Report "1" --o "1" Branch
  Seat "1" --o "1" Theater
```

### Purchase Entities
Below is the UML class diagram representing the purchase entities and their relationships:

```mermaid
classDiagram
  class MovieTicket {
    int id
    String movieName
    String branchName
    int theaterNum
    int seatNum
    int seatRow
    Movie movie
    Branch branch
  }
  class Booklet {
    int id
    int numOfEntries
    Customer customer
    +useEntry()
  }
  class MovieLink {
    int id
    String movieName
    String movieLink
    LocalDateTime creationTime
    LocalDateTime expirationTime
    Movie movie
  }
  class Payment {
    int paymentID
    String cardNumber
    LocalDate expiryDate
    String cvv
    Customer customer
  }
  class Purchase {
    int id
    PurchaseType purchaseType
    LocalDateTime dateOfPurchase
    String customerPID
    double price
    Booklet purchasedBooklet
    MovieLink purchasedMovieLink
    MovieTicket purchasedMovieTicket
    Customer customer
    Branch branch
    +setPriceByItem(PurchaseType purchasedItem)
  }
  class PriceConstants {
    <<constant>>
    +MOVIE_TICKET_PRICE : double
    +MOVIE_LINK_PRICE : double
    +BOOKLET_ENTRY_PRICE : double
  }
  class PurchaseType {
    <<enumeration>>
    MOVIE_LINK
    MOVIE_TICKET
    BOOKLET
  }
  class Customer {
    int id
    String firstName
    String lastName
    String email
    String personalID
    List~Payment~ payments
    List~Complaint~ complaints
  }

  Purchase "1" --o "1" Booklet : purchasedBooklet
  Purchase "1" --o "1" MovieLink : purchasedMovieLink
  Purchase "1" --o "1" MovieTicket : purchasedMovieTicket
  Purchase "1" --o "1" Customer : customer
  Purchase "1" --o "1" Branch : branch
  Booklet "1" --o "1" Customer
  MovieLink "1" --o "1" Movie
  MovieTicket "1" --o "1" Movie
  MovieTicket "1" --o "1" Branch
  Payment "1" --o "1" Customer
  Customer "1" --o "many" Payment
  Customer "1" --o "many" Complaint
```

## Class Descriptions

### Cinema Entities
    Branch: Represents a cinema branch.
    Chain: Represents a cinema chain.
    Seat: Represents a seat in a theater.
    Theater: Represents a theater in a branch.

### Purchase Entities
    PurchaseItem: Base class for items included in a purchase.
        - Attributes: id, price
        - Methods: +setPrice(double price)
    Booklet: Represents a booklet associated with a purchase.
        - Attributes: id, numOfEntries, price, customer
        - Methods: +useEntry()
    MovieTicket: Represents a ticket for a movie.
        - Attributes: id, price, movie, branch, movieName, branchName, theaterNum, seatNum, seatRow
    MovieLink: Represents a link to a movie.
        - Attributes: id, movie, movieName, movieLink, creationTime, expirationTime
    Payment: Represents a payment transaction.
        - Attributes: paymentID, cardNumber, expiryDate, cvv, price, customer
    Purchase: Represents a purchase, which can include multiple items and payments.
        - Attributes: id, purchaseType, dateOfPurchase, totalCost, movieTickets, payments, booklet
        - Methods: +calculateTotalCost()
    PurchaseType: Represents the type of purchase.
        - Values: MOVIE_LINK, MOVIE_TICKET, BOOKLET

### Movie Details Entities
    Movie: Represents a movie.
    MovieGenre: Represents the genre of a movie.
    MovieSlot: Represents a slot for a movie showing.
    TypeOfMovie: Represents the type of a movie.

### User Entities
    Customer: Represents a customer.
    Employee: Represents an employee.
    EmployeeType: Represents the type of an employee.

### User Requests Entities
    Complaint: Represents a complaint made by a customer.
    Report: Represents a report made by an employee.
