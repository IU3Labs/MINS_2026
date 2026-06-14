package org.example.coreservice.ui;

import lombok.RequiredArgsConstructor;
import org.example.coreservice.entity.enums.Weekday;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public abstract class BaseConsoleUI {

    protected final Scanner scanner;
    protected final InputUtil inputUtil;

    public BaseConsoleUI(Scanner scanner, InputUtil inputUtil) {
        this.scanner = scanner;
        this.inputUtil = inputUtil;
    }

    protected LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    protected String formatWeekday(Weekday day) {
        return switch (day) {
            case monday    -> "Понедельник";
            case tuesday   -> "Вторник";
            case wednesday -> "Среда";
            case thursday  -> "Четверг";
            case friday    -> "Пятница";
            case saturday  -> "Суббота";
            case sunday    -> "Воскресенье";
        };
    }

    protected Weekday parseWeekday(int choice) {
        return switch (choice) {
            case 1 -> Weekday.monday;
            case 2 -> Weekday.tuesday;
            case 3 -> Weekday.wednesday;
            case 4 -> Weekday.thursday;
            case 5 -> Weekday.friday;
            case 6 -> Weekday.saturday;
            default -> null;
        };
    }

    protected LocalDate askDate(String prompt) {
        System.out.print("\n" + prompt);
        LocalDate date = parseDate(scanner.next());
        if (date == null) {
            System.out.println("Неверный формат даты");
        }
        return date;
    }

}
