    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package technicalknowledgechat;

import com.techchat.model.EndUser;
import com.techchat.service.ChatService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author User
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private VBox messageContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private ChatService chatService;

    @FXML
    private void handleSendAction(ActionEvent event) {
        String messageText = userInput.getText();
        if (messageText == null || messageText.trim().isEmpty()) {
            return;
        }

        // Add user message
        addMessage(messageText, true);

        // Get bot response from ChatService
        String botResponse = chatService.handleUserMessage(messageText);
        addMessage(botResponse, false);

        userInput.clear();
    }

    private void addMessage(String text, boolean isUser) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5));

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        messageLabel.setPadding(new Insets(10));
        messageLabel.setStyle("-fx-font-size: 13px;");

        if (isUser) {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageLabel.setBackground(new Background(new BackgroundFill(
                    Color.rgb(0, 123, 255), new CornerRadii(15), Insets.EMPTY)));
            messageLabel.setTextFill(Color.WHITE);
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageLabel.setBackground(new Background(new BackgroundFill(
                    Color.rgb(230, 230, 230), new CornerRadii(15), Insets.EMPTY)));
            messageLabel.setTextFill(Color.BLACK);
        }

        messageBox.getChildren().add(messageLabel);
        messageContainer.getChildren().add(messageBox);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize ChatService
        chatService = new ChatService();

        // Create demo user and start session
        EndUser demoUser = new EndUser("u1", "Demo User", "demo@example.com");
        chatService.startNewSession(demoUser);

        // Initial bot greeting
        addMessage("Hello! I am your Technical Knowledge Chatbot. Ask me about JavaFX or OOP!", false);
    }

}
