package com.techchat.model;

import java.util.UUID;

/**
 * Represents a chunk of an article with its embedding vector
 * Used for semantic search in the RAG system
 */
public class ArticleChunk {

    private String chunkId;
    private String articleId;
    private String content;
    private double[] embedding; // Vector representation (1536 dimensions for OpenAI)
    private int chunkIndex; // Position in the original article

    /**
     * Constructor for creating a new chunk
     */
    public ArticleChunk(String articleId, String content, int chunkIndex) {
        this.chunkId = UUID.randomUUID().toString();
        this.articleId = articleId;
        this.content = content;
        this.chunkIndex = chunkIndex;
        this.embedding = null; // Will be set later by EmbeddingService
    }

    /**
     * Constructor for loading from storage (with existing ID and embedding)
     */
    public ArticleChunk(String chunkId, String articleId, String content,
            int chunkIndex, double[] embedding) {
        this.chunkId = chunkId;
        this.articleId = articleId;
        this.content = content;
        this.chunkIndex = chunkIndex;
        this.embedding = embedding;
    }

    // Getters
    public String getChunkId() {
        return chunkId;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getContent() {
        return content;
    }

    public double[] getEmbedding() {
        return embedding;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    // Setters
    public void setEmbedding(double[] embedding) {
        this.embedding = embedding;
    }

    /**
     * Check if this chunk has an embedding
     */
    public boolean hasEmbedding() {
        return embedding != null && embedding.length > 0;
    }

    /**
     * Get embedding dimension
     */
    public int getEmbeddingDimension() {
        return embedding != null ? embedding.length : 0;
    }

    @Override
    public String toString() {
        return String.format("ArticleChunk[id=%s, articleId=%s, index=%d, hasEmbedding=%s, contentLength=%d]",
                chunkId, articleId, chunkIndex, hasEmbedding(), content.length());
    }
}
