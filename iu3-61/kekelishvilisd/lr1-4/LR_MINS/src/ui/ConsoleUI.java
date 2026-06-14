package ui;

import com.university.proto.core.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Консольный UI теперь выступает в роли gRPC-клиента.
 * Не знает про внутренние сервисы, работает только через контракт CoreService.
 */
public class ConsoleUI {
    private final CoreServiceGrpc.CoreServiceBlockingStub stub;
    private final Scanner scanner;

    public ConsoleUI(String host, int port) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = CoreServiceGrpc.newBlockingStub(channel);
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
                    case 5 -> System.out.println(" Для лабы 4 реализованы основные cross-service вызовы. Остальное вынесено в локальные модули.");
                    case 9 -> { shutdown(); return; }
                    default -> System.out.println("️ Неверный выбор");
                }
            }
            catch (StudentNotFoundException e) {
                System.out.println("Студент не найдет: " + e.getMessage());
            } catch (StatusRuntimeException e) {
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

    private void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Регистрация слушателя");
        System.out.println("2. Поставить оценку");
        System.out.println("3. Получить отчет по оценкам");
        System.out.println("4. Отметить посещаемость");
        System.out.println("5. Прочие функции (локальные)");
        System.out.println("9. Выход");
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
        System.out.println("🔌 Завершение работы клиента...");
    }
}