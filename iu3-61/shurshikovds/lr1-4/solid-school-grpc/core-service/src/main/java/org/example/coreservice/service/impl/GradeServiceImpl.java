package org.example.coreservice.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.coreservice.entity.Grade;
import org.example.coreservice.entity.Lesson;
import org.example.coreservice.exception.MyEntityNotFoundException;
import org.example.coreservice.exception.ValidationException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.repository.GradeRepository;
import org.example.coreservice.repository.LessonRepository;
import org.example.coreservice.service.GradeService;
import org.example.reference.grpc.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final LessonRepository lessonRepository;
    private final ReferenceServiceGateway referenceGateway;

    private static final Logger log = LoggerFactory.getLogger(GradeServiceImpl.class);

    @Override
    public List<Grade> getStudentGrades(Long studentId) {
        return gradeRepository.findAllByStudentIdWithLessons(studentId);
    }

    @Override
    @Transactional
    public Grade addGrade(String traceId, Long studentId, Long lessonId, Short value) {
        log.info("[traceId={}] addGrade: studentId={} lessonId={} value={}",
                traceId, studentId, lessonId, value);

        if (value < 1 || value > 5) {
            throw new ValidationException("Оценка должна быть от 1 до 5");
        }

        ValidationResponse studentCheck =
                referenceGateway.validateStudentExists(traceId, studentId);
        if (!studentCheck.getValid()) {
            throw new ValidationException(studentCheck.getMessage());
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new MyEntityNotFoundException("Урок не найден"));

        if (gradeRepository.findByStudentIdAndLessonId(studentId, lessonId).isPresent()) {
            throw new ValidationException("Оценка за этот урок уже выставлена");
        }

        Grade grade = Grade.builder()
                .studentId(studentId)
                .lesson(lesson)
                .grade(value)
                .build();

        Grade saved = gradeRepository.save(grade);
        log.info("[traceId={}] addGrade: сохранена оценка id={}", traceId, saved.getId());
        return saved;
    }

    @Override
    public Optional<Double> getAverageGrade(Long studentId) {
        return gradeRepository.findAverageGradeByStudentId(studentId);
    }

}
