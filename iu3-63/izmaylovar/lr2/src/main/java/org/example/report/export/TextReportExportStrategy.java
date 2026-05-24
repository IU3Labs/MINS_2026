package org.example.report.export;

import org.example.domain.model.Session;
import org.example.report.AttendanceReport;

import java.util.List;
import java.util.Locale;

public class TextReportExportStrategy implements ReportExportStrategy {
    @Override
    public String format() {
        return "text";
    }

    @Override
    public String export(List<AttendanceReport> reports) {
        if (reports.isEmpty()) {
            return "No reports available.";
        }

        return reports.stream()
                .map(this::toBlock)
                .reduce((left, right) -> left + System.lineSeparator() + System.lineSeparator() + right)
                .orElse("No reports available.");
    }

    private String toBlock(AttendanceReport report) {
        return """
                Session ID: %s
                Movie: %s
                Start: %s
                Hall capacity: %d
                Reserved: %d
                Purchased: %d
                Cancelled: %d
                Occupied: %d
                Free: %d
                Occupancy: %.2f%%
                Revenue: %s
                """.formatted(
                report.getSessionId(),
                report.getMovieTitle(),
                report.getStartTime().format(Session.DISPLAY_FORMATTER),
                report.getHallCapacity(),
                report.getReservedCount(),
                report.getPurchasedCount(),
                report.getCancelledCount(),
                report.getOccupiedCount(),
                report.getFreeCount(),
                report.getOccupancyPercent(),
                report.getRevenue().toPlainString()
        ).stripTrailing();
    }
}
