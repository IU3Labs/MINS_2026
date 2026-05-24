package org.example.report.export;

import org.example.report.AttendanceReport;

import java.util.List;
import java.util.Locale;

public class JsonReportExportStrategy implements ReportExportStrategy {
    @Override
    public String format() {
        return "json";
    }

    @Override
    public String export(List<AttendanceReport> reports) {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");

        for (int i = 0; i < reports.size(); i++) {
            AttendanceReport report = reports.get(i);
            builder.append("  {\n")
                    .append("    \"sessionId\": \"").append(report.getSessionId()).append("\",\n")
                    .append("    \"movieTitle\": \"").append(escape(report.getMovieTitle())).append("\",\n")
                    .append("    \"startTime\": \"").append(report.getStartTime()).append("\",\n")
                    .append("    \"hallCapacity\": ").append(report.getHallCapacity()).append(",\n")
                    .append("    \"reserved\": ").append(report.getReservedCount()).append(",\n")
                    .append("    \"purchased\": ").append(report.getPurchasedCount()).append(",\n")
                    .append("    \"cancelled\": ").append(report.getCancelledCount()).append(",\n")
                    .append("    \"occupied\": ").append(report.getOccupiedCount()).append(",\n")
                    .append("    \"free\": ").append(report.getFreeCount()).append(",\n")
                    .append("    \"occupancyPercent\": ")
                    .append(String.format(Locale.US, "%.2f", report.getOccupancyPercent()))
                    .append(",\n")
                    .append("    \"revenue\": \"").append(report.getRevenue().toPlainString()).append("\"\n")
                    .append("  }");

            if (i < reports.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }

        builder.append("]");
        return builder.toString();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
