package org.example.report.export;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;
import org.example.domain.repository.SessionRepository;
import org.example.domain.repository.TicketRepository;
import org.example.domain.repository.inmemory.InMemorySessionRepository;
import org.example.domain.repository.inmemory.InMemoryTicketRepository;
import org.example.report.AttendanceReportService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportExportServiceTest {
    @Test
    void exportsReportsUsingRequestedStrategy() {
        SessionRepository sessionRepository = new InMemorySessionRepository();
        TicketRepository ticketRepository = new InMemoryTicketRepository();

        Session session = new Session(
                UUID.randomUUID(),
                new Movie(UUID.randomUUID(), "Dune", 166, "Sci-Fi", "16+"),
                new Hall(UUID.randomUUID(), "Blue Hall", 5, 5),
                LocalDateTime.of(2026, 4, 8, 18, 0),
                new BigDecimal("450")
        );

        sessionRepository.save(session);
        ticketRepository.save(new Ticket(
                UUID.randomUUID(),
                session.getId(),
                new Seat(1, 1),
                "Alice",
                TicketStatus.PURCHASED,
                session.getPrice(),
                LocalDateTime.of(2026, 4, 8, 17, 30)
        ));

        ReportExportService reportExportService = new ReportExportService(
                new AttendanceReportService(sessionRepository, ticketRepository),
                List.of(
                        new TextReportExportStrategy(),
                        new CsvReportExportStrategy(),
                        new JsonReportExportStrategy()
                )
        );

        String csv = reportExportService.exportSessionReport(session.getId(), "csv");
        String json = reportExportService.exportAllSessionsReport("json");

        assertTrue(csv.startsWith("sessionId,movieTitle,startTime"));
        assertTrue(csv.contains("Dune"));
        assertTrue(json.contains("\"movieTitle\": \"Dune\""));
    }
}
