package org.example.coreservice.ui.command.teacher;

import org.example.coreservice.entity.Lesson;
import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.entity.user.TeacherUser;
import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.service.GradeService;
import org.example.coreservice.service.LessonService;
import org.example.coreservice.service.ScheduleService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.reference.grpc.SchoolClassDto;
import org.example.reference.grpc.StudentDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConductLessonCommand extends BaseConsoleUI implements TeacherCommand {

    private final ScheduleService scheduleService;
    private final LessonService lessonService;
    private final ReferenceServiceGateway referenceGateway;
    private final GradeService gradeService;

    public ConductLessonCommand(ScheduleService scheduleService,
                                LessonService lessonService,
                                Scanner scanner, InputUtil inputUtil,
                                ReferenceServiceGateway referenceGateway,
                                GradeService gradeService) {
        super(scanner, inputUtil);
        this.scheduleService = scheduleService;
        this.lessonService = lessonService;
        this.referenceGateway = referenceGateway;
        this.gradeService = gradeService;
    }

    @Override public int key() { return 4; }
    @Override public String label() { return "Провести урок"; }

    @Override
    public void execute(TeacherUser teacherUser) {
        String traceId = UUID.randomUUID().toString();

        List<ScheduleEntry> entries = scheduleService.getTeacherSchedule(teacherUser.getTeacherId());
        if (entries.isEmpty()) {
            System.out.println("Расписание не найдено");
            return;
        }

        Map<Long, String> classNames = buildClassNameMap(traceId, entries);
        printScheduleEntriesForChoice(entries, classNames);

        ScheduleEntry entry = chooseScheduleEntry(entries);
        if (entry == null) return;

        LocalDate date = askDate("Дата проведения урока (дд.мм.гггг): ");
        if (date == null) return;

        try {
            Lesson lesson = lessonService.createLessonFromScheduleEntry(traceId, entry, date);
            String className = classNames.getOrDefault(lesson.getSchoolClassId(), "?");
            System.out.println("Урок проведён: " + lesson.getTitle()
                               + " | " + lesson.getLessonDate()
                               + " | Класс " + className);
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printScheduleEntriesForChoice(List<ScheduleEntry> entries, Map<Long, String> classNames) {
        System.out.printf("%n %-5s %-10s %-20s %-25s %-10s%n",
                "№", "День", "Время", "Предмет", "Класс");
        System.out.println(" " + "-".repeat(72));

        for (int i = 0; i < entries.size(); i++) {
            ScheduleEntry e = entries.get(i);
            System.out.printf(" %-5d %-10s %-20s %-25s %-10s%n",
                    i + 1,
                    formatWeekday(e.getDayOfWeek()),
                    e.getStartTime(),
                    e.getTitle(),
                    classNames.getOrDefault(e.getSchoolClassId(), "?"));
        }
    }

    private ScheduleEntry chooseScheduleEntry(List<ScheduleEntry> entries) {
        int index = inputUtil.readInt("Выберите урок из расписания (номер): ") - 1;
        if (index < 0 || index >= entries.size()) {
            System.out.println("Неверный номер");
            return null;
        }
        return entries.get(index);
    }

    private Map<Long, String> buildClassNameMap(String traceId, List<ScheduleEntry> entries) {
        Set<Long> ids = entries.stream()
                .map(ScheduleEntry::getSchoolClassId)
                .collect(Collectors.toSet());

        return referenceGateway.listSchoolClasses(traceId).stream()
                .filter(c -> ids.contains(c.getId()))
                .collect(Collectors.toMap(
                        SchoolClassDto::getId,
                        c -> c.getGrade() + c.getLetter()
                ));
    }
}

