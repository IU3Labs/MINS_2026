package com.university.repository;

import java.util.List;

import com.university.exceptions.InvalidNameException;
import com.university.exceptions.StudentAlreadyRegisteredException;
import com.university.exceptions.StudentNotFoundException;
import com.university.model.Grade;
import com.university.model.Student;

public interface StudentRepository {
    void save(Student student) throws StudentAlreadyRegisteredException, InvalidNameException;
    Student findById(int id) throws StudentNotFoundException;
    List<Student> findAll();
    int getNextId();
    void setNextId(int id);
}
