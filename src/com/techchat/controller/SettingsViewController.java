package com.techchat.controller;

import com.techchat.config.AppConfig;
import com.techchat.util.FadeInUtil;
import com.techchat.util.ThemeManager;
import com.techchat.util.LanguageManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import com.techchat.service.UserSession;

public class SettingsViewController implements Initializable {

    // General Settings
    @FXML
    private TextField appNameField;

    @FXML
    private ComboBox<String> themeComboBox;

    @FXML
    private ComboBox<String> languageComboBox;

    // AI Model Settings
    @FXML
    private PasswordField apiKeyField;

    @FXML
    private ComboBox<String> modelComboBox;

    @FXML
    private Slider temperatureSlider;

    @FXML
    private Spinner<Integer> maxTokensSpinner;

    // RAG Search Settings
    @FXML
    private Slider similarityThresholdSlider;

    @FXML
    private Label thresholdValueLabel;

    @FXML
    private Label statusLabel;

    // General Settings Section
    @FXML
    private VBox generalSettingsSection;

    // AI Settings Section (to hide for end users)
    @FXML
    private VBox aiSettingsSection;

    private AppConfig config;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        config = AppConfig.getInstance();

        // Initialize Theme combo box
        themeComboBox.setItems(FXCollections.observableArrayList("Light", "Dark"));

        // Initialize Language combo box
        languageComboBox.setItems(FXCollections.observableArrayList(
                "English",
                "Chinese (中文)",
                "Malay (Bahasa Melayu)"));

        // Initialize model combo box with available models
        modelComboBox.setItems(FXCollections.observableArrayList(
                "gpt-3.5-turbo",
                "gpt-4",
                "gpt-4-turbo",
                "gpt-4o",
                "gpt-4o-mini"));

        // Initialize max tokens spinner
        maxTokensSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4000, 500));

        // Initialize similarity threshold slider
        similarityThresholdSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            thresholdValueLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });

        // Check user role and hide AI settings for end users
        UserSession session = UserSession.getInstance();
        boolean isAdmin = session.isAdmin();
        aiSettingsSection.setVisible(isAdmin);
        aiSettingsSection.setManaged(isAdmin);

        // Load current settings
        loadSettings();
    }

    /**
     * Play entrance animations for settings view elements
     */
    private void playEntranceAnimations() {
        // Fade in settings sections with staggered timing
        if (generalSettingsSection != null) {
            FadeInUtil.fadeIn(generalSettingsSection, 0, 400);
        }
        if (aiSettingsSection != null && aiSettingsSection.isVisible()) {
            FadeInUtil.fadeIn(aiSettingsSection, 100, 400);
        }
    }

    /**
     * Load current settings from config
     */
    private void loadSettings() {
        // General Settings
        appNameField.setText(config.getAppName());

        String theme = config.getTheme();
        themeComboBox.setValue(theme.equals("dark") ? "Dark" : "Light");

        String lang = config.getLanguage();
        languageComboBox.setValue(getLanguageDisplayName(lang));

        // AI Settings
        apiKeyField.setText(config.getOpenAIKey());
        modelComboBox.setValue(config.getModel());
        temperatureSlider.setValue(config.getTemperature());
        maxTokensSpinner.getValueFactory().setValue(config.getMaxTokens());

        // RAG Settings - load from ChatService's RAG config
        try {
            double threshold = com.techchat.service.ChatService.getSharedKnowledgeBase().ragConfig
                    .getMinSimilarityThreshold();
            similarityThresholdSlider.setValue(threshold);
            thresholdValueLabel.setText(String.format("%.2f", threshold));
        } catch (Exception e) {
            // Use default if error
            similarityThresholdSlider.setValue(0.5);
            thresholdValueLabel.setText("0.50");
        }
    }

    /**
     * Handle save settings button click
     */
    @FXML
    private void handleSaveSettings() {
        // Get General Settings
        String appName = appNameField.getText().trim();
        String themeSelection = themeComboBox.getValue();
        String languageSelection = languageComboBox.getValue();

        // Get AI Settings
        String apiKey = apiKeyField.getText().trim();
        String model = modelComboBox.getValue();
        double temperature = temperatureSlider.getValue();
        int maxTokens = maxTokensSpinner.getValue();

        // Validation
        if (appName.isEmpty()) {
            showStatus("Please enter an application name", false);
            return;
        }

        if (themeSelection == null || themeSelection.isEmpty()) {
            showStatus("Please select a theme", false);
            return;
        }

        if (languageSelection == null || languageSelection.isEmpty()) {
            showStatus("Please select a language", false);
            return;
        }

        if (apiKey.isEmpty()) {
            showStatus("Please enter an API key", false);
            return;
        }

        if (model == null || model.isEmpty()) {
            showStatus("Please select a model", false);
            return;
        }

        // Convert selections to config format
        String theme = themeSelection.equals("Dark") ? "dark" : "light";
        String language = getLanguageCode(languageSelection);

        // Get similarity threshold
        double similarityThreshold = similarityThresholdSlider.getValue();

        // Save all settings
        boolean success = config.saveSettings(apiKey, model, maxTokens, temperature,
                appName, theme, language);

        if (success) {
            // Reload config
            config.reload();

            // Apply theme immediately
            try {
                ThemeManager.applyTheme(appNameField.getScene(), theme);
            } catch (Exception e) {
                System.err.println("Error applying theme: " + e.getMessage());
            }

            // Set language for next app start
            // Note: Full language switching requires app restart for all views
            LanguageManager.setLanguage(language);

            // Apply similarity threshold to RAG config
            try {
                com.techchat.service.ChatService.getSharedKnowledgeBase().ragConfig
                        .setMinSimilarityThreshold(similarityThreshold);
            } catch (Exception e) {
                System.err.println("Error setting similarity threshold: " + e.getMessage());
            }

            showStatus("✓ Settings saved successfully! Similarity threshold updated to " +
                    String.format("%.2f", similarityThreshold), true);
        } else {
            showStatus("Error saving settings. Check console for details.", false);
        }
    }

    /**
     * Show status message
     */
    private void showStatus(String message, boolean success) {
        statusLabel.setText(message);
        statusLabel.setStyle(success ? "-fx-text-fill: green; -fx-font-weight: bold;"
                : "-fx-text-fill: red; -fx-font-weight: bold;");

        // Clear message after 5 seconds
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> statusLabel.setText(""));
            }
        }, 5000);
    }

    /**
     * Convert language display name to language code
     */
    private String getLanguageCode(String displayName) {
        if (displayName.startsWith("Chinese")) {
            return "zh";
        } else if (displayName.startsWith("Malay")) {
            return "ms";
        } else {
            return "en";
        }
    }

    /**
     * Convert language code to display name
     */
    private String getLanguageDisplayName(String code) {
        switch (code.toLowerCase()) {
            case "zh":
                return "Chinese (中文)";
            case "ms":
                return "Malay (Bahasa Melayu)";
            default:
                return "English";
        }
    }
}
