package com.techchat.service;

import com.techchat.model.ChatSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory conversation manager
 * Stores all chat sessions during runtime
 * Data is cleared when application closes
 */
public class ConversationManager {

    private static ConversationManager instance;
    private final List<ChatSession> sessions;
    private final Map<String, ChatSession> sessionMap;

    private ConversationManager() {
        sessions = Collections.synchronizedList(new ArrayList<>());
        sessionMap = new ConcurrentHashMap<>();
    }

    public static ConversationManager getInstance() {
        if (instance == null) {
            instance = new ConversationManager();
        }
        return instance;
    }

    /**
     * Register a new chat session
     * Only registers sessions with at least 1 message
     */
    public void registerSession(ChatSession session) {
        if (session != null && !sessionMap.containsKey(session.getSessionId())) {
            // Only add sessions that have messages
            if (session.getMessageCount() > 0) {
                sessions.add(session);
                sessionMap.put(session.getSessionId(), session);
            }
        }
    }

    /**
     * Get all sessions sorted by start time (newest first)
     */
    public List<ChatSession> getAllSessions() {
        synchronized (sessions) {
            return sessions.stream()
                    .sorted((s1, s2) -> s2.getStartedAt().compareTo(s1.getStartedAt()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get a specific session by ID
     */
    public ChatSession getSessionById(String sessionId) {
        return sessionMap.get(sessionId);
    }

    /**
     * Search sessions by message content
     */
    public List<ChatSession> searchSessions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllSessions();
        }

        String lowerQuery = query.toLowerCase();
        synchronized (sessions) {
            return sessions.stream()
                    .filter(session -> {
                        // Search in messages
                        return session.getMessages().stream()
                                .anyMatch(msg -> msg.getContent().toLowerCase().contains(lowerQuery));
                    })
                    .sorted((s1, s2) -> s2.getStartedAt().compareTo(s1.getStartedAt()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get only bookmarked sessions
     */
    public List<ChatSession> getBookmarkedSessions() {
        synchronized (sessions) {
            return sessions.stream()
                    .filter(ChatSession::isBookmarked)
                    .sorted((s1, s2) -> s2.getStartedAt().compareTo(s1.getStartedAt()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get total number of sessions
     */
    public int getSessionCount() {
        return sessions.size();
    }

    /**
     * Delete a specific session by ID
     * 
     * @param sessionId The ID of the session to delete
     * @return true if session was found and deleted, false otherwise
     */
    public boolean deleteSession(String sessionId) {
        if (sessionId == null) {
            return false;
        }

        ChatSession session = sessionMap.get(sessionId);
        if (session != null) {
            synchronized (sessions) {
                sessions.remove(session);
                sessionMap.remove(sessionId);
            }
            return true;
        }
        return false;
    }

    /**
     * Clear all sessions (for cleanup)
     */
    public void clearAllSessions() {
        synchronized (sessions) {
            sessions.clear();
            sessionMap.clear();
        }
    }

    /**
     * Get recent sessions (limited)
     */
    public List<ChatSession> getRecentSessions(int limit) {
        synchronized (sessions) {
            return sessions.stream()
                    .sorted((s1, s2) -> s2.getStartedAt().compareTo(s1.getStartedAt()))
                    .limit(limit)
                    .collect(Collectors.toList());
        }
    }
}
