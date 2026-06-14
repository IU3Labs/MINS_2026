package org.example.coreservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.example.coreservice.entity.ScheduleEntry;
import org.example.coreservice.entity.enums.Weekday;
import org.example.coreservice.exception.ValidationException;
import org.example.coreservice.grpc.client.ReferenceServiceGateway;
import org.example.coreservice.repository.ScheduleEntryRepository;
import org.example.coreservice.service.ScheduleService;
import org.example.reference.grpc.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleEntryRepository scheduleEntryRepository;
    public final ReferenceServiceGateway referenceGateway;
    private final Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Override
    public List<ScheduleEntry> getClassSchedule(Long classId) {
        return scheduleEntryRepository
                .findAllBySchoolClassIdOrderByDayOfWeekAscStartTimeAsc(classId);
    }

    @Override
    public List<ScheduleEntry> getTeacherSchedule(Long id) {
        return scheduleEntryRepository
                .findAllByTeacherIdOrderByDayOfWeekAscStartTimeAsc(id);
    }

    @Override
    public void deleteScheduleEntry(Long id) {
        scheduleEntryRepository.deleteById(id);
    }

    @Override
    public void addScheduleEntry(String traceId, String title, Weekday day,
                                 LocalTime startTime, String room,
                                 Long schoolClassId, Long teacherId) {
        log.info("[traceId={}] addScheduleEntry: classId={} teacherId={} day={} time={}",
                traceId, schoolClassId, teacherId, day, startTime);

        ValidationResponse teacherCheck =
                referenceGateway.validateTeacherExists(traceId, teacherId);
        if (!teacherCheck.getValid()) {
            throw new ValidationException(teacherCheck.getMessage());
        }

        ValidationResponse classCheck =
                referenceGateway.validateSchoolClassExists(traceId, schoolClassId);
        if (!classCheck.getValid()) {
            throw new ValidationException(classCheck.getMessage());
        }

        checkClassAndTime(day, startTime, schoolClassId);
        checkRoomAndTime(day, startTime, room);
        checkTeacherAndTime(day, startTime, teacherId);

        ScheduleEntry entry = ScheduleEntry.builder()
                .title(title)
                .dayOfWeek(day)
                .startTime(startTime)
                .room(room)
                .schoolClassId(schoolClassId)
                .teacherId(teacherId)
                .build();

        scheduleEntryRepository.save(entry);
        log.info("[traceId={}] addScheduleEntry: запись расписания сохранена", traceId);
    }

    private void checkTeacherAndTime(Weekday day, LocalTime startTime, Long teacherId) {
        if (scheduleEntryRepository.existsByDayOfWeekAndStartTimeAndTeacherId(
                day, startTime, teacherId)) {
            throw new ValidationException(
                    "У учителя этого учителя уже есть урок в " + day + " в " + startTime
            );
        }
    }

    private void checkRoomAndTime(Weekday day, LocalTime startTime, String room) {
        if (scheduleEntryRepository.existsByDayOfWeekAndStartTimeAndRoom(
                day, startTime, room)) {
            throw new ValidationException(
                    "В кабинете" + room
                    + " уже есть урок в " + day + " в " + startTime
            );
        }
    }

    public void checkClassAndTime(Weekday day, LocalTime startTime, Long schoolClassId) {
        if (scheduleEntryRepository.existsByDayOfWeekAndStartTimeAndSchoolClassId(
                day, startTime, schoolClassId)) {
            throw new ValidationException(
                    "У этого класса уже есть урок в " + day + " в " + startTime
            );
        }
    }

}
