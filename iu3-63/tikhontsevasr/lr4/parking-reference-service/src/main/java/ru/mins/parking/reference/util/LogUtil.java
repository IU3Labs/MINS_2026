package ru.mins.parking.reference.util;

import java.util.logging.Logger;

public final class LogUtil {
    private LogUtil() {
    }

    public static void info(Logger logger, String message) {
        logger.info(prefix() + " " + message);
    }

    private static String prefix() {
        return "[TRACE_ID=" + TraceContext.currentOrDefault() + "]";
    }
}
