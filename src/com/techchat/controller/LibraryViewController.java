package com.techchat.controller;

import com.techchat.model.Article;
import com.techchat.service.ActivityLogService;
import com.techchat.service.ChatService;
import com.techchat.service.DocumentService;
import com.techchat.service.UserSession;
import com.techchat.util.FadeInUtil;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

public class LibraryViewController implements Initializable {

    @FXML
    private HBox searchBar;

    @FXML
    private TableView<DocumentService.DocumentInfo> documentsTable;

    @FXML
    private HBox actionButtons;

    @FXML
    private TableColumn<DocumentService.DocumentInfo, String> nameColumn;

    @FXML
    private TableColumn<DocumentService.DocumentInfo, String> typeColumn;

    @FXML
    private TableColumn<DocumentService.DocumentInfo, String> tagsColumn;

    @FXML
    private TableColumn<DocumentService.DocumentInfo, String> dateColumn;

    @FXML
    private TableColumn<DocumentService.DocumentInfo, Void> actionsColumn;

    private ObservableList<DocumentService.DocumentInfo> documents;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        // Initialize table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        tagsColumn.setCellValueFactory(new PropertyValueFactory<>("tags"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));

        // Setup actions column with delete button
        setupActionsColumn();

        // Initialize documents list
        documents = FXCollections.observableArrayList();
        documentsTable.setItems(documents);

        // Load existing articles from KB
        loadExistingArticles();
    }

    /**
     * Play entrance animations for library view elements
     */
    private void playEntranceAnimations() {
        // Fade in search bar, table, and action buttons with staggered timing
        if (searchBar != null) {
            FadeInUtil.fadeIn(searchBar, 0, 400);
        }
        if (documentsTable != null) {
            FadeInUtil.fadeIn(documentsTable, 100, 400);
        }
        if (actionButtons != null) {
            FadeInUtil.fadeIn(actionButtons, 200, 400);
        }
    }

    /**
     * Load existing articles from knowledge base
     */
    private void loadExistingArticles() {
        List<Article> articles = ChatService.getSharedKnowledgeBase().getAllArticles();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Article article : articles) {
            String tags = String.join(", ", article.getTags());
            String date = dateFormat.format(new Date());

            documents.add(new DocumentService.DocumentInfo(
                    article.getTitle(),
                    "article",
                    tags,
                    date));
        }
    }

    /**
     * Handle upload document button click
     */
    @FXML
    private void handleUploadDocument() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Document or Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported", "*.txt", "*.pdf", "*.docx", "*.png", "*.jpg",
                        "*.jpeg"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Documents", "*.docx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(documentsTable.getScene().getWindow());

        if (selectedFile != null) {
            processDocument(selectedFile);
        }
    }

    /**
     * Process uploaded document
     */
    private void processDocument(File file) {
        try {
            // Extract text and create article
            Article article = DocumentService.processDocument(file);

            // Add to knowledge base
            ChatService.getSharedKnowledgeBase().addArticle(article);

            // Auto-index the new article for semantic search
            try {
                System.out.println("Auto-indexing new article: " + article.getTitle());
                ChatService.getSharedKnowledgeBase().indexArticle(article);
                ChatService.getSharedKnowledgeBase().saveEmbeddings("embeddings_store.json");
                System.out.println("Indexing complete. Total embeddings: " +
                        ChatService.getSharedKnowledgeBase().getEmbeddingCount());
            } catch (Exception e) {
                System.err.println("Warning: Failed to index article: " + e.getMessage());
                // Don't fail the upload if indexing fails
            }

            // Add to table
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String tags = String.join(", ", article.getTags());
            String date = dateFormat.format(new Date());
            String fileExt = getFileExtension(file.getName());

            documents.add(new DocumentService.DocumentInfo(
                    article.getTitle(),
                    fileExt.toUpperCase(),
                    tags,
                    date));

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Document uploaded successfully!\n\n" +
                            "Title: " + article.getTitle() + "\n" +
                            "Content length: " + article.getContent().length() + " characters\n\n" +
                            "The document is now part of the knowledge base and will be used in chat responses.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Upload Error",
                    "Failed to process document:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get file extension
     */
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1)
            return "";
        return fileName.substring(lastDot + 1);
    }

    /**
     * Show alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Setup actions column with delete button
     */
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("🗑️ Delete");

            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(event -> {
                    DocumentService.DocumentInfo doc = getTableView().getItems().get(getIndex());
                    handleDeleteDocument(doc);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    /**
     * Handle document deletion with confirmation
     */
    private void handleDeleteDocument(DocumentService.DocumentInfo doc) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Document");
        confirmAlert.setHeaderText("Delete: " + doc.getName());
        confirmAlert.setContentText("Are you sure you want to delete this document? This action cannot be undone.");

        java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            boolean deleted = ChatService.getSharedKnowledgeBase().deleteArticleByTitle(doc.getName());

            if (deleted) {
                documents.remove(doc);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Document \"" + doc.getName() + "\" has been deleted.");
                ActivityLogService.logActivity(
                        UserSession.getInstance().getCurrentUser().getName(),
                        "DELETE_DOCUMENT",
                        "Deleted document: " + doc.getName());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete document.");
            }
        }
    }
}
