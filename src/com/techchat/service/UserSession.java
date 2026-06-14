package com.techchat.service;

import com.techchat.model.User;

/**
 * Singleton service to manage user session across the application
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() {
        // Private constructor for singleton
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null &&
                currentUser.getClass().getSimpleName().equals("KBAdmin");
    }

    public boolean isEndUser() {
        return currentUser != null &&
                currentUser.getClass().getSimpleName().equals("EndUser");
    }

    /**
     * Get the role of the current user as a string
     * 
     * @return "Admin" if KBAdmin, "EndUser" if EndUser, "Guest" if not logged in
     */
    public String getRole() {
        if (currentUser == null) {
            return "Guest";
        }

        String className = currentUser.getClass().getSimpleName();
        if (className.equals("KBAdmin")) {
            return "Admin";
        } else if (className.equals("EndUser")) {
            return "EndUser";
        }

        return "Unknown";
    }
}
