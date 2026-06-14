package com.university.repository;

import com.university.model.Grade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface GradeRepository {

    public void save(Grade grade);

    public List<Grade> findByStudentId(int studentId);
}

