package org.example.coreservice.ui.command.admin;

import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.command.admin.teacher.TeacherAdminCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ManageTeachersCommand extends BaseConsoleUI implements AdminCommand {

    private final Map<Integer, TeacherAdminCommand> commands;

    public ManageTeachersCommand(List<TeacherAdminCommand> commands,
                                 Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.commands = commands.stream()
                .collect(Collectors.toMap(TeacherAdminCommand::key, c -> c));
    }

    @Override public int key() { return 3; }
    @Override public String label() { return "Управление учителями"; }

    @Override
    public void execute() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Учителя ===");
            commands.values().forEach(c -> System.out.println(c.key() + ". " + c.label()));
            System.out.println("0. Назад");

            int choice = inputUtil.readInt("Выбор: ");
            if (choice == 0) { running = false; continue; }

            TeacherAdminCommand cmd = commands.get(choice);
            if (cmd != null) cmd.execute();
            else System.out.println("Неверный выбор");
        }
    }
}

