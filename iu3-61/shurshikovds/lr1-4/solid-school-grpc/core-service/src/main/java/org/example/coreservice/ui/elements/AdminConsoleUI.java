package org.example.coreservice.ui.elements;


import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.command.admin.AdminCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class AdminConsoleUI extends BaseConsoleUI {

    private final Map<Integer, AdminCommand> commands;

    public AdminConsoleUI(List<AdminCommand> commands,
                          Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.commands = commands.stream()
                .collect(Collectors.toMap(AdminCommand::key, c -> c));
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Меню администратора ===");
            commands.values().forEach(c -> System.out.println(c.key() + ". " + c.label()));
            System.out.println("0. Выход");

            int choice = inputUtil.readInt("Выбор: ");
            if (choice == 0) { running = false; continue; }

            AdminCommand cmd = commands.get(choice);
            if (cmd != null) cmd.execute();
            else System.out.println("Неверный выбор");
        }
    }
}

