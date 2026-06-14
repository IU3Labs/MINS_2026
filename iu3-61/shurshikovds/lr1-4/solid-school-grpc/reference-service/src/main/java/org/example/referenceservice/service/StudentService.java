package org.example.referenceservice.service;


import org.example.referenceservice.entity.SchoolClass;
import org.example.referenceservice.entity.Student;

import java.time.LocalDate;
import java.util.List;

public interface StudentService {
    void deleteById(Long id);

    void addStudent(String lastName, String firstName, String middleName, LocalDate birthDate, SchoolClass schoolClass);
    boolean existsById(Long id);
    Student getById(Long id);
    Student save(Student student);

    List<Student> getByClassId(Long classId);

    List<Student> getAll();
}
