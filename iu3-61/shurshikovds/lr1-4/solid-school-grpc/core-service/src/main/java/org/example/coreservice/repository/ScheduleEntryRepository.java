package org.example.coreservice.repository;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.entity.enums.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {

    @Query("""
    SELECT s FROM ScheduleEntry s
    WHERE s.schoolClassId = :classId
    ORDER BY s.dayOfWeek ASC, s.startTime ASC
""")
    List<ScheduleEntry> findAllBySchoolClassIdOrderByDayOfWeekAscStartTimeAsc(@Param("classId") Long classId);

    @Query("""
    SELECT s FROM ScheduleEntry s
    WHERE s.teacherId = :teacherId
    ORDER BY s.dayOfWeek ASC, s.startTime ASC
""")
    List<ScheduleEntry> findAllByTeacherIdOrderByDayOfWeekAscStartTimeAsc(@Param("teacherId") Long teacherId);

    boolean existsByDayOfWeekAndStartTimeAndSchoolClassId(Weekday day, LocalTime startTime, Long id);

    boolean existsByDayOfWeekAndStartTimeAndTeacherId(Weekday dayOfWeek, LocalTime startTime, Long teacherId);

    boolean existsByDayOfWeekAndStartTimeAndRoom(Weekday dayOfWeek, LocalTime startTime, String room);
}

