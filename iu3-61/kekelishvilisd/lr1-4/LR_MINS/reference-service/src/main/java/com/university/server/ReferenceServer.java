package com.university.server;

import com.university.repository.*;
import com.university.service.observer.InMemoryNotificationRepository;
import com.university.service.observer.NotificationManager;
import com.university.service.observer.NotificationRepository;
import com.university.service.observer.NotificationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class ReferenceServer {
    private static final int PORT = 50052;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Запуск Reference Service на порту " + PORT);

        StudentRepository studentRepo = new InMemoryStudentRepository();
        ScheduleRepository scheduleRepo = new InMemoryScheduleRepository();
        GradeRepository gradeRepo = new InMemoryGradeRepository();
        AttendanceRepository attRepo = new InMemoryAttendanceRepository();
        NotificationRepository notificationRepo = new InMemoryNotificationRepository();

        ReferenceServiceImpl referenceService = new ReferenceServiceImpl(
                studentRepo,
                scheduleRepo,
                gradeRepo,
                attRepo
        );

        Server server = ServerBuilder.forPort(PORT)
                .addService(referenceService)
                .build()
                .start();

        System.out.println(" Reference Service запущен и ждёт запросов...");
        System.out.println("   Порт: " + PORT);
        System.out.println("   Репозитории: Student, Schedule, Grade");

        server.awaitTermination();
    }
}
