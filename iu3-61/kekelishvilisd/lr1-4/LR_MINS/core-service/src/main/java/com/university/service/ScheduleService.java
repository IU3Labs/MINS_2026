package com.university.service;

import com.university.exceptions.InvalidNameException;
import com.university.model.*;
import com.university.repository.ScheduleRepository;
import java.util.List;


public interface ScheduleService {
    Lesson createLesson(Course course, String teacher, String room, String dateTime) throws InvalidNameException;
    List<Lesson> getScheduleForDay(String dayOfWeek);
    List<Lesson> getAllLessons();
}