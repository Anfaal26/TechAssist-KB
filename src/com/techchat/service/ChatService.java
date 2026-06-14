package com.techchat.service;

import com.techchat.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatService {

    private static KnowledgeBase sharedKnowledgeBase;
    private RAGConfig ragConfig;
    private ChatSession currentSession;
    private OpenAIService openAIService;

    public ChatService() {
        if (sharedKnowledgeBase == null) {
            sharedKnowledgeBase = new KnowledgeBase("kb_1", "Technical Knowledge Base",
                    "A knowledge base for technical questions");
            seedSampleArticles(); // TEMP: just to test search

            // Initialize RAG embedding system
            initializeEmbeddings();
        }
        this.ragConfig = new RAGConfig(); // default config
        this.openAIService = new OpenAIService();
    }

    /**
     * Initialize the embedding system for RAG
     */
    private void initializeEmbeddings() {
        try {
            System.out.println("Initializing RAG system...");
            sharedKnowledgeBase.loadEmbeddings("embeddings_store.json");
            int chunks = sharedKnowledgeBase.getEmbeddingCount();
            System.out.println("Loaded existing embeddings: " + chunks + " chunks");

            // If file exists but is empty, reindex
            if (chunks == 0) {
                System.out.println("Embedding file is empty. Reindexing...");
                sharedKnowledgeBase.reindexAll();
                sharedKnowledgeBase.saveEmbeddings("embeddings_store.json");
                System.out.println("Reindexing complete: " + sharedKnowledgeBase.getEmbeddingCount() + " chunks");
            }
        } catch (Exception e) {
            System.out.println("No existing embeddings found. Indexing articles...");
            try {
                sharedKnowledgeBase.reindexAll();
                sharedKnowledgeBase.saveEmbeddings("embeddings_store.json");
                System.out.println("Initial indexing complete: " + sharedKnowledgeBase.getEmbeddingCount() + " chunks");
            } catch (Exception ex) {
                System.err.println("Failed to index articles: " + ex.getMessage());
                System.err.println("Will fall back to keyword search");
            }
        }
    }

    /**
     * Get shared knowledge base (accessible from anywhere)
     */
    public static KnowledgeBase getSharedKnowledgeBase() {
        if (sharedKnowledgeBase == null) {
            sharedKnowledgeBase = new KnowledgeBase("kb_1", "Technical Knowledge Base",
                    "A knowledge base for technical questions");
        }
        return sharedKnowledgeBase;
    }

    private void seedSampleArticles() {
        List<String> tags1 = new ArrayList<>();
        tags1.add("java");
        tags1.add("oop");

        List<String> tags2 = new ArrayList<>();
        tags2.add("javafx");
        tags2.add("ui");

        List<String> tags3 = new ArrayList<>();
        tags3.add("fruit");
        tags3.add("food");

        sharedKnowledgeBase.addArticle(new Article(
                "art_1",
                "What is JavaFX?",
                "JavaFX is a framework for building Java desktop UIs.",
                tags2));

        sharedKnowledgeBase.addArticle(new Article(
                "art_2",
                "What is OOP?",
                "Object-Oriented Programming is a paradigm based on objects and classes.",
                tags1));

        sharedKnowledgeBase.addArticle(new Article(
                "art_3",
                "Blue_Banana_Article",
                "A banana is typically yellow when ripe. Bananas are a popular fruit known for their curved shape and sweet taste. They grow in tropical climates and are rich in potassium.",
                tags3));
    }

    public void startNewSession(EndUser user) {
        String sessionId = "sess_" + UUID.randomUUID();
        this.currentSession = new ChatSession(sessionId, user, sharedKnowledgeBase);
        currentSession.start();

        // Don't register yet - will register when session ends or user switches views
    }

    public ChatSession getCurrentSession() {
        return currentSession;
    }

    /**
     * Save the current session to history (only if it has messages)
     */
    public void saveCurrentSession() {
        if (currentSession != null) {
            ConversationManager.getInstance().registerSession(currentSession);
        }
    }

    /**
     * Main method your controller calls.
     * Now uses OpenAI for intelligent responses + knowledge base context
     */
    public String handleUserMessage(String userInput) {
        if (currentSession == null) {
            throw new IllegalStateException("No active chat session.");
        }

        // Start tracking response time
        long startTime = System.currentTimeMillis();

        Message userMsg = new Message(userInput, true);
        currentSession.addMessage(userMsg);

        // Track query in analytics
        com.techchat.service.AnalyticsService.getInstance().logQuery(
                userInput,
                currentSession.getUser() != null ? currentSession.getUser().getUserId() : "unknown",
                currentSession.getSessionId(),
                0 // result count will be updated below
        );

        // Search knowledge base using SEMANTIC SEARCH
        List<Article> related;
        try {
            related = sharedKnowledgeBase.semanticSearch(userInput, ragConfig.getTopK());
            System.out.println("=== SEMANTIC SEARCH DEBUG ===");
            System.out.println("Query: " + userInput);
            System.out.println("Found articles: " + related.size());
            for (Article a : related) {
                System.out.println("  - " + a.getTitle());
            }
        } catch (Exception e) {
            // Fallback to keyword search if embedding fails
            System.err.println("Semantic search failed, using keyword search: " + e.getMessage());
            related = sharedKnowledgeBase.searchArticles(userInput);
            System.out.println("=== KEYWORD SEARCH FALLBACK ===");
            System.out.println("Found articles: " + related.size());
        }

        // Build context from knowledge base
        StringBuilder context = new StringBuilder();
        if (!related.isEmpty()) {
            context.append("Relevant knowledge base articles:\n");
            for (Article a : related) {
                context.append("- ").append(a.getTitle()).append(": ")
                        .append(a.getContent()).append("\n");
            }
            context.append("\nUser question: ");
        }

        String enhancedQuery = context.toString() + userInput;

        // Get conversation history
        List<String> history = new ArrayList<>();
        List<Message> messages = currentSession.getMessages();
        for (int i = Math.max(0, messages.size() - 6); i < messages.size() - 1; i++) {
            history.add(messages.get(i).getContent());
        }

        // Get AI response
        String botText;
        Message botMsg;
        boolean aiCallSuccessful = true;
        String errorMessage = null;

        try {
            String rawResponse = openAIService.getChatResponse(enhancedQuery, history);

            // Add citations to response
            if (!related.isEmpty()) {
                botText = addCitations(rawResponse, related);
            } else {
                botText = rawResponse
                        + "\n\n💡 Note: I couldn't find any relevant articles in the knowledge base for this query. " +
                        "My response is based on general knowledge and may not reflect your specific documentation.";
            }

            // Create message with cited sources
            botMsg = new Message(botText, false);
            for (Article article : related) {
                botMsg.addCitedSource(article.getTitle());
            }
        } catch (Exception e) {
            aiCallSuccessful = false;
            errorMessage = e.getMessage();

            botText = "Sorry, I encountered an error: " + e.getMessage() +
                    "\n\nFalling back to knowledge base search...\n\n";

            // Fallback to simple KB search
            if (!related.isEmpty()) {
                botText += "Here are some related articles:\n";
                for (int i = 0; i < related.size(); i++) {
                    Article a = related.get(i);
                    botText += "[" + (i + 1) + "] " + a.getTitle() + "\n";
                }
            } else {
                botText += "I couldn't find any related articles.";
            }

            botMsg = new Message(botText, false);
            for (Article article : related) {
                botMsg.addCitedSource(article.getTitle());
            }
        }

        // Calculate response time and log to analytics
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        com.techchat.service.AnalyticsService.getInstance().logAICall(
                currentSession.getUser() != null ? currentSession.getUser().getUserId() : "unknown",
                currentSession.getSessionId(),
                aiCallSuccessful,
                responseTime,
                errorMessage);

        currentSession.addMessage(botMsg);
        return botText;
    }

    /**
     * Add citation markers [1], [2], etc. to response and append source list in APA
     * format
     */
    private String addCitations(String response, List<Article> sources) {
        if (sources.isEmpty()) {
            return response;
        }

        StringBuilder result = new StringBuilder(response);
        result.append("\n\n");
        result.append("References: ");

        java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat("yyyy");

        for (int i = 0; i < sources.size(); i++) {
            Article article = sources.get(i);
            String author = article.getAuthor() != null ? article.getAuthor() : "Unknown Author";
            String year = article.getCreatedAt() != null ? yearFormat.format(article.getCreatedAt()) : "n.d.";
            String title = article.getTitle();

            // Inline format: [1] Author (Year). Title.
            if (i > 0) {
                result.append(" | ");
            }
            result.append("[").append(i + 1).append("] ");
            result.append(author).append(" (").append(year).append("). ");
            result.append(title).append(".");
        }

        return result.toString();
    }
}
