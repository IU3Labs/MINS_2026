package com.university.service;

import com.university.exceptions.InvalidNameException;
import com.university.model.*;
import com.university.repository.ScheduleRepository;
import java.util.List;

public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepo;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    @Override
    public Lesson createLesson(Course course, String teacher, String room, String dateTime) throws InvalidNameException {

        validateCourseName(course.getName());
        int id = scheduleRepo.getNextId();
        Lesson lesson = new Lesson(id, course, teacher, room, dateTime);
        scheduleRepo.addLesson(lesson);
        return lesson;
    }

    private void validateCourseName(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException(name, "Пустое имя урока");
        }
        if (name.trim().length() > 100) {
            throw new InvalidNameException(name, "Слишком длинное имя урока");
        }
        if (name.matches(".*\\d.*")) {
            throw new InvalidNameException(name, "Цифровое имя урока");
        }
    }

    @Override
    public List<Lesson> getScheduleForDay(String dayOfWeek) {
        return scheduleRepo.getLessonsByDay(dayOfWeek);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return scheduleRepo.getAllLessons();
    }
}
