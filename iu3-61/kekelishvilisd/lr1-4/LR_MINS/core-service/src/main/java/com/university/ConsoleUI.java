package com.university;

import com.university.client.ReferenceServiceClient;
import com.university.exceptions.StudentNotFoundException;
import com.university.model.Lesson;
import com.university.proto.core.*;
import com.university.proto.reference.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class ConsoleUI {
    private final CoreServiceGrpc.CoreServiceBlockingStub stub;
    private final Scanner scanner;
    private final ReferenceServiceClient refClient;

    public ConsoleUI(String host, int port) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = CoreServiceGrpc.newBlockingStub(channel);
        this.refClient = new ReferenceServiceClient(channel);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Система Учебного Центра (gRPC Client) ===");
        while (true) {
            printMenu();
            int choice = getIntInput();

            String traceId = "ui-" + UUID.randomUUID().toString().substring(0, 8);

            try {
                switch (choice) {
                    case 1 -> registerStudent(traceId);
                    case 2 -> addGrade(traceId);
                    case 3 -> showReport(traceId);
                    case 4 -> markAttendance(traceId);
                    case 5 -> addLesson(traceId);
                    case 6 -> calculateDiscount(traceId);
                    case 7 -> { shutdown(); return; }
                    default -> System.out.println("️ Неверный выбор");
                }
            }catch (StatusRuntimeException e) {
                System.out.println("Ошибка связи с Core Service: " + e.getStatus().getDescription());
            } catch (Exception e) {
                System.out.println("Системная ошибка: " + e.getMessage());
            }
        }
    }

    private void registerStudent(String traceId) {
        System.out.print("Имя студента: ");
        String name = scanner.nextLine();

        RegisterStudentRequest request = RegisterStudentRequest.newBuilder()
                .setName(name)
                .setTraceId(traceId)
                .build();

        RegisterStudentResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .registerStudent(request);

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
        } else {
            System.out.println("Зарегистрирован: " + response.getName() + " (ID: " + response.getStudentId() + ")");
        }
    }

    private void addGrade(String traceId) {
        System.out.print("ID студента: ");
        int id = getIntInput();
        System.out.print("Предмет: ");
        String course = scanner.nextLine();
        System.out.print("Оценка (1-5): ");
        int val = getIntInput();

        AddGradeRequest request = AddGradeRequest.newBuilder()
                .setStudentId(id)
                .setCourseName(course)
                .setValue(val)
                .setTraceId(traceId)
                .build();

        AddGradeResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .addGrade(request);

        if (response.getSuccess()) {
            System.out.println("Оценка поставлена.");
        } else {
            System.out.println("Ошибка: " + response.getError());
        }
    }

    private void showReport(String traceId) {
        System.out.print("ID студента: ");
        int id = getIntInput();

        GetGradeReportRequest request = GetGradeReportRequest.newBuilder()
                .setStudentId(id)
                .setTraceId(traceId)
                .build();

        GetGradeReportResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .getGradeReport(request);

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
        } else {
            System.out.println("\nОтчёт:\n" + response.getReportText());
        }
    }

    private void markAttendance(String traceId) {
        System.out.print("ID студента: ");
        int studentId = getIntInput();
        System.out.print("ID занятия: ");
        int lessonId = getIntInput();
        System.out.print("Статус (PRESENT/ABSENT/LATE/EXCUSED): ");
        String status = scanner.nextLine().toUpperCase();

        MarkAttendanceRequest request = MarkAttendanceRequest.newBuilder()
                .setStudentId(studentId)
                .setLessonId(lessonId)
                .setStatus(status)
                .setTraceId(traceId)
                .build();

        MarkAttendanceResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .markAttendance(request);

        if (response.getSuccess()) {
            System.out.println("Посещаемость отмечена!");
        } else {
            System.out.println("Ошибка: " + response.getError());
        }
    }

    private void addLesson(String traceId) {
        System.out.print("Название курса: ");
        String courseName = scanner.nextLine();
        System.out.print("Преподаватель: ");
        String teacher = scanner.nextLine();
        System.out.print("Аудитория: ");
        String room = scanner.nextLine();
        System.out.print("Дата и время (напр. MON 10:00-11:30): ");
        String dateTime = scanner.nextLine();

        AddLessonResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .addLesson(
                        AddLessonRequest.newBuilder()
                                .setCourseName(courseName)
                                .setTeacher(teacher)
                                .setRoom(room)
                                .setDateTime(dateTime)
                                .setTraceId(traceId)
                                .build()
                );

        if (response.getLessonId() > 0) {
            System.out.println("Занятие добавлено! ID: " + response.getLessonId());
        } else {
            System.out.println("Ошибка: " + response.getError());
        }
    }

    private void showSchedule(String traceId) {
        System.out.print("День недели (MON/TUE/WED/THU/FRI) или ALL: ");
        String day = scanner.nextLine();

        ReferenceScheduleResponse response = refClient.getSchedule(day.toUpperCase(), traceId);

        if (!response.getError().isEmpty()) {
            System.out.println(" Ошибка: " + response.getError());
            return;
        }

        System.out.println("\n=== Расписание ===");
        if (response.getLessonsList().isEmpty()) {
            System.out.println("Расписание пусто");
            return;
        }

        for (ReferenceLessonData lesson : response.getLessonsList()) {
            System.out.printf("[%d] %s | %s | %s | %s%n",
                    lesson.getLessonId(),
                    lesson.getCourseName(),
                    lesson.getTeacher(),
                    lesson.getRoom(),
                    lesson.getDateTime());
        }
    }

    private void calculateDiscount(String traceId) {
        System.out.print("ID студента: ");
        int studentId = getIntInput();

        CalculateDiscountResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .calculateDiscount(
                        CalculateDiscountRequest.newBuilder()
                                .setStudentId(studentId)
                                .setTraceId(traceId)
                                .build()
                );

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
            return;
        }

        if (response.getDiscountPercent() > 0) {
            System.out.println("\nПоздравляем! Вам положена скидка:");
            System.out.println("   Скидка: " + response.getDiscountPercent() + "%");
            System.out.println("   Промокод: " + response.getPromoCode());
        } else {
            System.out.println("\nСкидка не положена (средний балл ниже отсечки или мало оценок)");
        }
    }

    private void showNotificationHistory() {
        String traceId = "ui-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.print("Показать уведомления для (имя или ALL): ");
        String name = scanner.nextLine();


        GetNotificationHistoryResponse response = refClient.getNotificationHistory(
                name.equalsIgnoreCase("ALL") ? "ALL" : name,
                traceId
        );

        if (!response.getError().isEmpty()) {
            System.out.println("Ошибка: " + response.getError());
            return;
        }

        if (response.getEntriesList().isEmpty()) {
            System.out.println("\nИстория уведомлений пуста");
            return;
        }

        System.out.println("\n=== История уведомлений ===");
        System.out.println("Всего записей: " + response.getEntriesCount());

        for (var entry : response.getEntriesList()) {
            System.out.printf("[%s] %s: %s%n",
                    entry.getTimestamp(),
                    entry.getObserverName(),
                    entry.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Регистрация слушателя");
        System.out.println("2. Поставить оценку");
        System.out.println("3. Получить отчет по оценкам");
        System.out.println("4. Отметить посещаемость");
        System.out.println("5. Добавить урок");
        System.out.println("6. Расчет скидки");
        System.out.println("7. Выход");
        System.out.print("Выбор: ");
    }

    private int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void shutdown() {
        System.out.println("Завершение работы клиента...");
    }
}
