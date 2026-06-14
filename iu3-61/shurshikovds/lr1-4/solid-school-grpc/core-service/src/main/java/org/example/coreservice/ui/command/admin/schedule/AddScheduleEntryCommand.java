package org.example.coreservice.ui.command.admin.schedule;

import org.example.coreservice.entity.enums.Weekday;
import org.example.coreservice.exception.SchoolException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.service.ScheduleService;
import org.example.coreservice.ui.BaseConsoleUI;
import org.example.coreservice.ui.InputUtil;
import org.example.coreservice.ui.SchoolClassSelector;
import org.example.coreservice.ui.TeachersPrinter;
import org.example.reference.grpc.TeacherDto;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class AddScheduleEntryCommand extends BaseConsoleUI implements org.example.solidschool.ui.command.admin.schedule.ScheduleAdminCommand {

    private final ScheduleService scheduleService;
    private final TeachersPrinter teachersPrinter;
    private final SchoolClassSelector schoolClassSelector;
    public final ReferenceServiceGateway referenceGateway;

    public AddScheduleEntryCommand(ScheduleService scheduleService,
                                   TeachersPrinter teachersPrinter,
                                   SchoolClassSelector schoolClassSelector,
                                   Scanner scanner, InputUtil inputUtil,
                                   ReferenceServiceGateway referenceGateway) {
        super(scanner, inputUtil);
        this.scheduleService = scheduleService;
        this.teachersPrinter = teachersPrinter;
        this.schoolClassSelector = schoolClassSelector;
        this.referenceGateway = referenceGateway;
    }

    @Override public int key() { return 1; }
    @Override public String label() { return "Добавить урок"; }

    @Override
    public void execute() {
        String traceId = UUID.randomUUID().toString();

        System.out.print("\nНазвание предмета: ");
        scanner.nextLine();
        String title = scanner.nextLine().trim();

        Weekday day = askWeekday();
        if (day == null) return;

        System.out.print("Время начала (чч:мм): ");
        LocalTime startTime = parseTime(scanner.next());
        if (startTime == null) return;

        System.out.print("Кабинет: ");
        String room = scanner.next().trim();

        Long schoolClassId = schoolClassSelector.ask(traceId);
        if (schoolClassId == null) return;

        Long teacherId = askTeacher(traceId);
        if (teacherId == null) return;

        try {
            scheduleService.addScheduleEntry(
                    traceId, title, day, startTime, room, schoolClassId, teacherId
            );
            System.out.println("Урок добавлен в расписание");
        } catch (SchoolException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private Weekday askWeekday() {
        System.out.println("\n1. Понедельник 2. Вторник 3. Среда");
        System.out.println("4. Четверг    5. Пятница  6. Суббота");
        Weekday day = parseWeekday(inputUtil.readInt("Выбор: "));
        if (day == null) System.out.println("Неверный выбор");
        return day;
    }

    private Long askTeacher(String traceId) {
        List<TeacherDto> teachers = referenceGateway.listTeachers(traceId);
        if (teachers.isEmpty()) {
            System.out.println("Учителей нет");
            return null;
        }

        teachersPrinter.printTeachersForChoice(teachers);

        int index = inputUtil.readInt("Выберите учителя (номер): ") - 1;
        if (index < 0 || index >= teachers.size()) {
            System.out.println("Неверный номер");
            return null;
        }

        return teachers.get(index).getId();
    }

    private LocalTime parseTime(String input) {
        try {
            return LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат времени");
            return null;
        }
    }
}

