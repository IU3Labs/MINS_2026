package org.example.coreservice.ui.command.admin.student;

import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.SchoolClassSelector;
import org.example.reference.grpc.OperationStatusResponse;
import org.example.reference.grpc.StudentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class DeleteStudentCommand extends BaseConsoleUI implements StudentAdminCommand {

    private final ReferenceServiceGateway referenceGateway;
    private final SchoolClassSelector schoolClassSelector;

    public DeleteStudentCommand(
                                SchoolClassSelector schoolClassSelector,
                                Scanner scanner,
                                InputUtil inputUtil,
                                ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.referenceGateway = referenceGateway;
        this.schoolClassSelector = schoolClassSelector;
    }

    @Override public int key() { return 2; }
    @Override public String label() { return "Удалить ученика"; }

    @Override
    public void execute() {
        String traceId = UUID.randomUUID().toString();

        List<StudentDto> students = referenceGateway.listStudents(traceId);
        if (students.isEmpty()) {
            System.out.println("Учеников нет");
            return;
        }

        printStudents(students);

        int index = inputUtil.readInt("Выберите ученика (номер): ") - 1;
        if (index < 0 || index >= students.size()) {
            System.out.println("Неверный номер");
            return;
        }

        StudentDto student = students.get(index);

        try {
            OperationStatusResponse response = referenceGateway.deleteStudent(traceId, student.getId());
            if (response.getSuccess()) System.out.println("Ученик удалён");
            else System.out.println("Ошибка: " + response.getMessage());
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printStudents(List<StudentDto> students) {
        System.out.printf("%n %-5s %-30s%n", "№", "ФИО");
        System.out.println(" " + "-".repeat(37));
        for (int i = 0; i < students.size(); i++) {
            StudentDto s = students.get(i);
            System.out.printf(" %-5d %-30s%n",
                    i + 1,
                    s.getLastName() + " " + s.getFirstName() + " " + s.getMiddleName());
        }
    }
}

