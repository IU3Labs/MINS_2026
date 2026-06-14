package org.example.coreservice.repository;

import org.example.coreservice.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("""
        SELECT g FROM Grade g
        JOIN FETCH g.lesson l
        WHERE g.studentId = :studentId
        ORDER BY l.lessonDate ASC
    """)
    List<Grade> findAllByStudentIdWithLessons(@Param("studentId") Long studentId);

    @Query("""
        SELECT AVG(g.grade) FROM Grade g
        JOIN g.lesson l
        WHERE g.studentId = :studentId
    """)
    Optional<Double> findAverageGradeByStudentId(
            @Param("studentId") Long studentId
    );

    Optional<Grade> findByStudentIdAndLessonId(Long studentId, Long lessonId);
}

