/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

public class EndUser extends User {
    
    public EndUser(String userId, String name, String email) {
        super(userId, name, email);
    }
    
    public void startChat(KnowledgeBase kb, ChatSession session) {
        System.out.println(name + " started a chat session");
        if (session != null) {
            session.start();
        }
    }
    
    public void submitFeedback(Message message, String summary, String feedback) {
        Feedback fb = new Feedback(
            generateFeedbackId(),
            5, // default rating
            feedback,
            new java.util.Date(),
            null, // session will be set later
            this
        );
        System.out.println("Feedback submitted by " + name + ": " + feedback);
    }
    
    public void reportIssue(String description, String articleId) {
        Issue issue = new Issue(
            generateIssueId(),
            description,
            IssueStatus.OPEN,
            new java.util.Date(),
            this,
            null // article will be set later
        );
        System.out.println("Issue reported by " + name + ": " + description);
    }
    
    private String generateFeedbackId() {
        return "fb_" + System.currentTimeMillis();
    }
    
    private String generateIssueId() {
        return "issue_" + System.currentTimeMillis();
    }
}
