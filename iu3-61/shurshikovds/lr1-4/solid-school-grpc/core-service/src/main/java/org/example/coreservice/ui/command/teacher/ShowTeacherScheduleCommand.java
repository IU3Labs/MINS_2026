package org.example.coreservice.ui.command.teacher;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.entity.user.TeacherUser;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.service.ScheduleService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.factory.ScheduleSortStrategyFactory;
import org.example.coreservice.ui.formatter.TeacherRowFormatter;
import org.example.coreservice.ui.strategy.ScheduleSortStrategy;
import org.example.reference.grpc.SchoolClassDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShowTeacherScheduleCommand extends BaseConsoleUI implements TeacherCommand {

    private final ScheduleService scheduleService;
    private final ScheduleSortStrategyFactory sortFactory;
    private final TeacherRowFormatter teacherRowFormatter;
    private final ReferenceServiceGateway referenceGateway;

    public ShowTeacherScheduleCommand(ScheduleService scheduleService,
                                      Scanner scanner, InputUtil inputUtil,
                                      ScheduleSortStrategyFactory sortFactory,
                                      TeacherRowFormatter teacherRowFormatter,
                                      ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.scheduleService = scheduleService;
        this.sortFactory = sortFactory;
        this.teacherRowFormatter = teacherRowFormatter;
        this.referenceGateway = referenceGateway;
    }

    @Override public int key() { return 1; }
    @Override public String label() { return "Моё расписание"; }

    @Override
    public void execute(TeacherUser teacherUser) {
        String traceId = UUID.randomUUID().toString();

        List<ScheduleEntry> entries = scheduleService.getTeacherSchedule(teacherUser.getTeacherId());
        if (entries.isEmpty()) {
            System.out.println("Расписание не найдено");
            return;
        }

        Map<Long, String> classNames = buildClassNameMap(traceId, entries);

        ScheduleSortStrategy strategy = sortFactory.ask(inputUtil);
        String formatted = strategy.format(entries, teacherRowFormatter, Map.of(), classNames);
        System.out.println(formatted);
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
