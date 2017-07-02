package groceryList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox
{
    private static final String CLOSE_BUTTON = "Ok";

    public static void display(String title, String message)
    {
	Stage alertBox = new Stage();

	alertBox.initModality(Modality.APPLICATION_MODAL);
	alertBox.setTitle(title);
	
	Label messageLabel = new Label(message);
	messageLabel.getStyleClass().add("alert-message-text");
	Button closeButton = new Button(CLOSE_BUTTON);
	closeButton.setOnAction(e -> alertBox.close());
	
	VBox alertLayout = new VBox();
	alertLayout.getChildren().addAll(messageLabel, closeButton);
	alertLayout.setAlignment(Pos.CENTER);
	alertLayout.setPadding(new Insets(10, 10, 10, 10));
	
	Scene scene = new Scene(alertLayout);
	scene.getStylesheets().add("groceryList/Stylesheet.css");
	
	alertBox.setScene(scene);
	alertBox.showAndWait();	
    }
}
