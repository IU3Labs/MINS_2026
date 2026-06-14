package com.university.service.report;

import com.university.model.AttendanceRecord;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceReport implements Report {
    private final int studentId;
    private final String studentName;
    private final List<AttendanceRecord> records;

    public AttendanceReport(int studentId, String studentName, List<AttendanceRecord> records) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.records = records;
    }

    @Override
    public String getTitle() {
        return "ОТЧЕТ ПО ПОСЕЩАЕМОСТИ | Студент: " + studentName + " (ID:" + studentId + ")";
    }

    @Override
    public List<String> getRows() {
        return records.stream()
                .map(r -> r.getLesson().getCourse().getName() + " | " + r.getDate() + " | " + r.getStatus())
                .collect(Collectors.toList());
    }

    @Override
    public String getSummary() {
        long present = records.stream()
                .filter(r -> r.getStatus() == com.university.model.AttendanceStatus.PRESENT)
                .count();
        return "Посещено: " + present + " из " + records.size() +
                " (" + (records.isEmpty() ? 0 : Math.round((double)present / records.size() * 100)) + "%)";
    }
}