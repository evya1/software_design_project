package il.cshaifasweng.OCSFMediatorExample.client.PurchaseFiles;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Seat;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieSlot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;


import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PAYMENT_SCREEN;
import static il.cshaifasweng.OCSFMediatorExample.client.FilePathController.PRIMARY_SCREEN;

public class ChooseSeatsController implements ClientDependent {
    SimpleClient client;
    Message localMessage;

    //region Buttons
    @FXML
    private Button returnBtn;

    @FXML
    private Button homeScreenBtn;

    @FXML
    private Button purchaseTicketBtn;
    //endregion

    //region Rows
    @FXML
    private ImageView row1seat1;

    @FXML
    private ImageView row1seat10;

    @FXML
    private ImageView row1seat2;

    @FXML
    private ImageView row1seat3;

    @FXML
    private ImageView row1seat4;

    @FXML
    private ImageView row1seat5;

    @FXML
    private ImageView row1seat6;

    @FXML
    private ImageView row1seat7;

    @FXML
    private ImageView row1seat8;

    @FXML
    private ImageView row1seat9;

    @FXML
    private ImageView row2seat1;

    @FXML
    private ImageView row2seat10;

    @FXML
    private ImageView row2seat2;

    @FXML
    private ImageView row2seat3;

    @FXML
    private ImageView row2seat4;

    @FXML
    private ImageView row2seat5;

    @FXML
    private ImageView row2seat6;

    @FXML
    private ImageView row2seat7;

    @FXML
    private ImageView row2seat8;

    @FXML
    private ImageView row2seat9;

    @FXML
    private ImageView row3seat1;

    @FXML
    private ImageView row3seat10;

    @FXML
    private ImageView row3seat2;

    @FXML
    private ImageView row3seat3;

    @FXML
    private ImageView row3seat4;

    @FXML
    private ImageView row3seat5;

    @FXML
    private ImageView row3seat6;

    @FXML
    private ImageView row3seat7;

    @FXML
    private ImageView row3seat8;

    @FXML
    private ImageView row3seat9;

    @FXML
    private ImageView row4seat1;

    @FXML
    private ImageView row4seat10;

    @FXML
    private ImageView row4seat2;

    @FXML
    private ImageView row4seat3;

    @FXML
    private ImageView row4seat4;

    @FXML
    private ImageView row4seat5;

    @FXML
    private ImageView row4seat6;

    @FXML
    private ImageView row4seat7;

    @FXML
    private ImageView row4seat8;

    @FXML
    private ImageView row4seat9;

    @FXML
    private ImageView row5seat1;

    @FXML
    private ImageView row5seat10;

    @FXML
    private ImageView row5seat2;

    @FXML
    private ImageView row5seat3;

    @FXML
    private ImageView row5seat4;

    @FXML
    private ImageView row5seat5;

    @FXML
    private ImageView row5seat6;

    @FXML
    private ImageView row5seat7;

    @FXML
    private ImageView row5seat8;

    @FXML
    private ImageView row5seat9;

    @FXML
    private ImageView row6seat1;

    @FXML
    private ImageView row6seat10;

    @FXML
    private ImageView row6seat2;

    @FXML
    private ImageView row6seat3;

    @FXML
    private ImageView row6seat4;

    @FXML
    private ImageView row6seat5;

    @FXML
    private ImageView row6seat6;

    @FXML
    private ImageView row6seat7;

    @FXML
    private ImageView row6seat8;

    @FXML
    private ImageView row6seat9;

    @FXML
    private ImageView row7seat1;

    @FXML
    private ImageView row7seat10;

    @FXML
    private ImageView row7seat2;

    @FXML
    private ImageView row7seat3;

    @FXML
    private ImageView row7seat4;

    @FXML
    private ImageView row7seat5;

    @FXML
    private ImageView row7seat6;

    @FXML
    private ImageView row7seat7;

    @FXML
    private ImageView row7seat8;

    @FXML
    private ImageView row7seat9;
    //endregion

    @FXML
    private Text screenTitle;


    @FXML
    public void initialize(){
        //This is the eventbus used to transfer information from the client to the server and back
        EventBus.getDefault().register(this);
        purchaseTicketBtn.setVisible(false);
        purchaseTicketBtn.setDisable(true);

        Image availableSeatImage = new Image("il/cshaifasweng/OCSFMediatorExample/client/theaters/AvailableSeat.png");
        Image takenSeatImage = new Image("il/cshaifasweng/OCSFMediatorExample/client/theaters/TakenSeat.png");

        List<Seat> seats = localMessage.getMovieSlot().getSeatList();

        System.out.println("this is the list " + seats.isEmpty()); //CHECK

        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            ImageView seatImageView = getSeatImageView(i);

            if (seat.isTaken()) {
                seatImageView.setImage(takenSeatImage);
                seatImageView.setUserData("taken");
                seatImageView.setDisable(true);
            } else {
                seatImageView.setImage(availableSeatImage);
                seatImageView.setUserData("available");
            }
        }


        // region Initialize event handlers for all seats
        row1seat1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat1));
        row1seat2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat2));
        row1seat3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat3));
        row1seat4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat4));
        row1seat5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat5));
        row1seat6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat6));
        row1seat7.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat7));
        row1seat8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat8));
        row1seat9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat9));
        row1seat10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row1seat10));

        row2seat1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat1));
        row2seat2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat2));
        row2seat3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat3));
        row2seat4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat4));
        row2seat5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat5));
        row2seat6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat6));
        row2seat7.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat7));
        row2seat8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat8));
        row2seat9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat9));
        row2seat10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row2seat10));

        row3seat1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat1));
        row3seat2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat2));
        row3seat3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat3));
        row3seat4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat4));
        row3seat5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat5));
        row3seat6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat6));
        row3seat7.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat7));
        row3seat8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat8));
        row3seat9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat9));
        row3seat10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row3seat10));

        row4seat1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat1));
        row4seat2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat2));
        row4seat3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat3));
        row4seat4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat4));
        row4seat5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat5));
        row4seat6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat6));
        row4seat7.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat7));
        row4seat8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat8));
        row4seat9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat9));
        row4seat10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row4seat10));

        row5seat1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat1));
        row5seat2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat2));
        row5seat3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat3));
        row5seat4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat4));
        row5seat5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat5));
        row5seat6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat6));
        row5seat7.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat7));
        row5seat8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat8));
        row5seat9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat9));
        row5seat10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row5seat10));

        row6seat1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat1));
        row6seat2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat2));
        row6seat3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat3));
        row6seat4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat4));
        row6seat5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat5));
        row6seat6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat6));
        row6seat7.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat7));
        row6seat8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat8));
        row6seat9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat9));
        row6seat10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row6seat10));

        row7seat1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat1));
        row7seat2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat2));
        row7seat3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat3));
        row7seat4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat4));
        row7seat5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat5));
        row7seat6.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat6));
        row7seat7.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat7));
        row7seat8.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat8));
        row7seat9.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat9));
        row7seat10.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleSeatClick(row7seat10));
        //endregion

        //Implement GUI logic here related to initialization of objects.

    }

    @Subscribe
    public void handleMessageFromServer(MessageEvent event){
        //Implement logic here
    }

    @FXML
    void purchaseTicketAction(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            //TODO: add check to see if chosen seat is already taken
            Stage stage = (Stage) purchaseTicketBtn.getScene().getWindow();
            Message message = new Message();
            //TODO: take the movieslot info + the current seats info and pass them on (add a seat list entity to message)
            message.setMessage("New Movie Ticket");
            //TODO: the return button in the purchase screen does not go back
            message.setSourceFXML(localMessage.getSourceFXML());
            EventBus.getDefault().unregister(this);
            client.moveScene(PAYMENT_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void returnHomeAction(ActionEvent event) {
        if (client == null) {
            System.err.println("Client is not initialized!\n");
            return;
        }
        try {
            Stage stage = (Stage) homeScreenBtn.getScene().getWindow();
            Message message = new Message();
            EventBus.getDefault().unregister(this);
            client.moveScene(PRIMARY_SCREEN, stage, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void returnBtnAction(ActionEvent event) {
        //TODO: it doesn't work for now
        Stage stage = (Stage) returnBtn.getScene().getWindow();
        client.moveScene(localMessage.getSourceFXML(), stage, null);
    }

    private void handleSeatClick(ImageView seat) {
        Image markedImage = new Image("il/cshaifasweng/OCSFMediatorExample/client/theaters/MarkedSeat.png");
        Image availableImage = new Image("il/cshaifasweng/OCSFMediatorExample/client/theaters/AvailableSeat.png");
        if(seat.getUserData().equals("selected")){
            seat.setImage(availableImage);
            seat.setUserData("available");
        }
        else if (seat.getUserData().equals("available")){
            seat.setImage(markedImage);
            seat.setUserData("selected");
        }
        else if(seat.getUserData().equals("taken")){
            seat.setDisable(true);
        }
        purchaseTicketBtn.setDisable(!areAnySeatsSelected());
        purchaseTicketBtn.setVisible(areAnySeatsSelected());
    }

    private ImageView getSeatImageView(int index) {
        switch (index) {
            //region 1st row
            case 0: return row1seat1;
            case 1: return row1seat2;
            case 2: return row1seat3;
            case 3: return row1seat4;
            case 4: return row1seat5;
            case 5: return row1seat6;
            case 6: return row1seat7;
            case 7: return row1seat8;
            case 8: return row1seat9;
            case 9: return row1seat10;
            //endregion
            //region 2nd row
            case 10: return row2seat1;
            case 11: return row2seat2;
            case 12: return row2seat3;
            case 13: return row2seat4;
            case 14: return row2seat5;
            case 15: return row2seat6;
            case 16: return row2seat7;
            case 17: return row2seat8;
            case 18: return row2seat9;
            case 19: return row2seat10;
            //endregion
            //region 3rd row
            case 20: return row3seat1;
            case 21: return row3seat2;
            case 22: return row3seat3;
            case 23: return row3seat4;
            case 24: return row3seat5;
            case 25: return row3seat6;
            case 26: return row3seat7;
            case 27: return row3seat8;
            case 28: return row3seat9;
            case 29: return row3seat10;
            //endregion
            //region 4th row
            case 30: return row4seat1;
            case 31: return row4seat2;
            case 32: return row4seat3;
            case 33: return row4seat4;
            case 34: return row4seat5;
            case 35: return row4seat6;
            case 36: return row4seat7;
            case 37: return row4seat8;
            case 38: return row4seat9;
            case 39: return row4seat10;
            //endregion
            //region 5th row
            case 40: return row5seat1;
            case 41: return row5seat2;
            case 42: return row5seat3;
            case 43: return row5seat4;
            case 44: return row5seat5;
            case 45: return row5seat6;
            case 46: return row5seat7;
            case 47: return row5seat8;
            case 48: return row5seat9;
            case 49: return row5seat10;
            //endregion
            //region 6th row
            case 50: return row6seat1;
            case 51: return row6seat2;
            case 52: return row6seat3;
            case 53: return row6seat4;
            case 54: return row6seat5;
            case 55: return row6seat6;
            case 56: return row6seat7;
            case 57: return row6seat8;
            case 58: return row6seat9;
            case 59: return row6seat10;
            //endregion
            //region 7th row
            case 60: return row7seat1;
            case 61: return row7seat2;
            case 62: return row7seat3;
            case 63: return row7seat4;
            case 64: return row7seat5;
            case 65: return row7seat6;
            case 66: return row7seat7;
            case 67: return row7seat8;
            case 68: return row7seat9;
            case 69: return row7seat10;
            //endregion

            default: return null;  // Fallback in case the index is out of bounds
        }
    }

    private boolean areAnySeatsSelected() {
        ImageView[] seats = {row1seat1, row1seat2, row1seat3, row1seat4, row1seat5, row1seat6, row1seat7, row1seat8, row1seat9, row1seat10,
                row2seat1, row2seat2, row2seat3, row2seat4, row2seat5, row2seat6, row2seat7, row2seat8, row2seat9, row2seat10,
                row3seat1, row3seat2, row3seat3, row3seat4, row3seat5, row3seat6, row3seat7, row3seat8, row3seat9, row3seat10,
                row4seat1, row4seat2, row4seat3, row4seat4, row4seat5, row4seat6, row4seat7, row4seat8, row4seat9, row4seat10,
                row5seat1, row5seat2, row5seat3, row5seat4, row5seat5, row5seat6, row5seat7, row5seat8, row5seat9, row5seat10,
                row6seat1, row6seat2, row6seat3, row6seat4, row6seat5, row6seat6, row6seat7, row6seat8, row6seat9, row6seat10,
                row7seat1, row7seat2, row7seat3, row7seat4, row7seat5, row7seat6, row7seat7, row7seat8, row7seat9, row7seat10};

        for (ImageView seat : seats) {
            if ("selected".equals(seat.getUserData())) {
                return true;
            }
        }
        return false;
    }

    //region Interface Methods
    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }
    //endregion
}
