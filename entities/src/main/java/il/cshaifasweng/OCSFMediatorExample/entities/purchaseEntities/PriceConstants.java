package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

public class PriceConstants {

    public static final double BOOKLET_PRICE = 127.5;
    public static final double MOVIE_LINK_PRICE = 27.5;
    public static final double MOVIE_TICKET_PRICE = 29.5;

    // Private constructor to prevent instantiation
    private PriceConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
