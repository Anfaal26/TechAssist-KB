package com.techchat.controller;

import com.techchat.model.ChatSession;
import com.techchat.model.Message;
import com.techchat.service.ConversationManager;
import com.techchat.service.ExportService;
import com.techchat.util.FadeInUtil;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller for Chat History View
 */
public class ChatHistoryViewController implements Initializable {

    @FXML
    private HBox historyHeader;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private CheckBox bookmarkedOnlyCheckbox;

    @FXML
    private ListView<ChatSession> conversationsList;

    @FXML
    private Label statsLabel;

    @FXML
    private Button viewButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button deleteButton;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        loadConversations();
        setupConversationCellFactory();

        // Enable search on Enter key
        searchField.setOnAction(event -> handleSearch());
    }

    /**
     * Play entrance animations for chat history view elements
     */
    private void playEntranceAnimations() {
        // Fade in history elements with staggered timing
        if (historyHeader != null) {
            FadeInUtil.fadeIn(historyHeader, 0, 400);
        }
        if (conversationsList != null) {
            FadeInUtil.fadeIn(conversationsList, 100, 400);
        }
    }

    /**
     * Load all conversations from ConversationManager
     */
    private void loadConversations() {
        List<ChatSession> sessions;

        if (bookmarkedOnlyCheckbox.isSelected()) {
            sessions = ConversationManager.getInstance().getBookmarkedSessions();
        } else {
            sessions = ConversationManager.getInstance().getAllSessions();
        }

        conversationsList.setItems(FXCollections.observableArrayList(sessions));
        statsLabel.setText("Total conversations: " + sessions.size());
    }

    /**
     * Setup custom cell factory for conversation list
     */
    private void setupConversationCellFactory() {
        conversationsList.setCellFactory(listView -> new ListCell<ChatSession>() {
            @Override
            protected void updateItem(ChatSession session, boolean empty) {
                super.updateItem(session, empty);

                if (empty || session == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    container.setPadding(new Insets(5));

                    // Preview text
                    String preview = session.getConversationPreview();
                    if (session.isBookmarked()) {
                        preview = "⭐ " + preview;
                    }
                    Label previewLabel = new Label(preview);
                    previewLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
                    previewLabel.setWrapText(true);

                    // Metadata
                    String metadata = String.format("%s | %d messages",
                            dateFormat.format(session.getStartedAt()),
                            session.getMessageCount());
                    Label metadataLabel = new Label(metadata);
                    metadataLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

                    container.getChildren().addAll(previewLabel, metadataLabel);
                    setGraphic(container);
                }
            }
        });
    }

    /**
     * Handle search button click
     */
    @FXML
    private void handleSearch() {
        String query = searchField.getText();

        if (query == null || query.trim().isEmpty()) {
            loadConversations();
            return;
        }

        List<ChatSession> results = ConversationManager.getInstance().searchSessions(query);

        // Apply bookmark filter if checked
        if (bookmarkedOnlyCheckbox.isSelected()) {
            results.removeIf(session -> !session.isBookmarked());
        }

        conversationsList.setItems(FXCollections.observableArrayList(results));
        statsLabel.setText("Found " + results.size() + " conversation(s)");
    }

    /**
     * Handle filter checkbox change
     */
    @FXML
    private void handleFilterChange() {
        if (searchField.getText() != null && !searchField.getText().trim().isEmpty()) {
            handleSearch();
        } else {
            loadConversations();
        }
    }

    /**
     * Handle view conversation button
     */
    @FXML
    private void handleViewConversation() {
        ChatSession selected = conversationsList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a conversation to view.");
            return;
        }

        // Create dialog to show full conversation
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Conversation: " + selected.getConversationPreview());
        dialog.setHeaderText("Started: " + dateFormat.format(selected.getStartedAt()));

        // Create content
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        for (Message msg : selected.getMessages()) {
            VBox messageBox = new VBox(3);

            String sender = msg.isUser() ? "USER" : "ASSISTANT";
            Label senderLabel = new Label("[" + sender + "] " + dateFormat.format(msg.getTimestamp()));
            senderLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");

            Label contentLabel = new Label(msg.getContent());
            contentLabel.setWrapText(true);
            contentLabel.setMaxWidth(500);

            messageBox.getChildren().addAll(senderLabel, contentLabel);

            // Add sources if available
            if (!msg.getCitedSources().isEmpty()) {
                Label sourcesLabel = new Label("Sources: " + String.join(", ", msg.getCitedSources()));
                sourcesLabel.setStyle("-fx-font-size: 10px; -fx-font-style: italic;");
                messageBox.getChildren().add(sourcesLabel);
            }

            content.getChildren().add(messageBox);
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(550);

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    /**
     * Handle export conversation button
     */
    @FXML
    private void handleExportConversation() {
        ChatSession selected = conversationsList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a conversation to export.");
            return;
        }

        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Export Conversation");
        fileChooser.setInitialFileName(ExportService.generateExportFilename(selected));
        fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            try {
                ExportService.exportToTxt(selected, file);
                showAlert("Export Successful", "Conversation exported to:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                showAlert("Export Failed", "Failed to export conversation:\n" + e.getMessage());
            }
        }
    }

    /**
     * Handle delete conversation button
     */
    @FXML
    private void handleDeleteConversation() {
        ChatSession selected = conversationsList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a conversation to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Conversation");
        confirmAlert.setHeaderText("Delete this conversation?");
        confirmAlert.setContentText("Preview: " + selected.getConversationPreview());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the session from ConversationManager
            boolean deleted = ConversationManager.getInstance().deleteSession(selected.getSessionId());

            if (deleted) {
                // Refresh the conversation list
                if (searchField.getText() != null && !searchField.getText().trim().isEmpty()) {
                    handleSearch();
                } else {
                    loadConversations();
                }

                showAlert("Deleted", "Conversation deleted successfully.");
            } else {
                showAlert("Error", "Failed to delete conversation.");
            }
        }
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
