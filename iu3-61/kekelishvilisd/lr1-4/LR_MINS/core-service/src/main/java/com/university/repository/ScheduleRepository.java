package com.university.repository;

import com.university.exceptions.InvalidNameException;
import com.university.exceptions.StudentAlreadyRegisteredException;
import com.university.model.Lesson;
import java.util.List;

public interface ScheduleRepository {
    void addLesson(Lesson lesson) throws InvalidNameException;
    Lesson getLessonById(int id);
    List<Lesson> getLessonsByDay(String dayOfWeek);
    List<Lesson> getAllLessons();
    int getNextId();
    void setNextId(int id);
}
