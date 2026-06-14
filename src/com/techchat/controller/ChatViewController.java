package com.techchat.controller;

import com.techchat.model.Article;
import com.techchat.model.ChatSession;
import com.techchat.model.EndUser;
import com.techchat.service.ChatService;
import com.techchat.service.FeedbackService;
import com.techchat.util.FadeInUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ChatViewController implements Initializable {

    @FXML
    private VBox chatHeader;

    @FXML
    private VBox messageContainer;

    @FXML
    private javafx.scene.control.ScrollPane chatScrollPane;

    @FXML
    private HBox inputArea;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    @FXML
    private Button imageButton;

    @FXML
    private ListView<String> sourcesList;

    @FXML
    private Button bookmarkButton;

    @FXML
    private Button exportButton;

    private ChatService chatService;
    private EndUser currentUser;
    private com.techchat.service.OpenAIService openAIService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        // Initialize ChatService
        chatService = new ChatService();
        openAIService = new com.techchat.service.OpenAIService();

        // Create demo user and start session
        currentUser = new EndUser("u1", "Demo User", "demo@example.com");
        chatService.startNewSession(currentUser);

        // Initial bot greeting
        addMessage("Hello! I am TechAssist. Ask me anything or upload an image! 📷", false);

        // Register cleanup handler to save session when view is destroyed
        chatScrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (oldScene != null && newScene == null) {
                // Scene is being removed - save session
                registerSessionToHistory();
            }
        });
    }

    /**
     * Play entrance animations for chat view elements
     */
    private void playEntranceAnimations() {
        // Fade in chat elements with staggered timing
        if (chatHeader != null) {
            FadeInUtil.fadeIn(chatHeader, 0, 400);
        }
        if (chatScrollPane != null) {
            FadeInUtil.fadeIn(chatScrollPane, 100, 400);
        }
        if (inputArea != null) {
            FadeInUtil.fadeIn(inputArea, 200, 400);
        }
        if (sourcesList != null) {
            FadeInUtil.fadeIn(sourcesList, 300, 400);
        }
    }

    /**
     * Register/save the current session to conversation history
     */
    private void registerSessionToHistory() {
        if (chatService != null && chatService.getCurrentSession() != null) {
            chatService.saveCurrentSession();
        }
    }

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

        // Update sources list from the bot's cited sources
        updateSourcesFromLastMessage();

        userInput.clear();
    }

    private void addMessage(String text, boolean isUser) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5));

        // Get current theme from config
        String currentTheme = com.techchat.config.AppConfig.getInstance().getTheme();
        boolean isDarkTheme = "dark".equalsIgnoreCase(currentTheme);

        // Check if message contains references section (now inline)
        String[] parts = text.split("\n\nReferences: ");
        boolean hasReferences = parts.length > 1;

        if (hasReferences && !isUser) {
            // Use TextFlow for styled citations
            javafx.scene.text.TextFlow textFlow = new javafx.scene.text.TextFlow();
            textFlow.setMaxWidth(450);
            textFlow.setPadding(new Insets(12));

            // Main message text (before references)
            javafx.scene.text.Text mainText = new javafx.scene.text.Text(parts[0].trim() + "\n\n");
            mainText.setStyle("-fx-font-size: 14px;");

            // References section (inline, smaller)
            javafx.scene.text.Text refLabel = new javafx.scene.text.Text("References: ");
            refLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

            javafx.scene.text.Text references = new javafx.scene.text.Text(parts[1].trim());
            references.setStyle("-fx-font-size: 10px; -fx-font-style: italic;");

            textFlow.getChildren().addAll(mainText, refLabel, references);

            if (isDarkTheme) {
                textFlow.setStyle("-fx-background-color: #2D333B; -fx-background-radius: 15;");
                mainText.setFill(Color.rgb(240, 246, 252)); // Brighter text for dark theme
                refLabel.setFill(Color.rgb(139, 148, 158)); // Muted gray for label
                references.setFill(Color.rgb(139, 148, 158)); // Muted gray for references
            } else {
                textFlow.setStyle("-fx-background-color: #F6F8FA; -fx-background-radius: 15;");
                mainText.setFill(Color.rgb(31, 41, 55)); // Dark text for light theme
                refLabel.setFill(Color.rgb(87, 96, 106)); // Muted gray for label
                references.setFill(Color.rgb(87, 96, 106)); // Muted gray for references
            }

            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageBox.getChildren().add(textFlow);
        } else {
            // Regular message without citations
            Label messageLabel = new Label(text);
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(400);
            messageLabel.setPadding(new Insets(12));
            messageLabel.setStyle("-fx-font-size: 14px;"); // Consistent font size

            if (isUser) {
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                messageLabel.setBackground(new Background(new BackgroundFill(
                        Color.rgb(31, 111, 235), new CornerRadii(15), Insets.EMPTY))); // Blue accent
                messageLabel.setTextFill(Color.WHITE);
            } else {
                messageBox.setAlignment(Pos.CENTER_LEFT);
                if (isDarkTheme) {
                    // Dark theme: lighter background with brighter text
                    messageLabel.setBackground(new Background(new BackgroundFill(
                            Color.rgb(45, 51, 59), new CornerRadii(15), Insets.EMPTY)));
                    messageLabel.setTextFill(Color.rgb(240, 246, 252)); // Brighter text
                } else {
                    // Light theme: light surface with dark text
                    messageLabel.setBackground(new Background(new BackgroundFill(
                            Color.rgb(246, 248, 250), new CornerRadii(15), Insets.EMPTY)));
                    messageLabel.setTextFill(Color.rgb(31, 41, 55));
                }
            }
            messageBox.getChildren().add(messageLabel);
        }

        // Add rating buttons for AI messages only
        if (!isUser) {
            HBox ratingBox = new HBox(5);
            ratingBox.setPadding(new Insets(5, 0, 0, 10));
            ratingBox.setAlignment(Pos.CENTER_LEFT);

            Label ratingLabel = new Label("Rate this response:");
            ratingLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

            // Create 5 star buttons
            for (int i = 1; i <= 5; i++) {
                final int rating = i;
                Button starBtn = new Button("⭐");
                starBtn.setStyle("-fx-font-size: 14px; -fx-background-color: transparent; -fx-cursor: hand;");
                starBtn.setOnAction(e -> handleRating(rating, text));
                ratingBox.getChildren().add(starBtn);
            }

            // Create VBox to stack message and rating
            VBox messageWithRating = new VBox(0);
            if (messageBox.getChildren().size() > 0) {
                messageWithRating.getChildren().add(messageBox.getChildren().get(0));
            }
            messageWithRating.getChildren().add(ratingBox);

            messageBox.getChildren().clear();
            messageBox.getChildren().add(messageWithRating);
        }

        messageContainer.getChildren().add(messageBox);

        // Auto-scroll to bottom after adding message
        scrollToBottom();
    }

    /**
     * Scroll the chat to the bottom to show latest messages
     */
    private void scrollToBottom() {
        // Use Platform.runLater to ensure the layout is updated before scrolling
        javafx.application.Platform.runLater(() -> {
            chatScrollPane.setVvalue(1.0);
        });
    }

    /**
     * Handle image upload button click
     */
    @FXML
    private void handleImageUpload() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif",
                        "*.bmp"));

        java.io.File selectedFile = fileChooser.showOpenDialog(imageButton.getScene().getWindow());

        if (selectedFile != null) {
            // Show user selected image
            addMessage("📷 Uploaded: " + selectedFile.getName(), true);

            // Show loading message
            addMessage("🔍 Analyzing image...", false);

            // Process image in background thread to avoid freezing UI
            new Thread(() -> {
                try {
                    String response = openAIService.getVisionResponse(
                            selectedFile.getAbsolutePath(),
                            "Describe this image in detail and extract any text you see.");

                    // Update UI on JavaFX thread
                    javafx.application.Platform.runLater(() -> {
                        // Remove loading message
                        messageContainer.getChildren().remove(messageContainer.getChildren().size() - 1);
                        // Add actual response
                        addMessage(response, false);
                    });
                } catch (Exception e) {
                    javafx.application.Platform.runLater(() -> {
                        // Remove loading message
                        messageContainer.getChildren().remove(messageContainer.getChildren().size() - 1);
                        // Show error
                        addMessage("❌ Error processing image: " + e.getMessage(), false);
                    });
                }
            }).start();
        }
    }

    /**
     * Update sources list from the last bot message's cited sources
     */
    private void updateSourcesFromLastMessage() {
        ChatSession session = chatService.getCurrentSession();
        if (session == null) {
            sourcesList.setItems(FXCollections.observableArrayList("No sources found"));
            return;
        }

        List<com.techchat.model.Message> messages = session.getMessages();
        if (messages.isEmpty()) {
            sourcesList.setItems(FXCollections.observableArrayList("No sources found"));
            return;
        }

        // Get the last bot message
        com.techchat.model.Message lastBotMessage = null;
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (!messages.get(i).isUser()) {
                lastBotMessage = messages.get(i);
                break;
            }
        }

        if (lastBotMessage != null && !lastBotMessage.getCitedSources().isEmpty()) {
            sourcesList.setItems(FXCollections.observableArrayList(lastBotMessage.getCitedSources()));
        } else {
            sourcesList.setItems(FXCollections.observableArrayList("No sources found"));
        }
    }

    /**
     * Handle bookmark button click
     */
    @FXML
    private void handleBookmark() {
        if (chatService.getCurrentSession() == null) {
            showAlert("No Active Chat", "There is no active conversation to bookmark.");
            return;
        }

        boolean currentlyBookmarked = chatService.getCurrentSession().isBookmarked();
        chatService.getCurrentSession().setBookmarked(!currentlyBookmarked);

        // Update button text
        if (chatService.getCurrentSession().isBookmarked()) {
            bookmarkButton.setText("⭐ Bookmarked");
            bookmarkButton.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white;");
        } else {
            bookmarkButton.setText("⭐ Bookmark");
            bookmarkButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: white;");
        }
    }

    /**
     * Handle export button click
     */
    @FXML
    private void handleExport() {
        if (chatService.getCurrentSession() == null ||
                chatService.getCurrentSession().getMessageCount() == 0) {
            showAlert("No Conversation", "There is no conversation to export.");
            return;
        }

        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Export Conversation");
        fileChooser.setInitialFileName(
                com.techchat.service.ExportService.generateExportFilename(chatService.getCurrentSession()));
        fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("Text Files", "*.txt"));

        java.io.File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            try {
                com.techchat.service.ExportService.exportToTxt(chatService.getCurrentSession(), file);
                showAlert("Export Successful", "Conversation exported to:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                showAlert("Export Failed", "Failed to export conversation:\n" + e.getMessage());
            }
        }
    }

    /**
     * Handle rating for AI response
     */
    private void handleRating(int rating, String messageText) {
        // Show optional comment dialog
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Feedback");
        dialog.setHeaderText("You rated this response: " + rating + "/5 stars");
        dialog.setContentText("Optional comment:");

        java.util.Optional<String> result = dialog.showAndWait();
        String comment = result.orElse("");

        // Submit feedback
        try {
            FeedbackService.getInstance().submitFeedback(
                    rating,
                    comment,
                    chatService.getCurrentSession(),
                    currentUser);
            showAlert("Thank You!", "Your feedback has been recorded.");
        } catch (Exception e) {
            showAlert("Error", "Failed to submit feedback: " + e.getMessage());
        }
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
