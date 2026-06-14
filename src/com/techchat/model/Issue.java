/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Issue {

    private String issueId;
    private String title;
    private String description;
    private IssueStatus status;
    private Date createdAt;

    private EndUser createdBy;     // who reported the issue
    private KBAdmin handledBy;     // admin who manages it
    private Article relatedArticle;

    private List<String> adminComments;

    // ---------- Constructor ----------
    public Issue(String issueId,
                 String description,
                 IssueStatus status,
                 Date createdAt,
                 EndUser createdBy,
                 Article relatedArticle) {

        this.issueId = issueId;
        this.title = "Issue " + issueId; // temporary title (you can override)
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.relatedArticle = relatedArticle;

        this.adminComments = new ArrayList<>();
    }

    // ---------- Getters ----------
    public String getIssueId() { return issueId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public IssueStatus getStatus() { return status; }
    public Date getCreatedAt() { return createdAt; }
    public EndUser getCreatedBy() { return createdBy; }
    public KBAdmin getHandledBy() { return handledBy; }
    public Article getRelatedArticle() { return relatedArticle; }
    public List<String> getAdminComments() { return adminComments; }

    // ---------- Setters ----------
    public void setTitle(String title) { this.title = title; }
    public void setRelatedArticle(Article relatedArticle) { this.relatedArticle = relatedArticle; }
    public void setHandledBy(KBAdmin admin) { this.handledBy = admin; }

    // ---------- UML Methods ----------

    /** updateStatus(newStatus: IssueStatus): void */
    public void updateStatus(IssueStatus newStatus) {
        this.status = newStatus;
        System.out.println("Issue " + issueId + " updated to status: " + newStatus);
    }

    /** addAdminComment(comment: String, admin: KBAdmin): void */
public void addAdminComment(String comment, KBAdmin admin) {
    if (admin == null) {
        throw new IllegalArgumentException("Admin cannot be null when adding a comment.");
    }

    this.handledBy = admin;
    adminComments.add(admin.getName() + ": " + comment);

    System.out.println("Admin " + admin.getName() +
                       " commented on Issue " + issueId);
}


    @Override
    public String toString() {
        return "Issue{" +
                "issueId='" + issueId + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", createdBy=" + (createdBy != null ? createdBy.getName() : "null") +
                ", handledBy=" + (handledBy != null ? handledBy.getName() : "null") +
                '}';
    }
}

