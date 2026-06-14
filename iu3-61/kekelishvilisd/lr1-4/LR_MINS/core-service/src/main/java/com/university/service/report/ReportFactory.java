package com.university.service.report;

import com.university.model.Grade;
import com.university.model.AttendanceRecord;
import java.util.List;

public interface ReportFactory {
    Report createReport(ReportType type, int studentId, String studentName,
                        List<Grade> grades, List<AttendanceRecord> attendance);

    enum ReportType {
        GRADES, ATTENDANCE, SUMMARY
    }
}
