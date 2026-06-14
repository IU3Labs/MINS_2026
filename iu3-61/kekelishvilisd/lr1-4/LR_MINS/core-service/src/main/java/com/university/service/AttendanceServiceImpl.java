package com.university.service;

import com.university.exceptions.StudentNotFoundException;
import com.university.model.*;
import com.university.repository.*;
import com.university.service.export.ReportExportStrategy;
import com.university.service.observer.NotificationManager;
import com.university.service.report.Report;
import com.university.service.report.ReportFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepo;
    private final StudentRepository studentRepo;
    private final ScheduleRepository scheduleRepo;
    private final NotificationManager notificationManager;
    private final ReportFactory reportFactory;

    public AttendanceServiceImpl(AttendanceRepository attRepo,
                                 StudentRepository studRepo,
                                 ScheduleRepository schedRepo,
                                 NotificationManager notificationManager,
                                 ReportFactory reportFactory) {
        this.attendanceRepo = attRepo;
        this.studentRepo = studRepo;
        this.scheduleRepo = schedRepo;
        this.notificationManager = notificationManager;
        this.reportFactory = reportFactory;
    }

    @Override
    public void markAttendance(int studentId, int lessonId, AttendanceStatus status) throws StudentNotFoundException {
        studentRepo.findById(studentId);
        Lesson lesson = scheduleRepo.getLessonById(lessonId);
        Student student = studentRepo.findById(studentId);

        AttendanceRecord record = new AttendanceRecord(
                attendanceRepo.getNextId(),
                student,
                lesson,
                LocalDate.now(),
                status
        );
        attendanceRepo.markAttendance(record);

        notificationManager.notifyObserver(student,
                "Посещаемость: " + status + " на \"" + lesson.getCourse().getName() + "\"");

    }

    @Override
    public Report getAttendanceReport(int studentId) throws StudentNotFoundException {
        Student student = studentRepo.findById(studentId);
        List<AttendanceRecord> records = attendanceRepo.getRecordsByStudent(studentId);

        return reportFactory.createReport(
                ReportFactory.ReportType.ATTENDANCE,
                studentId,
                student.getName(),
                new ArrayList<>(),
                records
        );
    }

    @Override
    public void exportReport(Report report, ReportExportStrategy strategy) {
        strategy.export(report);
    }
}
