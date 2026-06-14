/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

import java.util.List;

public class KBAdmin extends User {
    
    public KBAdmin(String userId, String name, String email) {
        super(userId, name, email);
    }
    
    public void createArticle(KnowledgeBase kb, String title, String content, List<String> tags) {
        if (kb != null) {
            Article article = new Article(
                generateArticleId(),
                title,
                content,
                tags
            );
            article.setAuthor(this.name);
            kb.addArticle(article);
            System.out.println("Article created: " + title);
        }
    }
    
    public void updateArticle(Article article, String content, List<String> tags) {
        if (article != null) {
            article.updateContent(content);
            if (tags != null) {
                article.setTags(tags);
            }
            System.out.println("Article updated: " + article.getTitle());
        }
    }
    
    public void archiveArticle(Article article) {
        if (article != null) {
            System.out.println("Article archived: " + article.getTitle());
            // Implementation would mark article as archived
        }
    }
    
    public void reviewFeedback(KnowledgeBase kb) {
        System.out.println("Admin " + name + " reviewing feedback");
        // Implementation would retrieve and display feedback
    }
    
    public void assignIssue(Issue issue, KBAdmin admin) {
        if (issue != null && admin != null) {
            System.out.println("Issue " + issue.getIssueId() + " assigned to " + admin.getName());
            // Implementation would update issue assignment
        }
    }
    
    public void updateIssueStatus(Issue issue, IssueStatus status) {
        if (issue != null) {
            issue.updateStatus(status);
            System.out.println("Issue status updated to: " + status);
        }
    }
    
    private String generateArticleId() {
        return "art_" + System.currentTimeMillis();
    }

   
}