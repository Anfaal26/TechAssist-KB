package com.techchat.service;

import com.techchat.config.AppConfig;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating embeddings using OpenAI's Embeddings API
 */
public class EmbeddingService {

    private static final String EMBEDDINGS_API_URL = "https://api.openai.com/v1/embeddings";
    private static final String DEFAULT_MODEL = "text-embedding-3-small";
    private static final int EMBEDDING_DIMENSION = 1536;

    private final HttpClient httpClient;
    private final AppConfig config;

    public EmbeddingService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.config = AppConfig.getInstance();
    }

    /**
     * Generate embedding for a single text
     * 
     * @param text The text to embed
     * @return Vector representation (1536 dimensions)
     * @throws Exception if API call fails
     */
    public double[] generateEmbedding(String text) throws Exception {
        if (!config.isConfigured()) {
            throw new Exception("OpenAI API key not configured");
        }

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be empty");
        }

        // Build request body
        String requestBody = buildEmbeddingRequest(text);

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EMBEDDINGS_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.getOpenAIKey())
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            // Send request
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseEmbeddingResponse(response.body());
            } else {
                throw new Exception("API returned status " + response.statusCode() +
                        ": " + response.body());
            }
        } catch (Exception e) {
            throw new Exception("Failed to generate embedding: " + e.getMessage(), e);
        }
    }

    /**
     * Generate embeddings for multiple texts in batch
     * More efficient than calling generateEmbedding multiple times
     */
    public List<double[]> generateEmbeddingsBatch(List<String> texts) throws Exception {
        if (!config.isConfigured()) {
            throw new Exception("OpenAI API key not configured");
        }

        if (texts == null || texts.isEmpty()) {
            return new ArrayList<>();
        }

        // For now, process sequentially
        // OpenAI API supports batch but parsing is more complex
        List<double[]> embeddings = new ArrayList<>();
        for (String text : texts) {
            if (text != null && !text.trim().isEmpty()) {
                embeddings.add(generateEmbedding(text));
            }
        }

        return embeddings;
    }

    /**
     * Build JSON request body for embedding API
     */
    private String buildEmbeddingRequest(String text) {
        // Escape JSON
        String escapedText = text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        return String.format(
                "{\"model\":\"%s\",\"input\":\"%s\"}",
                DEFAULT_MODEL,
                escapedText);
    }

    /**
     * Parse embedding vector from API response
     * OpenAI format: {"data":[{"embedding":[...]}]}
     */
    private double[] parseEmbeddingResponse(String jsonResponse) throws Exception {
        try {
            // Debug: Print first 500 chars of response
            System.out.println(
                    "API Response preview: " + jsonResponse.substring(0, Math.min(500, jsonResponse.length())));

            // OpenAI embeddings API returns: {"data":[{"embedding":[...]}]}
            // Find the "embedding" array
            int embeddingStart = jsonResponse.indexOf("\"embedding\"");
            if (embeddingStart == -1) {
                // Check if it's an error response
                if (jsonResponse.contains("\"error\"")) {
                    throw new Exception("API returned error: " + jsonResponse);
                }
                throw new Exception("No embedding found in response");
            }

            // Find the array after "embedding":
            int arrayStart = jsonResponse.indexOf("[", embeddingStart);
            if (arrayStart == -1) {
                throw new Exception("No array found after 'embedding' key");
            }

            // Find matching closing bracket
            int bracketCount = 1;
            int arrayEnd = arrayStart + 1;
            while (bracketCount > 0 && arrayEnd < jsonResponse.length()) {
                char c = jsonResponse.charAt(arrayEnd);
                if (c == '[')
                    bracketCount++;
                else if (c == ']')
                    bracketCount--;
                arrayEnd++;
            }
            arrayEnd--; // Back up to the closing bracket

            if (arrayEnd <= arrayStart) {
                throw new Exception("Could not find closing bracket for embedding array");
            }

            // Extract array content
            String arrayContent = jsonResponse.substring(arrayStart + 1, arrayEnd);
            String[] values = arrayContent.split(",");

            // Parse to double array
            double[] embedding = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                embedding[i] = Double.parseDouble(values[i].trim());
            }

            System.out.println("Successfully parsed embedding with " + embedding.length + " dimensions");

            // Verify dimension
            if (embedding.length != EMBEDDING_DIMENSION) {
                System.err.println("Warning: Expected " + EMBEDDING_DIMENSION +
                        " dimensions but got " + embedding.length);
            }

            return embedding;

        } catch (NumberFormatException e) {
            throw new Exception("Failed to parse number in embedding array: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Failed to parse embedding response: " + e.getMessage(), e);
        }
    }

    /**
     * Get the expected embedding dimension
     */
    public static int getEmbeddingDimension() {
        return EMBEDDING_DIMENSION;
    }
}
