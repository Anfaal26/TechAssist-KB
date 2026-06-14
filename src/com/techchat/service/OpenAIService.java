package com.techchat.service;

import com.techchat.config.AppConfig;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Service for interacting with OpenAI API
 */
public class OpenAIService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final HttpClient httpClient;
    private final AppConfig config;

    public OpenAIService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.config = AppConfig.getInstance();
    }

    /**
     * Send a chat message and get AI response
     */
    public String getChatResponse(String userMessage, List<String> conversationHistory) throws Exception {
        if (!config.isConfigured()) {
            return "⚠️ OpenAI API key not configured. Please add your API key in config.properties";
        }

        // Build the request body
        String requestBody = buildChatRequest(userMessage, conversationHistory);

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.getOpenAIKey())
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            // Send request and get response
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                return "Error: API returned status " + response.statusCode() +
                        "\n" + response.body();
            }
        } catch (Exception e) {
            throw new Exception("Failed to get response from OpenAI: " + e.getMessage(), e);
        }
    }

    /**
     * Process image with Vision API
     */
    public String getVisionResponse(String imagePath, String userPrompt) throws Exception {
        if (!config.isConfigured()) {
            return "⚠️ OpenAI API key not configured.";
        }

        // Convert image to Base64
        String base64Image = encodeImageToBase64(imagePath);

        // Build Vision API request
        String requestBody = buildVisionRequest(base64Image, userPrompt);

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.getOpenAIKey())
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                return "Error: API returned status " + response.statusCode();
            }
        } catch (Exception e) {
            throw new Exception("Failed to process image: " + e.getMessage(), e);
        }
    }

    /**
     * Analyze image from base64 string (for document processing)
     */
    public String analyzeImageFromBase64(String base64Image, String userPrompt) throws Exception {
        if (!config.isConfigured()) {
            return "⚠️ OpenAI API key not configured.";
        }

        // Build Vision API request
        String requestBody = buildVisionRequest(base64Image, userPrompt);

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.getOpenAIKey())
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                return "Error: API returned status " + response.statusCode();
            }
        } catch (Exception e) {
            throw new Exception("Failed to process image: " + e.getMessage(), e);
        }
    }

    private String buildChatRequest(String userMessage, List<String> history) {
        StringBuilder messages = new StringBuilder();
        messages.append("[");

        // System message for context
        messages.append(
                "{\"role\":\"system\",\"content\":\"You are a helpful technical assistant in a knowledge base system. Provide clear, concise answers.\"},");

        // Add conversation history if available
        if (history != null && !history.isEmpty()) {
            for (int i = 0; i < Math.min(history.size(), 10); i++) {
                String msg = history.get(i);
                boolean isUser = i % 2 == 0;
                messages.append("{\"role\":\"")
                        .append(isUser ? "user" : "assistant")
                        .append("\",\"content\":\"")
                        .append(escapeJson(msg))
                        .append("\"},");
            }
        }

        // Current user message
        messages.append("{\"role\":\"user\",\"content\":\"")
                .append(escapeJson(userMessage))
                .append("\"}");

        messages.append("]");

        return String.format(
                "{\"model\":\"%s\",\"messages\":%s,\"max_tokens\":%d,\"temperature\":%.1f}",
                config.getModel(),
                messages.toString(),
                config.getMaxTokens(),
                config.getTemperature());
    }

    private String buildVisionRequest(String base64Image, String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            prompt = "What's in this image? Please describe it in detail and extract any text you see.";
        }

        return String.format(
                "{\"model\":\"gpt-4o\",\"messages\":[" +
                        "{\"role\":\"user\",\"content\":[" +
                        "{\"type\":\"text\",\"text\":\"%s\"}," +
                        "{\"type\":\"image_url\",\"image_url\":{\"url\":\"data:image/jpeg;base64,%s\"}}" +
                        "]}],\"max_tokens\":%d}",
                escapeJson(prompt),
                base64Image,
                config.getMaxTokens());
    }

    private String parseResponse(String jsonResponse) {
        try {
            int contentIndex = jsonResponse.indexOf("\"content\":");
            if (contentIndex == -1) {
                return "Error: Could not parse response";
            }

            int startQuote = jsonResponse.indexOf("\"", contentIndex + 10);
            int endQuote = jsonResponse.indexOf("\"", startQuote + 1);

            if (startQuote == -1 || endQuote == -1) {
                return "Error: Could not extract content";
            }

            String content = jsonResponse.substring(startQuote + 1, endQuote);
            return unescapeJson(content);

        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }

    private String encodeImageToBase64(String imagePath) throws Exception {
        try {
            java.io.File imageFile = new java.io.File(imagePath);
            byte[] imageBytes = java.nio.file.Files.readAllBytes(imageFile.toPath());
            return java.util.Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new Exception("Error reading image file: " + e.getMessage(), e);
        }
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
}
