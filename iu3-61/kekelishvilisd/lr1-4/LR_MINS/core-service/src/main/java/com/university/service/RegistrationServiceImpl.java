package com.university.service;

import com.university.model.*;
import com.university.repository.*;
import com.university.exceptions.*;

public class RegistrationServiceImpl implements RegistrationService {
    private final StudentRepository repository;

    public RegistrationServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student registerStudent(String name) throws StudentAlreadyRegisteredException, InvalidNameException {
        validateStudentName(name);

        for (Student s : repository.findAll()) {
            if (s.getName().equalsIgnoreCase(name.trim())) {
                throw new StudentAlreadyRegisteredException(name.trim());
            }
        }

        int id = repository.getNextId();
        Student student = new Student(id, name.trim());
        repository.save(student);
        return student;
    }

    private void validateStudentName(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException(name, "Пустое");
        }
        if (name.trim().length() > 100) {
            throw new InvalidNameException(name, "Длинное");
        }
        if (name.matches(".*\\d.*")) {
            throw new InvalidNameException(name, "Цифровое");
        }
    }

}
