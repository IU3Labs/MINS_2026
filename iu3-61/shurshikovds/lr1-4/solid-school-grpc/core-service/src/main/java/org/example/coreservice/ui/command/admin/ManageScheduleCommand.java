package org.example.coreservice.ui.command.admin;

import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.solidschool.ui.command.admin.schedule.ScheduleAdminCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ManageScheduleCommand extends BaseConsoleUI implements AdminCommand {

    private final Map<Integer, ScheduleAdminCommand> commands;

    public ManageScheduleCommand(List<ScheduleAdminCommand> commands,
                                 Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.commands = commands.stream()
                .collect(Collectors.toMap(ScheduleAdminCommand::key, c -> c));
    }

    @Override public int key() { return 1; }
    @Override public String label() { return "Управление расписанием"; }

    @Override
    public void execute() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Расписание ===");
            commands.values().forEach(c -> System.out.println(c.key() + ". " + c.label()));
            System.out.println("0. Назад");

            int choice = inputUtil.readInt("Выбор: ");
            if (choice == 0) { running = false; continue; }

            ScheduleAdminCommand cmd = commands.get(choice);
            if (cmd != null) cmd.execute();
            else System.out.println("Неверный выбор");
        }
    }
}

