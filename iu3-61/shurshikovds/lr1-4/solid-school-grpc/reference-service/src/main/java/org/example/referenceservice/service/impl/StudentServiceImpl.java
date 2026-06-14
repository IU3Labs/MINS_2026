package org.example.referenceservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.example.referenceservice.entity.SchoolClass;
import org.example.referenceservice.entity.Student;
import org.example.referenceservice.repository.SchoolClassRepository;
import org.example.referenceservice.repository.StudentRepository;
import org.example.referenceservice.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Override
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public void addStudent(String lastName, String firstName, String middleName, LocalDate birthDate, SchoolClass schoolClass) {
        SchoolClass managedSchoolClass = schoolClassRepository.findById(schoolClass.getId())
                .orElseThrow(() -> new EntityNotFoundException("Учитель не найден"));

        Student student = Student.builder()
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .birthDate(birthDate)
                .schoolClass(managedSchoolClass)
                .build();

        studentRepository.save(student);
    }

    @Override
    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getById(Long id) {
        return studentRepository.findByStudentId(id).orElseThrow(() -> new EntityNotFoundException("Не найден студент: " + id));
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getByClassId(Long classId) {
        return studentRepository.findBySchoolClassId(classId);
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
}
