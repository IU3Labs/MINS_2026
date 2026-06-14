package org.example.coreservice.ui.elements;

import org.example.coreservice.entity.user.TeacherUser;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.command.teacher.TeacherCommand;
import org.example.reference.grpc.TeacherResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TeacherConsoleUI extends BaseConsoleUI {

    private final Map<Integer, TeacherCommand> commands;
    private final ReferenceServiceGateway referenceGateway;

    public TeacherConsoleUI(List<TeacherCommand> commands,
                            ReferenceServiceGateway referenceGateway,
                            Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.referenceGateway = referenceGateway;
        this.commands = commands.stream()
                .collect(Collectors.toMap(TeacherCommand::key, c -> c));
    }

    public void showMenu(TeacherUser teacherUser) {
        String traceId = UUID.randomUUID().toString();

        TeacherResponse teacher = referenceGateway.getTeacher(traceId, teacherUser.getTeacherId());

        boolean running = true;
        while (running) {
            System.out.println("\n=== Добрый день, " + teacher.getFirstName() + " ===");
            commands.values().forEach(c -> System.out.println(c.key() + ". " + c.label()));
            System.out.println("0. Выход");

            int choice = inputUtil.readInt("Выбор: ");
            if (choice == 0) { running = false; continue; }

            TeacherCommand cmd = commands.get(choice);
            if (cmd != null) cmd.execute(teacherUser);
            else System.out.println("Неверный выбор");
        }
    }
}
