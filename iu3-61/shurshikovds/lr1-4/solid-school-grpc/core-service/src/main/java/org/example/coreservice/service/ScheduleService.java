package org.example.coreservice.service;


import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.entity.enums.Weekday;

import java.time.LocalTime;
import java.util.List;

public interface ScheduleService {
    List<ScheduleEntry> getClassSchedule(Long classId);

    List<ScheduleEntry> getTeacherSchedule(Long id);

    void deleteScheduleEntry(Long id);

    void addScheduleEntry(String traceId, String title, Weekday day, LocalTime startTime, String room, Long schoolClassId, Long teacherId);
}
