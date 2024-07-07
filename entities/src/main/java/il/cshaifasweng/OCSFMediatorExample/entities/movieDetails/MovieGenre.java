package il.cshaifasweng.OCSFMediatorExample.entities.movieDetails;

public enum MovieGenre {
    COMEDY("Comedy"),
    ACTION("Action"),
    HORROR("Horror"),
    DRAMA("Drama"),
    ADVENTURE("Adventure"),
    DOCUMENTARY("Documentary");

    private final String description;

    MovieGenre(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}