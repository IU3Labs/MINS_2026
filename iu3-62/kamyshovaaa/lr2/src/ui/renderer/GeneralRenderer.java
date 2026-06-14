package ui.renderer;

import java.util.List;

public class GeneralRenderer {

    public void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    public void printSubHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public void printMenu(List<MenuItem> items) {
        for (MenuItem item : items) {
            System.out.println(item.option() + ". " + item.label());
        }
    }

    public void printError(String message) {
        System.out.println("Ошибка: " + message);
    }

    public void printSuccess(String message) {
        System.out.println(message);
    }

    public void printNotFound(String entity) {
        System.out.println(entity + " не найдены");
    }

    public void printSeparator(int length) {
        System.out.println("-".repeat(length));
    }

    public record MenuItem(String option, String label) {}
}
