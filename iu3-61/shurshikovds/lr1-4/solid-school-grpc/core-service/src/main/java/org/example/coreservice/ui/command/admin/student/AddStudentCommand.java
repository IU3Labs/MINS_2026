package org.example.coreservice.ui.command.admin.student;

import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.SchoolClassSelector;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

@Component
public class AddStudentCommand extends BaseConsoleUI implements StudentAdminCommand {

    private final ReferenceServiceGateway referenceGateway;
    private final SchoolClassSelector schoolClassSelector;

    public AddStudentCommand(SchoolClassSelector schoolClassSelector,
                             Scanner scanner,
                             InputUtil inputUtil,
                             ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.referenceGateway = referenceGateway;
        this.schoolClassSelector = schoolClassSelector;
    }

    @Override public int key() { return 1; }
    @Override public String label() { return "Добавить ученика"; }

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

        LocalDate birthDate = askDate("Дата рождения (дд.мм.гггг): ");
        if (birthDate == null) return;

        Long schoolClassId = schoolClassSelector.ask(traceId);
        if (schoolClassId == null) return;

        try {
            referenceGateway.createStudent(
                    traceId, lastName, firstName, middleName, birthDate, schoolClassId
            );
            System.out.println("Ученик добавлен");
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

