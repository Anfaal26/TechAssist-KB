package com.techchat.service;

import com.techchat.model.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for analytics and tracking
 */
public class AnalyticsService {

    private static AnalyticsService instance;
    private static final String QUERY_LOG_FILE = "query_log.csv";
    private static final String DOC_ACCESS_FILE = "document_access.csv";
    private static final String AI_CALL_LOG_FILE = "ai_call_log.csv";

    private final List<QueryLog> queryLogs;
    private final List<DocumentAccess> documentAccesses;
    private final List<AICallLog> aiCallLogs;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private AnalyticsService() {
        this.queryLogs = Collections.synchronizedList(new ArrayList<>());
        this.documentAccesses = Collections.synchronizedList(new ArrayList<>());
        this.aiCallLogs = Collections.synchronizedList(new ArrayList<>());
        loadAllData();
    }

    public static AnalyticsService getInstance() {
        if (instance == null) {
            instance = new AnalyticsService();
        }
        return instance;
    }

    // ==================== QUERY TRACKING ====================

    /**
     * Log a user query
     */
    public void logQuery(String query, String userId, String sessionId, int resultCount) {
        QueryLog log = new QueryLog(query, userId, sessionId, resultCount);
        synchronized (queryLogs) {
            queryLogs.add(log);
        }
        saveQueryLogs();
    }

    /**
     * Get most common queries
     */
    public Map<String, Long> getMostCommonQueries(int limit) {
        synchronized (queryLogs) {
            return queryLogs.stream()
                    .collect(Collectors.groupingBy(QueryLog::getQuery, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(limit)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
        }
    }

    /**
     * Get queries within date range
     */
    public List<QueryLog> getQueriesInRange(Date startDate, Date endDate) {
        synchronized (queryLogs) {
            return queryLogs.stream()
                    .filter(log -> !log.getTimestamp().before(startDate) &&
                            !log.getTimestamp().after(endDate))
                    .collect(Collectors.toList());
        }
    }

    // ==================== DOCUMENT ACCESS TRACKING ====================

    /**
     * Log document access
     */
    public void logDocumentAccess(String documentId, String documentTitle, String userId) {
        DocumentAccess access = new DocumentAccess(documentId, documentTitle, userId);
        synchronized (documentAccesses) {
            documentAccesses.add(access);
        }
        saveDocumentAccesses();
    }

    /**
     * Get most frequently accessed documents
     */
    public Map<String, Long> getMostAccessedDocuments(int limit) {
        synchronized (documentAccesses) {
            return documentAccesses.stream()
                    .collect(Collectors.groupingBy(DocumentAccess::getDocumentTitle, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(limit)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
        }
    }

    // ==================== AI CALL TRACKING ====================

    /**
     * Log AI API call
     */
    public void logAICall(String userId, String sessionId, boolean successful,
            long responseTime, String errorMessage) {
        AICallLog log = new AICallLog(userId, sessionId, successful, responseTime, errorMessage);
        synchronized (aiCallLogs) {
            aiCallLogs.add(log);
        }
        saveAICallLogs();
    }

    /**
     * Get daily AI call counts
     */
    public Map<String, Long> getDailyAICallCounts(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        Date startDate = cal.getTime();

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

        synchronized (aiCallLogs) {
            return aiCallLogs.stream()
                    .filter(log -> log.getTimestamp().after(startDate))
                    .collect(Collectors.groupingBy(
                            log -> dayFormat.format(log.getTimestamp()),
                            Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
        }
    }

    /**
     * Get AI call success rate
     */
    public double getAICallSuccessRate() {
        synchronized (aiCallLogs) {
            if (aiCallLogs.isEmpty())
                return 100.0;
            long successCount = aiCallLogs.stream().filter(AICallLog::isSuccessful).count();
            return (successCount * 100.0) / aiCallLogs.size();
        }
    }

    /**
     * Get average AI response time
     */
    public long getAverageResponseTime() {
        synchronized (aiCallLogs) {
            if (aiCallLogs.isEmpty())
                return 0;
            return (long) aiCallLogs.stream()
                    .mapToLong(AICallLog::getResponseTime)
                    .average()
                    .orElse(0.0);
        }
    }

    // ==================== GENERAL STATISTICS ====================

    /**
     * Get total query count
     */
    public int getTotalQueryCount() {
        return queryLogs.size();
    }

    /**
     * Get total AI calls
     */
    public int getTotalAICallCount() {
        return aiCallLogs.size();
    }

    /**
     * Get total document accesses
     */
    public int getTotalDocumentAccessCount() {
        return documentAccesses.size();
    }

    // ==================== PERSISTENCE ====================

    private void saveQueryLogs() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(QUERY_LOG_FILE))) {
            writer.println("queryId,query,userId,sessionId,timestamp,resultCount");
            synchronized (queryLogs) {
                for (QueryLog log : queryLogs) {
                    writer.println(log.toCSV());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving query logs: " + e.getMessage());
        }
    }

    private void saveDocumentAccesses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DOC_ACCESS_FILE))) {
            writer.println("accessId,documentId,documentTitle,userId,timestamp");
            synchronized (documentAccesses) {
                for (DocumentAccess access : documentAccesses) {
                    writer.println(access.toCSV());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving document accesses: " + e.getMessage());
        }
    }

    private void saveAICallLogs() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AI_CALL_LOG_FILE))) {
            writer.println("callId,userId,sessionId,timestamp,successful,responseTime,errorMessage");
            synchronized (aiCallLogs) {
                for (AICallLog log : aiCallLogs) {
                    writer.println(log.toCSV());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving AI call logs: " + e.getMessage());
        }
    }

    private void loadAllData() {
        loadQueryLogs();
        loadDocumentAccesses();
        loadAICallLogs();
    }

    private void loadQueryLogs() {
        File file = new File(QUERY_LOG_FILE);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    queryLogs.add(new QueryLog(
                            parts[0],
                            parts[1].replace(";", ","),
                            parts[2],
                            parts[3],
                            new Date(Long.parseLong(parts[4])),
                            Integer.parseInt(parts[5])));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading query logs: " + e.getMessage());
        }
    }

    private void loadDocumentAccesses() {
        File file = new File(DOC_ACCESS_FILE);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    documentAccesses.add(new DocumentAccess(
                            parts[0],
                            parts[1],
                            parts[2].replace(";", ","),
                            parts[3],
                            new Date(Long.parseLong(parts[4]))));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading document accesses: " + e.getMessage());
        }
    }

    private void loadAICallLogs() {
        File file = new File(AI_CALL_LOG_FILE);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 7) {
                    aiCallLogs.add(new AICallLog(
                            parts[0],
                            parts[1],
                            parts[2],
                            new Date(Long.parseLong(parts[3])),
                            Boolean.parseBoolean(parts[4]),
                            Long.parseLong(parts[5]),
                            parts[6].isEmpty() ? null : parts[6].replace(";", ",")));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading AI call logs: " + e.getMessage());
        }
    }
}
