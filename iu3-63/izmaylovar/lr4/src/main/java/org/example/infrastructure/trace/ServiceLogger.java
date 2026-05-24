package org.example.infrastructure.trace;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ServiceLogger {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String serviceName;
    private final String componentName;

    private ServiceLogger(String serviceName, String componentName) {
        this.serviceName = serviceName;
        this.componentName = componentName;
    }

    public static ServiceLogger forComponent(String serviceName, Class<?> componentType) {
        return new ServiceLogger(serviceName, componentType.getSimpleName());
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    private void log(String level, String message) {
        String traceId = TraceContext.ensureTraceId();
        System.out.printf(
                "%s [%s] [%s] [%s] [traceId=%s] %s%n",
                LocalDateTime.now().format(TIMESTAMP_FORMATTER),
                level,
                serviceName,
                componentName,
                traceId,
                message
        );
    }
}
