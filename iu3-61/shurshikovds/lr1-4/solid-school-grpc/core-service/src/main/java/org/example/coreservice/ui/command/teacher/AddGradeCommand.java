package org.example.coreservice.ui.command.teacher;

import org.example.coreservice.entity.Lesson;
import org.example.coreservice.entity.user.TeacherUser;
import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.service.GradeService;
import org.example.coreservice.service.LessonService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.reference.grpc.SchoolClassDto;
import org.example.reference.grpc.StudentDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AddGradeCommand extends BaseConsoleUI implements TeacherCommand {

    private final LessonService lessonService;
    private final GradeService gradeService;
    private final ReferenceServiceGateway referenceServiceGateway;

    public AddGradeCommand(LessonService lessonService,
                           GradeService gradeService,
                           Scanner scanner,
                           InputUtil inputUtil, ReferenceServiceGateway referenceServiceGateway) {
        super(scanner, inputUtil);
        this.lessonService = lessonService;
        this.gradeService = gradeService;
        this.referenceServiceGateway = referenceServiceGateway;
    }

    @Override public int key() { return 3; }
    @Override public String label() { return "Поставить оценку"; }

    @Override
    public void execute(TeacherUser teacherUser) {
        String traceId = UUID.randomUUID().toString();

        LocalDate date = askLessonDate();
        if (date == null) return;

        List<Lesson> lessons = lessonService.getTeacherLessonsForDate(teacherUser.getTeacherId(), date);
        if (lessons.isEmpty()) {
            System.out.println("Проведённых уроков за эту дату не найдено");
            return;
        }

        Map<Long, String> classNames = buildClassNameMap(traceId, lessons);
        printLessonsForChoice(lessons, classNames);

        Lesson lesson = chooseLesson(lessons);
        if (lesson == null) return;

        List<StudentDto> students = referenceServiceGateway.listStudentsByClass(traceId, lesson.getSchoolClassId());
        if (students.isEmpty()) {
            System.out.println("В классе нет учеников");
            return;
        }

        printStudentsForChoice(students);

        int index = inputUtil.readInt("Выберите ученика (номер): ") - 1;
        if (index < 0 || index >= students.size()) {
            System.out.println("Неверный номер");
            return;
        }

        StudentDto student = students.get(index);

        Short value = askGradeValue();
        if (value == null) return;

        try {
            gradeService.addGrade(traceId, student.getId(), lesson.getId(), value);
            System.out.println("Оценка " + value + " поставлена — "
                               + student.getLastName() + " " + student.getFirstName());
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private LocalDate askLessonDate() {
        System.out.print("\nДата урока (дд.мм.гггг): ");
        LocalDate date = parseDate(scanner.next());
        if (date == null) {
            System.out.println("Неверный формат даты");
        }
        return date;
    }

    private void printLessonsForChoice(List<Lesson> lessons, Map<Long, String> classNames) {
        System.out.printf("%n %-5s %-25s %-10s%n", "№", "Предмет", "Класс");
        System.out.println(" " + "-".repeat(42));
        for (int i = 0; i < lessons.size(); i++) {
            Lesson l = lessons.get(i);
            System.out.printf(" %-5d %-25s %-10s%n",
                    i + 1,
                    l.getTitle(),
                    classNames.getOrDefault(l.getSchoolClassId(), "?"));
        }
    }

    private Lesson chooseLesson(List<Lesson> lessons) {
        int index = inputUtil.readInt("Выберите урок (номер): ") - 1;
        if (index < 0 || index >= lessons.size()) {
            System.out.println("Неверный номер");
            return null;
        }
        return lessons.get(index);
    }

    private void printStudentsForChoice(List<StudentDto> students) {
        System.out.printf("%n %-5s %-30s%n", "№", "ФИО");
        System.out.println(" " + "-".repeat(35));
        for (int i = 0; i < students.size(); i++) {
            StudentDto s = students.get(i);
            System.out.printf(" %-5d %-30s%n",
                    i + 1,
                    s.getLastName() + " " + s.getFirstName() + " " + s.getMiddleName());
        }
    }

    private Short askGradeValue() {
        System.out.print("Оценка (1-5): ");
        short val = scanner.nextShort();
        if (val < 1 || val > 5) {
            System.out.println("Оценка должна быть от 1 до 5");
            return null;
        }
        return val;
    }

    private Map<Long, String> buildClassNameMap(String traceId, List<Lesson> lessons) {
        Set<Long> ids = lessons.stream()
                .map(Lesson::getSchoolClassId)
                .collect(Collectors.toSet());

        return referenceServiceGateway.listSchoolClasses(traceId).stream()
                .filter(c -> ids.contains(c.getId()))
                .collect(Collectors.toMap(
                        SchoolClassDto::getId,
                        c -> c.getGrade() + c.getLetter()
                ));
    }
}

