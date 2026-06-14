package com.techchat.util;

import com.techchat.config.AppConfig;
import javafx.scene.Scene;

/**
 * Utility class to manage theme switching
 */
public class ThemeManager {

    private static final String LIGHT_THEME_PATH = "/com/techchat/styles/light-theme.css";
    private static final String DARK_THEME_PATH = "/com/techchat/styles/dark-theme.css";

    /**
     * Apply theme to a scene based on theme name
     * 
     * @param scene     The scene to apply theme to
     * @param themeName Theme name: "light" or "dark"
     */
    public static void applyTheme(Scene scene, String themeName) {
        if (scene == null) {
            System.err.println("Cannot apply theme: scene is null");
            return;
        }

        // Clear existing stylesheets
        scene.getStylesheets().clear();

        // Apply theme based on name
        String themeResource;
        if ("dark".equalsIgnoreCase(themeName)) {
            themeResource = DARK_THEME_PATH;
        } else {
            // Default to light theme
            themeResource = LIGHT_THEME_PATH;
        }

        try {
            String themeUrl = ThemeManager.class.getResource(themeResource).toExternalForm();
            scene.getStylesheets().add(themeUrl);
            System.out.println("Applied " + themeName + " theme successfully");
        } catch (Exception e) {
            System.err.println("Error loading theme: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Apply the current theme from config to a scene
     * 
     * @param scene The scene to apply theme to
     */
    public static void applyCurrentTheme(Scene scene) {
        AppConfig config = AppConfig.getInstance();
        String currentTheme = config.getTheme();
        applyTheme(scene, currentTheme);
    }

    /**
     * Get the currently active theme name from config
     * 
     * @return Current theme name
     */
    public static String getCurrentTheme() {
        return AppConfig.getInstance().getTheme();
    }
}
