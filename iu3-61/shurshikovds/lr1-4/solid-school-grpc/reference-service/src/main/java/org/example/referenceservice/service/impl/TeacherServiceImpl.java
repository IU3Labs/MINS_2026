package org.example.referenceservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.example.referenceservice.entity.Teacher;
import org.example.referenceservice.repository.TeacherRepository;
import org.example.referenceservice.service.TeacherService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public void addTeacher(String lastName, String firstName, String middleName, LocalDate birthDate, String specialty) {
        Teacher teacher = Teacher.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .birthDate(birthDate)
                .specialty(specialty)
                .build();

        teacherRepository.save(teacher);
    }

    @Override
    public Boolean existsById(Long id) {
        return teacherRepository.existsById(id);
    }

    @Override
    public Teacher getById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher with id " + id + " not found"));
    }
}
