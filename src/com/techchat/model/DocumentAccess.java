package com.techchat.model;

import java.util.Date;
import java.util.UUID;

/**
 * Model for tracking document access for analytics
 */
public class DocumentAccess {
    private String accessId;
    private String documentId;
    private String documentTitle;
    private String userId;
    private Date timestamp;

    public DocumentAccess(String documentId, String documentTitle, String userId) {
        this.accessId = "da_" + UUID.randomUUID().toString();
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.userId = userId;
        this.timestamp = new Date();
    }

    // Constructor for loading from CSV
    public DocumentAccess(String accessId, String documentId, String documentTitle,
            String userId, Date timestamp) {
        this.accessId = accessId;
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters
    public String getAccessId() {
        return accessId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public String getUserId() {
        return userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Convert to CSV string
     */
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%d",
                accessId,
                documentId,
                documentTitle.replace(",", ";"),
                userId,
                timestamp.getTime());
    }
}
