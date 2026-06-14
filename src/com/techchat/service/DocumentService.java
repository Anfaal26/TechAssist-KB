package com.techchat.service;

import com.techchat.model.Article;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for processing and managing uploaded documents
 */
public class DocumentService {

    /**
     * Process uploaded file and create Article
     */
    public static Article processDocument(File file) throws Exception {
        String fileName = file.getName();
        String fileType = getFileExtension(fileName);

        String content;
        if (fileType.equalsIgnoreCase("png") ||
                fileType.equalsIgnoreCase("jpg") ||
                fileType.equalsIgnoreCase("jpeg")) {
            content = extractImageText(file);
        } else if (fileType.equalsIgnoreCase("txt")) {
            content = readTextFile(file);
        } else if (fileType.equalsIgnoreCase("pdf")) {
            content = extractPDFText(file);
        } else if (fileType.equalsIgnoreCase("docx")) {
            content = extractDOCXText(file);
        } else {
            throw new Exception("Unsupported file type: " + fileType);
        }

        // Create article from document
        String docId = "doc_" + System.currentTimeMillis();
        String title = fileName.replace("." + fileType, "");

        List<String> tags = new ArrayList<>();
        tags.add(fileType);
        tags.add("uploaded");

        return new Article(docId, title, content, tags);
    }

    /**
     * Read text file content
     */

    private static String readTextFile(File file) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /**
     * Extract text from image using Vision API (OCR)
     */
    private static String extractImageText(File file) throws Exception {
        try {
            // Read image file as bytes
            byte[] imageBytes = Files.readAllBytes(file.toPath());

            // Convert to base64
            String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);

            // Create OpenAIService instance and use Vision API to extract text
            String prompt = "Extract all text from this image. If there is no readable text, describe what you see in the image in detail.";
            OpenAIService openAIService = new OpenAIService();
            String extractedContent = openAIService.analyzeImageFromBase64(base64Image, prompt);

            if (extractedContent == null || extractedContent.trim().isEmpty()) {
                return "⚠️ Could not extract content from image. The image may be empty or unreadable.";
            }

            return extractedContent;
        } catch (Exception e) {
            throw new Exception("Failed to process image: " + e.getMessage());
        }
    }

    /**
     * Extract text from PDF (basic extraction)
     */
    private static String extractPDFText(File file) throws Exception {
        byte[] pdfBytes = Files.readAllBytes(file.toPath());
        StringBuilder text = new StringBuilder();

        // Simple PDF text extraction (works for basic PDFs)
        // Look for text between BT and ET operators
        String pdfContent = new String(pdfBytes, "ISO-8859-1");

        int btIndex = 0;
        while ((btIndex = pdfContent.indexOf("BT", btIndex)) != -1) {
            int etIndex = pdfContent.indexOf("ET", btIndex);
            if (etIndex == -1)
                break;

            String textBlock = pdfContent.substring(btIndex, etIndex);

            // Extract text from Tj operators
            int tjIndex = 0;
            while ((tjIndex = textBlock.indexOf("Tj", tjIndex)) != -1) {
                int openParen = textBlock.lastIndexOf('(', tjIndex);
                int closeParen = textBlock.indexOf(')', openParen);

                if (openParen != -1 && closeParen != -1) {
                    String extractedText = textBlock.substring(openParen + 1, closeParen);
                    text.append(extractedText).append(" ");
                }
                tjIndex++;
            }

            btIndex = etIndex;
        }

        String result = text.toString().trim();
        if (result.isEmpty()) {
            return "⚠️ Could not extract text from PDF. Try converting to TXT or use OCR for complex PDFs.";
        }

        return result;
    }

    /**
     * Extract text from DOCX file (ZIP archive with XML)
     */
    private static String extractDOCXText(File file) throws Exception {
        StringBuilder text = new StringBuilder();
        final int MAX_TEXT_LENGTH = 10_000_000; // 10MB of text limit

        try (java.util.zip.ZipInputStream zipInputStream = new java.util.zip.ZipInputStream(
                new java.io.FileInputStream(file))) {

            java.util.zip.ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // The main document text is in word/document.xml
                if (entry.getName().equals("word/document.xml")) {
                    // Read the XML content
                    StringBuilder xmlContent = new StringBuilder();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        xmlContent.append(new String(buffer, 0, len, "UTF-8"));
                    }

                    // Extract text from XML (between <w:t> tags)
                    String xml = xmlContent.toString();
                    int startIndex = 0;
                    while ((startIndex = xml.indexOf("<w:t", startIndex)) != -1) {
                        int contentStart = xml.indexOf(">", startIndex) + 1;
                        int contentEnd = xml.indexOf("</w:t>", contentStart);

                        if (contentEnd != -1) {
                            String textContent = xml.substring(contentStart, contentEnd);
                            // Decode XML entities
                            textContent = textContent.replace("&lt;", "<")
                                    .replace("&gt;", ">")
                                    .replace("&amp;", "&")
                                    .replace("&quot;", "\"")
                                    .replace("&apos;", "'");

                            // Check size limit
                            if (text.length() + textContent.length() > MAX_TEXT_LENGTH) {
                                text.append("\n\n⚠️ Document truncated: exceeded maximum size limit (10MB).");
                                break;
                            }

                            text.append(textContent);
                        }
                        startIndex = contentEnd + 1;
                    }
                    break; // Found the document, no need to continue
                }
            }
        }

        String result = text.toString().trim();
        if (result.isEmpty()) {
            return "⚠️ Could not extract text from DOCX. File may be empty or corrupted.";
        }

        return result;
    }

    /**
     * Get file extension
     */
    private static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1)
            return "";
        return fileName.substring(lastDot + 1);
    }

    /**
     * Document metadata class for table display
     */
    public static class DocumentInfo {
        private final String name;
        private final String type;
        private final String tags;
        private final String dateAdded;

        public DocumentInfo(String name, String type, String tags, String dateAdded) {
            this.name = name;
            this.type = type;
            this.tags = tags;
            this.dateAdded = dateAdded;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getTags() {
            return tags;
        }

        public String getDateAdded() {
            return dateAdded;
        }
    }
}
