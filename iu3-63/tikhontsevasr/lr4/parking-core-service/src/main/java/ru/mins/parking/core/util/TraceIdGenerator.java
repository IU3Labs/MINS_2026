package ru.mins.parking.core.util;

import java.util.UUID;

public final class TraceIdGenerator {
    private TraceIdGenerator() {
    }

    public static String newTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
