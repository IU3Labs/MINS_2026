package org.example.bootstrap;

import org.example.console.ConsoleInputHandler;
import org.example.console.ConsolePrinter;
import org.example.report.AttendanceReportService;
import org.example.report.export.ReportExportService;
import org.example.service.CinemaService;
import org.example.service.HallService;
import org.example.service.MovieService;
import org.example.service.SessionService;
import org.example.service.subscription.SessionSubscriptionService;

public record CinemaApplication(
        MovieService movieService,
        HallService hallService,
        SessionService sessionService,
        CinemaService cinemaService,
        AttendanceReportService attendanceReportService,
        ReportExportService reportExportService,
        SessionSubscriptionService sessionSubscriptionService,
        ConsolePrinter printer,
        ConsoleInputHandler inputHandler
) {
}
