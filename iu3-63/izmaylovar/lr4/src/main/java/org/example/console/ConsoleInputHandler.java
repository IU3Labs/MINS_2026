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
    private static final String BACK_COMMAND = "0";

    private final Scanner scanner = new Scanner(System.in);

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer number.");
            }
        }
    }

    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            try {
                return new BigDecimal(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a decimal number.");
            }
        }
    }

    public String readString(String prompt) {
        return readLine(prompt);
    }

    public UUID readUuid(String prompt) {
        while (true) {
            try {
                return UUID.fromString(readLine(prompt));
            } catch (IllegalArgumentException e) {
                System.out.println("Enter a valid UUID.");
            }
        }
    }

    public LocalDateTime readDateTime(String prompt) {
        while (true) {
            try {
                return LocalDateTime.parse(readLine(prompt), INPUT_DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Use format yyyy-MM-dd HH:mm.");
            }
        }
    }

    public int readIntOrBack(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLineOrBack(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer number or 0 to go back.");
            }
        }
    }

    public BigDecimal readBigDecimalOrBack(String prompt) {
        while (true) {
            try {
                return new BigDecimal(readLineOrBack(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a decimal number or 0 to go back.");
            }
        }
    }

    public String readStringOrBack(String prompt) {
        return readLineOrBack(prompt);
    }

    public UUID readUuidOrBack(String prompt) {
        while (true) {
            try {
                return UUID.fromString(readLineOrBack(prompt));
            } catch (IllegalArgumentException e) {
                System.out.println("Enter a valid UUID or 0 to go back.");
            }
        }
    }

    public LocalDateTime readDateTimeOrBack(String prompt) {
        while (true) {
            try {
                return LocalDateTime.parse(readLineOrBack(prompt), INPUT_DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Use format yyyy-MM-dd HH:mm or 0 to go back.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String readLineOrBack(String prompt) {
        String value = readLine(withBackHint(prompt));
        if (BACK_COMMAND.equals(value)) {
            throw new MenuBackException();
        }
        return value;
    }

    private String withBackHint(String prompt) {
        if (prompt.endsWith(": ")) {
            return prompt.substring(0, prompt.length() - 2) + " (0 to back): ";
        }
        return prompt + " (0 to back)";
    }
}
