package com.techchat.model;

import java.util.Date;
import java.util.UUID;

/**
 * Model for tracking AI API calls for analytics
 */
public class AICallLog {
    private String callId;
    private String userId;
    private String sessionId;
    private Date timestamp;
    private boolean successful;
    private long responseTime; // in milliseconds
    private String errorMessage; // if failed

    public AICallLog(String userId, String sessionId, boolean successful,
            long responseTime, String errorMessage) {
        this.callId = "ai_" + UUID.randomUUID().toString();
        this.userId = userId;
        this.sessionId = sessionId;
        this.timestamp = new Date();
        this.successful = successful;
        this.responseTime = responseTime;
        this.errorMessage = errorMessage;
    }

    // Constructor for loading from CSV
    public AICallLog(String callId, String userId, String sessionId, Date timestamp,
            boolean successful, long responseTime, String errorMessage) {
        this.callId = callId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.successful = successful;
        this.responseTime = responseTime;
        this.errorMessage = errorMessage;
    }

    // Getters
    public String getCallId() {
        return callId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Convert to CSV string
     */
    public String toCSV() {
        return String.format("%s,%s,%s,%d,%b,%d,%s",
                callId,
                userId,
                sessionId,
                timestamp.getTime(),
                successful,
                responseTime,
                errorMessage != null ? errorMessage.replace(",", ";") : "");
    }
}
