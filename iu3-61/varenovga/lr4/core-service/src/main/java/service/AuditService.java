package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static final String LOG_FILE_PATH = "audit.log";
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

    public static void logSuccess(String actionType, String userId, String details) {
        String entry = String.format("[%s] ✅ %s | User: %s | %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)), actionType, userId, details);
        try (FileWriter w = new FileWriter(LOG_FILE_PATH, true)) { w.write(entry); }
        catch (IOException e) { /* Swallowing Exception - AntiPattern */ }
    }

    public static void logError(String actionType, String userId, String error) {
        String entry = String.format("[%s] ❌ %s | User: %s | ERROR: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)), actionType, userId, error);
        try (FileWriter w = new FileWriter(LOG_FILE_PATH, true)) { w.write(entry); }
        catch (IOException e) { /* Swallowing Exception - AntiPattern */ }
    }
}