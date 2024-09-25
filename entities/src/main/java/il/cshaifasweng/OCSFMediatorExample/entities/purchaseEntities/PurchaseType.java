package il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities;

public enum PurchaseType {
    MOVIE_LINK("Movie Link"),
    MOVIE_TICKET("Movie Ticket"),
    BOOKLET("Booklet"),
    ALL_TYPES("All Types");


    private final String description;

    PurchaseType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
