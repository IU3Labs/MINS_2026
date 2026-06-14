package com.university.service.report;

import com.university.model.AttendanceRecord;
import com.university.model.Grade;

import java.util.List;

public class ReportFactoryImpl implements ReportFactory {

    @Override
    public Report createReport(ReportType type,
                                      int studentId,
                                      String studentName,
                                      List<Grade> grades,
                                      List<AttendanceRecord> attendance) {
        switch (type) {
            case GRADES:
                return new GradeReport(studentId, studentName, grades);
            case ATTENDANCE:
                return new AttendanceReport(studentId, studentName, attendance);
            default:
                return new GradeReport(studentId, studentName, grades);
        }
    }
}
