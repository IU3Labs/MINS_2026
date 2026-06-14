package org.example.referenceservice.repository;

import org.example.referenceservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findBySchoolClassId(Long schoolClassId);
    @Query("""
       select s
       from Student s
       join fetch s.schoolClass
       where s.id = :id
       """)
    Optional<Student> findByStudentId(@Param(value = "id") Long id);
}

