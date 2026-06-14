package org.example.coreservice.ui.command.admin.teacher;

import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

@Component
public class AddTeacherCommand extends BaseConsoleUI implements TeacherAdminCommand {

    private final ReferenceServiceGateway referenceGateway;

    public AddTeacherCommand(
                             Scanner scanner,
                             InputUtil inputUtil,
                             ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.referenceGateway = referenceGateway;
    }

    @Override public int key() { return 1; }
    @Override public String label() { return "Добавить учителя"; }

    @Override
    public void execute() {
        String traceId = UUID.randomUUID().toString();

        scanner.nextLine();
        System.out.print("\nФамилия: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Имя: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Отчество: ");
        String middleName = scanner.nextLine().trim();

        System.out.print("Специальность: ");
        String specialty = scanner.nextLine().trim();

        try {
            referenceGateway.createTeacher(
                    traceId, lastName, firstName, middleName, specialty
            );
            System.out.println("Учитель добавлен");
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

