package ru.laskan0.travelagency.util;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readRequiredLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("Поле не может быть пустым. Повторите ввод.");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readRequiredLine(prompt));
            } catch (NumberFormatException exception) {
                System.out.println("Нужно ввести целое число.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readRequiredLine(prompt).replace(',', '.'));
            } catch (NumberFormatException exception) {
                System.out.println("Нужно ввести число.");
            }
        }
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            String answer = readRequiredLine(prompt + " (y/n): ").toLowerCase();
            if (answer.equals("y") || answer.equals("yes") || answer.equals("д") || answer.equals("да")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no") || answer.equals("н") || answer.equals("нет")) {
                return false;
            }
            System.out.println("Введите y/n.");
        }
    }
}
