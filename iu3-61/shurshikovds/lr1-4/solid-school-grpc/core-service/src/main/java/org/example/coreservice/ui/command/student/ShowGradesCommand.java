package org.example.coreservice.ui.command.student;


import org.example.coreservice.entity.Grade;
import org.example.coreservice.entity.user.StudentUser;
import org.example.coreservice.service.GradeService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ShowGradesCommand extends BaseConsoleUI implements StudentCommand {

    private final GradeService gradeService;

    public ShowGradesCommand(GradeService gradeService,
                             Scanner scanner, InputUtil inputUtil) {
        super(scanner, inputUtil);
        this.gradeService = gradeService;
    }

    @Override public int key() { return 1; }
    @Override public String label() { return "Мои оценки"; }

    @Override
    public void execute(StudentUser student) {
        List<Grade> grades = gradeService.getStudentGrades(student.getStudentId());

        if (grades.isEmpty()) {
            System.out.println("Оценок пока нет");
            return;
        }

        printGradesTable(grades);
    }

    private void printGradesTable(List<Grade> grades) {
        System.out.println("\n=== Мои оценки ===");
        System.out.printf("%-25s %-12s %-7s%n", "Предмет", "Дата", "Оценка");
        System.out.println("-".repeat(46));

        grades.forEach(g -> System.out.printf(
                "%-25s %-12s %-7d%n",
                g.getLesson().getTitle(),
                g.getLesson().getLessonDate(),
                g.getGrade()
        ));
    }
}

