package com.techchat.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for splitting text into chunks for embeddings
 * Splits text at sentence boundaries to preserve context
 */
public class TextSplitter {

    private static final int DEFAULT_CHUNK_SIZE = 500; // tokens
    private static final int DEFAULT_OVERLAP = 50; // tokens

    /**
     * Split text into chunks with default size and overlap
     */
    public static List<String> splitText(String text) {
        return splitText(text, DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP);
    }

    /**
     * Split text into chunks of specified size with overlap
     * 
     * @param text      The text to split
     * @param chunkSize Maximum size of each chunk in tokens
     * @param overlap   Number of tokens to overlap between chunks
     * @return List of text chunks
     */
    public static List<String> splitText(String text, int chunkSize, int overlap) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> chunks = new ArrayList<>();

        // Split into sentences first to preserve context
        String[] sentences = text.split("(?<=[.!?])\\s+");

        StringBuilder currentChunk = new StringBuilder();
        int currentTokens = 0;
        List<String> overlapSentences = new ArrayList<>();

        for (String sentence : sentences) {
            int sentenceTokens = estimateTokens(sentence);

            // If adding this sentence would exceed chunk size
            if (currentTokens + sentenceTokens > chunkSize && currentChunk.length() > 0) {
                // Save current chunk
                chunks.add(currentChunk.toString().trim());

                // Start new chunk with overlap from previous chunk
                currentChunk = new StringBuilder();
                currentTokens = 0;

                // Add overlap sentences
                for (String overlapSent : overlapSentences) {
                    currentChunk.append(overlapSent).append(" ");
                    currentTokens += estimateTokens(overlapSent);
                }

                overlapSentences.clear();
            }

            // Add sentence to current chunk
            currentChunk.append(sentence).append(" ");
            currentTokens += sentenceTokens;

            // Track sentences for overlap (keep last few sentences)
            overlapSentences.add(sentence);
            if (estimateTokens(String.join(" ", overlapSentences)) > overlap) {
                overlapSentences.remove(0);
            }
        }

        // Add final chunk if not empty
        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }

    /**
     * Estimate token count using simple word-based approximation
     * Real tokenization is more complex, but this is sufficient for chunking
     * 
     * @param text The text to estimate
     * @return Estimated number of tokens
     */
    public static int estimateTokens(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        // Split by whitespace and count
        String[] words = text.trim().split("\\s+");

        // Approximate: average English word ≈ 1.3 tokens
        // For simplicity, we'll use 1 word ≈ 1 token
        return words.length;
    }

    /**
     * Get recommended chunk size for embeddings
     */
    public static int getDefaultChunkSize() {
        return DEFAULT_CHUNK_SIZE;
    }

    /**
     * Get recommended overlap size
     */
    public static int getDefaultOverlap() {
        return DEFAULT_OVERLAP;
    }
}
