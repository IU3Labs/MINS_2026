package com.university.repository;


import com.university.exceptions.StudentNotFoundException;
import com.university.model.Student;
import java.util.*;


public class InMemoryStudentRepository implements StudentRepository {
    private final Map<Integer, Student> storage = new HashMap<>();
    private int nextId = 1;

    public InMemoryStudentRepository() {
        //loadFromFile();
    }

    @Override
    public void save(Student student) {
        storage.put(student.getId(), student);
    }

    @Override
    public Student findById(int id) throws StudentNotFoundException {
        Student student = storage.get(id);
        if (student == null) {
            throw new StudentNotFoundException(id);
        }
        return student;
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(storage.values());
    }


    @Override
    public int getNextId() {
        return nextId++;
    }

    @Override
    public void setNextId(int id) {
        this.nextId = id;
    }



}
