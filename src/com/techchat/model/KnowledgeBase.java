/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

import com.techchat.service.VectorStore;
import com.techchat.service.EmbeddingService;
import com.techchat.util.TextSplitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KnowledgeBase {
    private String kbId;
    private String name;
    private String description;
    private List<Article> articles;
    private VectorStore vectorStore;
    private EmbeddingService embeddingService;
    public RAGConfig ragConfig; // Public to allow runtime configuration from Settings UI

    public KnowledgeBase(String kbId, String name, String description) {
        this.kbId = kbId;
        this.name = name;
        this.description = description;
        this.articles = new ArrayList<>();
        this.vectorStore = new VectorStore();
        this.embeddingService = new EmbeddingService();
        this.ragConfig = new RAGConfig();
    }

    /**
     * Smart search that splits query into words and matches any word
     */
    public List<Article> searchArticles(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Split query into individual words and filter out common stop words
        String[] queryWords = query.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ") // Remove punctuation
                .split("\\s+");

        List<String> keywords = new ArrayList<>();
        for (String word : queryWords) {
            // Skip very short words and common stop words
            if (word.length() > 2 && !isStopWord(word)) {
                keywords.add(word);
            }
        }

        if (keywords.isEmpty()) {
            // Fallback to original query if no keywords found
            keywords.add(query.toLowerCase());
        }

        // Score each article based on keyword matches
        return articles.stream()
                .map(article -> new ArticleScore(article, scoreArticle(article, keywords)))
                .filter(scored -> scored.score > 0) // Only include articles with matches
                .sorted((a, b) -> Integer.compare(b.score, a.score)) // Sort by score descending
                .map(scored -> scored.article)
                .collect(Collectors.toList());
    }

    /**
     * Check if a word is a common stop word
     */
    private boolean isStopWord(String word) {
        String[] stopWords = { "the", "is", "at", "which", "on", "a", "an", "and", "or", "but", "in", "with", "to",
                "for", "of", "as", "by", "from", "up", "about", "into", "through", "during", "what" };
        for (String stopWord : stopWords) {
            if (word.equals(stopWord)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Score an article based on keyword matches
     */
    private int scoreArticle(Article article, List<String> keywords) {
        int score = 0;
        String lowerTitle = article.getTitle().toLowerCase();
        String lowerContent = article.getContent().toLowerCase();

        for (String keyword : keywords) {
            // Title matches are worth more
            if (lowerTitle.contains(keyword)) {
                score += 10;
            }
            // Content matches
            if (lowerContent.contains(keyword)) {
                score += 5;
            }
            // Tag matches
            for (String tag : article.getTags()) {
                if (tag.toLowerCase().contains(keyword)) {
                    score += 3;
                }
            }
        }

        return score;
    }

    /**
     * Helper class to hold article with its relevance score
     */
    private static class ArticleScore {
        Article article;
        int score;

        ArticleScore(Article article, int score) {
            this.article = article;
            this.score = score;
        }
    }

    public Article getArticleById(String articleId) {
        return articles.stream()
                .filter(a -> a.getArticleId().equals(articleId))
                .findFirst()
                .orElse(null);
    }

    public void addArticle(Article article) {
        if (article != null && !articles.contains(article)) {
            articles.add(article);
        }
    }

    public void removeArticle(Article article) {
        articles.remove(article);
    }

    /**
     * Delete article by title
     * 
     * @param title The title of the article to delete
     * @return true if article was found and deleted, false otherwise
     */
    public boolean deleteArticleByTitle(String title) {
        return articles.removeIf(article -> article.getTitle().equals(title));
    }

    // Getters and Setters
    public String getKbId() {
        return kbId;
    }

    public void setKbId(String kbId) {
        this.kbId = kbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Get all articles in the knowledge base
     *
     * @return List of all articles
     */
    public List<Article> getAllArticles() {
        return new ArrayList<>(articles);
    }

    // ============== SEMANTIC SEARCH (RAG) ==============

    /**
     * Semantic search using embeddings and vector similarity
     * 
     * @param query The search query
     * @param topK  Number of results to return
     * @return List of most relevant articles
     */
    public List<Article> semanticSearch(String query, int topK) throws Exception {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Generate embedding for the query
        double[] queryEmbedding = embeddingService.generateEmbedding(query);

        // Search for similar chunks with similarity threshold
        List<ArticleChunk> similarChunks = vectorStore.searchSimilar(
                queryEmbedding,
                topK * 3,
                ragConfig.getMinSimilarityThreshold());

        // Map chunks back to articles (deduplicate)
        Set<String> seenArticleIds = new HashSet<>();
        List<Article> results = new ArrayList<>();

        for (ArticleChunk chunk : similarChunks) {
            if (!seenArticleIds.contains(chunk.getArticleId())) {
                Article article = getArticleById(chunk.getArticleId());
                if (article != null) {
                    results.add(article);
                    seenArticleIds.add(chunk.getArticleId());

                    if (results.size() >= topK) {
                        break;
                    }
                }
            }
        }

        return results;
    }

    /**
     * Index a single article (chunk andgembed)
     */
    public void indexArticle(Article article) throws Exception {
        if (article == null) {
            return;
        }

        System.out.println("Indexing article: " + article.getTitle());

        // Combine title and content for chunking
        String fullText = article.getTitle() + ". " + article.getContent();

        // Split into chunks
        List<String> chunks = TextSplitter.splitText(fullText,
                ragConfig.getChunkSize(), ragConfig.getChunkOverlap());

        System.out.println("  Created " + chunks.size() + " chunks");

        // Generate embeddings for each chunk
        for (int i = 0; i < chunks.size(); i++) {
            String chunkText = chunks.get(i);

            try {
                // Create chunk with embedding
                ArticleChunk chunk = new ArticleChunk(article.getArticleId(), chunkText, i);
                double[] embedding = embeddingService.generateEmbedding(chunkText);
                chunk.setEmbedding(embedding);

                // Add to vector store
                vectorStore.addChunk(chunk);

                // Small delay to avoid rate limits
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println("  Error embedding chunk " + i + ": " + e.getMessage());
            }
        }

        System.out.println("  Indexing complete");
    }

    /**
     * Reindex all articles in the knowledge base
     */
    public void reindexAll() throws Exception {
        System.out.println("=== Starting full reindex ===");
        System.out.println("Total articles: " + articles.size());

        vectorStore.clear();

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            System.out.println("[" + (i + 1) + "/" + articles.size() + "] Indexing: " + article.getTitle());

            try {
                indexArticle(article);
            } catch (Exception e) {
                System.err.println("Failed to index article: " + e.getMessage());
            }
        }

        System.out.println("=== Reindex complete ===");
        System.out.println("Total chunks: " + vectorStore.size());
    }

    /**
     * Hybrid search combining keyword and semantic search
     */
    public List<Article> hybridSearch(String query, int topK) throws Exception {
        // Get results from both methods
        List<Article> keywordResults = searchArticles(query);
        List<Article> semanticResults = semanticSearch(query, topK);

        // Merge and deduplicate
        Map<String, Article> merged = new HashMap<>();

        // Add semantic results first (higher priority)
        for (Article article : semanticResults) {
            merged.put(article.getArticleId(), article);
        }

        // Add keyword results
        for (Article article : keywordResults) {
            if (!merged.containsKey(article.getArticleId())) {
                merged.put(article.getArticleId(), article);
            }
        }

        // Return top K
        return merged.values().stream()
                .limit(topK)
                .collect(Collectors.toList());
    }

    /**
     * Save embeddings to file
     */
    public void saveEmbeddings(String filepath) throws IOException {
        vectorStore.saveToFile(filepath);
    }

    /**
     * Load embeddings from file
     */
    public void loadEmbeddings(String filepath) throws IOException {
        vectorStore.loadFromFile(filepath);
    }

    /**
     * Get vector store stats
     */
    public int getEmbeddingCount() {
        return vectorStore.size();
    }
}
