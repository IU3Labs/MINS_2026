package main.java.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemAuditLogger {
    private static final String LOG_FILE_PATH = "audit.log";
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

    public static void logAction(String actionType, String userId, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String logEntry = String.format("[%s] %s | User: %s | %s\n",
                timestamp, actionType, userId, details);

        try {
            FileWriter writer = new FileWriter(LOG_FILE_PATH, true);
            writer.write(logEntry);
            writer.close();
        } catch (IOException e) {
            // ❌ SWALLOWING EXCEPTION (намеренно для ЛР3)
        }
    }

    public static void logSuccess(String actionType, String userId, String details) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String logEntry = String.format("[%s] ✅ %s | User: %s | %s\n",
                timestamp, actionType, userId, details);

        try {
            FileWriter writer = new FileWriter(LOG_FILE_PATH, true);
            writer.write(logEntry);
            writer.close();
        } catch (IOException e) {
            // ❌ SWALLOWING EXCEPTION
        }
    }

    public static void logError(String actionType, String userId, String error) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String logEntry = String.format("[%s] ❌ %s | User: %s | ERROR: %s\n",
                timestamp, actionType, userId, error);

        try {
            FileWriter writer = new FileWriter(LOG_FILE_PATH, true);
            writer.write(logEntry);
            writer.close();
        } catch (IOException e) {
            // ❌ SWALLOWING EXCEPTION
        }
    }

    public static String getLogFilePath() {
        return LOG_FILE_PATH;
    }
}