/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

import java.util.Date;
import java.util.List;

public class Article {
    private String articleId;
    private String title;
    private String content;
    private List<String> tags;
    private Date createdAt;
    private Date updatedAt;
    private String author; // KBAdmin name
    
    public Article(String articleId, String title, String content, List<String> tags) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = new Date();
    }
    
    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            this.updatedAt = new Date();
        }
    }
    
    public void removeTag(String tag) {
        tags.remove(tag);
        this.updatedAt = new Date();
    }
    
    // Getters and Setters
    public String getArticleId() { return articleId; }
    public void setArticleId(String articleId) { this.articleId = articleId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title;
        this.updatedAt = new Date();
    }
    
    public String getContent() { return content; }
    public void setContent(String content) { 
        this.content = content;
        this.updatedAt = new Date();
    }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { 
        this.tags = tags;
        this.updatedAt = new Date();
    }
    
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}