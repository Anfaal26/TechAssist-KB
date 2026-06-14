package com.techchat.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for API settings
 */
public class APIConfig {
    private static APIConfig instance;
    private Properties properties;

    private static final String CONFIG_FILE = "src/config.properties";

    private APIConfig() {
        properties = new Properties();
        loadConfig();
    }

    public static APIConfig getInstance() {
        if (instance == null) {
            instance = new APIConfig();
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
        properties.setProperty("openai.api.key", "");
        properties.setProperty("openai.model", "gpt-3.5-turbo");
        properties.setProperty("openai.max.tokens", "500");
        properties.setProperty("openai.temperature", "0.7");
    }

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

    /**
     * Save settings to config.properties file
     */
    public boolean saveSettings(String apiKey, String model, int maxTokens, double temperature) {
        try (java.io.FileOutputStream output = new java.io.FileOutputStream(CONFIG_FILE)) {
            properties.setProperty("openai.api.key", apiKey);
            properties.setProperty("openai.model", model);
            properties.setProperty("openai.max.tokens", String.valueOf(maxTokens));
            properties.setProperty("openai.temperature", String.valueOf(temperature));

            properties.store(output, "OpenAI Configuration");
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
