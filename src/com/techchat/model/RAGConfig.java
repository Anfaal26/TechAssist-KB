/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.techchat.model;

public class RAGConfig {

    private String configId;
    private String modelName; // e.g. "gpt-4.1-mini"
    private String apiKey; // DON'T hardcode real key in code
    private String endpointUrl; // API URL
    private int topK;
    private double temperature;
    private int chunkSize; // Size of text chunks for embeddings
    private int chunkOverlap; // Overlap between chunks
    private String embeddingModel; // Embedding model name
    private double minSimilarityThreshold; // Minimum cosine similarity to consider a result relevant

    public RAGConfig(String configId, String modelName, String apiKey,
            String endpointUrl, int topK, double temperature,
            int chunkSize, int chunkOverlap, String embeddingModel, double minSimilarityThreshold) {
        this.configId = configId;
        this.modelName = modelName;
        this.apiKey = apiKey;
        this.endpointUrl = endpointUrl;
        this.topK = topK;
        this.temperature = temperature;
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.embeddingModel = embeddingModel;
        this.minSimilarityThreshold = minSimilarityThreshold;
    }

    // simple default
    public RAGConfig() {
        this("default-config",
                "gpt-4.1-mini",
                "",
                "https://api.openai.com/v1/chat/completions",
                5,
                0.3,
                500, // default chunk size
                50, // default overlap
                "text-embedding-3-small",
                0.5); // default minimum similarity threshold
    }

    public String getConfigId() {
        return configId;
    }

    public String getModelName() {
        return modelName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public int getTopK() {
        return topK;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getChunkOverlap() {
        return chunkOverlap;
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void updateEndpointUrl(String newUrl) {
        if (newUrl == null || newUrl.isEmpty()) {
            throw new IllegalArgumentException("Endpoint URL cannot be empty");
        }
        this.endpointUrl = newUrl;
    }

    public void updateParameters(int topK, double temperature) {
        if (topK <= 0)
            throw new IllegalArgumentException("topK must be > 0");
        if (temperature < 0 || temperature > 2)
            throw new IllegalArgumentException("temperature must be between 0 and 2");
        this.topK = topK;
        this.temperature = temperature;
    }

    public void updateChunkingParameters(int chunkSize, int chunkOverlap) {
        if (chunkSize <= 0)
            throw new IllegalArgumentException("chunkSize must be > 0");
        if (chunkOverlap < 0 || chunkOverlap >= chunkSize)
            throw new IllegalArgumentException("chunkOverlap must be >= 0 and < chunkSize");
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
    }

    public double getMinSimilarityThreshold() {
        return minSimilarityThreshold;
    }

    public void setMinSimilarityThreshold(double threshold) {
        if (threshold < 0.0 || threshold > 1.0)
            throw new IllegalArgumentException("minSimilarityThreshold must be between 0.0 and 1.0");
        this.minSimilarityThreshold = threshold;
    }
}
