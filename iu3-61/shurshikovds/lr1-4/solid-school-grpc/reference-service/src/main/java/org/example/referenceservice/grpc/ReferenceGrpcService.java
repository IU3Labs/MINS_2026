package org.example.referenceservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.example.reference.grpc.*;
import org.example.referenceservice.entity.SchoolClass;
import org.example.referenceservice.entity.Student;
import org.example.referenceservice.entity.Teacher;
import org.example.referenceservice.service.SchoolClassService;
import org.example.referenceservice.service.StudentService;
import org.example.referenceservice.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import java.time.LocalDate;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class ReferenceGrpcService extends ReferenceServiceGrpc.ReferenceServiceImplBase {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final SchoolClassService schoolClassService;
    private static final Logger log = LoggerFactory.getLogger(ReferenceGrpcService.class);

    // ── Validate ─────────────────────────────────────────

    @Override
    public void validateStudentExists(ValidateStudentRequest request,
                                      StreamObserver<ValidationResponse> responseObserver) {
        log.info("[traceId={}] ValidateStudentExists studentId={}",
                request.getTraceId(), request.getStudentId());
        boolean exists = studentService.existsById(request.getStudentId());
        responseObserver.onNext(ValidationResponse.newBuilder()
                .setValid(exists)
                .setMessage(exists ? "" : "Ученик с id=" + request.getStudentId() + " не найден")
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void validateTeacherExists(ValidateTeacherRequest request,
                                      StreamObserver<ValidationResponse> responseObserver) {
        log.info("[traceId={}] ValidateTeacherExists teacherId={}",
                request.getTraceId(), request.getTeacherId());
        boolean exists = teacherService.existsById(request.getTeacherId());
        responseObserver.onNext(ValidationResponse.newBuilder()
                .setValid(exists)
                .setMessage(exists ? "" : "Учитель с id=" + request.getTeacherId() + " не найден")
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void validateSchoolClassExists(ValidateSchoolClassRequest request,
                                          StreamObserver<ValidationResponse> responseObserver) {
        log.info("[traceId={}] ValidateSchoolClassExists classId={}",
                request.getTraceId(), request.getSchoolClassId());
        boolean exists = schoolClassService.existsById(request.getSchoolClassId());
        responseObserver.onNext(ValidationResponse.newBuilder()
                .setValid(exists)
                .setMessage(exists ? "" : "Класс с id=" + request.getSchoolClassId() + " не найден")
                .build());
        responseObserver.onCompleted();
    }

    // ── Get single ───────────────────────────────────────

    @Override
    public void getStudent(GetStudentRequest request,
                           StreamObserver<StudentResponse> responseObserver) {
        log.info("[traceId={}] GetStudent studentId={}",
                request.getTraceId(), request.getStudentId());
        Student s = studentService.getById(request.getStudentId());
        responseObserver.onNext(StudentResponse.newBuilder()
                .setId(s.getId())
                .setLastName(s.getLastName())
                .setFirstName(s.getFirstName())
                .setMiddleName(s.getMiddleName() != null ? s.getMiddleName() : "")
                .setSchoolClassId(s.getSchoolClass().getId())
                .setSchoolClassName(s.getSchoolClass().getGrade() + s.getSchoolClass().getLetter())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getTeacher(GetTeacherRequest request,
                           StreamObserver<TeacherResponse> responseObserver) {
        log.info("[traceId={}] GetTeacher teacherId={}",
                request.getTraceId(), request.getTeacherId());
        Teacher t = teacherService.getById(request.getTeacherId());
        responseObserver.onNext(TeacherResponse.newBuilder()
                .setId(t.getId())
                .setLastName(t.getLastName())
                .setFirstName(t.getFirstName())
                .setMiddleName(t.getMiddleName() != null ? t.getMiddleName() : "")
                .setSpecialty(t.getSpecialty() != null ? t.getSpecialty() : "")
                .build());
        responseObserver.onCompleted();
    }

    // ── Lists ─────────────────────────────────────────────

    @Override
    public void listTeachers(ListTeachersRequest request,
                             StreamObserver<ListTeachersResponse> responseObserver) {
        log.info("[traceId={}] ListTeachers", request.getTraceId());
        List<TeacherDto> dtos = teacherService.getAll().stream()
                .map(t -> TeacherDto.newBuilder()
                        .setId(t.getId())
                        .setLastName(t.getLastName())
                        .setFirstName(t.getFirstName())
                        .setMiddleName(t.getMiddleName() != null ? t.getMiddleName() : "")
                        .setSpecialty(t.getSpecialty() != null ? t.getSpecialty() : "")
                        .build())
                .toList();
        responseObserver.onNext(ListTeachersResponse.newBuilder()
                .addAllTeachers(dtos).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listSchoolClasses(ListSchoolClassesRequest request,
                                  StreamObserver<ListSchoolClassesResponse> responseObserver) {
        log.info("[traceId={}] ListSchoolClasses", request.getTraceId());
        List<SchoolClassDto> dtos = schoolClassService.getAll().stream()
                .map(c -> SchoolClassDto.newBuilder()
                        .setId(c.getId())
                        .setGrade(c.getGrade())
                        .setLetter(c.getLetter())
                        .build())
                .toList();
        responseObserver.onNext(ListSchoolClassesResponse.newBuilder()
                .addAllClasses(dtos).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listStudentsByClass(ListStudentsByClassRequest request,
                                    StreamObserver<ListStudentsResponse> responseObserver) {
        log.info("[traceId={}] ListStudentsByClass classId={}",
                request.getTraceId(), request.getSchoolClassId());
        List<StudentDto> dtos = studentService.getByClassId(request.getSchoolClassId())
                .stream()
                .map(s -> StudentDto.newBuilder()
                        .setId(s.getId())
                        .setLastName(s.getLastName())
                        .setFirstName(s.getFirstName())
                        .setMiddleName(s.getMiddleName() != null ? s.getMiddleName() : "")
                        .build())
                .toList();
        responseObserver.onNext(ListStudentsResponse.newBuilder()
                .addAllStudents(dtos).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listStudents(ListStudentsRequest request,
                             StreamObserver<ListStudentsResponse> responseObserver) {
        String traceId = request.getTraceId();
        log.info("[traceId={}] ListStudents", traceId);

        List<StudentDto> dtos = studentService.getAll().stream()
                .map(s -> StudentDto.newBuilder()
                        .setId(s.getId())
                        .setLastName(s.getLastName())
                        .setFirstName(s.getFirstName())
                        .setMiddleName(s.getMiddleName() != null ? s.getMiddleName() : "")
                        .build())
                .toList();

        responseObserver.onNext(ListStudentsResponse.newBuilder()
                .addAllStudents(dtos)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void createStudent(CreateStudentRequest request,
                              StreamObserver<StudentResponse> responseObserver) {
        String traceId = request.getTraceId();
        log.info("[traceId={}] CreateStudent lastName={} firstName={} classId={}",
                traceId, request.getLastName(), request.getFirstName(), request.getSchoolClassId());

        try {
            SchoolClass schoolClass = schoolClassService.getById(request.getSchoolClassId());

            Student student = Student.builder()
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .middleName(request.getMiddleName())
                    .birthDate(LocalDate.parse(request.getBirthDate()))
                    .schoolClass(schoolClass)
                    .build();

            Student saved = studentService.save(student);

            responseObserver.onNext(StudentResponse.newBuilder()
                    .setId(saved.getId())
                    .setLastName(saved.getLastName())
                    .setFirstName(saved.getFirstName())
                    .setMiddleName(saved.getMiddleName() != null ? saved.getMiddleName() : "")
                    .setSchoolClassId(saved.getSchoolClass().getId())
                    .setSchoolClassName(saved.getSchoolClass().getGrade() + saved.getSchoolClass().getLetter())
                    .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[traceId={}] CreateStudent error: {}", traceId, e.getMessage(), e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Не удалось создать ученика: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void deleteStudent(DeleteStudentRequest request,
                              StreamObserver<OperationStatusResponse> responseObserver) {
        String traceId = request.getTraceId();
        Long studentId = request.getStudentId();
        log.info("[traceId={}] DeleteStudent studentId={}", traceId, studentId);

        try {
            studentService.deleteById(studentId);

            responseObserver.onNext(OperationStatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("")
                    .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[traceId={}] DeleteStudent error: {}", traceId, e.getMessage(), e);
            responseObserver.onNext(OperationStatusResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Не удалось удалить ученика: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createTeacher(CreateTeacherRequest request,
                              StreamObserver<TeacherResponse> responseObserver) {
        String traceId = request.getTraceId();
        log.info("[traceId={}] CreateTeacher lastName={} firstName={}",
                traceId, request.getLastName(), request.getFirstName());

        try {
            Teacher teacher = Teacher.builder()
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .middleName(request.getMiddleName())
                    .specialty(request.getSpecialty())
                    .build();

            Teacher saved = teacherService.save(teacher);

            responseObserver.onNext(TeacherResponse.newBuilder()
                    .setId(saved.getId())
                    .setLastName(saved.getLastName())
                    .setFirstName(saved.getFirstName())
                    .setMiddleName(saved.getMiddleName() != null ? saved.getMiddleName() : "")
                    .setSpecialty(saved.getSpecialty() != null ? saved.getSpecialty() : "")
                    .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[traceId={}] CreateTeacher error: {}", traceId, e.getMessage(), e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Не удалось создать учителя: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void deleteTeacher(DeleteTeacherRequest request,
                              StreamObserver<OperationStatusResponse> responseObserver) {
        String traceId = request.getTraceId();
        Long teacherId = request.getTeacherId();
        log.info("[traceId={}] DeleteTeacher teacherId={}", traceId, teacherId);

        try {
            teacherService.deleteById(teacherId);

            responseObserver.onNext(OperationStatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("")
                    .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[traceId={}] DeleteTeacher error: {}", traceId, e.getMessage(), e);
            responseObserver.onNext(OperationStatusResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Не удалось удалить учителя: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }
}
