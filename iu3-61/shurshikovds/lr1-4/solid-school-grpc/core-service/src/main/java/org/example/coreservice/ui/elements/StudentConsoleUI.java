package org.example.coreservice.ui.elements;

import org.example.coreservice.entity.user.StudentUser;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.command.student.StudentCommand;
import org.example.reference.grpc.StudentResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class StudentConsoleUI extends BaseConsoleUI {

    private final Map<Integer, StudentCommand> commands;
    private final ReferenceServiceGateway referenceGateway;

    public StudentConsoleUI(List<StudentCommand> commands,
                            Scanner scanner, InputUtil inputUtil, ReferenceServiceGateway referenceServiceGateway) {
        super(scanner, inputUtil);
        this.commands = commands.stream()
                .collect(Collectors.toMap(StudentCommand::key, c -> c));
        this.referenceGateway = referenceServiceGateway;
    }

    public void showMenu(StudentUser studentUser) {
        String traceId = UUID.randomUUID().toString();

        StudentResponse student = referenceGateway.getStudent(traceId, studentUser.getStudentId());

        boolean running = true;
        while (running) {
            System.out.println("\n=== Добрый день, " + student.getFirstName() + " ===");
            System.out.println("Вы учитесь в " + student.getSchoolClassName() + " классе.");
            commands.values().forEach(c -> System.out.println(c.key() + ". " + c.label()));
            System.out.println("0. Выход");

            int choice = inputUtil.readInt("Выбор: ");
            if (choice == 0) { running = false; continue; }

            StudentCommand cmd = commands.get(choice);
            if (cmd != null) cmd.execute(studentUser);
            else System.out.println("Неверный выбор");
        }
    }
}
