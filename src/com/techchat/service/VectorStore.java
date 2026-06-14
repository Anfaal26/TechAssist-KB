package com.techchat.service;

import com.techchat.model.ArticleChunk;
import java.io.*;
import java.util.*;

/**
 * Vector store for managing article chunk embeddings
 * Supports in-memory storage with file persistence
 */
public class VectorStore {

    private List<ArticleChunk> chunks;
    private Map<String, ArticleChunk> chunkIndex; // Quick lookup by chunkId

    public VectorStore() {
        this.chunks = new ArrayList<>();
        this.chunkIndex = new HashMap<>();
    }

    /**
     * Add a chunk with its embedding to the store
     */
    public void addChunk(ArticleChunk chunk) {
        if (chunk == null || !chunk.hasEmbedding()) {
            throw new IllegalArgumentException("Chunk must have an embedding");
        }

        // Remove existing if present
        if (chunkIndex.containsKey(chunk.getChunkId())) {
            removeChunk(chunk.getChunkId());
        }

        chunks.add(chunk);
        chunkIndex.put(chunk.getChunkId(), chunk);
    }

    /**
     * Remove a chunk from the store
     */
    public void removeChunk(String chunkId) {
        ArticleChunk chunk = chunkIndex.remove(chunkId);
        if (chunk != null) {
            chunks.remove(chunk);
        }
    }

    /**
     * Remove all chunks for a specific article
     */
    public void removeChunksByArticleId(String articleId) {
        chunks.removeIf(chunk -> chunk.getArticleId().equals(articleId));
        chunkIndex.entrySet().removeIf(entry -> entry.getValue().getArticleId().equals(articleId));
    }

    /**
     * Search for similar chunks using cosine similarity
     * 
     * @param queryEmbedding The query vector
     * @param topK           Number of top results to return
     * @param minSimilarity  Minimum similarity threshold (0.0 to 1.0)
     * @return List of most similar chunks, sorted by similarity (highest first)
     */
    public List<ArticleChunk> searchSimilar(double[] queryEmbedding, int topK, double minSimilarity) {
        if (queryEmbedding == null || queryEmbedding.length == 0) {
            return new ArrayList<>();
        }

        // Calculate similarity for all chunks
        List<ChunkSimilarity> similarities = new ArrayList<>();
        for (ArticleChunk chunk : chunks) {
            if (chunk.hasEmbedding()) {
                double similarity = cosineSimilarity(queryEmbedding, chunk.getEmbedding());

                // Only include chunks above the similarity threshold
                if (similarity >= minSimilarity) {
                    similarities.add(new ChunkSimilarity(chunk, similarity));
                }
            }
        }

        // Sort by similarity (descending)
        similarities.sort((a, b) -> Double.compare(b.similarity, a.similarity));

        // Debug logging
        if (!similarities.isEmpty()) {
            System.out.println("=== TOP SIMILARITY SCORES ===");
            int logCount = Math.min(5, similarities.size());
            for (int i = 0; i < logCount; i++) {
                ChunkSimilarity cs = similarities.get(i);
                String preview = cs.chunk.getContent().length() > 50
                        ? cs.chunk.getContent().substring(0, 50) + "..."
                        : cs.chunk.getContent();
                System.out.printf("  %.3f - %s\n", cs.similarity, preview);
            }
            System.out
                    .println("Total results above threshold (%.2f): %d".formatted(minSimilarity, similarities.size()));
        } else {
            System.out.println("=== NO RESULTS ABOVE THRESHOLD ===");
            System.out.printf("  Threshold: %.2f\n", minSimilarity);
            System.out.println("  No chunks met the minimum similarity requirement.");
        }

        // Return top K from filtered results
        int resultCount = Math.min(topK, similarities.size());
        List<ArticleChunk> results = new ArrayList<>();
        for (int i = 0; i < resultCount; i++) {
            results.add(similarities.get(i).chunk);
        }

        return results;
    }

    /**
     * Calculate cosine similarity between two vectors
     * Result is between -1 and 1, where 1 means identical direction
     */
    public double cosineSimilarity(double[] vec1, double[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }

        double denominator = Math.sqrt(norm1) * Math.sqrt(norm2);
        if (denominator == 0.0) {
            return 0.0;
        }

        return dotProduct / denominator;
    }

    /**
     * Get total number of chunks stored
     */
    public int size() {
        return chunks.size();
    }

    /**
     * Check if store is empty
     */
    public boolean isEmpty() {
        return chunks.isEmpty();
    }

    /**
     * Clear all chunks
     */
    public void clear() {
        chunks.clear();
        chunkIndex.clear();
    }

    /**
     * Save chunks to JSON file
     */
    public void saveToFile(String filepath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write("[\n");

            for (int i = 0; i < chunks.size(); i++) {
                ArticleChunk chunk = chunks.get(i);
                writer.write("  {\n");
                writer.write("    \"chunkId\": \"" + escapeJson(chunk.getChunkId()) + "\",\n");
                writer.write("    \"articleId\": \"" + escapeJson(chunk.getArticleId()) + "\",\n");
                writer.write("    \"content\": \"" + escapeJson(chunk.getContent()) + "\",\n");
                writer.write("    \"chunkIndex\": " + chunk.getChunkIndex() + ",\n");
                writer.write("    \"embedding\": [");

                double[] emb = chunk.getEmbedding();
                for (int j = 0; j < emb.length; j++) {
                    writer.write(String.valueOf(emb[j]));
                    if (j < emb.length - 1)
                        writer.write(",");
                }

                writer.write("]\n");
                writer.write("  }");
                if (i < chunks.size() - 1)
                    writer.write(",");
                writer.write("\n");
            }

            writer.write("]\n");
        }
    }

    /**
     * Load chunks from JSON file
     */
    public void loadFromFile(String filepath) throws IOException {
        chunks.clear();
        chunkIndex.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line).append("\n");
            }

            // Parse JSON manually (simple parser for our specific format)
            parseChunksFromJson(json.toString());
        }
    }

    /**
     * Simple JSON parser for loading chunks
     */
    private void parseChunksFromJson(String json) {
        // Split by chunk objects
        String[] chunkStrings = json.split("\\},\\s*\\{");

        for (String chunkStr : chunkStrings) {
            try {
                String chunkId = extractJsonValue(chunkStr, "chunkId");
                String articleId = extractJsonValue(chunkStr, "articleId");
                String content = extractJsonValue(chunkStr, "content");
                int chunkIndex = Integer.parseInt(extractJsonValue(chunkStr, "chunkIndex"));
                double[] embedding = parseEmbeddingArray(chunkStr);

                ArticleChunk chunk = new ArticleChunk(chunkId, articleId, content,
                        chunkIndex, embedding);
                addChunk(chunk);
            } catch (Exception e) {
                System.err.println("Error parsing chunk: " + e.getMessage());
            }
        }
    }

    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":\\s*\"([^\"]*)\"|\"" + key + "\":\\s*([0-9]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1) != null ? unescapeJson(m.group(1)) : m.group(2);
        }
        return "";
    }

    private double[] parseEmbeddingArray(String json) {
        int start = json.indexOf("\"embedding\": [");
        if (start == -1)
            return new double[0];

        int arrayStart = json.indexOf("[", start);
        int arrayEnd = json.indexOf("]", arrayStart);

        String arrayContent = json.substring(arrayStart + 1, arrayEnd);
        String[] values = arrayContent.split(",");

        double[] embedding = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            embedding[i] = Double.parseDouble(values[i].trim());
        }

        return embedding;
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeJson(String text) {
        return text.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    /**
     * Helper class to pair chunks with their similarity scores
     */
    private static class ChunkSimilarity {
        ArticleChunk chunk;
        double similarity;

        ChunkSimilarity(ArticleChunk chunk, double similarity) {
            this.chunk = chunk;
            this.similarity = similarity;
        }
    }

    /**
     * Get all chunks (for debugging/stats)
     */
    public List<ArticleChunk> getAllChunks() {
        return new ArrayList<>(chunks);
    }
}
