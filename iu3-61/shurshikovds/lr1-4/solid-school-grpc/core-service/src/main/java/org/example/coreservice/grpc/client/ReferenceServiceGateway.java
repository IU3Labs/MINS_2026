package org.example.coreservice.grpc.client;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.example.coreservice.exception.ServiceUnavailableException;
import org.example.reference.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReferenceServiceGateway {

    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;
    private static final Logger log = LoggerFactory.getLogger(ReferenceServiceGateway.class);

    // ── Validate ─────────────────────────────────────────

    public ValidationResponse validateStudentExists(String traceId, Long studentId) {
        try {
            log.info("[traceId={}] → ValidateStudentExists studentId={}", traceId, studentId);
            return stub.validateStudentExists(
                    ValidateStudentRequest.newBuilder()
                            .setTraceId(traceId)
                            .setStudentId(studentId)
                            .build());
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            return ValidationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Справочный сервис временно недоступен")
                    .build();
        }
    }

    public ValidationResponse validateTeacherExists(String traceId, Long teacherId) {
        try {
            log.info("[traceId={}] → ValidateTeacherExists teacherId={}", traceId, teacherId);
            return stub.validateTeacherExists(
                    ValidateTeacherRequest.newBuilder()
                            .setTraceId(traceId)
                            .setTeacherId(teacherId)
                            .build());
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            return ValidationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Справочный сервис временно недоступен")
                    .build();
        }
    }

    public ValidationResponse validateSchoolClassExists(String traceId, Long schoolClassId) {
        try {
            log.info("[traceId={}] → ValidateSchoolClassExists classId={}", traceId, schoolClassId);
            return stub.validateSchoolClassExists(
                    ValidateSchoolClassRequest.newBuilder()
                            .setTraceId(traceId)
                            .setSchoolClassId(schoolClassId)
                            .build());
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            return ValidationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Справочный сервис временно недоступен")
                    .build();
        }
    }

    // ── Get single ───────────────────────────────────────

    public StudentResponse getStudent(String traceId, Long studentId) {
        try {
            log.info("[traceId={}] → GetStudent studentId={}", traceId, studentId);
            return stub.getStudent(
                    GetStudentRequest.newBuilder()
                            .setTraceId(traceId)
                            .setStudentId(studentId)
                            .build());
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            throw new ServiceUnavailableException("Справочный сервис недоступен");
        }
    }

    public TeacherResponse getTeacher(String traceId, Long teacherId) {
        try {
            log.info("[traceId={}] → GetTeacher teacherId={}", traceId, teacherId);
            return stub.getTeacher(
                    GetTeacherRequest.newBuilder()
                            .setTraceId(traceId)
                            .setTeacherId(teacherId)
                            .build());
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            throw new ServiceUnavailableException("Справочный сервис недоступен");
        }
    }

    // ── Lists ─────────────────────────────────────────────

    public List<TeacherDto> listTeachers(String traceId) {
        try {
            log.info("[traceId={}] → ListTeachers", traceId);
            return stub.listTeachers(
                    ListTeachersRequest.newBuilder()
                            .setTraceId(traceId)
                            .build()
            ).getTeachersList();
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            return List.of();
        }
    }

    public List<SchoolClassDto> listSchoolClasses(String traceId) {
        try {
            log.info("[traceId={}] → ListSchoolClasses", traceId);
            return stub.listSchoolClasses(
                    ListSchoolClassesRequest.newBuilder()
                            .setTraceId(traceId)
                            .build()
            ).getClassesList();
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            return List.of();
        }
    }

    public List<StudentDto> listStudentsByClass(String traceId, Long schoolClassId) {
        try {
            log.info("[traceId={}] → ListStudentsByClass classId={}", traceId, schoolClassId);
            return stub.listStudentsByClass(
                    ListStudentsByClassRequest.newBuilder()
                            .setTraceId(traceId)
                            .setSchoolClassId(schoolClassId)
                            .build()
            ).getStudentsList();
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            return List.of();
        }
    }

    public List<StudentDto> listStudents(String traceId) {
        try {
            log.info("[traceId={}] → ListStudents", traceId);
            return stub.listStudents(
                    ListStudentsRequest.newBuilder()
                            .setTraceId(traceId)
                            .build()
            ).getStudentsList();
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            return List.of();
        }
    }

    public StudentResponse createStudent(String traceId,
                                         String lastName,
                                         String firstName,
                                         String middleName,
                                         LocalDate birthDate,
                                         Long schoolClassId) {
        try {
            log.info("[traceId={}] → CreateStudent lastName={} firstName={} classId={}",
                    traceId, lastName, firstName, schoolClassId);
            return stub.createStudent(
                    CreateStudentRequest.newBuilder()
                            .setTraceId(traceId)
                            .setLastName(lastName)
                            .setFirstName(firstName)
                            .setMiddleName(middleName == null ? "" : middleName)
                            .setBirthDate(birthDate.toString())
                            .setSchoolClassId(schoolClassId)
                            .build()
            );
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            throw new ServiceUnavailableException("ReferenceService", e);
        }
    }

    public OperationStatusResponse deleteStudent(String traceId, Long studentId) {
        try {
            log.info("[traceId={}] → DeleteStudent studentId={}", traceId, studentId);
            return stub.deleteStudent(
                    DeleteStudentRequest.newBuilder()
                            .setTraceId(traceId)
                            .setStudentId(studentId)
                            .build()
            );
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            throw new ServiceUnavailableException("ReferenceService", e);
        }
    }

    public TeacherResponse createTeacher(String traceId,
                                         String lastName,
                                         String firstName,
                                         String middleName,
                                         String specialty) {
        try {
            log.info("[traceId={}] → CreateTeacher lastName={} firstName={}",
                    traceId, lastName, firstName);
            return stub.createTeacher(
                    CreateTeacherRequest.newBuilder()
                            .setTraceId(traceId)
                            .setLastName(lastName)
                            .setFirstName(firstName)
                            .setMiddleName(middleName == null ? "" : middleName)
                            .setSpecialty(specialty == null ? "" : specialty)
                            .build()
            );
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            throw new ServiceUnavailableException("ReferenceService", e);
        }
    }

    public OperationStatusResponse deleteTeacher(String traceId, Long teacherId) {
        try {
            log.info("[traceId={}] → DeleteTeacher teacherId={}", traceId, teacherId);
            return stub.deleteTeacher(
                    DeleteTeacherRequest.newBuilder()
                            .setTraceId(traceId)
                            .setTeacherId(teacherId)
                            .build()
            );
        } catch (StatusRuntimeException e) {
            log.error("[traceId={}] ReferenceService НЕДОСТУПЕН: {}", traceId, e.getStatus());
            throw new ServiceUnavailableException("ReferenceService", e);
        }
    }
}