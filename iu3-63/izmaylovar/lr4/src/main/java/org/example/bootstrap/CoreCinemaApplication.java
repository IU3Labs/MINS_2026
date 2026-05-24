package org.example.bootstrap;

import org.example.console.ConsoleInputHandler;
import org.example.console.ConsolePrinter;
import org.example.integration.reference.ReferenceServiceClient;
import org.example.legacypricing.LegacyQuickPriceCalculator;
import org.example.report.AttendanceReportService;
import org.example.report.export.ReportExportService;
import org.example.service.CinemaService;
import org.example.service.HallOperations;
import org.example.service.MovieOperations;
import org.example.service.SessionService;
import org.example.service.subscription.SessionSubscriptionService;

public record CoreCinemaApplication(
        MovieOperations movieService,
        HallOperations hallService,
        SessionService sessionService,
        CinemaService cinemaService,
        LegacyQuickPriceCalculator legacyQuickPriceCalculator,
        AttendanceReportService attendanceReportService,
        ReportExportService reportExportService,
        SessionSubscriptionService sessionSubscriptionService,
        ConsolePrinter printer,
        ConsoleInputHandler inputHandler,
        ReferenceServiceClient referenceClient
) {
}
