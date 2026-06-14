package com.techchat.service;

import com.techchat.model.EndUser;
import com.techchat.model.KBAdmin;
import com.techchat.model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to manage user data stored in CSV file
 */
public class UserDataService {
    private static final String USER_DATA_FILE = "src/users.csv";
    private static final String HEADER = "userId,username,password,name,email,role";

    /**
     * Initialize CSV file with default users if it doesn't exist
     */
    public static void initializeUserData() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(HEADER);
                // Add default users
                writer.println("U001,user,user123,User,user@techchat.com,EndUser");
                writer.println("A001,admin,admin123,Admin User,admin@techchat.com,KBAdmin");
                System.out.println("Created users.csv with default accounts");
            } catch (IOException e) {
                System.err.println("Error creating user data file: " + e.getMessage());
            }
        }
    }

    /**
     * Register a new user
     */
    public static boolean registerUser(String username, String password, String name, String email, String role) {
        // Check if username already exists
        if (usernameExists(username)) {
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_DATA_FILE, true))) {
            String userId = generateUserId(role);
            writer.println(String.format("%s,%s,%s,%s,%s,%s", userId, username, password, name, email, role));
            return true;
        } catch (IOException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticate user with username and password
     */
    public static User authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String userId = parts[0];
                    String storedUsername = parts[1];
                    String storedPassword = parts[2];
                    String name = parts[3];
                    String email = parts[4];
                    String role = parts[5];

                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        // Create appropriate user object based on role
                        if (role.equals("KBAdmin")) {
                            return new KBAdmin(userId, name, email);
                        } else {
                            return new EndUser(userId, name, email);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user data: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all users (for admin view)
     */
    public static List<UserData> getAllUsers() {
        List<UserData> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    users.add(new UserData(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }

        return users;
    }

    /**
     * User data class for admin display
     */
    public static class UserData {
        private final String userId;
        private final String username;
        private final String password;
        private final String name;
        private final String email;
        private final String role;

        public UserData(String userId, String username, String password, String name, String email, String role) {
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public String getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }

    /**
     * Check if username already exists
     */
    private static boolean usernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Generate unique user ID
     */
    private static String generateUserId(String role) {
        String prefix = role.equals("KBAdmin") ? "A" : "U";
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith(prefix)) {
                    try {
                        int id = Integer.parseInt(parts[0].substring(1));
                        maxId = Math.max(maxId, id);
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error generating user ID: " + e.getMessage());
        }

        return String.format("%s%03d", prefix, maxId + 1);
    }

    /**
     * Delete user by userId
     * 
     * @param userId The ID of the user to delete
     * @return true if user was found and deleted, false otherwise
     */
    public static boolean deleteUser(String userId) {
        List<UserData> users = getAllUsers();
        boolean removed = users.removeIf(user -> user.getUserId().equals(userId));

        if (removed) {
            saveUsersToFile(users);
        }

        return removed;
    }

    /**
     * Save users list to CSV file
     * 
     * @param users The list of users to save
     */
    private static void saveUsersToFile(List<UserData> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_DATA_FILE))) {
            writer.println(HEADER);
            for (UserData user : users) {
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                        user.getUserId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole());
            }
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }
}
