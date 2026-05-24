package org.example.console;

import org.example.domain.exception.CinemaException;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.legacypricing.LegacyQuickPriceCalculator;
import org.example.report.AttendanceReportService;
import org.example.report.export.ReportExportService;
import org.example.service.CinemaService;
import org.example.service.HallService;
import org.example.service.MovieService;
import org.example.service.SessionService;
import org.example.service.subscription.SessionSubscriptionService;

import java.util.UUID;

public class ConsoleMenu {
    private final MovieService movieService;
    private final HallService hallService;
    private final SessionService sessionService;
    private final CinemaService cinemaService;
    private final LegacyQuickPriceCalculator legacyQuickPriceCalculator;
    private final AttendanceReportService reportService;
    private final ReportExportService reportExportService;
    private final SessionSubscriptionService sessionSubscriptionService;
    private final ConsolePrinter printer;
    private final ConsoleInputHandler input;

    public ConsoleMenu(MovieService movieService,
                       HallService hallService,
                       SessionService sessionService,
                       CinemaService cinemaService,
                       LegacyQuickPriceCalculator legacyQuickPriceCalculator,
                       AttendanceReportService reportService,
                       ReportExportService reportExportService,
                       SessionSubscriptionService sessionSubscriptionService,
                       ConsolePrinter printer,
                       ConsoleInputHandler input) {
        this.movieService = movieService;
        this.hallService = hallService;
        this.sessionService = sessionService;
        this.cinemaService = cinemaService;
        this.legacyQuickPriceCalculator = legacyQuickPriceCalculator;
        this.reportService = reportService;
        this.reportExportService = reportExportService;
        this.sessionSubscriptionService = sessionSubscriptionService;
        this.printer = printer;
        this.input = input;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printer.printMessage("""

                    === Cinema Console ===
                    1. Movies
                    2. Halls
                    3. Sessions
                    4. Tickets
                    5. Reports
                    6. Subscriptions
                    0. Exit
                    """);

            try {
                switch (input.readInt("Choose action: ")) {
                    case 1 -> moviesMenu();
                    case 2 -> hallsMenu();
                    case 3 -> sessionsMenu();
                    case 4 -> ticketsMenu();
                    case 5 -> reportsMenu();
                    case 6 -> subscriptionsMenu();
                    case 0 -> running = false;
                    default -> printer.printError("Unknown menu item.");
                }
            } catch (CinemaException e) {
                printer.printError(e.getMessage());
            } catch (Exception e) {
                printer.printError("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void moviesMenu() {
        boolean running = true;
        while (running) {
            printer.printMessage("""

                    === Movies ===
                    1. List movies
                    2. Add movie
                    3. Edit movie
                    4. Delete movie
                    0. Back
                    """);

            try {
                switch (input.readInt("Choose action: ")) {
                    case 1 -> printer.printMovies(movieService.getAllMovies());
                    case 2 -> createMovie();
                    case 3 -> updateMovie();
                    case 4 -> deleteMovie();
                    case 0 -> running = false;
                    default -> printer.printError("Unknown menu item.");
                }
            } catch (MenuBackException ignored) {
            }
        }
    }

    private void hallsMenu() {
        boolean running = true;
        while (running) {
            printer.printMessage("""

                    === Halls ===
                    1. List halls
                    2. Add hall
                    3. Edit hall
                    4. Delete hall
                    0. Back
                    """);

            try {
                switch (input.readInt("Choose action: ")) {
                    case 1 -> printer.printHalls(hallService.getAllHalls());
                    case 2 -> createHall();
                    case 3 -> updateHall();
                    case 4 -> deleteHall();
                    case 0 -> running = false;
                    default -> printer.printError("Unknown menu item.");
                }
            } catch (MenuBackException ignored) {
            }
        }
    }

    private void sessionsMenu() {
        boolean running = true;
        while (running) {
            printer.printMessage("""

                    === Sessions ===
                    1. List sessions
                    2. Add session
                    3. Edit session
                    4. Delete session
                    0. Back
                    """);

            try {
                switch (input.readInt("Choose action: ")) {
                    case 1 -> printer.printSessions(sessionService.getAllSessions());
                    case 2 -> createSession();
                    case 3 -> updateSession();
                    case 4 -> deleteSession();
                    case 0 -> running = false;
                    default -> printer.printError("Unknown menu item.");
                }
            } catch (MenuBackException ignored) {
            }
        }
    }

    private void ticketsMenu() {
        boolean running = true;
        while (running) {
            printer.printMessage("""

                    === Tickets ===
                    1. List sessions
                    2. Show seat map
                    3. Reserve ticket
                    4. Buy ticket
                    5. Buy reserved ticket
                    6. Cancel ticket
                    7. List tickets for session
                    8. Legacy quick price calculator
                    0. Back
                    """);

            try {
                switch (input.readInt("Choose action: ")) {
                    case 1 -> printer.printSessions(cinemaService.getAllSessions());
                    case 2 -> showSeatMap();
                    case 3 -> reserveTicket();
                    case 4 -> buyTicket();
                    case 5 -> buyReservedTicket();
                    case 6 -> cancelTicket();
                    case 7 -> listTicketsForSession();
                    case 8 -> calculateLegacyQuickPrice();
                    case 0 -> running = false;
                    default -> printer.printError("Unknown menu item.");
                }
            } catch (MenuBackException ignored) {
            }
        }
    }

    private void reportsMenu() {
        boolean running = true;
        while (running) {
            printer.printMessage("""

                    === Reports ===
                    1. Session report
                    2. All session reports
                    3. Export session report
                    4. Export all session reports
                    0. Back
                    """);

            try {
                switch (input.readInt("Choose action: ")) {
                    case 1 -> showSessionReport();
                    case 2 -> printer.printAllReports(reportService.generateAllSessionsReport());
                    case 3 -> exportSessionReport();
                    case 4 -> exportAllReports();
                    case 0 -> running = false;
                    default -> printer.printError("Unknown menu item.");
                }
            } catch (MenuBackException ignored) {
            }
        }
    }

    private void subscriptionsMenu() {
        boolean running = true;
        while (running) {
            printer.printMessage("""

                    === Subscriptions ===
                    1. Subscribe customer to session
                    2. Unsubscribe customer from session
                    3. Show session subscribers
                    4. Show customer notifications
                    0. Back
                    """);

            try {
                switch (input.readInt("Choose action: ")) {
                    case 1 -> subscribeCustomer();
                    case 2 -> unsubscribeCustomer();
                    case 3 -> showSubscribers();
                    case 4 -> showNotifications();
                    case 0 -> running = false;
                    default -> printer.printError("Unknown menu item.");
                }
            } catch (MenuBackException ignored) {
            }
        }
    }

    private void createMovie() {
        String title = input.readStringOrBack("Title: ");
        int duration = input.readIntOrBack("Duration in minutes: ");
        String genre = input.readStringOrBack("Genre: ");
        String rating = input.readStringOrBack("Age rating: ");
        printer.printMessage("Movie created: " + movieService.createMovie(title, duration, genre, rating).getId());
    }

    private void updateMovie() {
        printer.printMovies(movieService.getAllMovies());
        UUID movieId = input.readUuidOrBack("Movie ID: ");
        String title = input.readStringOrBack("New title: ");
        int duration = input.readIntOrBack("New duration in minutes: ");
        String genre = input.readStringOrBack("New genre: ");
        String rating = input.readStringOrBack("New age rating: ");
        printer.printMessage("Movie updated: " + movieService.updateMovie(movieId, title, duration, genre, rating).getId());
    }

    private void deleteMovie() {
        printer.printMovies(movieService.getAllMovies());
        movieService.deleteMovie(input.readUuidOrBack("Movie ID to delete: "));
        printer.printMessage("Movie deleted.");
    }

    private void createHall() {
        String name = input.readStringOrBack("Hall name: ");
        int rows = input.readIntOrBack("Rows: ");
        int seatsPerRow = input.readIntOrBack("Seats per row: ");
        printer.printMessage("Hall created: " + hallService.createHall(name, rows, seatsPerRow).getId());
    }

    private void updateHall() {
        printer.printHalls(hallService.getAllHalls());
        UUID hallId = input.readUuidOrBack("Hall ID: ");
        String name = input.readStringOrBack("New hall name: ");
        int rows = input.readIntOrBack("New rows count: ");
        int seatsPerRow = input.readIntOrBack("New seats per row: ");
        printer.printMessage("Hall updated: " + hallService.updateHall(hallId, name, rows, seatsPerRow).getId());
    }

    private void deleteHall() {
        printer.printHalls(hallService.getAllHalls());
        hallService.deleteHall(input.readUuidOrBack("Hall ID to delete: "));
        printer.printMessage("Hall deleted.");
    }

    private void createSession() {
        printer.printMovies(movieService.getAllMovies());
        UUID movieId = input.readUuidOrBack("Movie ID: ");
        printer.printHalls(hallService.getAllHalls());
        UUID hallId = input.readUuidOrBack("Hall ID: ");
        var startTime = input.readDateTimeOrBack("Start time (yyyy-MM-dd HH:mm): ");
        var price = input.readBigDecimalOrBack("Price: ");
        printer.printMessage("Session created: " + sessionService.createSession(movieId, hallId, startTime, price).getId());
    }

    private void updateSession() {
        printer.printSessions(sessionService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        printer.printMovies(movieService.getAllMovies());
        UUID movieId = input.readUuidOrBack("New movie ID: ");
        printer.printHalls(hallService.getAllHalls());
        UUID hallId = input.readUuidOrBack("New hall ID: ");
        var startTime = input.readDateTimeOrBack("New start time (yyyy-MM-dd HH:mm): ");
        var price = input.readBigDecimalOrBack("New price: ");
        printer.printMessage("Session updated: " + sessionService.updateSession(sessionId, movieId, hallId, startTime, price).getId());
    }

    private void deleteSession() {
        printer.printSessions(sessionService.getAllSessions());
        sessionService.deleteSession(input.readUuidOrBack("Session ID to delete: "));
        printer.printMessage("Session deleted.");
    }

    private void showSeatMap() {
        printer.printSessions(cinemaService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        Session session = cinemaService.getSession(sessionId);
        printer.printSeatMap(session, cinemaService.getTicketsForSession(sessionId));
    }

    private void reserveTicket() {
        printer.printSessions(cinemaService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        int row = input.readIntOrBack("Row: ");
        int seat = input.readIntOrBack("Seat: ");
        String customer = input.readStringOrBack("Customer name: ");
        printer.printTicket(cinemaService.reserveTicket(sessionId, row, seat, customer));
    }

    private void buyTicket() {
        printer.printSessions(cinemaService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        int row = input.readIntOrBack("Row: ");
        int seat = input.readIntOrBack("Seat: ");
        String customer = input.readStringOrBack("Customer name: ");
        printer.printTicket(cinemaService.purchaseTicket(sessionId, row, seat, customer));
    }

    private void buyReservedTicket() {
        UUID ticketId = input.readUuidOrBack("Reserved ticket ID: ");
        printer.printTicket(cinemaService.purchaseReservedTicket(ticketId));
    }

    private void cancelTicket() {
        UUID ticketId = input.readUuidOrBack("Ticket ID: ");
        Ticket ticket = cinemaService.cancelTicket(ticketId);
        printer.printTicket(ticket);
    }

    private void listTicketsForSession() {
        printer.printSessions(cinemaService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        printer.printTickets(cinemaService.getTicketsForSession(sessionId));
    }

    private void calculateLegacyQuickPrice() {
        printer.printSessions(cinemaService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        Session session = cinemaService.getSession(sessionId);
        int row = input.readIntOrBack("Preferred row: ");
        int ticketsCount = input.readIntOrBack("Tickets count: ");
        printer.printMessage(
                legacyQuickPriceCalculator.calculate(session, row, ticketsCount).toDisplayString()
        );
    }

    private void showSessionReport() {
        printer.printSessions(sessionService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        printer.printSessionReport(reportService.generateSessionReport(sessionId));
    }

    private void exportSessionReport() {
        printer.printSessions(sessionService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        String format = readExportFormat();
        printer.printMessage(reportExportService.exportSessionReport(sessionId, format));
    }

    private void exportAllReports() {
        String format = readExportFormat();
        printer.printMessage(reportExportService.exportAllSessionsReport(format));
    }

    private String readExportFormat() {
        printer.printMessage("Available formats: " + String.join(", ", reportExportService.supportedFormats()));
        return input.readStringOrBack("Export format: ");
    }

    private void subscribeCustomer() {
        printer.printSessions(sessionService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        sessionService.getSession(sessionId);
        String customerName = input.readStringOrBack("Customer name: ");
        sessionSubscriptionService.subscribe(sessionId, customerName);
        printer.printMessage("Customer subscribed.");
    }

    private void unsubscribeCustomer() {
        printer.printSessions(sessionService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        sessionService.getSession(sessionId);
        String customerName = input.readStringOrBack("Customer name: ");
        sessionSubscriptionService.unsubscribe(sessionId, customerName);
        printer.printMessage("Customer unsubscribed.");
    }

    private void showSubscribers() {
        printer.printSessions(sessionService.getAllSessions());
        UUID sessionId = input.readUuidOrBack("Session ID: ");
        Session session = sessionService.getSession(sessionId);
        printer.printSubscribers(session, sessionSubscriptionService.getSubscribers(sessionId));
    }

    private void showNotifications() {
        String customerName = input.readStringOrBack("Customer name: ");
        printer.printNotifications(customerName, sessionSubscriptionService.getNotifications(customerName));
    }
}
