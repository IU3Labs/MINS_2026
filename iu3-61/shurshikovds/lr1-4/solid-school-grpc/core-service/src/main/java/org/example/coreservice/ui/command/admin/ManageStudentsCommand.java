package org.example.coreservice.ui.command.admin;

import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.command.admin.student.StudentAdminCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ManageStudentsCommand extends BaseConsoleUI implements AdminCommand {

    private final Map<Integer, StudentAdminCommand> commands;

    public ManageStudentsCommand(List<StudentAdminCommand> commands,
                                 Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.commands = commands.stream()
                .collect(Collectors.toMap(StudentAdminCommand::key, c -> c));
    }

    @Override public int key() { return 2; }
    @Override public String label() { return "Управление учениками"; }

    @Override
    public void execute() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Ученики ===");
            commands.values().forEach(c -> System.out.println(c.key() + ". " + c.label()));
            System.out.println("0. Назад");

            int choice = inputUtil.readInt("Выбор: ");
            if (choice == 0) { running = false; continue; }

            StudentAdminCommand cmd = commands.get(choice);
            if (cmd != null) cmd.execute();
            else System.out.println("Неверный выбор");
        }
    }
}

