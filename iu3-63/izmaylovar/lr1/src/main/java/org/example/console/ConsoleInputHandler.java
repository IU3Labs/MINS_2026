package org.example.console;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleInputHandler {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Scanner scanner = new Scanner(System.in);

    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer number.");
            }
        }
    }

    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a decimal number.");
            }
        }
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public UUID readUuid(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return UUID.fromString(scanner.nextLine().trim());
            } catch (IllegalArgumentException e) {
                System.out.println("Enter a valid UUID.");
            }
        }
    }

    public LocalDateTime readDateTime(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDateTime.parse(scanner.nextLine().trim(), INPUT_DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Use format yyyy-MM-dd HH:mm.");
            }
        }
    }
}
