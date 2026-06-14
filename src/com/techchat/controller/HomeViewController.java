package com.techchat.controller;

import com.techchat.model.Article;
import com.techchat.service.ActivityLogService;
import com.techchat.service.AnalyticsService;
import com.techchat.service.ChatService;
import com.techchat.service.UserDataService;
import com.techchat.service.UserSession;
import com.techchat.util.FadeInUtil;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomeViewController implements Initializable {

    @FXML
    private VBox statsContainer;

    @FXML
    private HBox actionCardsContainer;

    @FXML
    private ListView<String> recentActivityList;

    @FXML
    private Label totalDocumentsLabel;

    @FXML
    private Label activeUsersLabel;

    @FXML
    private Label totalQueriesLabel;

    @FXML
    private Label avgResponseTimeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        // Load statistics
        loadStatistics();

        // Load content based on user role
        String role = UserSession.getInstance().getRole();

        if ("Admin".equals(role)) {
            loadAdminActivityLog();
        } else {
            loadRecentDocuments();
        }
    }

    /**
     * Play entrance animations for home view elements
     */
    private void playEntranceAnimations() {
        // Fade in stats, action cards, and activity list with staggered timing
        if (statsContainer != null) {
            FadeInUtil.fadeIn(statsContainer, 0, 400);
        }
        if (actionCardsContainer != null) {
            FadeInUtil.fadeIn(actionCardsContainer, 100, 400);
        }
        if (recentActivityList != null) {
            FadeInUtil.fadeIn(recentActivityList, 200, 400);
        }
    }

    /**
     * Load real statistics from services
     */
    private void loadStatistics() {
        // Total documents from knowledge base
        int totalDocs = ChatService.getSharedKnowledgeBase().getAllArticles().size();
        totalDocumentsLabel.setText(String.valueOf(totalDocs));

        // Active users (total registered users)
        int totalUsers = UserDataService.getAllUsers().size();
        activeUsersLabel.setText(String.valueOf(totalUsers));

        // Total queries from analytics service
        int totalQueries = AnalyticsService.getInstance().getTotalQueryCount();
        totalQueriesLabel.setText(String.valueOf(totalQueries));

        // Average response time from AI call logs
        long avgResponseMs = AnalyticsService.getInstance().getAverageResponseTime();
        if (avgResponseMs > 0) {
            // Convert to seconds with 1 decimal place
            double avgResponseSec = avgResponseMs / 1000.0;
            avgResponseTimeLabel.setText(String.format("%.1fs", avgResponseSec));
        } else {
            avgResponseTimeLabel.setText("--");
        }
    }

    /**
     * Load activity log for admin users
     */
    private void loadAdminActivityLog() {
        List<ActivityLogService.ActivityLog> logs = ActivityLogService.getRecentLogs(15);

        // Reverse to show newest first (same as Admin Dashboard)
        Collections.reverse(logs);

        ObservableList<String> activities = FXCollections.observableArrayList();

        for (ActivityLogService.ActivityLog log : logs) {
            // log.getTimestamp() already returns a formatted String
            String formatted = String.format("%s | %s | %s",
                    log.getTimestamp(),
                    log.getUsername(),
                    log.getAction());
            activities.add(formatted);
        }

        recentActivityList.setItems(activities);
    }

    /**
     * Load recent documents for normal users
     */
    private void loadRecentDocuments() {
        List<Article> allArticles = ChatService.getSharedKnowledgeBase().getAllArticles();
        ObservableList<String> documents = FXCollections.observableArrayList();

        // Sort by article ID (which contains timestamp) to get most recent first
        List<Article> sortedArticles = new ArrayList<>(allArticles);
        Collections.sort(sortedArticles, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                // Newer articles first (reverse order)
                return a2.getArticleId().compareTo(a1.getArticleId());
            }
        });

        // Show last 10 documents
        int count = Math.min(10, sortedArticles.size());
        if (count == 0) {
            documents.add("No documents available yet.");
            documents.add("Upload documents in the Document Library to get started!");
        } else {
            for (int i = 0; i < count; i++) {
                Article article = sortedArticles.get(i);
                String formatted = String.format("📄 New document: %s", article.getTitle());
                documents.add(formatted);
            }
        }

        recentActivityList.setItems(documents);
    }

    @FXML
    private void goToChat() {
        MainAppController controller = MainAppController.getInstance();
        if (controller != null) {
            controller.showChat();
        } else {
            System.out.println("Navigate to Chat - Use Chat Assistant button in sidebar");
        }
    }

    @FXML
    private void goToSearch() {
        MainAppController controller = MainAppController.getInstance();
        if (controller != null) {
            controller.showSearch();
        } else {
            System.out.println("Navigate to Search - Use Search button in sidebar");
        }
    }
}
