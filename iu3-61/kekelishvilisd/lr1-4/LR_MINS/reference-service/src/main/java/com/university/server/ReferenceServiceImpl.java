package com.university.server;

import com.university.exceptions.StudentNotFoundException;
import com.university.model.*;
import com.university.model.NotificationEntry;
import com.university.proto.reference.*;
import com.university.repository.*;
import com.university.service.*;
import com.university.service.observer.NotificationManager;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {

    private final StudentRepository studentRepo;
    private final ScheduleRepository scheduleRepo;
    private final GradeRepository gradeRepo;
    private final AttendanceRepository attendanceRepo;


    public ReferenceServiceImpl(StudentRepository studentRepo,
                                ScheduleRepository scheduleRepo,
                                GradeRepository gradeRepo,
                                AttendanceRepository attendanceRepo) {
        this.studentRepo = studentRepo;
        this.scheduleRepo = scheduleRepo;
        this.gradeRepo = gradeRepo;
        this.attendanceRepo = attendanceRepo;

    }

    @Override
    public void validateStudent(ValidateStudentRequest request,
                                StreamObserver<ValidateStudentResponse> responseObserver) {
        String traceId = request.getTraceId();
        System.out.println("[Trace:" + traceId + "] [ReferenceService] validateStudent(id=" +
                request.getStudentId() + ")");

        var builder = ValidateStudentResponse.newBuilder();

        try {
            Student student = studentRepo.findById(request.getStudentId());
            builder.setExists(true).setStudentName(student.getName());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (StudentNotFoundException e) {
            Status status = Status.NOT_FOUND.withDescription(e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (Exception e) {
            Status status = Status.INTERNAL.withDescription("Внутренняя ошибка" + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        }
    }

    @Override
    public void validateLesson(ValidateLessonRequest request,
                               StreamObserver<ValidateLessonResponse> responseObserver) {
        String traceId = request.getTraceId();
        System.out.println("[Trace:" + traceId + "] [ReferenceService] validateLesson(id=" +
                request.getLessonId() + ")");

        var builder = ValidateLessonResponse.newBuilder();

        try {
            Lesson lesson = scheduleRepo.getLessonById(request.getLessonId());

            if (lesson != null) {
                String lessonInfo = lesson.getCourse().getName() + " | " +
                        lesson.getTeacherName() + " | " +
                        lesson.getRoom() + " | " +
                        lesson.getDateTime();

                builder.setExists(true)
                        .setLessonInfo(lessonInfo);
                System.out.println("[Trace:" + traceId + "] [ReferenceService] Занятие найдено");
            } else {
                builder.setExists(false);
                System.out.println("[Trace:" + traceId + "] [ReferenceService] Занятие не найдено");
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            builder.setError("Ошибка валидации занятия: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void listCourses(ListCoursesRequest request,
                            StreamObserver<ListCoursesResponse> responseObserver) {
        String traceId = request.getTraceId();
        System.out.println("[Trace:" + traceId + "] [ReferenceService] listCourses()");

        var builder = ListCoursesResponse.newBuilder();

        try {

            List<String> courses = scheduleRepo.getAllLessons().stream()
                    .map(lesson -> lesson.getCourse().getName())
                    .distinct()
                    .collect(Collectors.toList());

            builder.addAllCourses(courses);
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

            System.out.println("[Trace:" + traceId + "] [ReferenceService] Найдено курсов: " + courses.size());

        } catch (Exception e) {
            builder.setError("Ошибка получения списка курсов: " + e.getMessage());
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        }
    }


    /**
     * Получить информацию о студенте
     */
    public Student getStudentById(int studentId) throws StudentNotFoundException {
        return studentRepo.findById(studentId);
    }

    /**
     * Получить все оценки студента
     */

    public double getStudentAverageGrade(int studentId) {
        List<Grade> grades = gradeRepo.findByStudentId(studentId);
        if (grades.isEmpty()) {
            return 0.0;
        }

        return grades.stream()
                .mapToInt(Grade::getValue)
                .average()
                .orElse(0.0);
    }

    public boolean courseExists(String courseName) {
        return scheduleRepo.getAllLessons().stream()
                .anyMatch(lesson -> lesson.getCourse().getName().equalsIgnoreCase(courseName));
    }

    @Override
    public void saveStudent(SaveStudentRequest request,
                            StreamObserver<SaveStudentResponse> responseObserver) {
        String traceId = request.getTraceId();
        String name = request.getName();

        System.out.println("[Trace:" + traceId + "] [ReferenceService] saveStudent(name=" + name + ")");

        var builder = SaveStudentResponse.newBuilder();

        try {

            for (Student s : studentRepo.findAll()) {
                if (s.getName().equalsIgnoreCase(name.trim())) {
                    responseObserver.onNext(
                            builder.setStudentId(-1)
                                    .setError("Студент уже существует")
                                    .build()
                    );
                    responseObserver.onCompleted();
                    return;
                }
            }

            int newId = studentRepo.getNextId();
            Student newStudent = new Student(newId, name.trim());
            studentRepo.save(newStudent);

            System.out.println("[Trace:" + traceId + "] Студент сохранён: ID=" + newId);

            responseObserver.onNext(builder.setStudentId(newId).build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(
                    builder.setStudentId(-1)
                            .setError("Ошибка: " + e.getMessage())
                            .build()
            );
            responseObserver.onCompleted();
        }
    }

    @Override
    public void saveGrade(SaveGradeRequest request,
                          StreamObserver<SaveGradeResponse> responseObserver) {
        String traceId = request.getTraceId();
        int studentId = request.getStudentId();
        String courseName = request.getCourseName();
        int value = request.getValue();

        System.out.println("[Trace:" + traceId + "] [ReferenceService] saveGrade(student=" +
                studentId + ", course=" + courseName + ", value=" + value + ")");

        var builder = SaveGradeResponse.newBuilder();

        try {
            if (value < 1 || value > 5) {
                responseObserver.onNext(builder
                        .setSuccess(false)
                        .setError("Оценка должна быть от 1 до 5!")
                        .build());
                responseObserver.onCompleted();
                return;
            }


            Student student = studentRepo.findById(studentId);
            if (student == null) {
                responseObserver.onNext(builder
                        .setSuccess(false)
                        .setError("Студент не найден")
                        .build());
                responseObserver.onCompleted();
                return;
            }

            Grade grade = new Grade(student, new Course(courseName), value);
            gradeRepo.save(grade);

            System.out.println("[Trace:" + traceId + "] [ReferenceService] Оценка сохранена");

            responseObserver.onNext(builder.setSuccess(true).build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(builder
                    .setSuccess(false)
                    .setError("Ошибка: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void saveAttendance(SaveAttendanceRequest request,
                               StreamObserver<SaveAttendanceResponse> responseObserver) {
        String traceId = request.getTraceId();
        int studentId = request.getStudentId();
        int lessonId = request.getLessonId();
        String status = request.getStatus();

        System.out.println("[Trace:" + traceId + "] [ReferenceService] saveAttendance(student=" +
                studentId + ", lesson=" + lessonId + ", status=" + status + ")");

        var builder = SaveAttendanceResponse.newBuilder();

        try {
            Student student = studentRepo.findById(studentId);
            if (student == null) {
                responseObserver.onNext(builder
                        .setSuccess(false)
                        .setError("Студент не найден")
                        .build());
                responseObserver.onCompleted();
                return;
            }

            Lesson lesson = scheduleRepo.getLessonById(lessonId);
            if (lesson == null) {
                responseObserver.onNext(builder
                        .setSuccess(false)
                        .setError("Занятие не найдено")
                        .build());
                responseObserver.onCompleted();
                return;
            }

            AttendanceStatus attendanceStatus = AttendanceStatus.valueOf(status.toUpperCase());

            AttendanceRecord record = new AttendanceRecord(
                    attendanceRepo.getNextId(),
                    student,
                    lesson,
                    LocalDate.now(),
                    attendanceStatus
            );
            attendanceRepo.markAttendance(record);

            System.out.println("[Trace:" + traceId + "] [ReferenceService] Посещаемость сохранена");

            responseObserver.onNext(builder.setSuccess(true).build());
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onNext(builder
                    .setSuccess(false)
                    .setError("Неверный статус: " + status)
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(builder
                    .setSuccess(false)
                    .setError("Ошибка: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getStudentGrades(GetStudentGradesRequest request,
                                 StreamObserver<GetStudentGradesResponse> responseObserver) {
        String traceId = request.getTraceId();
        int studentId = request.getStudentId();

        System.out.println("[Trace:" + traceId + "] [ReferenceService] getStudentGrades(student=" + studentId + ")");

        var builder = GetStudentGradesResponse.newBuilder();

        try {
            List<Grade> grades = gradeRepo.findByStudentId(studentId);

            for (Grade grade : grades) {
                builder.addGrades(GradeData.newBuilder()
                        .setCourseName(grade.getCourse().getName())
                        .setValue(grade.getValue())
                        .build());
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

            System.out.println("[Trace:" + traceId + "] [ReferenceService] Найдено оценок: " + grades.size());

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(builder
                    .setError("Ошибка: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getStudentAverage(GetStudentAverageRequest request,
                                  StreamObserver<GetStudentAverageResponse> responseObserver) {
        String traceId = request.getTraceId();
        int studentId = request.getStudentId();

        System.out.println("[Trace:" + traceId + "] [ReferenceService] getStudentAverage(student=" + studentId + ")");

        var builder = GetStudentAverageResponse.newBuilder();

        try {
            List<Grade> grades = gradeRepo.findByStudentId(studentId);

            if (grades.isEmpty()) {
                responseObserver.onNext(builder
                        .setAverage(0.0)
                        .setCount(0)
                        .build());
                responseObserver.onCompleted();
                return;
            }

            double average = grades.stream()
                    .mapToInt(Grade::getValue)
                    .average()
                    .orElse(0.0);

            responseObserver.onNext(builder
                    .setAverage(average)
                    .setCount(grades.size())
                    .build());
            responseObserver.onCompleted();

            System.out.println("[Trace:" + traceId + "] [ReferenceService] ✅ Средний балл: " + average);

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(builder
                    .setError("Ошибка: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void saveLesson(SaveLessonRequest request,
                          StreamObserver<SaveLessonResponse> responseObserver) {
        String traceId = request.getTraceId();
        System.out.println("[Trace:" + traceId + "] [ReferenceService] addLesson(course=" +
                request.getCourseName() + ", teacher=" + request.getTeacher() + ")");

        var builder = SaveLessonResponse.newBuilder();

        try {
            Course course = new Course(request.getCourseName());
            int lessonId = scheduleRepo.getNextId();

            Lesson lesson = new Lesson(lessonId, course, request.getTeacher(),
                    request.getRoom(), request.getDateTime());
            scheduleRepo.addLesson(lesson);

            System.out.println("[Trace:" + traceId + "] [ReferenceService] Занятие добавлено: ID=" + lessonId);

            responseObserver.onNext(builder.setLessonId(lessonId).build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(builder
                    .setLessonId(-1)
                    .setError("Ошибка: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getSchedule(ReferenceScheduleRequest request, StreamObserver<ReferenceScheduleResponse> responseObserver) {
        try {
            var builder = ReferenceScheduleResponse.newBuilder();
            var lessons = request.getDayFilter().equalsIgnoreCase("ALL")
                    ? scheduleRepo.getAllLessons()
                    : scheduleRepo.getLessonsByDay(request.getDayFilter());

            for (var l : lessons) {
                builder.addLessons(ReferenceLessonData.newBuilder()
                        .setLessonId(l.getId()).setCourseName(l.getCourse().getName())
                        .setTeacher(l.getTeacherName()).setRoom(l.getRoom()).setDateTime(l.getDateTime()).build());
            }
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getNotificationHistory(GetNotificationHistoryRequest request,
                                       StreamObserver<GetNotificationHistoryResponse> responseObserver) {
        String traceId = request.getTraceId();
        String observerName = request.getObserverName();

        System.out.println("[Trace:" + traceId + "] [ReferenceService] getNotificationHistory(observer=" +
                observerName + ")");

        var builder = GetNotificationHistoryResponse.newBuilder();

        try {

            List<NotificationEntry> history;

            /*if (observerName.equalsIgnoreCase("ALL")) {
                history = notificationManager.getHistory();
            } else {
                history = notificationManager.getHistoryByObserver(observerName);
            }

            for (var entry : history) {
                builder.addEntries(com.university.proto.reference.NotificationEntry.newBuilder()
                        .setObserverName(entry.observerName())
                        .setMessage(entry.message())
                        .setTimestamp(entry.timestamp().toString())
                        .build());
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();

            System.out.println("[Trace:" + traceId + "] [ReferenceService] Найдено уведомлений: " + history.size());*/

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(builder
                    .setError("Ошибка: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }
}
