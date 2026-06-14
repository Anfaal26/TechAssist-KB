package com.techchat.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for application and API settings
 */
public class AppConfig {
    private static AppConfig instance;
    private Properties properties;

    private static final String CONFIG_FILE = "src/config.properties";

    private AppConfig() {
        properties = new Properties();
        loadConfig();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    private void loadConfig() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            System.out.println("Configuration loaded successfully");
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
            // Use default values
            setDefaults();
        }
    }

    private void setDefaults() {
        // API Settings
        properties.setProperty("openai.api.key", "");
        properties.setProperty("openai.model", "gpt-3.5-turbo");
        properties.setProperty("openai.max.tokens", "500");
        properties.setProperty("openai.temperature", "0.7");

        // App Settings
        properties.setProperty("app.name", "TechAssist Knowledge Base System");
        properties.setProperty("app.theme", "light");
        properties.setProperty("app.language", "en");
    }

    // ========== API Settings ==========

    public String getOpenAIKey() {
        return properties.getProperty("openai.api.key", "");
    }

    public String getModel() {
        return properties.getProperty("openai.model", "gpt-3.5-turbo");
    }

    public int getMaxTokens() {
        return Integer.parseInt(properties.getProperty("openai.max.tokens", "500"));
    }

    public double getTemperature() {
        return Double.parseDouble(properties.getProperty("openai.temperature", "0.7"));
    }

    public boolean isConfigured() {
        String key = getOpenAIKey();
        return key != null && !key.isEmpty() && !key.equals("YOUR_API_KEY_HERE");
    }

    // ========== App Settings ==========

    public String getAppName() {
        return properties.getProperty("app.name", "TechAssist Knowledge Base System");
    }

    public String getTheme() {
        return properties.getProperty("app.theme", "light");
    }

    public String getLanguage() {
        return properties.getProperty("app.language", "en");
    }

    /**
     * Save all settings to config.properties file
     */
    public boolean saveSettings(String apiKey, String model, int maxTokens, double temperature,
            String appName, String theme, String language) {
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            // API settings
            properties.setProperty("openai.api.key", apiKey);
            properties.setProperty("openai.model", model);
            properties.setProperty("openai.max.tokens", String.valueOf(maxTokens));
            properties.setProperty("openai.temperature", String.valueOf(temperature));

            // App settings
            properties.setProperty("app.name", appName);
            properties.setProperty("app.theme", theme);
            properties.setProperty("app.language", language);

            properties.store(output, "Application Configuration");
            System.out.println("Settings saved successfully");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reload configuration from file
     */
    public void reload() {
        loadConfig();
    }
}
