/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

import java.util.Date;

public class Feedback {
    private String feedbackId;
    private int rating; // 1-5
    private String comment;
    private Date createdAt;
    private ChatSession session;
    private EndUser user;
    
    public Feedback(String feedbackId, int rating, String comment, 
                    Date createdAt, ChatSession session, EndUser user) {
        this.feedbackId = feedbackId;
        this.rating = Math.max(1, Math.min(5, rating)); // Ensure 1-5 range
        this.comment = comment;
        this.createdAt = createdAt;
        this.session = session;
        this.user = user;
    }
    
    public void updateRating(int newRating) {
        this.rating = Math.max(1, Math.min(5, newRating));
    }
    
    public void updateComment(String newComment) {
        this.comment = newComment;
    }
    
    // Getters and Setters
    public String getFeedbackId() { return feedbackId; }
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { 
        this.rating = Math.max(1, Math.min(5, rating)); 
    }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public Date getCreatedAt() { return createdAt; }
    
    public ChatSession getSession() { return session; }
    public void setSession(ChatSession session) { this.session = session; }
    
    public EndUser getUser() { return user; }
    public void setUser(EndUser user) { this.user = user; }
}
