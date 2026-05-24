package org.example.bootstrap;

import org.example.console.ConsoleInputHandler;
import org.example.console.ConsolePrinter;
import org.example.domain.repository.HallRepository;
import org.example.domain.repository.MovieRepository;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.domain.repository.inmemory.InMemoryHallRepository;
import org.example.domain.repository.inmemory.InMemoryMovieRepository;
import org.example.domain.repository.inmemory.InMemorySessionRepository;
import org.example.domain.repository.inmemory.InMemoryTicketRepository;
import org.example.legacypricing.LegacyQuickPriceCalculator;
import org.example.report.AttendanceReportService;
import org.example.report.export.CsvReportExportStrategy;
import org.example.report.export.JsonReportExportStrategy;
import org.example.report.export.ReportExportService;
import org.example.report.export.TextReportExportStrategy;
import org.example.service.CinemaService;
import org.example.service.HallService;
import org.example.service.MovieService;
import org.example.service.SeatAvailabilityService;
import org.example.service.SessionService;
import org.example.service.factory.PurchasedTicketFactory;
import org.example.service.factory.ReservedTicketFactory;
import org.example.service.factory.TicketFactoryRegistry;
import org.example.service.subscription.SessionSubscriptionService;
import org.example.service.validation.CustomerNameValidationHandler;
import org.example.service.validation.SeatAvailabilityValidationHandler;
import org.example.service.validation.SeatExistsValidationHandler;
import org.example.service.validation.TicketIssuingValidationHandler;

import java.util.List;

public class DataInitializer {
    public CinemaApplication init() {
        HallRepository hallRepository = new InMemoryHallRepository();
        MovieRepository movieRepository = new InMemoryMovieRepository();
        SessionRepository sessionRepository = new InMemorySessionRepository();
        TicketRepository ticketRepository = new InMemoryTicketRepository();

        SessionSubscriptionService sessionSubscriptionService = new SessionSubscriptionService();
        TicketFactoryRegistry ticketFactoryRegistry = new TicketFactoryRegistry(
                List.of(new ReservedTicketFactory(), new PurchasedTicketFactory())
        );

        SeatAvailabilityService seatAvailabilityService =
                new SeatAvailabilityService(sessionRepository, ticketRepository);
        TicketIssuingValidationHandler ticketIssuingValidationHandler = new CustomerNameValidationHandler();
        ticketIssuingValidationHandler
                .linkWith(new SeatExistsValidationHandler())
                .linkWith(new SeatAvailabilityValidationHandler(seatAvailabilityService));

        MovieService movieService = new MovieService(movieRepository, sessionRepository);
        HallService hallService = new HallService(hallRepository, sessionRepository);
        SessionService sessionService = new SessionService(
                sessionRepository,
                movieRepository,
                hallRepository,
                ticketRepository,
                sessionSubscriptionService
        );

        CinemaService cinemaService = new CinemaService(
                sessionRepository,
                ticketRepository,
                seatAvailabilityService,
                ticketFactoryRegistry,
                sessionSubscriptionService,
                ticketIssuingValidationHandler
        );
        LegacyQuickPriceCalculator legacyQuickPriceCalculator = new LegacyQuickPriceCalculator();

        AttendanceReportService attendanceReportService =
                new AttendanceReportService(sessionRepository, ticketRepository);
        ReportExportService reportExportService = new ReportExportService(
                attendanceReportService,
                List.of(
                        new TextReportExportStrategy(),
                        new CsvReportExportStrategy(),
                        new JsonReportExportStrategy()
                )
        );

        return new CinemaApplication(
                movieService,
                hallService,
                sessionService,
                cinemaService,
                legacyQuickPriceCalculator,
                attendanceReportService,
                reportExportService,
                sessionSubscriptionService,
                new ConsolePrinter(),
                new ConsoleInputHandler()
        );
    }
}
