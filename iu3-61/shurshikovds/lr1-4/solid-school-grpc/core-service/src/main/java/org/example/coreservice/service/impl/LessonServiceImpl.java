package org.example.coreservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coreservice.entity.Lesson;
import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.exception.ValidationException;
import org.example.coreservice.repository.LessonRepository;
import org.example.coreservice.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final Logger log = LoggerFactory.getLogger(LessonServiceImpl.class);

    @Override
    public List<Lesson> getTeacherLessonsForDate(Long id, LocalDate date) {
        return lessonRepository
                .findAllByTeacherIdAndLessonDateOrderByStartTimeAsc(id, date);
    }

    @Override
    @Transactional
    public Lesson createLessonFromScheduleEntry(String traceId, ScheduleEntry entry, LocalDate date) {
        log.info("[traceId={}] createLessonFromScheduleEntry: scheduleEntryId={} date={}",
                traceId, entry.getId(), date);

        if (lessonRepository.existsByScheduleEntryIdAndLessonDate(entry.getId(), date)) {
            throw new ValidationException(
                    "Урок по этому расписанию на " + date + " уже проведён");
        }

        Lesson lesson = Lesson.builder()
                .scheduleEntry(entry)
                .title(entry.getTitle())
                .lessonDate(date)
                .startTime(entry.getStartTime())
                .room(entry.getRoom())
                .teacherId(entry.getTeacherId())         // было: entry.getTeacher()
                .schoolClassId(entry.getSchoolClassId()) // было: entry.getSchoolClass()
                .build();

        Lesson saved = lessonRepository.save(lesson);
        log.info("[traceId={}] createLessonFromScheduleEntry: урок создан id={}", traceId, saved.getId());
        return saved;
    }

}
