package org.example.referenceservice.service;


import org.example.referenceservice.entity.SchoolClass;

import java.util.List;

public interface SchoolClassService {
    List<SchoolClass> getAll();
    SchoolClass getClassByGradeAndLetter(Short grade, String letter);

    SchoolClass getClassWithStudents(Long id);

    Boolean existsById(Long id);

    SchoolClass getById(Long id);
}
