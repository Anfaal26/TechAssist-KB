package com.techchat.service;

import com.techchat.model.ChatSession;
import com.techchat.model.Message;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Service for exporting conversations to TXT format
 */
public class ExportService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Export a conversation to TXT file
     */
    public static void exportToTxt(ChatSession session, File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Write header
            writer.write("========================================\n");
            writer.write("CHAT CONVERSATION EXPORT\n");
            writer.write("========================================\n");
            writer.write("Session ID: " + session.getSessionId() + "\n");
            writer.write("User: " + session.getUser().getName() + "\n");
            writer.write("Started: " + dateFormat.format(session.getStartedAt()) + "\n");
            writer.write("Message Count: " + session.getMessageCount() + "\n");
            writer.write("========================================\n\n");

            // Write messages
            List<Message> messages = session.getMessages();
            for (int i = 0; i < messages.size(); i++) {
                Message msg = messages.get(i);

                // Message header
                String sender = msg.isUser() ? "USER" : "ASSISTANT";
                writer.write("[" + sender + "] " + dateFormat.format(msg.getTimestamp()) + "\n");

                // Message content
                writer.write(msg.getContent() + "\n");

                // Cited sources (if any)
                if (!msg.isUser() && msg.getCitedSources() != null && !msg.getCitedSources().isEmpty()) {
                    writer.write("\nSources:\n");
                    for (String source : msg.getCitedSources()) {
                        writer.write("  - " + source + "\n");
                    }
                }

                writer.write("\n");

                // Separator between messages
                if (i < messages.size() - 1) {
                    writer.write("----------------------------------------\n\n");
                }
            }

            // Footer
            writer.write("\n========================================\n");
            writer.write("END OF CONVERSATION\n");
            writer.write("Exported: " + dateFormat.format(new java.util.Date()) + "\n");
            writer.write("========================================\n");
        }
    }

    /**
     * Generate default filename for export
     */
    public static String generateExportFilename(ChatSession session) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(session.getStartedAt());
        String preview = session.getConversationPreview()
                .replaceAll("[^a-zA-Z0-9]", "_")
                .substring(0, Math.min(30, session.getConversationPreview().length()));
        return "chat_" + timestamp + "_" + preview + ".txt";
    }
}
