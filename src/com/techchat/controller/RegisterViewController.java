package com.techchat.controller;

import com.techchat.service.UserDataService;
import com.techchat.util.FadeInUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterViewController implements Initializable {

    @FXML
    private VBox registerCard;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Circle circle3;

    @FXML
    private Circle circle4;

    @FXML
    private Circle hex1;

    @FXML
    private Circle hex2;

    @FXML
    private Circle hex3;

    @FXML
    private Line dataLine1;

    @FXML
    private Line dataLine2;

    @FXML
    private Line dataLine3;

    @FXML
    private Line dataLine4;

    @FXML
    private Line dataLine5;

    @FXML
    private Line dataLine6;

    @FXML
    private Line dataLine7;

    @FXML
    private Line dataLine8;

    @FXML
    private Line dataLine9;

    @FXML
    private Line dataLine10;

    @FXML
    private Line dataLine11;

    @FXML
    private Line dataLine12;

    @FXML
    private Line dataLine13;

    @FXML
    private Line dataLine14;

    @FXML
    private Line dataLine15;

    @FXML
    private Line dataLine16;

    @FXML
    private Line dataLine17;

    @FXML
    private Line dataLine18;

    @FXML
    private Line dataLine19;

    @FXML
    private Line dataLine20;

    @FXML
    private Line dataLine21;

    @FXML
    private Line dataLine22;

    @FXML
    private Line dataLine23;

    @FXML
    private Line dataLine24;

    @FXML
    private Line dataLine25;

    @FXML
    private Line dataLine26;

    @FXML
    private Line dataLine27;

    @FXML
    private Line dataLine28;

    @FXML
    private Line dataLine29;

    @FXML
    private Group waveGroup1;

    @FXML
    private Group waveGroup2;

    @FXML
    private Group waveGroup3;

    // Intermediate wave groups - smaller dots for smooth connection
    @FXML
    private Group intermediateWaveGroup1;

    @FXML
    private Group intermediateWaveGroup2;

    @FXML
    private Group intermediateWaveGroup3;

    // Ultra-fine wave groups - tiny dots to complete the pattern
    @FXML
    private Group ultraFineWaveGroup1;

    @FXML
    private Group ultraFineWaveGroup2;

    @FXML
    private Group ultraFineWaveGroup3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        // Start continuous background animations
        animateFloatingCircles();
        animateDataFlow();
        animateWaves();
    }

    /**
     * Play entrance animations for the register card
     */
    private void playEntranceAnimations() {
        // Fade in the register card with slide-up effect
        FadeInUtil.fadeInWithSlideUp(registerCard, 200);
    }

    /**
     * Animate floating circles in background with dramatic tech-savvy movement
     */
    private void animateFloatingCircles() {
        // Circle 1: Dramatic pulsing with movement
        TranslateTransition float1 = new TranslateTransition(Duration.seconds(6), circle1);
        float1.setByX(80);
        float1.setByY(-60);
        float1.setCycleCount(Timeline.INDEFINITE);
        float1.setAutoReverse(true);

        FadeTransition fade1 = new FadeTransition(Duration.seconds(3), circle1);
        fade1.setFromValue(0.08);
        fade1.setToValue(0.15);
        fade1.setCycleCount(Timeline.INDEFINITE);
        fade1.setAutoReverse(true);

        ScaleTransition scale1 = new ScaleTransition(Duration.seconds(5), circle1);
        scale1.setFromX(1.0);
        scale1.setFromY(1.0);
        scale1.setToX(1.3);
        scale1.setToY(1.3);
        scale1.setCycleCount(Timeline.INDEFINITE);
        scale1.setAutoReverse(true);

        ParallelTransition parallel1 = new ParallelTransition(float1, fade1, scale1);
        parallel1.play();

        // Circle 2: Fast diagonal with rotation effect
        TranslateTransition float2 = new TranslateTransition(Duration.seconds(5), circle2);
        float2.setByX(-70);
        float2.setByY(50);
        float2.setCycleCount(Timeline.INDEFINITE);
        float2.setAutoReverse(true);

        FadeTransition fade2 = new FadeTransition(Duration.seconds(2.5), circle2);
        fade2.setFromValue(0.06);
        fade2.setToValue(0.12);
        fade2.setCycleCount(Timeline.INDEFINITE);
        fade2.setAutoReverse(true);

        RotateTransition rotate2 = new RotateTransition(Duration.seconds(8), circle2);
        rotate2.setByAngle(360);
        rotate2.setCycleCount(Timeline.INDEFINITE);

        ParallelTransition parallel2 = new ParallelTransition(float2, fade2, rotate2);
        parallel2.play();

        // Circle 3: Pulsing glow with dramatic scale
        TranslateTransition float3 = new TranslateTransition(Duration.seconds(7), circle3);
        float3.setByX(60);
        float3.setByY(70);
        float3.setCycleCount(Timeline.INDEFINITE);
        float3.setAutoReverse(true);

        FadeTransition fade3 = new FadeTransition(Duration.seconds(2), circle3);
        fade3.setFromValue(0.07);
        fade3.setToValue(0.18);
        fade3.setCycleCount(Timeline.INDEFINITE);
        fade3.setAutoReverse(true);

        ScaleTransition scale3 = new ScaleTransition(Duration.seconds(4), circle3);
        scale3.setFromX(1.0);
        scale3.setFromY(1.0);
        scale3.setToX(1.4);
        scale3.setToY(1.4);
        scale3.setCycleCount(Timeline.INDEFINITE);
        scale3.setAutoReverse(true);

        ParallelTransition parallel3 = new ParallelTransition(float3, fade3, scale3);
        parallel3.play();

        // Circle 4: Fast vertical with intense pulse
        TranslateTransition float4 = new TranslateTransition(Duration.seconds(4), circle4);
        float4.setByY(80);
        float4.setByX(30);
        float4.setCycleCount(Timeline.INDEFINITE);
        float4.setAutoReverse(true);

        FadeTransition fade4 = new FadeTransition(Duration.seconds(1.5), circle4);
        fade4.setFromValue(0.05);
        fade4.setToValue(0.15);
        fade4.setCycleCount(Timeline.INDEFINITE);
        fade4.setAutoReverse(true);

        ParallelTransition parallel4 = new ParallelTransition(float4, fade4);
        parallel4.play();

        // Hexagons - Quick sporadic tech glitches
        TranslateTransition hexFloat1 = new TranslateTransition(Duration.seconds(3), hex1);
        hexFloat1.setByX(100);
        hexFloat1.setByY(-40);
        hexFloat1.setCycleCount(Timeline.INDEFINITE);
        hexFloat1.setAutoReverse(true);

        FadeTransition hexFade1 = new FadeTransition(Duration.seconds(1.5), hex1);
        hexFade1.setFromValue(0.04);
        hexFade1.setToValue(0.10);
        hexFade1.setCycleCount(Timeline.INDEFINITE);
        hexFade1.setAutoReverse(true);

        ScaleTransition hexScale1 = new ScaleTransition(Duration.seconds(2), hex1);
        hexScale1.setFromX(1.0);
        hexScale1.setFromY(1.0);
        hexScale1.setToX(1.6);
        hexScale1.setToY(1.6);
        hexScale1.setCycleCount(Timeline.INDEFINITE);
        hexScale1.setAutoReverse(true);

        ParallelTransition hexParallel1 = new ParallelTransition(hexFloat1, hexFade1, hexScale1);
        hexParallel1.play();

        TranslateTransition hexFloat2 = new TranslateTransition(Duration.seconds(3.5), hex2);
        hexFloat2.setByX(-80);
        hexFloat2.setByY(60);
        hexFloat2.setCycleCount(Timeline.INDEFINITE);
        hexFloat2.setAutoReverse(true);

        FadeTransition hexFade2 = new FadeTransition(Duration.seconds(1.2), hex2);
        hexFade2.setFromValue(0.03);
        hexFade2.setToValue(0.09);
        hexFade2.setCycleCount(Timeline.INDEFINITE);
        hexFade2.setAutoReverse(true);

        RotateTransition hexRotate2 = new RotateTransition(Duration.seconds(5), hex2);
        hexRotate2.setByAngle(-360);
        hexRotate2.setCycleCount(Timeline.INDEFINITE);

        ParallelTransition hexParallel2 = new ParallelTransition(hexFloat2, hexFade2, hexRotate2);
        hexParallel2.play();

        TranslateTransition hexFloat3 = new TranslateTransition(Duration.seconds(4), hex3);
        hexFloat3.setByX(70);
        hexFloat3.setByY(-70);
        hexFloat3.setCycleCount(Timeline.INDEFINITE);
        hexFloat3.setAutoReverse(true);

        FadeTransition hexFade3 = new FadeTransition(Duration.seconds(1.8), hex3);
        hexFade3.setFromValue(0.04);
        hexFade3.setToValue(0.11);
        hexFade3.setCycleCount(Timeline.INDEFINITE);
        hexFade3.setAutoReverse(true);

        ScaleTransition hexScale3 = new ScaleTransition(Duration.seconds(3), hex3);
        hexScale3.setFromX(1.0);
        hexScale3.setFromY(1.0);
        hexScale3.setToX(1.5);
        hexScale3.setToY(1.5);
        hexScale3.setCycleCount(Timeline.INDEFINITE);
        hexScale3.setAutoReverse(true);

        ParallelTransition hexParallel3 = new ParallelTransition(hexFloat3, hexFade3, hexScale3);
        hexParallel3.play();
    }

    /**
     * Animate data flow lines with distance contrast effect
     */
    private void animateDataFlow() {
        // Bundle 1 - Far background (slower, subtle)
        animateDataLine(dataLine1, 5, 18, false);
        animateDataLine(dataLine2, 5.5, 16, false);
        animateDataLine(dataLine3, 5.2, 20, false);

        // Bundle 2 - Mid distance (medium speed)
        animateDataLine(dataLine4, 4, 18, false);
        animateDataLine(dataLine5, 3.8, 16, false);
        animateDataLine(dataLine6, 4.2, 17, false);

        // Bundle 3 - Close foreground (faster, more prominent)
        animateDataLine(dataLine7, 2.8, 18, false);
        animateDataLine(dataLine8, 3, 16, false);
        animateDataLine(dataLine9, 2.9, 18, false);

        // Bundle 4 - Far right background (slower, subtle)
        animateDataLine(dataLine10, 5.3, 19, false);
        animateDataLine(dataLine11, 5.6, 20, false);

        // Bundle 5 - Far left edge (slowest, most subtle)
        animateDataLine(dataLine12, 5.8, 22, false);
        animateDataLine(dataLine13, 6, 20, false);

        // Bundle 6 - Mid-far left (medium-slow)
        animateDataLine(dataLine14, 4.5, 18, false);
        animateDataLine(dataLine15, 4.3, 17, false);

        // Bundle 7 - Near center (faster, prominent)
        animateDataLine(dataLine16, 3.2, 17, false);
        animateDataLine(dataLine17, 3.1, 16, false);

        // Bundle 8 - Far right edge (slowest)
        animateDataLine(dataLine18, 5.9, 21, false);
        animateDataLine(dataLine19, 6.1, 22, false);

        // Bundle 9 - BOLD near-left (thick, faster)
        animateDataLine(dataLine20, 2.5, 18, false);
        animateDataLine(dataLine21, 2.3, 16, false);

        // Bundle 10 - BOLD center (very thick, fastest)
        animateDataLine(dataLine22, 2.0, 16, false);
        animateDataLine(dataLine23, 1.8, 14, false);

        // Bundle 11 - BOLD right (thick, faster)
        animateDataLine(dataLine24, 2.4, 17, false);
        animateDataLine(dataLine25, 2.2, 15, false);

        // Bundle 12 - Medium-left
        animateDataLine(dataLine26, 4.0, 19, false);
        animateDataLine(dataLine27, 3.7, 18, false);

        // Bundle 13 - Medium-right
        animateDataLine(dataLine28, 3.9, 18, false);
        animateDataLine(dataLine29, 3.6, 17, false);
    }

    /**
     * Animate individual data line with dash offset animation
     */
    private void animateDataLine(Line line, double duration, double dashOffset, boolean horizontal) {
        // Create pulsing fade effect
        FadeTransition fade = new FadeTransition(Duration.seconds(duration / 2), line);
        fade.setFromValue(line.getOpacity());
        fade.setToValue(line.getOpacity() * 1.5);
        fade.setCycleCount(Timeline.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();

        // Create flowing effect using stroke dash offset animation
        Timeline flow = new Timeline();
        KeyValue keyValue = new KeyValue(line.strokeDashOffsetProperty(), dashOffset);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(duration), keyValue);
        flow.getKeyFrames().add(keyFrame);
        flow.setCycleCount(Timeline.INDEFINITE);
        flow.play();

        // Add slight movement to enhance effect
        if (horizontal) {
            TranslateTransition move = new TranslateTransition(Duration.seconds(duration * 2), line);
            move.setByY(10);
            move.setCycleCount(Timeline.INDEFINITE);
            move.setAutoReverse(true);
            move.play();
        } else {
            TranslateTransition move = new TranslateTransition(Duration.seconds(duration * 2), line);
            move.setByX(10);
            move.setCycleCount(Timeline.INDEFINITE);
            move.setAutoReverse(true);
            move.play();
        }
    }

    /**
     * Animate wave groups to create flowing wave effect
     */
    private void animateWaves() {
        // Wave 1 - slow vertical flow
        TranslateTransition wave1 = new TranslateTransition(Duration.seconds(8), waveGroup1);
        wave1.setByY(40);
        wave1.setCycleCount(Timeline.INDEFINITE);
        wave1.setAutoReverse(true);

        FadeTransition fade1 = new FadeTransition(Duration.seconds(4), waveGroup1);
        fade1.setFromValue(1.0);
        fade1.setToValue(0.4);
        fade1.setCycleCount(Timeline.INDEFINITE);
        fade1.setAutoReverse(true);

        ParallelTransition parallel1 = new ParallelTransition(wave1, fade1);
        parallel1.play();

        // Wave 2 - medium speed with opposite direction
        TranslateTransition wave2 = new TranslateTransition(Duration.seconds(6), waveGroup2);
        wave2.setByY(-35);
        wave2.setCycleCount(Timeline.INDEFINITE);
        wave2.setAutoReverse(true);

        FadeTransition fade2 = new FadeTransition(Duration.seconds(3), waveGroup2);
        fade2.setFromValue(1.0);
        fade2.setToValue(0.3);
        fade2.setCycleCount(Timeline.INDEFINITE);
        fade2.setAutoReverse(true);

        ParallelTransition parallel2 = new ParallelTransition(wave2, fade2);
        parallel2.play();

        // Wave 3 - fast flow
        TranslateTransition wave3 = new TranslateTransition(Duration.seconds(7), waveGroup3);
        wave3.setByY(50);
        wave3.setCycleCount(Timeline.INDEFINITE);
        wave3.setAutoReverse(true);

        FadeTransition fade3 = new FadeTransition(Duration.seconds(3.5), waveGroup3);
        fade3.setFromValue(1.0);
        fade3.setToValue(0.5);
        fade3.setCycleCount(Timeline.INDEFINITE);
        fade3.setAutoReverse(true);

        ParallelTransition parallel3 = new ParallelTransition(wave3, fade3);
        parallel3.play();

        // Intermediate Wave 1 - between wave 1 and 2
        TranslateTransition intWave1 = new TranslateTransition(Duration.seconds(7), intermediateWaveGroup1);
        intWave1.setByY(30);
        intWave1.setCycleCount(Timeline.INDEFINITE);
        intWave1.setAutoReverse(true);

        FadeTransition intFade1 = new FadeTransition(Duration.seconds(3.5), intermediateWaveGroup1);
        intFade1.setFromValue(1.0);
        intFade1.setToValue(0.35);
        intFade1.setCycleCount(Timeline.INDEFINITE);
        intFade1.setAutoReverse(true);

        ParallelTransition intParallel1 = new ParallelTransition(intWave1, intFade1);
        intParallel1.play();

        // Intermediate Wave 2 - between wave 2 and 3
        TranslateTransition intWave2 = new TranslateTransition(Duration.seconds(6.5), intermediateWaveGroup2);
        intWave2.setByY(-40);
        intWave2.setCycleCount(Timeline.INDEFINITE);
        intWave2.setAutoReverse(true);

        FadeTransition intFade2 = new FadeTransition(Duration.seconds(3.2), intermediateWaveGroup2);
        intFade2.setFromValue(1.0);
        intFade2.setToValue(0.4);
        intFade2.setCycleCount(Timeline.INDEFINITE);
        intFade2.setAutoReverse(true);

        ParallelTransition intParallel2 = new ParallelTransition(intWave2, intFade2);
        intParallel2.play();

        // Intermediate Wave 3 - extra smooth connection
        TranslateTransition intWave3 = new TranslateTransition(Duration.seconds(7.5), intermediateWaveGroup3);
        intWave3.setByY(35);
        intWave3.setCycleCount(Timeline.INDEFINITE);
        intWave3.setAutoReverse(true);

        FadeTransition intFade3 = new FadeTransition(Duration.seconds(3.8), intermediateWaveGroup3);
        intFade3.setFromValue(1.0);
        intFade3.setToValue(0.45);
        intFade3.setCycleCount(Timeline.INDEFINITE);
        intFade3.setAutoReverse(true);

        ParallelTransition intParallel3 = new ParallelTransition(intWave3, intFade3);
        intParallel3.play();

        // Ultra-fine Wave 1 - tiny filling dots
        TranslateTransition ultraWave1 = new TranslateTransition(Duration.seconds(7.2), ultraFineWaveGroup1);
        ultraWave1.setByY(25);
        ultraWave1.setCycleCount(Timeline.INDEFINITE);
        ultraWave1.setAutoReverse(true);

        FadeTransition ultraFade1 = new FadeTransition(Duration.seconds(3.6), ultraFineWaveGroup1);
        ultraFade1.setFromValue(1.0);
        ultraFade1.setToValue(0.3);
        ultraFade1.setCycleCount(Timeline.INDEFINITE);
        ultraFade1.setAutoReverse(true);

        ParallelTransition ultraParallel1 = new ParallelTransition(ultraWave1, ultraFade1);
        ultraParallel1.play();

        // Ultra-fine Wave 2 - micro filling dots
        TranslateTransition ultraWave2 = new TranslateTransition(Duration.seconds(6.8), ultraFineWaveGroup2);
        ultraWave2.setByY(-30);
        ultraWave2.setCycleCount(Timeline.INDEFINITE);
        ultraWave2.setAutoReverse(true);

        FadeTransition ultraFade2 = new FadeTransition(Duration.seconds(3.4), ultraFineWaveGroup2);
        ultraFade2.setFromValue(1.0);
        ultraFade2.setToValue(0.35);
        ultraFade2.setCycleCount(Timeline.INDEFINITE);
        ultraFade2.setAutoReverse(true);

        ParallelTransition ultraParallel2 = new ParallelTransition(ultraWave2, ultraFade2);
        ultraParallel2.play();

        // Ultra-fine Wave 3 - final smoothing layer
        TranslateTransition ultraWave3 = new TranslateTransition(Duration.seconds(7.8), ultraFineWaveGroup3);
        ultraWave3.setByY(38);
        ultraWave3.setCycleCount(Timeline.INDEFINITE);
        ultraWave3.setAutoReverse(true);

        FadeTransition ultraFade3 = new FadeTransition(Duration.seconds(3.9), ultraFineWaveGroup3);
        ultraFade3.setFromValue(1.0);
        ultraFade3.setToValue(0.4);
        ultraFade3.setCycleCount(Timeline.INDEFINITE);
        ultraFade3.setAutoReverse(true);

        ParallelTransition ultraParallel3 = new ParallelTransition(ultraWave3, ultraFade3);
        ultraParallel3.play();
    }

    @FXML
    private void handleRegister() {
        // Clear previous messages
        errorLabel.setText("");
        successLabel.setText("");

        // Get input values
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password are required");
            return;
        }

        if (username.length() < 3) {
            errorLabel.setText("Username must be at least 3 characters");
            return;
        }

        if (password.length() < 6) {
            errorLabel.setText("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        // Register user (EndUser only, use username as name, empty email)
        boolean success = UserDataService.registerUser(
                username,
                password,
                username, // Use username as name
                "", // Empty email
                "EndUser" // Always EndUser
        );

        if (success) {
            // Log registration
            com.techchat.service.ActivityLogService.logRegistration(username);

            successLabel.setText("Registration successful! Redirecting to login...");
            // Clear form
            clearForm();

            // Redirect to login after a delay
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> handleBackToLogin());
                }
            }, 2000); // 2 seconds delay
        } else {
            errorLabel.setText("Username already exists. Please choose another.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/techchat/view/LoginView.fxml"));
            Scene scene = new Scene(root);

            // Don't apply theme - login page has custom design

            Stage stage = (Stage) usernameField.getScene().getWindow();

            // Preserve window dimensions
            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(scene);
            stage.setTitle("TechAssist Knowledge Base System - Login");

            // Restore dimensions if they were set
            if (width > 0 && height > 0) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        } catch (IOException e) {
            errorLabel.setText("Error loading login page");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
