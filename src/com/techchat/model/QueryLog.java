package com.techchat.model;

import java.util.Date;
import java.util.UUID;

/**
 * Model for tracking user queries for analytics
 */
public class QueryLog {
    private String queryId;
    private String query;
    private String userId;
    private String sessionId;
    private Date timestamp;
    private int resultCount; // Number of documents retrieved

    public QueryLog(String query, String userId, String sessionId, int resultCount) {
        this.queryId = "q_" + UUID.randomUUID().toString();
        this.query = query;
        this.userId = userId;
        this.sessionId = sessionId;
        this.timestamp = new Date();
        this.resultCount = resultCount;
    }

    // Constructor for loading from CSV
    public QueryLog(String queryId, String query, String userId, String sessionId,
            Date timestamp, int resultCount) {
        this.queryId = queryId;
        this.query = query;
        this.userId = userId;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.resultCount = resultCount;
    }

    // Getters
    public String getQueryId() {
        return queryId;
    }

    public String getQuery() {
        return query;
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

    public int getResultCount() {
        return resultCount;
    }

    /**
     * Convert to CSV string
     */
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%d,%d",
                queryId,
                query.replace(",", ";"),
                userId,
                sessionId,
                timestamp.getTime(),
                resultCount);
    }
}
