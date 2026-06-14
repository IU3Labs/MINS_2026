package com.university.repository;

import com.university.model.AttendanceRecord;
import com.university.model.AttendanceStatus;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class InMemoryAttendanceRepository implements AttendanceRepository {
    private final Map<Integer, AttendanceRecord> records = new HashMap<>();
    private int nextId = 1;

    @Override
    public void markAttendance(AttendanceRecord record) {
        records.put(record.getId(), record);
    }

    @Override
    public AttendanceRecord getRecord(int studentId, int lessonId, LocalDate date) {
        return records.values().stream()
                .filter(r -> r.getStudent().getId() == studentId &&
                        r.getLesson().getId() == lessonId &&
                        r.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<AttendanceRecord> getRecordsByStudent(int studentId) {
        return records.values().stream()
                .filter(r -> r.getStudent().getId() == studentId)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceRecord> getRecordsByLesson(int lessonId) {
        return records.values().stream()
                .filter(r -> r.getLesson().getId() == lessonId)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(int recordId, AttendanceStatus newStatus) {
        AttendanceRecord record = records.get(recordId);
        if (record != null) {
            record.updateStatus(newStatus);
        }
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
