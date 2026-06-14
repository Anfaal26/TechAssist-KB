package com.techchat.util;

import com.techchat.config.AppConfig;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class to manage language/localization switching
 */
public class LanguageManager {

    private static ResourceBundle currentBundle;
    private static Locale currentLocale;

    private static final String BUNDLE_BASE_NAME = "com.techchat.resources.labels";

    /**
     * Initialize the language manager with the locale from config
     */
    public static void initialize() {
        AppConfig config = AppConfig.getInstance();
        String languageCode = config.getLanguage();
        setLanguage(languageCode);
    }

    /**
     * Set the current language
     * 
     * @param languageCode Language code: "en", "zh", "ms"
     */
    public static void setLanguage(String languageCode) {
        try {
            currentLocale = new Locale(languageCode);
            currentBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, currentLocale);
            System.out.println("Language set to: " + languageCode);
        } catch (Exception e) {
            System.err.println("Error loading language bundle for: " + languageCode);
            System.err.println("Application will continue without localization.");
            // Set defaults to avoid null pointer exceptions
            currentLocale = new Locale("en");
            currentBundle = null; // Will use keys as fallback in getString()
        }
    }

    /**
     * Get a localized string by key
     * 
     * @param key The resource key
     * @return Localized string
     */
    public static String getString(String key) {
        try {
            if (currentBundle == null) {
                initialize();
            }
            return currentBundle.getString(key);
        } catch (Exception e) {
            System.err.println("Missing translation key: " + key);
            return key; // Return the key itself if translation is missing
        }
    }

    /**
     * Get the current resource bundle
     * 
     * @return Current ResourceBundle
     */
    public static ResourceBundle getResourceBundle() {
        if (currentBundle == null) {
            initialize();
        }
        return currentBundle;
    }

    /**
     * Get the current locale
     * 
     * @return Current Locale
     */
    public static Locale getCurrentLocale() {
        if (currentLocale == null) {
            initialize();
        }
        return currentLocale;
    }

    /**
     * Get display name for a language code
     * 
     * @param languageCode Language code
     * @return Display name
     */
    public static String getLanguageDisplayName(String languageCode) {
        switch (languageCode.toLowerCase()) {
            case "en":
                return "English";
            case "zh":
                return "Chinese (中文)";
            case "ms":
                return "Malay (Bahasa Melayu)";
            default:
                return languageCode;
        }
    }
}
