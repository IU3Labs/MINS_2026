package com.university.server;

import com.university.client.ReferenceServiceClient;
import com.university.exceptions.StudentNotFoundException;
import com.university.model.Student;
import com.university.proto.core.*;
import com.university.proto.reference.*;
import com.university.service.QuickDiscountCalculator;
import com.university.service.observer.NotificationManager;
import io.grpc.stub.StreamObserver;
import java.util.UUID;

public class CoreServiceImpl extends CoreServiceGrpc.CoreServiceImplBase {

    private final ReferenceServiceClient refClient;

    private final QuickDiscountCalculator discountCalculator;
    private final NotificationManager notificationManager;

    public CoreServiceImpl(ReferenceServiceClient refClient,
                           QuickDiscountCalculator discountCalculator,
                           NotificationManager notificationManager) {
        this.refClient = refClient;
        this.discountCalculator = discountCalculator;
        this.notificationManager = notificationManager;
    }

    private String getOrGenerateTraceId(String provided) {
        return provided.isEmpty() ? "trace-" + UUID.randomUUID().toString().substring(0, 8) : provided;
    }

    @Override
    public void registerStudent(RegisterStudentRequest request,
                                StreamObserver<RegisterStudentResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] registerStudent(name=" + request.getName() + ")");

        var builder = RegisterStudentResponse.newBuilder();

        try {
            if (request.getName().isBlank()) {
                builder.setError("Имя не может быть пустым");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            if (request.getName().length() > 100) {
                builder.setError("Имя слишком длинное (макс. 100 символов)");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            SaveStudentResponse saveResponse = refClient.saveStudent(request.getName(), traceId);

            if (saveResponse.getStudentId() < 0) {
                builder.setError(saveResponse.getError());
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            builder.setStudentId(saveResponse.getStudentId())
                    .setName(request.getName());

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            System.out.println("[Trace:" + traceId + "] [CoreService] Студент зарегистрирован: ID=" + saveResponse.getStudentId());

        }
        catch (Exception e) {
            builder.setError("Системная ошибка: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            System.err.println("[Trace:" + traceId + "] [CoreService] Ошибка: " + e.getMessage());
        }
    }

    @Override
    public void addGrade(AddGradeRequest request,
                         StreamObserver<AddGradeResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] addGrade(student=" +
                request.getStudentId() + ", course=" + request.getCourseName() + ")");
        var builder = AddGradeResponse.newBuilder();

        try {
            boolean studentExists = refClient.validateStudent(request.getStudentId(), traceId);
            if (!studentExists) {
                builder.setError("Студент не найден или справочник недоступен");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            /*if (request.getValue() < 1 || request.getValue() > 5) {
                builder.setError("Оценка должна быть от 1 до 5");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }*/

            SaveGradeResponse saveResponse = refClient.saveGrade(
                    request.getStudentId(),
                    request.getCourseName(),
                    request.getValue(),
                    traceId
            );

            if (!saveResponse.getSuccess()) {
                builder.setError(saveResponse.getError());
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }
            Student s = new Student(request.getStudentId(), request.getCourseName());

            notificationManager.notifyObserver(s, "Оценка выставлена.");

            builder.setSuccess(true);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

            System.out.println("[Trace:" + traceId + "] [CoreService] Оценка выставлена");

        }  catch (StudentNotFoundException e) {
            builder.setError(e.getMessage());
        }
        catch (Exception e) {
            builder.setError("Системная ошибка: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            System.err.println("[Trace:" + traceId + "] [CoreService] Ошибка: " + e.getMessage());
        }
    }

    @Override
    public void markAttendance(MarkAttendanceRequest request,
                               StreamObserver<MarkAttendanceResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] markAttendance(student=" +
                request.getStudentId() + ", lesson=" + request.getLessonId() + ")");

        var builder = MarkAttendanceResponse.newBuilder();

        try {
            if (!refClient.validateStudent(request.getStudentId(), traceId)) {
                builder.setError("Студент не найден");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }
            if (!refClient.validateLesson(request.getLessonId(), traceId)) {
                builder.setError("Занятие не найдено");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            SaveAttendanceResponse saveResponse = refClient.saveAttendance(
                    request.getStudentId(),
                    request.getLessonId(),
                    request.getStatus(),
                    traceId
            );

            if (!saveResponse.getSuccess()) {
                builder.setError(saveResponse.getError());
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            builder.setSuccess(true);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

            System.out.println("[Trace:" + traceId + "] [CoreService] Посещаемость отмечена");

        } catch (Exception e) {
            builder.setError("Ошибка: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            System.err.println("[Trace:" + traceId + "] [CoreService] Ошибка: " + e.getMessage());
        }
    }

    @Override
    public void getGradeReport(GetGradeReportRequest request,
                               StreamObserver<GetGradeReportResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] getGradeReport(student=" +
                request.getStudentId() + ")");

        var builder = GetGradeReportResponse.newBuilder();

        try {
            if (!refClient.validateStudent(request.getStudentId(), traceId)) {
                builder.setError("Студент не найден");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }


            GetStudentAverageResponse avgResponse = refClient.getStudentAverage(request.getStudentId(), traceId);
            double average = avgResponse.getAverage();
            int gradesCount = avgResponse.getCount();

            String reportText = "=== Отчёт по студенту ID=" + request.getStudentId() + " ===\n" +
                    "Средний балл: " + String.format("%.2f", average) + "\n" +
                    "Всего оценок: " + gradesCount;

            builder.setReportText(reportText);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

            System.out.println("[Trace:" + traceId + "] [CoreService] Отчёт сгенерирован");

        } catch (Exception e) {
            builder.setError("Ошибка генерации отчёта: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
            System.err.println("[Trace:" + traceId + "] [CoreService] Ошибка: " + e.getMessage());
        }
    }

    public void calculateDiscount(CalculateDiscountRequest request,
                                  StreamObserver<CalculateDiscountResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        var builder = CalculateDiscountResponse.newBuilder();

        try {
            var gradesResp = refClient.getStudentGrades(request.getStudentId(), traceId);
            int gradesCount = gradesResp.getGradesList().size();

            var avgResp = refClient.getStudentAverage(request.getStudentId(), traceId);
            double average = avgResp.getAverage();

            if (gradesCount == 0 && average == 0.0) {
                builder.setError("Студент не найден или у него нет оценок");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                return;
            }

            int discount = discountCalculator.calculateDiscountPercent(average, gradesCount);
            String promo = discount > 0 ? discountCalculator.getPromoCode(discount) : "";

            builder.setDiscountPercent(discount).setPromoCode(promo);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            builder.setError("Ошибка расчёта: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void addLesson(AddLessonRequest request,
                          StreamObserver<AddLessonResponse> responseObserver) {
        String traceId = getOrGenerateTraceId(request.getTraceId());
        System.out.println("[Trace:" + traceId + "] [CoreService] addLesson(course=" +
                request.getCourseName() + ")");

        var builder = AddLessonResponse.newBuilder();

        try {
            SaveLessonResponse refResponse = refClient.saveLesson(
                    request.getCourseName(),
                    request.getTeacher(),
                    request.getRoom(),
                    request.getDateTime(),
                    traceId
            );

            if (refResponse.getLessonId() > 0) {
                builder.setLessonId(refResponse.getLessonId());
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
                System.out.println("[Trace:" + traceId + "] [CoreService] Занятие добавлено");
            } else {
                builder.setError(refResponse.getError());
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
            }

        } catch (Exception e) {
            e.printStackTrace();
            builder.setError("Ошибка: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

}
