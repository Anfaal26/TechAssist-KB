package com.techchat.controller;

import com.techchat.model.User;
import com.techchat.service.UserSession;
import com.techchat.util.FadeInUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainAppController implements Initializable {

    private static MainAppController instance;

    @FXML
    private VBox sidebar;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button homeButton;

    @FXML
    private Button chatButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button libraryButton;

    @FXML
    private Button adminButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    public static MainAppController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Store instance for global access
        instance = this;

        // Play entrance animations
        playEntranceAnimations();

        // Load home view by default
        showHome();

        // Configure menu based on user role
        refreshMenuForRole();
    }

    /**
     * Play entrance animations for main app UI
     */
    private void playEntranceAnimations() {
        // Fade in sidebar and content area
        if (sidebar != null) {
            FadeInUtil.fadeIn(sidebar, 0, 500);
        }
        if (contentArea != null) {
            FadeInUtil.fadeIn(contentArea, 200, 500);
        }
    }

    /**
     * Show/hide menu items based on current user role
     */
    public void refreshMenuForRole() {
        UserSession session = UserSession.getInstance();

        if (!session.isLoggedIn()) {
            return;
        }

        // Admin button is only visible to KBAdmin
        boolean isAdmin = session.isAdmin();
        adminButton.setVisible(isAdmin);
        adminButton.setManaged(isAdmin);

        // Settings is visible to all users (for theme switching)
        settingsButton.setVisible(true);
        settingsButton.setManaged(true);
    }

    @FXML
    private void showHome() {
        loadView("HomeView.fxml");
    }

    @FXML
    public void showChat() {
        loadView("ChatView.fxml");
    }

    @FXML
    public void showSearch() {
        loadView("SearchView.fxml");
    }

    @FXML
    private void showLibrary() {
        loadView("LibraryView.fxml");
    }

    @FXML
    private void showAdmin() {
        loadView("AdminView.fxml");
    }

    @FXML
    private void showSettings() {
        loadView("SettingsView.fxml");
    }

    @FXML
    private void showHistory() {
        loadView("ChatHistoryView.fxml");
    }

    @FXML
    private void handleLogout() {
        // Log logout activity
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            com.techchat.service.ActivityLogService.logLogout(currentUser.getName());
        }

        // Logout user
        UserSession.getInstance().logout();

        // Load welcome view
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/techchat/view/WelcomeView.fxml"));
            Scene scene = new Scene(root);

            // Apply current theme
            com.techchat.util.ThemeManager.applyCurrentTheme(scene);

            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Preserve window dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(scene);
            stage.setTitle("TechAssist Knowledge Base System - Welcome");

            // Restore dimensions if they were set
            if (width > 0 && height > 0) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        } catch (IOException e) {
            System.err.println("Error loading login view");
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/techchat/view/" + fxmlFile));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
