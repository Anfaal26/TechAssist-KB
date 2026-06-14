package com.techchat.controller;

import com.techchat.model.User;
import com.techchat.service.UserSession;
import com.techchat.util.ThemeManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginViewController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox loginCard;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    // Möbius Wave Groups - 3 main waves
    @FXML
    private Group mobiusWave1;

    @FXML
    private Group mobiusWave2;

    @FXML
    private Group mobiusWave3;

    // Intermediate waves - medium dots for enhanced connection
    @FXML
    private Group intermediateWave1;

    @FXML
    private Group intermediateWave2;

    @FXML
    private Group intermediateWave3;

    // Connecting waves - smaller dots for smooth flow
    @FXML
    private Group connectWave1;

    @FXML
    private Group connectWave2;

    @FXML
    private Group connectWave3;

    // Ultra-fine waves - tiny dots to complete the Möbius pattern
    @FXML
    private Group ultraFineWave1;

    @FXML
    private Group ultraFineWave2;

    @FXML
    private Group ultraFineWave3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize user data file
        com.techchat.service.UserDataService.initializeUserData();

        // Add enter key listener to password field
        passwordField.setOnAction(event -> handleLogin());

        // Start animations
        playEntranceAnimations();
        animateMobiusWaves();
    }

    /**
     * Play entrance animations for the login card
     */
    private void playEntranceAnimations() {
        // Initially hide the card
        loginCard.setOpacity(0);
        loginCard.setTranslateY(30);

        // Fade in and slide up
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), loginCard);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideUp = new TranslateTransition(Duration.millis(800), loginCard);
        slideUp.setFromY(30);
        slideUp.setToY(0);

        ParallelTransition entrance = new ParallelTransition(fadeIn, slideUp);
        entrance.setDelay(Duration.millis(200));
        entrance.play();
    }

    /**
     * Animate Möbius waves - flowing 3D wave effect with main and connecting waves
     */
    private void animateMobiusWaves() {
        // Main waves - larger dots (90 each)
        animateWaveGroup(mobiusWave1, 0, 8.0); // Blue - 0°
        animateWaveGroup(mobiusWave2, Math.PI * 2 / 3, 10.0); // Dark blue - 120°
        animateWaveGroup(mobiusWave3, Math.PI * 4 / 3, 12.0); // Cyan - 240°

        // Intermediate waves - medium dots for enhanced connection (100 each)
        animateWaveGroup(intermediateWave1, Math.PI / 6, 8.5); // Between wave 1 & 2 - 30°
        animateWaveGroup(intermediateWave2, Math.PI * 5 / 6, 10.5); // Between wave 2 & 3 - 150°
        animateWaveGroup(intermediateWave3, Math.PI * 3 / 2, 12.5); // Between wave 3 & 1 - 270°

        // Connecting waves - smaller dots to fill gaps (60-80 each)
        animateWaveGroup(connectWave1, Math.PI / 3, 9.0); // Between wave 1 & 2 - 60°
        animateWaveGroup(connectWave2, Math.PI, 11.0); // Between wave 2 & 3 - 180°
        animateWaveGroup(connectWave3, Math.PI * 5 / 3, 13.0); // Between wave 3 & 1 - 300°

        // Ultra-fine waves - tiny dots for complete filling (60 each)
        animateWaveGroup(ultraFineWave1, Math.PI / 9, 8.7); // Ultra-fine 1 - 20°
        animateWaveGroup(ultraFineWave2, Math.PI * 7 / 9, 10.3); // Ultra-fine 2 - 140°
        animateWaveGroup(ultraFineWave3, Math.PI * 13 / 9, 12.7); // Ultra-fine 3 - 260°
    }

    /**
     * Animate a single wave group in Möbius pattern
     */
    private void animateWaveGroup(Group waveGroup, double phaseShift, double duration) {
        final int dotCount = waveGroup.getChildren().size();
        final double screenWidth = 1000; // Approximate screen width

        // Create continuous wave animation
        Timeline waveAnimation = new Timeline();
        waveAnimation.setCycleCount(Timeline.INDEFINITE);

        // Update wave positions at 60fps
        waveAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            double time = System.currentTimeMillis() / 1000.0; // Current time in seconds

            for (int i = 0; i < dotCount; i++) {
                Circle dot = (Circle) waveGroup.getChildren().get(i);

                // Calculate position along wave
                double t = (double) i / dotCount; // 0 to 1
                double x = (t * screenWidth) - (screenWidth / 2); // -500 to +500

                // Create Möbius wave pattern
                double freq1 = 2.0; // First frequency
                double freq2 = 3.0; // Second frequency for complexity

                // Y position: combination of sine waves with phase
                double y = Math.sin((t * Math.PI * freq1) + (time / duration) + phaseShift) * 80
                        + Math.sin((t * Math.PI * freq2) - (time / duration * 0.7) + phaseShift) * 40;

                // Z-depth effect (simulate 3D by varying opacity and size)
                double z = Math.cos((t * Math.PI * 2) + (time / duration) + phaseShift);
                double scale = 0.6 + (z + 1) * 0.4; // Scale from 0.6 to 1.4
                double baseOpacity = dot.getFill().toString().contains("79c0ff") ? 0.4
                        : (dot.getFill().toString().contains("1f6feb") ? 0.5 : 0.6);
                double opacity = baseOpacity * scale;

                // Apply transformations
                dot.setTranslateX(x);
                dot.setTranslateY(y);
                dot.setOpacity(opacity);
                dot.setScaleX(scale);
                dot.setScaleY(scale);
            }
        }));

        waveAnimation.play();
    }

    /**
     * Handle field hover effect
     */
    @FXML
    private void handleFieldHover(javafx.scene.input.MouseEvent event) {
        Control field = (Control) event.getSource();
        field.setStyle(field.getStyle() + "; -fx-background-color: #161b22;");
    }

    /**
     * Handle field exit effect
     */
    @FXML
    private void handleFieldExit(javafx.scene.input.MouseEvent event) {
        Control field = (Control) event.getSource();
        field.setStyle(
                field.getStyle().replace("; -fx-background-color: #161b22;", "; -fx-background-color: #0d1117;"));
    }

    /**
     * Handle button hover effect
     */
    @FXML
    private void handleButtonHover(javafx.scene.input.MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
        scale.setToX(1.05);
        scale.setToY(1.05);
        scale.play();
    }

    /**
     * Handle button exit effect
     */
    @FXML
    private void handleButtonExit(javafx.scene.input.MouseEvent event) {
        Button button = (Button) event.getSource();
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Clear previous error
        errorLabel.setText("");

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        // Authenticate from CSV file
        User user = com.techchat.service.UserDataService.authenticateUser(username, password);

        if (user != null) {
            // Success animation
            animateSuccess();

            // Login successful
            UserSession.getInstance().login(user);
            com.techchat.service.ActivityLogService.logLogin(username, true);

            // Load main application with delay for animation
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(600), e -> loadMainApp(user)));
            timeline.play();
        } else {
            // Login failed
            com.techchat.service.ActivityLogService.logLogin(username, false);
            showError("Invalid username or password");
            shakeAnimation(loginCard);
        }
    }

    /**
     * Show error with fade animation
     */
    private void showError(String message) {
        errorLabel.setText(message);
        FadeTransition fade = new FadeTransition(Duration.millis(300), errorLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Shake animation for error
     */
    private void shakeAnimation(VBox node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(70), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    /**
     * Success animation
     */
    private void animateSuccess() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), loginButton);
        scale.setToX(0.95);
        scale.setToY(0.95);
        scale.play();
    }

    /**
     * Load main application
     */
    private void loadMainApp(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/techchat/view/MainApp.fxml"));
            Parent root = loader.load();

            MainAppController mainController = loader.getController();
            mainController.refreshMenuForRole();

            Scene scene = new Scene(root);
            ThemeManager.applyCurrentTheme(scene);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(scene);
            stage.setTitle("TechAssist Knowledge Base System - " + user.getName());

            if (width > 0 && height > 0) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        } catch (IOException e) {
            errorLabel.setText("Error loading application");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/techchat/view/RegisterView.fxml"));
            Scene scene = new Scene(root);
            // Don't apply theme - registration page has custom design

            Stage stage = (Stage) usernameField.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(scene);
            stage.setTitle("TechAssist Knowledge Base System - Register");

            if (width > 0 && height > 0) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        } catch (IOException e) {
            errorLabel.setText("Error loading registration page");
            e.printStackTrace();
        }
    }
}
