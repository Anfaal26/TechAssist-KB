package com.techchat.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service to log and retrieve user activities
 */
public class ActivityLogService {
    private static final String LOG_FILE = "src/activity_log.csv";
    private static final String HEADER = "timestamp,username,action,details";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Initialize log file if it doesn't exist
     */
    public static void initializeLogFile() {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(HEADER);
                System.out.println("Created activity_log.csv");
            } catch (IOException e) {
                System.err.println("Error creating activity log file: " + e.getMessage());
            }
        }
    }

    /**
     * Log an activity
     */
    public static void logActivity(String username, String action, String details) {
        initializeLogFile();

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = dateFormat.format(new Date());
            writer.println(String.format("%s,%s,%s,%s", timestamp, username, action, details));
        } catch (IOException e) {
            System.err.println("Error logging activity: " + e.getMessage());
        }
    }

    /**
     * Log a login activity
     */
    public static void logLogin(String username, boolean success) {
        String action = success ? "LOGIN_SUCCESS" : "LOGIN_FAILED";
        String details = success ? "User logged in successfully" : "Failed login attempt";
        logActivity(username, action, details);
    }

    /**
     * Log a logout activity
     */
    public static void logLogout(String username) {
        logActivity(username, "LOGOUT", "User logged out");
    }

    /**
     * Log a registration activity
     */
    public static void logRegistration(String username) {
        logActivity(username, "REGISTRATION", "New user registered");
    }

    /**
     * Get all activity logs
     */
    public static List<ActivityLog> getAllLogs() {
        List<ActivityLog> logs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4); // Limit to 4 parts
                if (parts.length == 4) {
                    logs.add(new ActivityLog(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading activity logs: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Get recent activity logs (last N entries)
     */
    public static List<ActivityLog> getRecentLogs(int count) {
        List<ActivityLog> allLogs = getAllLogs();
        int startIndex = Math.max(0, allLogs.size() - count);
        return allLogs.subList(startIndex, allLogs.size());
    }

    /**
     * Activity Log data class
     */
    public static class ActivityLog {
        private final String timestamp;
        private final String username;
        private final String action;
        private final String details;

        public ActivityLog(String timestamp, String username, String action, String details) {
            this.timestamp = timestamp;
            this.username = username;
            this.action = action;
            this.details = details;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getUsername() {
            return username;
        }

        public String getAction() {
            return action;
        }

        public String getDetails() {
            return details;
        }
    }
}
