package org.example.referenceservice.service;


import org.example.referenceservice.entity.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface TeacherService {
    List<Teacher> getAll();

    void deleteById(Long id);
    Teacher save(Teacher teacher);

    void addTeacher(String lastName, String firstName, String middleName, LocalDate birthDate, String specialty);

    Boolean existsById(Long id);

    Teacher getById(Long id);
}
