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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeViewController implements Initializable {

    @FXML
    private Group mobiusWave1;
    @FXML
    private Group mobiusWave2;
    @FXML
    private Group mobiusWave3;
    @FXML
    private Group redOppositeWave;
    @FXML
    private Group intermediateWave1;
    @FXML
    private Group intermediateWave2;
    @FXML
    private Group intermediateWave3;
    @FXML
    private Group connectWave1;
    @FXML
    private Group connectWave2;
    @FXML
    private Group connectWave3;
    @FXML
    private Group ultraFineWave1;
    @FXML
    private Group ultraFineWave2;
    @FXML
    private Group ultraFineWave3;
    @FXML
    private Group extraMicroWave1;
    @FXML
    private Group extraMicroWave2;
    @FXML
    private Group extraMicroWave3;
    @FXML
    private Group extraMicroWave4;
    @FXML
    private Group extraMicroWave5;
    @FXML
    private Group extraMicroWave6;
    @FXML
    private Group extraMicroWave7;
    @FXML
    private Group extraMicroWave8;
    @FXML
    private Group extraMicroWave9;
    @FXML
    private Group extraMicroWave10;
    @FXML
    private Group extraMicroWave11;
    @FXML
    private Group extraMicroWave12;
    @FXML
    private Group extraMicroWave13;
    @FXML
    private Group extraMicroWave14;
    @FXML
    private Group extraMicroWave15;
    @FXML
    private Group upperParticles;
    @FXML
    private Group lowerParticles;
    @FXML
    private Circle orbitalParticle;
    @FXML
    private Circle chaserParticle1;
    @FXML
    private Circle chaserParticle2;
    @FXML
    private Circle chaserParticle3;
    @FXML
    private Circle cometParticle;
    @FXML
    private Circle cometTail1;
    @FXML
    private Circle cometTail2;
    @FXML
    private Circle cometTail3;
    @FXML
    private Circle cometTail4;
    @FXML
    private Circle cometTail5;
    @FXML
    private StackPane backgroundPane;

    // UI elements for animations
    @FXML
    private Label titleLabel;
    @FXML
    private Label sloganLabel;

    // Modal elements
    @FXML
    private StackPane modalOverlay;
    @FXML
    private VBox loginModal;
    @FXML
    private VBox registerModal;

    // Login form fields
    @FXML
    private TextField loginUsernameField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Label loginErrorLabel;
    @FXML
    private Button loginButton;

    // Register form fields
    @FXML
    private TextField registerUsernameField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private PasswordField registerConfirmPasswordField;
    @FXML
    private Label registerErrorLabel;
    @FXML
    private Label registerSuccessLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize user data file
        com.techchat.service.UserDataService.initializeUserData();

        // Add enter key listeners for password fields
        loginPasswordField.setOnAction(event -> handleLoginSubmit());
        registerConfirmPasswordField.setOnAction(event -> handleRegisterSubmit());

        // Start animations
        playEntranceAnimations();
        animateMobiusWaves();
        // generateStaticParticles(); // Removed - static purple particles disabled
        animateOrbitalParticle();
        animateCometParticle();
        animateTitleHeartbeat(); // Add heartbeat animation
    }

    /**
     * Play modern entrance animations for UI elements
     */
    private void playEntranceAnimations() {
        // Start everything hidden
        titleLabel.setOpacity(0);
        titleLabel.setTranslateY(30);

        // Hide background elements initially
        if (backgroundPane != null)
            backgroundPane.setOpacity(0);
        if (orbitalParticle != null)
            orbitalParticle.setOpacity(0);
        if (chaserParticle1 != null)
            chaserParticle1.setOpacity(0);
        if (chaserParticle2 != null)
            chaserParticle2.setOpacity(0);
        if (chaserParticle3 != null)
            chaserParticle3.setOpacity(0);
        if (cometParticle != null)
            cometParticle.setOpacity(0);
        // Static particles disabled
        // if (upperParticles != null)
        // upperParticles.setOpacity(0);
        // if (lowerParticles != null)
        // lowerParticles.setOpacity(0);

        // 1. Fade in background pane with waves (duration 1200ms, start immediately)
        if (backgroundPane != null) {
            FadeTransition bgFade = new FadeTransition(Duration.millis(1200), backgroundPane);
            bgFade.setFromValue(0);
            bgFade.setToValue(1);
            bgFade.play();
        }

        // 2. Fade in static particles (duration 1500ms, delayed 300ms) - DISABLED
        // if (upperParticles != null) {
        // FadeTransition upperFade = new FadeTransition(Duration.millis(1500),
        // upperParticles);
        // upperFade.setFromValue(0);
        // upperFade.setToValue(1);
        // upperFade.setDelay(Duration.millis(300));
        // upperFade.play();
        // }

        // if (lowerParticles != null) {
        // FadeTransition lowerFade = new FadeTransition(Duration.millis(1500),
        // lowerParticles);
        // lowerFade.setFromValue(0);
        // lowerFade.setToValue(1);
        // lowerFade.setDelay(Duration.millis(300));
        // lowerFade.play();
        // }

        // 3. Fade in orbital particles (duration 1000ms, delayed 600ms)
        if (orbitalParticle != null) {
            FadeTransition orbitalFade = new FadeTransition(Duration.millis(1000), orbitalParticle);
            orbitalFade.setFromValue(0);
            orbitalFade.setToValue(0.8); // Original opacity
            orbitalFade.setDelay(Duration.millis(600));
            orbitalFade.play();
        }

        if (chaserParticle1 != null) {
            FadeTransition chaser1Fade = new FadeTransition(Duration.millis(1000), chaserParticle1);
            chaser1Fade.setFromValue(0);
            chaser1Fade.setToValue(0.7);
            chaser1Fade.setDelay(Duration.millis(700));
            chaser1Fade.play();
        }

        if (chaserParticle2 != null) {
            FadeTransition chaser2Fade = new FadeTransition(Duration.millis(1000), chaserParticle2);
            chaser2Fade.setFromValue(0);
            chaser2Fade.setToValue(0.7);
            chaser2Fade.setDelay(Duration.millis(800));
            chaser2Fade.play();
        }

        if (chaserParticle3 != null) {
            FadeTransition chaser3Fade = new FadeTransition(Duration.millis(1000), chaserParticle3);
            chaser3Fade.setFromValue(0);
            chaser3Fade.setToValue(0.7);
            chaser3Fade.setDelay(Duration.millis(900));
            chaser3Fade.play();
        }

        if (cometParticle != null) {
            FadeTransition cometFade = new FadeTransition(Duration.millis(1200), cometParticle);
            cometFade.setFromValue(0);
            cometFade.setToValue(0.8);
            cometFade.setDelay(Duration.millis(1000));
            cometFade.play();
        }

        // 4. Animate title - fade in and slide up (delayed 400ms)
        FadeTransition titleFade = new FadeTransition(Duration.millis(1000), titleLabel);
        titleFade.setFromValue(0);
        titleFade.setToValue(1);

        TranslateTransition titleSlide = new TranslateTransition(Duration.millis(1000), titleLabel);
        titleSlide.setFromY(30);
        titleSlide.setToY(0);

        // Scale effect for dramatic entrance
        ScaleTransition titleScale = new ScaleTransition(Duration.millis(1000), titleLabel);
        titleScale.setFromX(0.9);
        titleScale.setFromY(0.9);
        titleScale.setToX(1.0);
        titleScale.setToY(1.0);

        ParallelTransition titleEntrance = new ParallelTransition(titleFade, titleSlide, titleScale);
        titleEntrance.setDelay(Duration.millis(400));
        titleEntrance.play();

        // 5. Animate login/register links with stagger (delayed 900ms)
        javafx.scene.layout.HBox linksBox = (javafx.scene.layout.HBox) titleLabel.getParent().getChildrenUnmodifiable()
                .stream()
                .filter(node -> node instanceof javafx.scene.layout.HBox)
                .findFirst()
                .orElse(null);

        if (linksBox != null) {
            linksBox.setOpacity(0);
            linksBox.setTranslateY(20);

            FadeTransition linksFade = new FadeTransition(Duration.millis(800), linksBox);
            linksFade.setFromValue(0);
            linksFade.setToValue(1);

            TranslateTransition linksSlide = new TranslateTransition(Duration.millis(800), linksBox);
            linksSlide.setFromY(20);
            linksSlide.setToY(0);

            ParallelTransition linksEntrance = new ParallelTransition(linksFade, linksSlide);
            linksEntrance.setDelay(Duration.millis(900));
            linksEntrance.play();
        }
    }

    /**
     * Animate Möbius waves - flowing 3D wave effect
     */
    private void animateMobiusWaves() {
        // Main waves - larger dots (250 each)
        animateWaveGroup(mobiusWave1, 0, 8.0); // Blue - 0°
        animateWaveGroup(mobiusWave2, Math.PI * 2 / 3, 10.0); // Dark blue - 120°
        animateWaveGroup(mobiusWave3, Math.PI * 4 / 3, 12.0); // Cyan - 240°

        // Red opposite wave - 4x speed with opposite direction (250 dots)
        animateOppositeWaveGroup(redOppositeWave, Math.PI / 2, 2.5); // Red - 90°, 4x faster

        // Intermediate waves - medium dots (100 each)
        animateWaveGroup(intermediateWave1, Math.PI / 6, 9.0); // 30°
        animateWaveGroup(intermediateWave2, Math.PI * 5 / 6, 11.0); // 150°
        animateWaveGroup(intermediateWave3, Math.PI * 3 / 2, 13.0); // 270°

        // Connecting waves - smaller dots (80-60 each)
        animateWaveGroup(connectWave1, Math.PI / 3, 9.5); // 60°
        animateWaveGroup(connectWave2, Math.PI, 11.5); // 180°
        animateWaveGroup(connectWave3, Math.PI * 5 / 3, 13.5); // 300°

        // Ultra-fine waves - tiny dots (60 each)
        animateWaveGroup(ultraFineWave1, Math.PI / 4, 10.5); // 45°
        animateWaveGroup(ultraFineWave2, Math.PI * 7 / 6, 12.5); // 210°
        animateWaveGroup(ultraFineWave3, Math.PI * 11 / 6, 14.5); // 330°

        // Extra micro waves - additional filling for ultra-dense effect (50 each)
        animateWaveGroup(extraMicroWave1, Math.PI / 5, 11.2); // 36°
        animateWaveGroup(extraMicroWave2, Math.PI * 4 / 5, 13.2); // 144°
        animateWaveGroup(extraMicroWave3, Math.PI * 8 / 5, 15.2); // 288°

        // Additional extra micro waves - maximum density (40-45 each)
        animateWaveGroup(extraMicroWave4, Math.PI / 10, 11.8); // 18°
        animateWaveGroup(extraMicroWave5, Math.PI * 3 / 10, 12.8); // 54°
        animateWaveGroup(extraMicroWave6, Math.PI * 7 / 10, 13.8); // 126°
        animateWaveGroup(extraMicroWave7, Math.PI * 9 / 10, 14.8); // 162°
        animateWaveGroup(extraMicroWave8, Math.PI * 11 / 10, 11.5); // 198°
        animateWaveGroup(extraMicroWave9, Math.PI * 13 / 10, 12.5); // 234°
        animateWaveGroup(extraMicroWave10, Math.PI * 15 / 10, 13.5); // 270°
        animateWaveGroup(extraMicroWave11, Math.PI * 17 / 10, 14.2); // 306°
        animateWaveGroup(extraMicroWave12, Math.PI * 19 / 10, 15.5); // 342°
        animateWaveGroup(extraMicroWave13, Math.PI / 12, 11.9); // 15°
        animateWaveGroup(extraMicroWave14, Math.PI * 5 / 12, 13.3); // 75°
        animateWaveGroup(extraMicroWave15, Math.PI * 7 / 12, 14.7); // 105°
    }

    /**
     * Animate a wave group with Möbius strip effect
     */
    private void animateWaveGroup(Group waveGroup, double phaseShift, double duration) {
        final int dotCount = waveGroup.getChildren().size();
        final double screenWidth = 1000;

        Timeline waveAnimation = new Timeline();
        waveAnimation.setCycleCount(Timeline.INDEFINITE);

        waveAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            double time = System.currentTimeMillis() / 1000.0;

            for (int i = 0; i < dotCount; i++) {
                Circle dot = (Circle) waveGroup.getChildren().get(i);

                double t = (double) i / dotCount;
                double x = (t * screenWidth) - (screenWidth / 2);

                double freq1 = 2.0;
                double freq2 = 3.0;

                double y = Math.sin((t * Math.PI * freq1) + (time / duration) + phaseShift) * 110.4
                        + Math.sin((t * Math.PI * freq2) - (time / duration * 0.7) + phaseShift) * 55.2;

                double z = Math.cos((t * Math.PI * 2) + (time / duration) + phaseShift);
                double scale = 0.6 + (z + 1) * 0.4;
                double baseOpacity = dot.getFill().toString().contains("79c0ff") ? 0.4
                        : (dot.getFill().toString().contains("1f6feb") ? 0.5 : 0.6);
                double opacity = baseOpacity * scale;

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
     * Animate a wave group with Möbius strip effect - OPPOSITE DIRECTION
     * Uses NEGATIVE time multiplier to reverse oscillation direction
     */
    private void animateOppositeWaveGroup(Group waveGroup, double phaseShift, double duration) {
        if (waveGroup == null)
            return;

        final int dotCount = waveGroup.getChildren().size();
        final double screenWidth = 1000;

        Timeline waveAnimation = new Timeline();
        waveAnimation.setCycleCount(Timeline.INDEFINITE);

        waveAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            double time = System.currentTimeMillis() / 1000.0;

            for (int i = 0; i < dotCount; i++) {
                Circle dot = (Circle) waveGroup.getChildren().get(i);

                double t = (double) i / dotCount;
                double x = (t * screenWidth) - (screenWidth / 2);

                double freq1 = 2.0;
                double freq2 = 3.0;

                // OPPOSITE DIRECTION: Use NEGATIVE time multiplier
                // This makes the wave oscillate in the opposite direction
                double y = Math.sin((t * Math.PI * freq1) - (time / duration) + phaseShift) * 110.4
                        + Math.sin((t * Math.PI * freq2) + (time / duration * 0.7) + phaseShift) * 55.2;

                double z = Math.cos((t * Math.PI * 2) - (time / duration) + phaseShift);
                double scale = 0.6 + (z + 1) * 0.4;

                // Red wave has higher base opacity for visibility
                double baseOpacity = 0.65;
                double opacity = baseOpacity * scale;

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
     * Generate random static particles in upper and lower regions
     * Positioned OUTSIDE the Möbius wave vertical oscillation zone
     */
    private void generateStaticParticles() {
        final int particleCount = 200; // Number of particles for each region
        final double screenWidth = 1000;
        final double screenHeight = 600;

        // Wave oscillates ±96px from center (20% increase from original 80)
        // Upper region: from top to above wave zone (y from -300 to -120)
        final double upperRegionStart = -screenHeight / 2; // -300 (top edge)
        final double upperRegionEnd = -120; // Well above wave oscillation

        // Lower region: from below wave zone to bottom (y from +120 to +300)
        final double lowerRegionStart = 120; // Well below wave oscillation
        final double lowerRegionEnd = screenHeight / 2; // +300 (bottom edge)

        java.util.Random random = new java.util.Random();

        // Generate upper region particles
        for (int i = 0; i < particleCount; i++) {
            Circle particle = new Circle();

            // Random size (0.8 to 2.2)
            double size = 0.8 + random.nextDouble() * 1.4;
            particle.setRadius(size);

            // Random position in upper region (full width, upper height)
            double x = (random.nextDouble() * screenWidth) - (screenWidth / 2);
            double y = upperRegionStart + (random.nextDouble() * (upperRegionEnd - upperRegionStart));

            particle.setTranslateX(x);
            particle.setTranslateY(y);

            // Random opacity (0.15 to 0.5) - subtle starfield
            double opacity = 0.15 + random.nextDouble() * 0.35;
            particle.setOpacity(opacity);

            // Varied violet/purple colors
            String[] colors = { "#a78bfa", "#8b5cf6", "#c084fc", "#9333ea", "#d8b4fe", "#e9d5ff" };
            particle.setFill(javafx.scene.paint.Color.web(colors[random.nextInt(colors.length)]));

            upperParticles.getChildren().add(particle);
        }

        // Generate lower region particles
        for (int i = 0; i < particleCount; i++) {
            Circle particle = new Circle();

            // Random size (0.8 to 2.2)
            double size = 0.8 + random.nextDouble() * 1.4;
            particle.setRadius(size);

            // Random position in lower region (full width, lower height)
            double x = (random.nextDouble() * screenWidth) - (screenWidth / 2);
            double y = lowerRegionStart + (random.nextDouble() * (lowerRegionEnd - lowerRegionStart));

            particle.setTranslateX(x);
            particle.setTranslateY(y);

            // Random opacity (0.15 to 0.5) - subtle starfield
            double opacity = 0.15 + random.nextDouble() * 0.35;
            particle.setOpacity(opacity);

            // Varied violet/purple colors
            String[] colors = { "#a78bfa", "#8b5cf6", "#c084fc", "#9333ea", "#d8b4fe", "#e9d5ff" };
            particle.setFill(javafx.scene.paint.Color.web(colors[random.nextInt(colors.length)]));

            lowerParticles.getChildren().add(particle);
        }
    }

    /**
     * Animate orbital particle in 3D elliptical orbit with chasers
     * Features variable speed that decelerates at vertical extremes (like a
     * pendulum)
     */
    private void animateOrbitalParticle() {
        final double orbitDuration = 8000; // 8 seconds for full orbit
        final double radiusX = 154; // Horizontal orbit radius (30% smaller)
        final double radiusY = 266; // Vertical orbit radius (30% smaller)
        final double centerX = 0; // Center of screen
        final double centerY = 0; // Center of screen
        final double tiltAngle = Math.PI * 40 / 180; // 40 degree tilt from vertical for 3D effect

        // Phase offsets for chaser particles - TRAIN FORMATION
        // Particles follow closely behind each other (not spread around orbit)
        // Small phase differences create train effect, but each still decelerates at
        // vertical extremes
        final double trainSpacing = Math.PI / 12; // Approximately 15° spacing between particles
        final double[] phaseOffsets = {
                0, // Lead particle
                trainSpacing, // 1st follower (15° behind)
                trainSpacing * 2, // 2nd follower (30° behind)
                trainSpacing * 3 // 3rd follower (45° behind)
        };
        final Circle[] particles = { orbitalParticle, chaserParticle1, chaserParticle2, chaserParticle3 };

        Timeline orbitAnimation = new Timeline();
        orbitAnimation.setCycleCount(Timeline.INDEFINITE);

        orbitAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            double time = System.currentTimeMillis() % orbitDuration;
            double baseTime = (time / orbitDuration) * 2 * Math.PI; // 0 to 2π

            // Animate each particle with its phase offset
            // CRITICAL: Apply easing INDIVIDUALLY to each particle based on its own
            // vertical position
            for (int i = 0; i < particles.length; i++) {
                Circle particle = particles[i];

                // Add phase offset to base time to get this particle's raw progress
                double particlePhase = baseTime + phaseOffsets[i];

                // Apply pendulum-like easing INDIVIDUALLY for this particle
                // This makes THIS particle slow down when IT reaches vertical extremes
                double easingStrength = 0.75; // High value = dramatic bunching (0 = none, 1 = extreme)

                // Transform using sine - this redistributes the angle progression
                // Particles spend more time at vertical extremes (slow) and less in middle
                // (fast)
                double easedProgress = particlePhase +
                        easingStrength * Math.sin(2 * particlePhase);

                // Now use the eased progress for position calculation
                double progress = easedProgress;

                // Calculate position on ellipse
                double x = centerX + radiusX * Math.cos(progress);
                double y = centerY + radiusY * Math.sin(progress);

                // Apply tilt for 3D effect
                double y3D = y * Math.cos(tiltAngle);
                double z = y * Math.sin(tiltAngle); // Depth

                // Calculate depth-based scale and opacity (0 = far, 1 = near)
                double depthFactor = (z + radiusY * Math.sin(tiltAngle)) / (2 * radiusY * Math.sin(tiltAngle));
                double scale = 0.2 + (depthFactor * 2.0); // Scale from 0.2 to 2.2 - VERY DRAMATIC depth effect
                double opacity = 0.2 + (depthFactor * 0.8); // Opacity from 0.2 to 1.0

                // Update particle position and appearance
                particle.setTranslateX(x);
                particle.setTranslateY(y3D);
                particle.setScaleX(scale);
                particle.setScaleY(scale);
                particle.setOpacity(opacity);
            }
        }));

        orbitAnimation.play();
    }

    /**
     * Animate red comet particle with randomized path and trailing tail
     * Features:
     * - Randomized spiral motion for unpredictable path
     * - OPPOSITE oscillating direction to Möbius wave (negative time)
     * - Dynamic fading effect based on spiral phase
     * - Trailing tail particles that follow behind
     */
    private void animateCometParticle() {
        if (cometParticle == null)
            return;

        final double cometDuration = 10000; // 10 seconds for slower, more dramatic cycle
        final double spiralRadiusMin = 80; // Moderate minimum for balanced spiral
        final double spiralRadiusMax = 350; // Moderate maximum for balanced effect
        final double centerX = 0; // Center of screen
        final double centerY = 0; // Center of screen

        // Store previous positions for tail following
        final java.util.LinkedList<double[]> positionHistory = new java.util.LinkedList<>();
        final Circle[] tailParticles = { cometTail1, cometTail2, cometTail3, cometTail4, cometTail5 };

        // Random number generator for path variations
        final java.util.Random random = new java.util.Random();

        Timeline cometAnimation = new Timeline();
        cometAnimation.setCycleCount(Timeline.INDEFINITE);

        cometAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            double time = System.currentTimeMillis() % cometDuration;
            double progress = (time / cometDuration) * 2 * Math.PI; // 0 to 2π

            // OPPOSITE OSCILLATION: Use NEGATIVE time multiplier
            double oppositeProgress = -progress;

            // Spiral effect - radius expands and contracts for dramatic spiral orbit
            double spiralPhase = Math.sin(progress * 2); // Oscillates between -1 and 1
            double radius = spiralRadiusMin + ((spiralPhase + 1) / 2) * (spiralRadiusMax - spiralRadiusMin);

            // Random variations DISABLED for clean spiral
            // orbit
            // radius += (random.nextDouble() - 0.5) * 30;

            // Calculate position with random wobble
            double x = centerX + radius * Math.cos(oppositeProgress);
            double y = centerY + radius * Math.sin(oppositeProgress);

            // Add random perturbations for organic movement - DISABLED for clean orbit
            // x += (random.nextDouble() - 0.5) * 20;
            // y += (random.nextDouble() - 0.5) * 20;

            // Add secondary wave motion - DISABLED for clean circular orbit
            // y += Math.sin(progress * 3) * (35 + random.nextDouble() * 10);

            // Dynamic fading synchronized with spiral - brighter when expanded
            double opacity = 0.5 + ((spiralPhase + 1) / 2) * 0.4; // 0.5 to 0.9

            // Update comet head position
            cometParticle.setTranslateX(x);
            cometParticle.setTranslateY(y);
            cometParticle.setOpacity(opacity);

            // Scale synchronized with spiral - MUCH MORE DRAMATIC size change
            double scale = 0.3 + ((spiralPhase + 1) / 2) * 2.2; // 0.3 to 2.5 - very dramatic!
            cometParticle.setScaleX(scale);
            cometParticle.setScaleY(scale);

            // Tail effect DISABLED

            /*
             * 
             * // Store current position in history for tail
             * 
             * positionHistory.addFirst(new double[] { x, y, opacity, scale });
             * 
             * if (positionHistory.size() > 50) { // Keep last 50 positions
             * 
             * positionHistory.removeLast();
             * 
             * }
             * 
             */

            /*
             * Tail particles DISABLED
             * for (int i = 0; i < tailParticles.length; i++) {
             * if (tailParticles[i] != null) {
             * // Each tail particle follows at a delayed position with more spacing
             * int historyIndex = (i + 1) * 5; // Increased spacing between tail particles
             * 
             * if (historyIndex < positionHistory.size()) {
             * double[] pos = positionHistory.get(historyIndex);
             * tailParticles[i].setTranslateX(pos[0]);
             * tailParticles[i].setTranslateY(pos[1]);
             * 
             * // Fade out progressively
             * double tailOpacity = pos[2] * (1.0 - (i * 0.15)); // Each tail dimmer
             * tailParticles[i].setOpacity(tailOpacity);
             * 
             * // Scale down progressively
             * double tailScale = pos[3] * (1.0 - (i * 0.1));
             * tailParticles[i].setScaleX(tailScale);
             * tailParticles[i].setScaleY(tailScale); }
             * 
             * }
             * 
             */
        }));

        cometAnimation.play();
    }

    /**
     * Animate the title's and slogan's glow with a slow, gentle pulsing effect
     * Creates a soft breathing animation like a twinkling star
     */
    private void animateTitleHeartbeat() {
        // Animate title
        if (titleLabel != null && titleLabel.getEffect() != null) {
            javafx.scene.effect.Blend titleBlend = (javafx.scene.effect.Blend) titleLabel.getEffect();
            javafx.scene.effect.DropShadow titleGlow = (javafx.scene.effect.DropShadow) titleBlend.getTopInput();
            animateGlow(titleGlow);
        }

        // Animate slogan with same effect
        if (sloganLabel != null && sloganLabel.getEffect() != null) {
            javafx.scene.effect.Blend sloganBlend = (javafx.scene.effect.Blend) sloganLabel.getEffect();
            javafx.scene.effect.DropShadow sloganGlow = (javafx.scene.effect.DropShadow) sloganBlend.getTopInput();
            animateGlow(sloganGlow);
        }
    }

    /**
     * Helper method to animate a DropShadow effect with the star-like breathing
     * pattern
     */
    private void animateGlow(javafx.scene.effect.DropShadow glowEffect) {
        // Star-like glow parameters - subtle and slow
        final double baseRadius = 15.0; // Base glow radius
        final double peakRadius = 25.0; // Peak glow radius (gentler than before)
        final double baseSpread = 0.4; // Base spread
        final double peakSpread = 0.55; // Peak spread (subtle)

        // Create a Timeline for slow, gentle star-like pulsing
        // Simple breathing pattern: slow expansion, slow contraction
        Timeline heartbeat = new Timeline(
                // Resting state
                new KeyFrame(Duration.millis(0),
                        new KeyValue(glowEffect.radiusProperty(), baseRadius, Interpolator.EASE_BOTH),
                        new KeyValue(glowEffect.spreadProperty(), baseSpread, Interpolator.EASE_BOTH)),

                // Slow expansion to peak (like a star brightening)
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(glowEffect.radiusProperty(), peakRadius, Interpolator.EASE_BOTH),
                        new KeyValue(glowEffect.spreadProperty(), peakSpread, Interpolator.EASE_BOTH)),

                // Slow contraction back to resting (like a star dimming)
                new KeyFrame(Duration.millis(4000),
                        new KeyValue(glowEffect.radiusProperty(), baseRadius, Interpolator.EASE_BOTH),
                        new KeyValue(glowEffect.spreadProperty(), baseSpread, Interpolator.EASE_BOTH)));

        heartbeat.setCycleCount(Timeline.INDEFINITE);
        heartbeat.play();
    }

    @FXML
    private void handleLogin(javafx.scene.input.MouseEvent event) {
        // Show login modal instead of navigating
        showLoginModal();
    }

    @FXML
    private void handleRegister(javafx.scene.input.MouseEvent event) {
        // Show register modal instead of navigating
        showRegisterModal();
    }

    private void showLoginModal() {
        modalOverlay.setVisible(true);
        loginModal.setVisible(true);
        registerModal.setVisible(false);
        loginUsernameField.clear();
        loginPasswordField.clear();
        loginErrorLabel.setText("");
        loginUsernameField.requestFocus();

        // Add fade-in and scale animation
        loginModal.setOpacity(0);
        loginModal.setScaleX(0.9);
        loginModal.setScaleY(0.9);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), loginModal);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), loginModal);
        scaleUp.setFromX(0.9);
        scaleUp.setFromY(0.9);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);

        ParallelTransition entrance = new ParallelTransition(fadeIn, scaleUp);
        entrance.play();
    }

    private void showRegisterModal() {
        modalOverlay.setVisible(true);
        registerModal.setVisible(true);
        loginModal.setVisible(false);
        registerUsernameField.clear();
        registerPasswordField.clear();
        registerConfirmPasswordField.clear();
        registerErrorLabel.setText("");
        registerSuccessLabel.setText("");
        registerUsernameField.requestFocus();

        // Add fade-in and scale animation
        registerModal.setOpacity(0);
        registerModal.setScaleX(0.9);
        registerModal.setScaleY(0.9);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), registerModal);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), registerModal);
        scaleUp.setFromX(0.9);
        scaleUp.setFromY(0.9);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);

        ParallelTransition entrance = new ParallelTransition(fadeIn, scaleUp);
        entrance.play();
    }

    @FXML
    private void closeModals() {
        modalOverlay.setVisible(false);
        loginModal.setVisible(false);
        registerModal.setVisible(false);
    }

    @FXML
    private void switchToLogin() {
        showLoginModal();
    }

    @FXML
    private void switchToRegister() {
        showRegisterModal();
    }

    @FXML
    private void handleLoginSubmit() {
        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText();
        loginErrorLabel.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            showLoginError("Please enter both username and password");
            return;
        }

        User user = com.techchat.service.UserDataService.authenticateUser(username, password);
        if (user != null) {
            UserSession.getInstance().login(user);
            com.techchat.service.ActivityLogService.logLogin(username, true);
            closeModals();
            loadMainApp(user);
        } else {
            com.techchat.service.ActivityLogService.logLogin(username, false);
            showLoginError("Invalid username or password");
            shakeAnimation(loginModal);
        }
    }

    @FXML
    private void handleRegisterSubmit() {
        String username = registerUsernameField.getText().trim();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmPasswordField.getText();
        registerErrorLabel.setText("");
        registerSuccessLabel.setText("");

        if (username.isEmpty() || username.length() < 3) {
            showRegisterError("Username must be at least 3 characters");
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            showRegisterError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showRegisterError("Passwords do not match");
            return;
        }

        boolean success = com.techchat.service.UserDataService.registerUser(username, password, username, "", "user");
        if (success) {
            showRegisterSuccess("Account created successfully! Please login.");
            registerUsernameField.clear();
            registerPasswordField.clear();
            registerConfirmPasswordField.clear();
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), e -> showLoginModal()));
            timeline.play();
        } else {
            showRegisterError("Registration failed. Username may already exist.");
        }
    }

    private void showLoginError(String message) {
        loginErrorLabel.setText(message);
        FadeTransition fade = new FadeTransition(Duration.millis(300), loginErrorLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void showRegisterError(String message) {
        registerErrorLabel.setText(message);
        FadeTransition fade = new FadeTransition(Duration.millis(300), registerErrorLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void showRegisterSuccess(String message) {
        registerSuccessLabel.setText(message);
        FadeTransition fade = new FadeTransition(Duration.millis(300), registerSuccessLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void shakeAnimation(VBox node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(70), node);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void loadMainApp(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/techchat/view/MainApp.fxml"));
            Parent root = loader.load();
            MainAppController mainController = loader.getController();
            mainController.refreshMenuForRole();
            Scene scene = new Scene(root);
            ThemeManager.applyCurrentTheme(scene);
            Stage stage = (Stage) mobiusWave1.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(scene);
            stage.setTitle("TechAssist Knowledge Base System - " + user.getName());
            if (width > 0 && height > 0) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        } catch (IOException e) {
            loginErrorLabel.setText("Error loading application");
            e.printStackTrace();
        }
    }

    private void navigateToPage(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);

            Stage stage = (Stage) mobiusWave1.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(scene);
            stage.setTitle(title);

            if (width > 0 && height > 0) {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLinkHover(javafx.scene.input.MouseEvent event) {
        javafx.scene.control.Label label = (javafx.scene.control.Label) event.getSource();
        label.setStyle(label.getStyle() + "-fx-text-fill: #58a6ff;");
    }

    @FXML
    private void handleLinkExit(javafx.scene.input.MouseEvent event) {
        javafx.scene.control.Label label = (javafx.scene.control.Label) event.getSource();
        String style = label.getStyle().replace("-fx-text-fill: #58a6ff;", "-fx-text-fill: #ffffff;");
        label.setStyle(style);
    }
}
