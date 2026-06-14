package org.example.coreservice.service;


import org.example.coreservice.entity.Lesson;
import org.example.coreservice.entity.ScheduleEntry;

import java.time.LocalDate;
import java.util.List;

public interface LessonService {
    List<Lesson> getTeacherLessonsForDate(Long id, LocalDate date);

    Lesson createLessonFromScheduleEntry(String traceId, ScheduleEntry entry, LocalDate date);
}
