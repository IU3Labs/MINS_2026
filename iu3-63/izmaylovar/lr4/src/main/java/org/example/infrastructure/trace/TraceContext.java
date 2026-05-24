package org.example.infrastructure.trace;

import java.util.UUID;

public final class TraceContext {
    private static final ThreadLocal<String> CURRENT_TRACE_ID = new ThreadLocal<>();

    private TraceContext() {
    }

    public static String ensureTraceId() {
        String existing = CURRENT_TRACE_ID.get();
        if (existing != null && !existing.isBlank()) {
            return existing;
        }

        String created = UUID.randomUUID().toString();
        CURRENT_TRACE_ID.set(created);
        return created;
    }

    public static String currentTraceId() {
        return CURRENT_TRACE_ID.get();
    }

    public static void setTraceId(String traceId) {
        if (traceId == null || traceId.isBlank()) {
            CURRENT_TRACE_ID.remove();
            return;
        }
        CURRENT_TRACE_ID.set(traceId);
    }

    public static void clear() {
        CURRENT_TRACE_ID.remove();
    }

    public static void runWithTrace(String traceId, Runnable action) {
        String previous = CURRENT_TRACE_ID.get();
        try {
            setTraceId(traceId);
            action.run();
        } finally {
            restore(previous);
        }
    }

    private static void restore(String previous) {
        if (previous == null || previous.isBlank()) {
            CURRENT_TRACE_ID.remove();
        } else {
            CURRENT_TRACE_ID.set(previous);
        }
    }
}
