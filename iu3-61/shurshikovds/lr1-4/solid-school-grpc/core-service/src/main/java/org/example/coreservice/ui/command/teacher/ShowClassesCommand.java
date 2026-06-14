package org.example.coreservice.ui.command.teacher;

import org.example.coreservice.entity.Grade;
import org.example.coreservice.entity.user.TeacherUser;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.service.GradeService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.SchoolClassSelector;
import org.example.reference.grpc.SchoolClassDto;
import org.example.reference.grpc.StudentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class ShowClassesCommand extends BaseConsoleUI implements TeacherCommand {

    private final SchoolClassSelector schoolClassSelector;
    private final GradeService gradeService;
    private final ReferenceServiceGateway referenceGateway;

    public ShowClassesCommand(SchoolClassSelector schoolClassSelector,
                              GradeService gradeService,
                              Scanner scanner, InputUtil inputUtil,
                              ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.schoolClassSelector = schoolClassSelector;
        this.gradeService = gradeService;
        this.referenceGateway = referenceGateway;
    }

    @Override public int key() { return 2; }
    @Override public String label() { return "Состав классов"; }

    @Override
    public void execute(TeacherUser teacherUser) {
        String traceId = UUID.randomUUID().toString();

        Long schoolClassId = schoolClassSelector.ask(traceId);
        if (schoolClassId == null) return;

        SchoolClassDto schoolClass = referenceGateway.listSchoolClasses(traceId).stream()
                .filter(c -> c.getId() == schoolClassId)
                .findFirst()
                .orElse(null);
        if (schoolClass == null) {
            System.out.println("Класс не найден");
            return;
        }

        List<StudentDto> students = referenceGateway.listStudentsByClass(traceId, schoolClassId);
        printClassStudents(schoolClass, students);
        if (students.isEmpty()) return;

        int index = inputUtil.readInt("Выберите ученика (номер): ") - 1;
        if (index < 0 || index >= students.size()) {
            System.out.println("Неверный номер");
            return;
        }

        StudentDto student = students.get(index);
        showStudentGrades(student);
    }

    private void printClassStudents(SchoolClassDto c, List<StudentDto> students) {
        System.out.println("\n=== Класс " + c.getGrade() + c.getLetter() + " ===");

        if (students.isEmpty()) {
            System.out.println("Учеников нет");
            return;
        }

        System.out.printf(" %-5s %-30s%n", "№", "ФИО");
        System.out.println(" " + "-".repeat(37));

        for (int i = 0; i < students.size(); i++) {
            StudentDto s = students.get(i);
            System.out.printf(" %-5d %-30s%n",
                    i + 1,
                    s.getLastName() + " " + s.getFirstName() + " " + s.getMiddleName());
        }
    }

    private void showStudentGrades(StudentDto student) {
        List<Grade> grades = gradeService.getStudentGrades(student.getId());

        System.out.println("\n=== Табель: "
                           + student.getLastName() + " "
                           + student.getFirstName() + " "
                           + student.getMiddleName() + " ===");

        if (grades.isEmpty()) {
            System.out.println("Оценок нет");
            return;
        }

        System.out.printf(" %-15s %-25s %-8s%n", "Дата", "Предмет", "Оценка");
        System.out.println(" " + "-".repeat(50));

        grades.forEach(g -> System.out.printf(
                " %-15s %-25s %-8d%n",
                g.getLesson().getLessonDate(),
                g.getLesson().getTitle(),
                g.getGrade()
        ));

        gradeService.getAverageGrade(student.getId())
                .ifPresent(avg -> System.out.printf("%n Средняя оценка: %.2f%n", avg));
    }
}

