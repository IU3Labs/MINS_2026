package org.example.coreservice.ui.command.admin.teacher;

import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.TeachersPrinter;
import org.example.reference.grpc.OperationStatusResponse;
import org.example.reference.grpc.TeacherDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class DeleteTeacherCommand extends BaseConsoleUI implements TeacherAdminCommand {

    private final ReferenceServiceGateway referenceGateway;
    private final TeachersPrinter teachersPrinter;

    public DeleteTeacherCommand(
                                TeachersPrinter teachersPrinter,
                                Scanner scanner,
                                InputUtil inputUtil,
                                ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.referenceGateway = referenceGateway;
        this.teachersPrinter = teachersPrinter;
    }

    @Override public int key() { return 2; }
    @Override public String label() { return "Удалить учителя"; }

    @Override
    public void execute() {
        String traceId = UUID.randomUUID().toString();

        List<TeacherDto> teachers = referenceGateway.listTeachers(traceId);
        if (teachers.isEmpty()) {
            System.out.println("Учителей нет");
            return;
        }

        printTeachers(teachers);

        int index = inputUtil.readInt("Выберите учителя (номер): ") - 1;
        if (index < 0 || index >= teachers.size()) {
            System.out.println("Неверный номер");
            return;
        }

        TeacherDto teacher = teachers.get(index);

        try {
            OperationStatusResponse response = referenceGateway.deleteTeacher(traceId, teacher.getId());
            if (response.getSuccess()) System.out.println("Учитель удалён");
            else System.out.println("Ошибка: " + response.getMessage());
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printTeachers(List<TeacherDto> teachers) {
        System.out.printf("%n %-5s %-35s %-20s%n", "№", "ФИО", "Специальность");
        System.out.println(" " + "-".repeat(64));

        for (int i = 0; i < teachers.size(); i++) {
            TeacherDto t = teachers.get(i);
            System.out.printf(" %-5d %-35s %-20s%n",
                    i + 1,
                    t.getLastName() + " " + t.getFirstName() + " " + t.getMiddleName(),
                    t.getSpecialty());
        }
    }
}
