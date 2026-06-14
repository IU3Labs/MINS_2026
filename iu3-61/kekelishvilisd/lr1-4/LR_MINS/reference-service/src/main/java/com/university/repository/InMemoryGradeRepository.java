package com.university.repository;

import com.university.model.Grade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryGradeRepository implements GradeRepository {

    private final List<Grade> storage = new ArrayList<>();

    @Override
    public void save(Grade grade) {
        storage.add(grade);
    }

    @Override
    public List<Grade> findByStudentId(int studentId) {
        return storage.stream()
                .filter(grade -> grade.getStudent().getId() == studentId)
                .collect(Collectors.toList());
    }

}
