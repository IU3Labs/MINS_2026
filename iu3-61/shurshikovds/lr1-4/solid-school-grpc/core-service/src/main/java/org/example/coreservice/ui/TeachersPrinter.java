package org.example.coreservice.ui;

import org.example.reference.grpc.TeacherDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class TeachersPrinter extends BaseConsoleUI{


    public TeachersPrinter(Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
    }

    public void printTeachersForChoice(List<TeacherDto> teachers) {
        System.out.printf("%n  %-5s %-30s %-20s%n", "№", "ФИО", "Специальность");
        System.out.println("  " + "-".repeat(57));

        for (int i = 0; i < teachers.size(); i++) {
            TeacherDto t = teachers.get(i);
            System.out.printf("  %-5d %-30s %-20s%n",
                    i + 1,
                    t.getLastName() + " " + t.getFirstName() + " " + t.getMiddleName(),
                    t.getSpecialty()
            );
        }
    }
}
