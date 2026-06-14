package org.example.referenceservice.repository;

import org.example.referenceservice.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    @Query("""
        SELECT c FROM SchoolClass c
        LEFT JOIN FETCH c.students
        WHERE c.grade = :grade
        AND c.letter = :letter
    """)
    Optional<SchoolClass> findByGradeAndLetter(@Param("grade")Short grade,
                                               @Param("letter")String letter);

    @Query("""
        SELECT c FROM SchoolClass c
        LEFT JOIN FETCH c.students
        WHERE c.id = :id
    """)
    Optional<SchoolClass> findByIdWithStudents(@Param("id") Long id);
}

