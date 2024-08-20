package il.cshaifasweng.OCSFMediatorExample.client.Customer;

import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.MovieLink;
import javafx.application.Platform;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


import java.util.concurrent.TimeUnit;


public class ExpiredLinkChecker implements Runnable {

    private SimpleClient client;
    private int id;
    private List<MovieLink> movieLinks;
    private CustomerController customerController;
    private boolean stopChecker = false;

    public ExpiredLinkChecker(SimpleClient client, int id, List<MovieLink> movieLinks, CustomerController customerController) {
        this.client = client;
        this.id = id;
        this.movieLinks = movieLinks;
        this.customerController = customerController;
    }

    @Override
    public void run() {
        while (true) {
            if (stopChecker){
                break;
            }

            try {
                for (MovieLink movieLink : movieLinks) {
                    if (movieLink.isValid()) {
                        LocalDateTime expirationTime = movieLink.getExpirationTime();
                        LocalDateTime creationTime = movieLink.getCreationTime();
                        LocalDateTime now = LocalDateTime.now();


                        long delay = Duration.between(now, expirationTime).toMillis();

                        if (delay < 0 && movieLink.isActive()) {
                            System.out.println("time expired");
                            movieLink.setInvalid();
                            movieLink.setInactive();
                            Thread.sleep(1000);
                            Platform.runLater(() -> customerController.linkRefreshRequest(0, movieLink));
                        } else if (!movieLink.isActive()) {
                            delay = Duration.between(creationTime, now).toMillis();
                            if (delay > 0) {
                                System.out.println("time active");
                                Thread.sleep(1500);
                                movieLink.setActive();
                                Platform.runLater(() -> customerController.linkRefreshRequest(1, movieLink));

                            } else if (!movieLink.isNotified() && delay > -3600000) {
                                System.out.println("active notification");
                                Thread.sleep(1500);
                                movieLink.setNotified();
                                Platform.runLater(() -> customerController.linkRefreshRequest(2, movieLink));

                            }


                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void stopChecker(){
        this.stopChecker=true;
    }

    public List<MovieLink> getMovieLinks() {return this.movieLinks;}

    public void updateChecker(List<MovieLink> movieLinks){this.movieLinks=movieLinks;}

}


