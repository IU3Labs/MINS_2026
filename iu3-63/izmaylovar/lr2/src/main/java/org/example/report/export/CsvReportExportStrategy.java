package org.example.report.export;

import org.example.domain.model.Session;
import org.example.report.AttendanceReport;

import java.util.List;

public class CsvReportExportStrategy implements ReportExportStrategy {
    @Override
    public String format() {
        return "csv";
    }

    @Override
    public String export(List<AttendanceReport> reports) {
        String header = "sessionId,movieTitle,startTime,hallCapacity,reserved,purchased,cancelled,occupied,free,occupancyPercent,revenue";
        if (reports.isEmpty()) {
            return header;
        }

        String body = reports.stream()
                .map(this::toLine)
                .reduce((left, right) -> left + System.lineSeparator() + right)
                .orElse("");

        return header + System.lineSeparator() + body;
    }

    private String toLine(AttendanceReport report) {
        return String.join(",",
                report.getSessionId().toString(),
                escape(report.getMovieTitle()),
                report.getStartTime().format(Session.DISPLAY_FORMATTER),
                String.valueOf(report.getHallCapacity()),
                String.valueOf(report.getReservedCount()),
                String.valueOf(report.getPurchasedCount()),
                String.valueOf(report.getCancelledCount()),
                String.valueOf(report.getOccupiedCount()),
                String.valueOf(report.getFreeCount()),
                String.format(java.util.Locale.US, "%.2f", report.getOccupancyPercent()),
                report.getRevenue().toPlainString()
        );
    }

    private String escape(String value) {
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
