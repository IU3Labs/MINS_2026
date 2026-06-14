package com.university.service;


import com.university.exceptions.StudentNotFoundException;
import com.university.model.AttendanceStatus;
import com.university.service.export.ReportExportStrategy;
import com.university.service.report.Report;

import java.util.List;

public interface AttendanceService {
    void markAttendance(int studentId, int lessonId, AttendanceStatus status) throws StudentNotFoundException;
    Report getAttendanceReport(int studentId) throws StudentNotFoundException;
    void exportReport(Report report, ReportExportStrategy strategy);
}
