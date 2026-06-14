package com.techchat.controller;

import com.techchat.model.Article;
import com.techchat.service.ChatService;
import com.techchat.util.FadeInUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SearchViewController implements Initializable {

    @FXML
    private HBox searchBar;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Article> resultsList;

    @FXML
    private Label resultCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        // Initialize with empty results
        resultsList.setItems(FXCollections.observableArrayList());

        // Custom cell factory to display article details
        resultsList.setCellFactory(listView -> new ArticleListCell());

        // Enable search on Enter key
        searchField.setOnAction(e -> handleSearch());

        // Add double-click handler to view full article
        resultsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                Article selected = resultsList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showArticleDialog(selected);
                }
            }
        });
    }

    /**
     * Play entrance animations for search view elements
     */
    private void playEntranceAnimations() {
        // Fade in search elements with staggered timing
        if (searchBar != null) {
            FadeInUtil.fadeIn(searchBar, 0, 400);
        }
        if (resultsList != null) {
            FadeInUtil.fadeIn(resultsList, 100, 400);
        }
    }

    /**
     * Show a dialog with the full article content
     */
    private void showArticleDialog(Article article) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Article Viewer");
        alert.setHeaderText(article.getTitle());

        // Create scrollable content area
        TextArea contentArea = new TextArea(article.getContent());
        contentArea.setWrapText(true);
        contentArea.setEditable(false);
        contentArea.setPrefRowCount(20);
        contentArea.setPrefColumnCount(60);

        // Create container with tags
        VBox content = new VBox(10);
        content.getChildren().add(contentArea);

        if (!article.getTags().isEmpty()) {
            Label tagsLabel = new Label("Tags: " + String.join(", ", article.getTags()));
            tagsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #666;");
            content.getChildren().add(tagsLabel);
        }

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefWidth(700);
        alert.getDialogPane().setPrefHeight(500);
        alert.showAndWait();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query != null && !query.trim().isEmpty()) {
            // Search knowledge base
            List<Article> results = ChatService.getSharedKnowledgeBase().searchArticles(query);

            // Update results list
            resultsList.setItems(FXCollections.observableArrayList(results));

            // Update result count
            int count = results.size();
            if (count == 0) {
                resultCount.setText("No results found");
            } else if (count == 1) {
                resultCount.setText("1 result found");
            } else {
                resultCount.setText(count + " results found");
            }
        } else {
            // Clear results if query is empty
            resultsList.setItems(FXCollections.observableArrayList());
            resultCount.setText("0 results found");
        }
    }

    /**
     * Custom ListCell to display article information nicely
     */
    private static class ArticleListCell extends ListCell<Article> {
        @Override
        protected void updateItem(Article article, boolean empty) {
            super.updateItem(article, empty);

            if (empty || article == null) {
                setGraphic(null);
                setText(null);
            } else {
                VBox container = new VBox(5);
                container.setPadding(new Insets(10));
                container.getStyleClass().add("search-result-card");

                // Title
                Label title = new Label(article.getTitle());
                title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                title.getStyleClass().add("search-result-title");

                // Content preview (first 150 characters)
                String contentPreview = article.getContent();
                if (contentPreview.length() > 150) {
                    contentPreview = contentPreview.substring(0, 150) + "...";
                }
                Label content = new Label(contentPreview);
                content.setWrapText(true);
                content.getStyleClass().add("search-result-content");

                // Hint to double-click
                Label hint = new Label("(Double-click to view full content)");
                hint.setStyle("-fx-font-size: 10px; -fx-font-style: italic;");
                hint.getStyleClass().add("search-result-hint");

                // Tags
                if (!article.getTags().isEmpty()) {
                    Label tags = new Label("Tags: " + String.join(", ", article.getTags()));
                    tags.setStyle("-fx-font-size: 11px;");
                    tags.getStyleClass().add("search-result-tags");
                    container.getChildren().addAll(title, content, tags, hint);
                } else {
                    container.getChildren().addAll(title, content, hint);
                }

                setGraphic(container);
            }
        }
    }
}
