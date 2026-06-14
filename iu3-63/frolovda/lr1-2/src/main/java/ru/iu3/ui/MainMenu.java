package ru.iu3.ui;

import java.util.List;
import java.util.Scanner;

import ru.iu3.ui.interfaces.MenuItem;

public class MainMenu {
    private Scanner scanner;
    private OutputUI display;
    private  List<MenuItem> items;

    public MainMenu(Scanner scanner, OutputUI display, List<MenuItem> items) {
        this.scanner = scanner;
        this.display = display;
        this.items = items;
    }

    public void run() {
        boolean running = true;
        while (running) {
            display.showWelcome();
            display.showOptions(items);
            try {
                display.showPrompt();
                int choice = Integer.parseInt(scanner.nextLine());
                MenuItem selected = findItem(choice);
                if (selected != null) {
                    running = selected.execute();
                } else {
                    display.showInvalidChoice();
                }
            } catch (Exception e) {
                display.showError(e.getMessage());
            }
        }
    }

    private MenuItem findItem(int key) {
        for (MenuItem item : items) {
            if (item.getKey() == key) {
                return item;
            }
        }
        return null;
    }
}
