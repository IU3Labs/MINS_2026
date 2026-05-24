package org.example.report.export;

import org.example.report.AttendanceReport;

import java.util.List;

public interface ReportExportStrategy {
    String format();

    String export(List<AttendanceReport> reports);
}
