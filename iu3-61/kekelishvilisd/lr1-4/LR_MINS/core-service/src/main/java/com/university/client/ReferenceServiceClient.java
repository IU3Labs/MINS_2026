package com.university.client;

import com.university.proto.reference.*;

import io.grpc.*;

import java.util.concurrent.TimeUnit;


public class ReferenceServiceClient {
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;


    public ReferenceServiceClient(ManagedChannel channel) {
        this.channel = channel;
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }


    public boolean validateStudent(int studentId, String traceId) {
        try {
            var request = ValidateStudentRequest.newBuilder()
                    .setStudentId(studentId)
                    .setTraceId(traceId)
                    .build();

            var response = stub
                    .withDeadlineAfter(3, TimeUnit.SECONDS)
                    .validateStudent(request);

            return response.getExists();

        } catch (StatusRuntimeException e) {
            System.err.println("[Trace:" + traceId + "] Reference Service недоступен: " + e.getStatus());
            return false;
        }
    }

    public boolean validateLesson(int lessonId, String traceId) {
        try {
            var request = ValidateLessonRequest.newBuilder()
                    .setLessonId(lessonId)
                    .setTraceId(traceId)
                    .build();

            var response = stub
                    .withDeadlineAfter(3, TimeUnit.SECONDS)
                    .validateLesson(request);

            return response.getExists();

        } catch (StatusRuntimeException e) {
            System.err.println("[Trace:" + traceId + "] Reference Service недоступен: " + e.getStatus());
            return false;
        }
    }


    public SaveStudentResponse saveStudent(String name, String traceId) {
        try {
            var request = SaveStudentRequest.newBuilder()
                    .setName(name)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS).saveStudent(request);

        } catch (StatusRuntimeException e) {
            return SaveStudentResponse.newBuilder()
                    .setStudentId(-1)
                    .setError("Reference Service недоступен")
                    .build();
        }
    }

    public SaveGradeResponse saveGrade(int studentId, String courseName, int value, String traceId) {
        try {
            var request = SaveGradeRequest.newBuilder()
                    .setStudentId(studentId)
                    .setCourseName(courseName)
                    .setValue(value)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS).saveGrade(request);

        } catch (StatusRuntimeException e) {
            return SaveGradeResponse.newBuilder()
                    .setSuccess(false)
                    .setError("Reference Service недоступен")
                    .build();
        }
    }

    public SaveAttendanceResponse saveAttendance(int studentId, int lessonId, String status, String traceId) {
        try {
            var request = SaveAttendanceRequest.newBuilder()
                    .setStudentId(studentId)
                    .setLessonId(lessonId)
                    .setStatus(status)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS).saveAttendance(request);

        } catch (StatusRuntimeException e) {
            return SaveAttendanceResponse.newBuilder()
                    .setSuccess(false)
                    .setError("Reference Service недоступен")
                    .build();
        }
    }


    public GetStudentGradesResponse getStudentGrades(int studentId, String traceId) {
        try {
            var request = GetStudentGradesRequest.newBuilder()
                    .setStudentId(studentId)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS).getStudentGrades(request);

        } catch (StatusRuntimeException e) {
            return GetStudentGradesResponse.newBuilder()
                    .setError("Reference Service недоступен")
                    .build();
        }
    }

    public GetStudentAverageResponse getStudentAverage(int studentId, String traceId) {
        try {
            var request = GetStudentAverageRequest.newBuilder()
                    .setStudentId(studentId)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS).getStudentAverage(request);

        } catch (StatusRuntimeException e) {
            return GetStudentAverageResponse.newBuilder()
                    .setError("Reference Service недоступен")
                    .build();
        }
    }

    public SaveLessonResponse saveLesson(String courseName, String teacher, String room,
                                       String dateTime, String traceId) {
        try {
            var request = SaveLessonRequest.newBuilder()
                    .setCourseName(courseName)
                    .setTeacher(teacher)
                    .setRoom(room)
                    .setDateTime(dateTime)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS).saveLesson(request);

        } catch (StatusRuntimeException e) {
            System.err.println("gRPC ошибка saveLesson: " + e.getStatus().getCode() + " | " + e.getStatus().getDescription());
            return SaveLessonResponse.newBuilder()
                    .setLessonId(-1)
                    .setError("Reference Service недоступен")
                    .build();
        }
    }


    public GetNotificationHistoryResponse getNotificationHistory(String observerName, String traceId) {
        try {
            var request = GetNotificationHistoryRequest.newBuilder()
                    .setObserverName(observerName)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getNotificationHistory(request);

        } catch (StatusRuntimeException e) {
            return GetNotificationHistoryResponse.newBuilder()
                    .setError("Reference Service недоступен")
                    .build();
        }
    }

    public ReferenceScheduleResponse getSchedule(String dayFilter, String traceId) {
        try {
            var request = ReferenceScheduleRequest.newBuilder()
                    .setDayFilter(dayFilter)
                    .setTraceId(traceId)
                    .build();

            return stub.withDeadlineAfter(5, TimeUnit.SECONDS).getSchedule(request);

        } catch (StatusRuntimeException e) {
            return ReferenceScheduleResponse.newBuilder()
                    .setError("Reference Service недоступен")
                    .build();
        }
    }


    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.err.println("Прервано ожидание завершения канала");
        }
    }
}
