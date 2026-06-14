package com.university.repository;


import com.university.model.AttendanceRecord;
import com.university.model.AttendanceStatus;
import java.util.List;
import java.time.LocalDate;

public interface AttendanceRepository {
    void markAttendance(AttendanceRecord record);
    AttendanceRecord getRecord(int studentId, int lessonId, LocalDate date);
    List<AttendanceRecord> getRecordsByStudent(int studentId);
    List<AttendanceRecord> getRecordsByLesson(int lessonId);
    void updateStatus(int recordId, AttendanceStatus newStatus);
    int getNextId();
    void setNextId(int id);
}
