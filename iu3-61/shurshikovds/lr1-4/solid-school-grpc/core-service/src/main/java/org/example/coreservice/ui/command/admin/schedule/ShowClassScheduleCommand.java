package org.example.coreservice.ui.command.admin.schedule;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.service.ScheduleService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.SchoolClassSelector;
import org.example.coreservice.ui.factory.ScheduleSortStrategyFactory;
import org.example.coreservice.ui.formatter.AdminRowFormatter;
import org.example.coreservice.ui.strategy.ScheduleSortStrategy;
import org.example.reference.grpc.TeacherDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShowClassScheduleCommand extends BaseConsoleUI implements org.example.solidschool.ui.command.admin.schedule.ScheduleAdminCommand {

    private final ScheduleService scheduleService;
    private final SchoolClassSelector schoolClassSelector;
    private final ScheduleSortStrategyFactory scheduleSortStrategyFactory;
    private final AdminRowFormatter rowFormatter;
    private final ReferenceServiceGateway referenceGateway;

    public ShowClassScheduleCommand(ScheduleService scheduleService,
                                    SchoolClassSelector schoolClassSelector,
                                    Scanner scanner,
                                    InputUtil inputUtil,
                                    ScheduleSortStrategyFactory scheduleSortStrategyFactory,
                                    AdminRowFormatter rowFormatter,
                                    ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.scheduleService = scheduleService;
        this.schoolClassSelector = schoolClassSelector;
        this.scheduleSortStrategyFactory = scheduleSortStrategyFactory;
        this.rowFormatter = rowFormatter;
        this.referenceGateway = referenceGateway;
    }

    @Override public int key() { return 3; }
    @Override public String label() { return "Просмотреть расписание класса"; }

    @Override
    public void execute() {
        String traceId = UUID.randomUUID().toString();

        Long schoolClassId = schoolClassSelector.ask(traceId);
        if (schoolClassId == null) return;

        List<ScheduleEntry> entries = scheduleService.getClassSchedule(schoolClassId);
        if (entries.isEmpty()) {
            System.out.println("Уроков нет");
            return;
        }

        Map<Long, String> teacherNames = buildTeacherNameMap(traceId, entries);

        ScheduleSortStrategy strategy = scheduleSortStrategyFactory.ask(inputUtil);
        String formatted = strategy.format(entries, rowFormatter, teacherNames, Map.of());
        System.out.println(formatted);
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

