/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatSession {
    private String sessionId;
    private Date startedAt;
    private EndUser user;
    private KnowledgeBase knowledgeBase;
    private List<Message> messages;
    private boolean isBookmarked;

    public ChatSession(String sessionId, EndUser user, KnowledgeBase knowledgeBase) {
        this.sessionId = sessionId;
        this.user = user;
        this.knowledgeBase = knowledgeBase;
        this.messages = new ArrayList<>();
        this.startedAt = new Date();
    }

    public void start() {
        System.out.println("Chat session started: " + sessionId + " at " + startedAt);
    }

    public void end() {
        System.out.println("Chat session ended: " + sessionId);
        System.out.println("Duration: " + getDuration() + " seconds");
        System.out.println("Total messages: " + messages.size());
    }

    public void addMessage(Message message) {
        if (message != null) {
            messages.add(message);
        }
    }

    private long getDuration() {
        return (new Date().getTime() - startedAt.getTime()) / 1000;
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public EndUser getUser() {
        return user;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(KnowledgeBase kb) {
        this.knowledgeBase = kb;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.isBookmarked = bookmarked;
    }

    /**
     * Get conversation preview (first user message or "Empty conversation")
     */
    public String getConversationPreview() {
        for (Message msg : messages) {
            if (msg.isUser()) {
                String preview = msg.getContent();
                return preview.length() > 50 ? preview.substring(0, 50) + "..." : preview;
            }
        }
        return "Empty conversation";
    }

    /**
     * Get total message count
     */
    public int getMessageCount() {
        return messages.size();
    }
}
