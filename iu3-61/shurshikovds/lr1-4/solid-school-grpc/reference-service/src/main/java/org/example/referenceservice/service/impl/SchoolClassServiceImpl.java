package org.example.referenceservice.service.impl;

import lombok.RequiredArgsConstructor;

import org.example.referenceservice.entity.SchoolClass;
import org.example.referenceservice.exception.MyEntityNotFoundException;
import org.example.referenceservice.repository.SchoolClassRepository;
import org.example.referenceservice.service.SchoolClassService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;

    @Override
    public List<SchoolClass> getAll() {
        return schoolClassRepository.findAll();
    }

    @Override
    public SchoolClass getClassByGradeAndLetter(Short grade, String letter) {
        return schoolClassRepository.findByGradeAndLetter(grade, letter)
                .orElseThrow(() -> new MyEntityNotFoundException("Класс " + grade + letter + " не найден"));
    }

    @Override
    public SchoolClass getClassWithStudents(Long id) {
        return schoolClassRepository.findByIdWithStudents(id)
                .orElseThrow(() -> new MyEntityNotFoundException("Класс " + id + " не найден."));
    }

    @Override
    public Boolean existsById(Long id) {
        return schoolClassRepository.existsById(id);
    }

    @Override
    public SchoolClass getById(Long id) {
        return schoolClassRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Класс с id " + id + " не найден."));
    }
}
