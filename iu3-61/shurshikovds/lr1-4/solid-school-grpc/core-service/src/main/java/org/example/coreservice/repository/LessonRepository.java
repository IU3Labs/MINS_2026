package org.example.coreservice.repository;

import org.example.coreservice.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("""
        SELECT l FROM Lesson l
        WHERE l.teacherId = :teacherId
        AND l.lessonDate = :date
        ORDER BY l.startTime ASC
    """)
    List<Lesson> findAllByTeacherIdAndLessonDateOrderByStartTimeAsc(@Param("teacherId") Long teacherId,
                                                                    @Param("date") LocalDate date);

    @Query("""
        SELECT l FROM Lesson l
        LEFT JOIN FETCH l.grades g
        WHERE l.id = :id
    """)
    java.util.Optional<Lesson> findByIdWithGrades(@Param("id") Long id);

    boolean existsByScheduleEntryIdAndLessonDate(Long scheduleEntryId, LocalDate lessonDate);
}

