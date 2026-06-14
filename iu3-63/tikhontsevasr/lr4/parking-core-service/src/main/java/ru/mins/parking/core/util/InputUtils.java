package ru.mins.parking.core.util;

import ru.mins.parking.core.exception.ValidationException;

import java.util.Scanner;

public final class InputUtils {
    private InputUtils() {
    }

    public static int parsePositiveInt(String value, String fieldName) {
        try {
            int result = Integer.parseInt(value.trim());
            if (result <= 0) {
                throw new ValidationException("Поле \"" + fieldName + "\" должно быть положительным");
            }
            return result;
        } catch (NumberFormatException exception) {
            throw new ValidationException("Поле \"" + fieldName + "\" должно быть числом");
        }
    }

    public static String normalizeText(String value, String fieldName) {
        if (value == null || value.trim().isBlank()) {
            throw new ValidationException("Поле \"" + fieldName + "\" не должно быть пустым");
        }
        return value.trim();
    }

    public static <E extends Enum<E>> E readEnum(Scanner scanner, Class<E> enumClass, String fieldName) {
        E[] values = enumClass.getEnumConstants();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i].name());
        }
        System.out.print("Выберите " + fieldName + ": ");
        int choice = parsePositiveInt(scanner.nextLine(), fieldName);
        if (choice > values.length) {
            throw new ValidationException("Некорректное значение поля \"" + fieldName + "\"");
        }
        return values[choice - 1];
    }
}
