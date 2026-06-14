package ru.iu3.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileAppLogger implements AppLogger {
    private static final Object LOCK = new Object();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path logFile;

    public FileAppLogger(String filePath) {
        this.logFile = Path.of(filePath);
    }

    @Override
    public void log(String layer, String traceId, String message) {
        String line = String.format("%s [%s] traceId=%s %s%n",
                LocalDateTime.now().format(FORMATTER), layer, traceId, message);

        synchronized (LOCK) {
            try {
                Path parent = logFile.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.writeString(logFile, line, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось записать лог в файл " + logFile, e);
            }
        }
    }
}
