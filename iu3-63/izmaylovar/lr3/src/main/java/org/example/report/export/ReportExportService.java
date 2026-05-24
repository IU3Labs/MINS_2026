package org.example.report.export;

import org.example.domain.exception.ValidationException;
import org.example.report.AttendanceReport;
import org.example.report.AttendanceReportService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ReportExportService {
    private final AttendanceReportService attendanceReportService;
    private final Map<String, ReportExportStrategy> strategies = new LinkedHashMap<>();

    public ReportExportService(AttendanceReportService attendanceReportService,
                               List<ReportExportStrategy> exportStrategies) {
        this.attendanceReportService = attendanceReportService;
        for (ReportExportStrategy strategy : exportStrategies) {
            strategies.put(strategy.format().toLowerCase(Locale.ROOT), strategy);
        }
    }

    public String exportSessionReport(UUID sessionId, String format) {
        AttendanceReport report = attendanceReportService.generateSessionReport(sessionId);
        return resolve(format).export(List.of(report));
    }

    public String exportAllSessionsReport(String format) {
        return resolve(format).export(attendanceReportService.generateAllSessionsReport());
    }

    public List<String> supportedFormats() {
        return List.copyOf(strategies.keySet());
    }

    private ReportExportStrategy resolve(String format) {
        String normalized = format == null ? "" : format.trim().toLowerCase(Locale.ROOT);
        ReportExportStrategy strategy = strategies.get(normalized);
        if (strategy == null) {
            throw new ValidationException("Unknown export format. Available formats: " + String.join(", ", supportedFormats()));
        }
        return strategy;
    }
}
