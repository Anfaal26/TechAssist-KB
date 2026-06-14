/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Message {
    public enum SenderType {
        USER, AI
    }

    private String messageId;
    private SenderType senderType;
    private String content;
    private Date timestamp;
    private List<String> citedSources;

    public Message(String messageId, SenderType senderType, String content) {
        this.messageId = messageId;
        this.senderType = senderType;
        this.content = content;
        this.timestamp = new Date();
        this.citedSources = new ArrayList<>();
    }

    // Convenience constructor for boolean
    public Message(String content, boolean isUser) {
        this.messageId = generateMessageId();
        this.senderType = isUser ? SenderType.USER : SenderType.AI;
        this.content = content;
        this.timestamp = new Date();
        this.citedSources = new ArrayList<>();
    }

    public void addContent(String newContent) {
        this.content += newContent;
    }

    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis();
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public SenderType getSenderType() {
        return senderType;
    }

    public void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<String> getCitedSources() {
        return citedSources;
    }

    public void setCitedSources(List<String> sources) {
        this.citedSources = sources;
    }

    public void addCitedSource(String source) {
        if (source != null && !citedSources.contains(source)) {
            citedSources.add(source);
        }
    }

    // Convenience method
    public boolean isUser() {
        return senderType == SenderType.USER;
    }

    @Override
    public String toString() {
        return senderType + ": " + content;
    }
}