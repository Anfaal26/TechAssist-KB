package com.techchat.service;

import com.techchat.model.Feedback;
import com.techchat.model.ChatSession;
import com.techchat.model.EndUser;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing feedback on AI responses
 */
public class FeedbackService {

    private static FeedbackService instance;
    private static final String FEEDBACK_FILE = "feedback.csv";
    private final List<Feedback> feedbackList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private FeedbackService() {
        this.feedbackList = Collections.synchronizedList(new ArrayList<>());
        loadFeedback();
    }

    public static FeedbackService getInstance() {
        if (instance == null) {
            instance = new FeedbackService();
        }
        return instance;
    }

    /**
     * Submit new feedback
     */
    public void submitFeedback(int rating, String comment, ChatSession session, EndUser user) {
        String feedbackId = "fb_" + UUID.randomUUID().toString();
        Feedback feedback = new Feedback(feedbackId, rating, comment, new Date(), session, user);

        synchronized (feedbackList) {
            feedbackList.add(feedback);
        }

        saveFeedback();
    }

    /**
     * Get all feedback
     */
    public List<Feedback> getAllFeedback() {
        synchronized (feedbackList) {
            return new ArrayList<>(feedbackList);
        }
    }

    /**
     * Get low-rated feedback (rating <= 2)
     */
    public List<Feedback> getLowRatedFeedback() {
        synchronized (feedbackList) {
            return feedbackList.stream()
                    .filter(f -> f.getRating() <= 2)
                    .sorted((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get feedback by rating
     */
    public List<Feedback> getFeedbackByRating(int rating) {
        synchronized (feedbackList) {
            return feedbackList.stream()
                    .filter(f -> f.getRating() == rating)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get average rating
     */
    public double getAverageRating() {
        synchronized (feedbackList) {
            if (feedbackList.isEmpty())
                return 0.0;
            return feedbackList.stream()
                    .mapToInt(Feedback::getRating)
                    .average()
                    .orElse(0.0);
        }
    }

    /**
     * Get feedback count by rating
     */
    public Map<Integer, Long> getRatingDistribution() {
        synchronized (feedbackList) {
            return feedbackList.stream()
                    .collect(Collectors.groupingBy(Feedback::getRating, Collectors.counting()));
        }
    }

    /**
     * Get total feedback count
     */
    public int getFeedbackCount() {
        return feedbackList.size();
    }

    /**
     * Save feedback to CSV file
     */
    private void saveFeedback() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FEEDBACK_FILE))) {
            // Write header
            writer.println("feedbackId,rating,comment,createdAt,sessionId,userId");

            synchronized (feedbackList) {
                for (Feedback feedback : feedbackList) {
                    String comment = feedback.getComment() != null ? feedback.getComment().replace(",", ";") : "";
                    String sessionId = feedback.getSession() != null ? feedback.getSession().getSessionId() : "";
                    String userId = feedback.getUser() != null ? feedback.getUser().getName() : "";

                    writer.printf("%s,%d,%s,%s,%s,%s%n",
                            feedback.getFeedbackId(),
                            feedback.getRating(),
                            comment,
                            dateFormat.format(feedback.getCreatedAt()),
                            sessionId,
                            userId);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
    }

    /**
     * Load feedback from CSV file
     */
    private void loadFeedback() {
        File file = new File(FEEDBACK_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    try {
                        String feedbackId = parts[0];
                        int rating = Integer.parseInt(parts[1]);
                        String comment = parts[2].isEmpty() ? null : parts[2].replace(";", ",");
                        Date createdAt = dateFormat.parse(parts[3]);

                        // Note: Session and User objects are not fully reconstructed
                        // Only IDs are stored for reference
                        Feedback feedback = new Feedback(feedbackId, rating, comment,
                                createdAt, null, null);
                        feedbackList.add(feedback);
                    } catch (Exception e) {
                        System.err.println("Error parsing feedback line: " + e.getMessage());
                    }
                }
            }

            System.out.println("Loaded " + feedbackList.size() + " feedback entries");
        } catch (IOException e) {
            System.err.println("Error loading feedback: " + e.getMessage());
        }
    }
}
