package ru.mins.parking.core.util;

import java.util.logging.Logger;

public final class LogUtil {
    private LogUtil() {
    }

    public static void info(Logger logger, String message) {
        logger.info(prefix() + " " + message);
    }

    public static void error(Logger logger, String message) {
        logger.severe(prefix() + " " + message);
    }

    private static String prefix() {
        return "[TRACE_ID=" + TraceContext.ensure() + "]";
    }
}
