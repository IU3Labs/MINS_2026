package com.university.exceptions;

public class StudentNotFoundException extends TrainingCenterException {
    public StudentNotFoundException(int id) {
        super("Студент с ID " + id + " не найден");
    }
}
