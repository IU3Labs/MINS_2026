package util;

import exception.InvalidOperationException;

public final class InputUtils {
    private InputUtils() {
    }

    public static int parsePositiveInt(String value, String fieldName) {
        try {
            int result = Integer.parseInt(value.trim());
            if (result <= 0) {
                throw new InvalidOperationException(fieldName + " должен быть положительным числом.");
            }
            return result;
        } catch (NumberFormatException exception) {
            throw new InvalidOperationException(fieldName + " должен быть целым числом.");
        }
    }

    public static String normalizeText(String value, String fieldName) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new InvalidOperationException("Поле '" + fieldName + "' не должно быть пустым.");
        }
        return normalized;
    }
}
