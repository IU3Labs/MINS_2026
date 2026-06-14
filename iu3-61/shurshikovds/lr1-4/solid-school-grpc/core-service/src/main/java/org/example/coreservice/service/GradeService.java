package org.example.coreservice.service;


import org.example.coreservice.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    List<Grade> getStudentGrades(Long studentId);

    Grade addGrade(String traceId, Long studentId, Long lessonId, Short value);

    Optional<Double> getAverageGrade(Long studentId);
}
