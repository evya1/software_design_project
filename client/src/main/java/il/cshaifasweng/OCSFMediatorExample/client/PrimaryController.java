package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {

	@FXML
	private TextField timeTF;

	@FXML
	private TextField MessageTF;

	@FXML
	private Button SendBtn;

	@FXML
	private TextField DataFromServerTF;

	@FXML
	private DatePicker movieDate;

	@FXML
	private Label dateLabel;

	@FXML
	private ListView<String> movieList;

	@FXML
	private Label movieInfo;

	private int msgId;

	@FXML
	void sendMessage(ActionEvent event) {
		try {
			Message message = new Message(msgId++, MessageTF.getText());
			MessageTF.clear();
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Subscribe
	public void setDataFromServerTF(MessageEvent event) {
		DataFromServerTF.setText(event.getMessage().getMessage());
	}

	@Subscribe
	public void getStarterData(NewSubscriberEvent event) {
		try {
			Message message = new Message(msgId, "send Submitters IDs");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Subscribe
	public void errorEvent(ErrorEvent event) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR,
					String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n",
							event.getMessage().getId(),
							event.getMessage().getMessage(),
							event.getMessage().getTimeStamp().format(dtf))
			);
			alert.setTitle("Error!");
			alert.setHeaderText("Error:");
			alert.show();
		});
	}

	@FXML
	void initialize() {
		EventBus.getDefault().register(this);
		MessageTF.clear();
		DataFromServerTF.clear();
		msgId = 0;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			LocalTime currentTime = LocalTime.now();
			timeTF.setText(currentTime.format(dtf));
		}),
				new KeyFrame(Duration.seconds(1))
		);
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
		try {
			Message message = new Message(msgId, "add client");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Making it so previous dates become disabled
		movieDate.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);
			}
		});

		// Filling the list
		movieList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
				currentMovie = movieList.getSelectionModel().getSelectedItem();
				movieInfo.setText(String.format("Name: %s\nHebrew Name: %s\nproducer: %s\nMain Cast: %s\nMovie Description:\n%s",
						currentMovie, "שם סרט בעברית", "Producer producer", "That guy, that girl, and these guys", "In a far distant land, it's said there's a distant land that contains a story about a distant land that talks about a distant land and a distant land.".replaceAll("(.{80})", "$1-\n-")));
			}
		});
	}

	// GUI-related variables and methods
	String[] movies = {"Jacked Sparrow", "The Assignment - Return of Calculas", "Bobsponge The Movie"};
	double[] duration = {2.5, 2.75, 3};
	int[] hours = {1230, 1515, 1000, 1800};
	String[] dates = {"2024-06-28", "2024-06-28", "2024-06-29"};

	String currentMovie;

	@FXML
	private void getDate(ActionEvent event) {
		movieList.getItems().clear();

		LocalDate localDate = movieDate.getValue();
		List<Integer> indices = new ArrayList<>();
		List<String> currentMovies = new ArrayList<>();

		if (localDate != null) {
			String currentDate = localDate.toString();
			for (int i = 0; i < dates.length; i++) {
				if (dates[i].equals(currentDate)) {
					indices.add(i);
				}
			}
			for (int i = 0; i < indices.size(); i++) {
				currentMovies.add(movies[indices.get(i)]);
			}
			movieList.getItems().addAll(currentMovies);
		} else {
			dateLabel.setText("No date selected");
		}
	}
}
