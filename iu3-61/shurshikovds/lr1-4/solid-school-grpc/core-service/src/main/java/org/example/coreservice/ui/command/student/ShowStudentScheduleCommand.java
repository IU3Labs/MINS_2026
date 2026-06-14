package org.example.coreservice.ui.command.student;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.entity.user.StudentUser;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.service.ScheduleService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.ScheduleExporter;
import org.example.coreservice.ui.factory.ScheduleSortStrategyFactory;
import org.example.coreservice.ui.formatter.StudentRowFormatter;
import org.example.coreservice.ui.strategy.ScheduleSortStrategy;
import org.example.reference.grpc.StudentResponse;
import org.example.reference.grpc.TeacherDto;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShowStudentScheduleCommand extends BaseConsoleUI implements StudentCommand {

    private final ScheduleService scheduleService;
    private final ReferenceServiceGateway referenceGateway;
    private final ScheduleSortStrategyFactory scheduleSortStrategyFactory;
    private final StudentRowFormatter studentRowFormatter;
    private final ScheduleExporter scheduleExporter;

    public ShowStudentScheduleCommand(ScheduleService scheduleService,
                                      ReferenceServiceGateway referenceGateway,
                                      Scanner scanner, InputUtil inputUtil,
                                      ScheduleSortStrategyFactory scheduleSortStrategyFactory,
                                      StudentRowFormatter studentRowFormatter,
                                      ScheduleExporter scheduleExporter) {
        super(scanner, inputUtil);
        this.scheduleService = scheduleService;
        this.referenceGateway = referenceGateway;
        this.scheduleSortStrategyFactory = scheduleSortStrategyFactory;
        this.studentRowFormatter = studentRowFormatter;
        this.scheduleExporter = scheduleExporter;
    }

    @Override
    public int key() {
        return 2;
    }

    @Override
    public String label() {
        return "Расписание";
    }

    @Override
    public void execute(StudentUser studentUser) {
        String traceId = UUID.randomUUID().toString();

        StudentResponse student = referenceGateway.getStudent(traceId, studentUser.getStudentId());
        List<ScheduleEntry> schedule = scheduleService.getClassSchedule(student.getSchoolClassId());

        if (schedule.isEmpty()) {
            System.out.println("Расписание не найдено");
            return;
        }

        Map<Long, String> teacherNames = buildTeacherNameMap(traceId, schedule);

        ScheduleSortStrategy strategy = scheduleSortStrategyFactory.ask(inputUtil);
        String formattedSchedule = strategy.format(schedule, studentRowFormatter, teacherNames, Map.of());

        System.out.println("\nВыберите действие:");
        System.out.println("1. Показать в консоли");
        System.out.println("2. Экспортировать в файл");
        int action = inputUtil.readInt("Введите 1 или 2: ");

        if (action == 1) System.out.println(formattedSchedule);
        else scheduleExporter.format(formattedSchedule);
    }

    private Map<Long, String> buildTeacherNameMap(String traceId, List<ScheduleEntry> entries) {
        Set<Long> ids = entries.stream()
                .map(ScheduleEntry::getTeacherId)
                .collect(Collectors.toSet());

        return referenceGateway.listTeachers(traceId).stream()
                .filter(t -> ids.contains(t.getId()))
                .collect(Collectors.toMap(
                        TeacherDto::getId,
                        t -> t.getLastName() + " "
                             + t.getFirstName().charAt(0) + "."
                             + t.getMiddleName().charAt(0) + "."
                ));
    }
}
