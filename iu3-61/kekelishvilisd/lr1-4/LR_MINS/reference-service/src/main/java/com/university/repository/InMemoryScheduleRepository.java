package com.university.repository;

import com.university.exceptions.InvalidNameException;
import com.university.model.Lesson;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryScheduleRepository implements ScheduleRepository {
    private final Map<Integer, Lesson> lessons = new HashMap<>();
    private int nextId = 1;

    @Override
    public void addLesson(Lesson lesson) throws InvalidNameException {
        lessons.put(lesson.getId(), lesson);
    }

    @Override
    public Lesson getLessonById(int id) {
        return lessons.get(id);
    }

    @Override
    public List<Lesson> getLessonsByDay(String dayOfWeek) {
        return lessons.values().stream()
                .filter(l -> l.getDateTime().startsWith(dayOfWeek))
                .collect(Collectors.toList());
    }

    @Override
    public List<Lesson> getAllLessons() {
        return new ArrayList<>(lessons.values());
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
