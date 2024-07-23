package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

public class PriceConstants {

//    public static final double BOOKLET_PRICE = 127.5;
//    public static final double MOVIE_LINK_PRICE = 27.5;
//    public static final double MOVIE_TICKET_PRICE = 29.5;

    private static double bookletPrice;
    private static double movieTicketPrice;
    private static double movieLinkPrice;

    public static double getBookletPrice() {
        return bookletPrice;
    }

    public static void setBookletPrice(double bP) {
        bookletPrice = bP;
    }

    public static double getMovieTicketPrice() {
        return movieTicketPrice;
    }

    public static void setMovieTicketPrice(double MTP) {
        movieTicketPrice = MTP;
    }

    public static double getMovieLinkPrice() {
        return movieLinkPrice;
    }

    public static void setMovieLinkPrice(double MLP) {
        movieLinkPrice = MLP;
    }




}
