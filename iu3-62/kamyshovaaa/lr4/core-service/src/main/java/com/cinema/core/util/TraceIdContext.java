package com.cinema.core.util;

import org.slf4j.MDC;
import java.util.UUID;

public class TraceIdContext {

    private static final ThreadLocal<String> currentTraceId = new ThreadLocal<>();

    public static String getCurrentTraceId() {
        String traceId = currentTraceId.get();
        if (traceId == null) {
            traceId = MDC.get("traceId");
        }
        if (traceId == null) {
            traceId = generateShortUuid();
            setCurrentTraceId(traceId);
        }
        return traceId;
    }

    public static void setCurrentTraceId(String traceId) {
        currentTraceId.set(traceId);
        MDC.put("traceId", traceId);
    }

    public static void clear() {
        currentTraceId.remove();
        MDC.remove("traceId");
    }

    public static String generateShortUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}