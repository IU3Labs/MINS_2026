package ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    public double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine());
    }

    public LocalDate readDate(String prompt) {
        System.out.print(prompt);
        return LocalDate.parse(scanner.nextLine());
    }

    public LocalDateTime readDateTime(String datePrompt, String timePrompt) {
        LocalDate date = readDate(datePrompt);
        System.out.print(timePrompt);
        return LocalDateTime.of(date, LocalTime.parse(scanner.nextLine()));
    }

    public UUID readUuid(String prompt) {
        System.out.print(prompt);
        return UUID.fromString(scanner.nextLine());
    }

    public String readChoice(List<String> options) {
        String input = scanner.nextLine();
        if (input.isEmpty() && !options.isEmpty()) {
            return options.get(0);
        }
        return input;
    }

    public boolean confirm(String prompt) {
        System.out.print(prompt + " (д/н): ");
        return scanner.nextLine().equalsIgnoreCase("д");
    }
}
