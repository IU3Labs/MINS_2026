package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
public class UserInputHandler {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String readChoice(Scanner scanner) {
        return scanner.nextLine();
    }

    public int readInt(Scanner scanner, String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt);
        }
        return Integer.parseInt(scanner.nextLine());
    }

    public String readLine(Scanner scanner, String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt);
        }
        return scanner.nextLine();
    }

    public LocalDate readDate(Scanner scanner, String prompt) throws DateTimeParseException {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        return LocalDate.parse(input, DATE_FORMAT);
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void error(String message) {
        System.err.println("❌ " + message);
    }
}