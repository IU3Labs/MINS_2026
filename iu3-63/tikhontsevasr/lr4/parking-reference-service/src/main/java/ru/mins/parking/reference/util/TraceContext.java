package ru.mins.parking.reference.util;

public final class TraceContext {
    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    private TraceContext() {
    }

    public static void set(String traceId) {
        TRACE_ID.set(traceId);
    }

    public static String currentOrDefault() {
        String traceId = TRACE_ID.get();
        return traceId == null || traceId.isBlank() ? "no-trace" : traceId;
    }

    public static void clear() {
        TRACE_ID.remove();
    }
}
