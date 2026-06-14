package org.example.coreservice.ui.command.admin.schedule;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.service.ScheduleService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.SchoolClassSelector;
import org.example.coreservice.ui.formatter.AdminRowFormatter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class DeleteScheduleEntryCommand extends BaseConsoleUI implements org.example.solidschool.ui.command.admin.schedule.ScheduleAdminCommand {

    private final ScheduleService scheduleService;
    private final SchoolClassSelector schoolClassSelector;
    private final AdminRowFormatter rowFormatter;

    public DeleteScheduleEntryCommand(ScheduleService scheduleService,
                                      SchoolClassSelector schoolClassSelector,
                                      Scanner scanner, InputUtil inputUtil, AdminRowFormatter rowFormatter) {
        super(scanner, inputUtil);
        this.scheduleService = scheduleService;
        this.schoolClassSelector = schoolClassSelector;
        this.rowFormatter = rowFormatter;
    }

    @Override public int key() { return 2; }
    @Override public String label() { return "Удалить урок"; }

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

        printEntries(entries);

        int index = inputUtil.readInt("Выберите урок для удаления (номер): ") - 1;
        if (index < 0 || index >= entries.size()) {
            System.out.println("Неверный номер");
            return;
        }

        ScheduleEntry entry = entries.get(index);

        try {
            scheduleService.deleteScheduleEntry(entry.getId());
            System.out.println("Урок удалён");
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printEntries(List<ScheduleEntry> entries) {
        System.out.printf("%n %-5s %-12s %-10s %-25s %-10s%n",
                "№", "День", "Время", "Предмет", "Кабинет");
        System.out.println(" " + "-".repeat(68));

        for (int i = 0; i < entries.size(); i++) {
            ScheduleEntry e = entries.get(i);
            System.out.printf(" %-5d %-12s %-10s %-25s %-10s%n",
                    i + 1,
                    formatWeekday(e.getDayOfWeek()),
                    e.getStartTime(),
                    e.getTitle(),
                    e.getRoom());
        }
    }
}

