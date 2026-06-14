package ru.mins.parking.core.util;

public final class TraceContext {
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    private TraceContext() {
    }

    public static void set(String traceId) {
        TRACE_ID.set(traceId);
    }

    public static String ensure() {
        String traceId = TRACE_ID.get();
        if (traceId == null || traceId.isBlank()) {
            traceId = TraceIdGenerator.newTraceId();
            TRACE_ID.set(traceId);
        }
        return traceId;
    }

    public static void clear() {
        TRACE_ID.remove();
    }
}
