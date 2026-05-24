package org.example.console;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;
import org.example.report.AttendanceReport;
import org.example.service.subscription.SessionNotification;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConsolePrinter implements Printer {
    @Override
    public void printMovies(List<Movie> movies) {
        System.out.println("\n=== Movies ===");
        if (movies.isEmpty()) {
            System.out.println("No movies created yet.\n");
            return;
        }

        for (Movie movie : movies) {
            System.out.println(
                    "ID: " + movie.getId()
                            + " | Title: " + movie.getTitle()
                            + " | Duration: " + movie.getDurationMinutes() + " min"
                            + " | Genre: " + movie.getGenre()
                            + " | Rating: " + movie.getAgeRating()
            );
        }
        System.out.println();
    }

    @Override
    public void printHalls(List<Hall> halls) {
        System.out.println("\n=== Halls ===");
        if (halls.isEmpty()) {
            System.out.println("No halls created yet.\n");
            return;
        }

        for (Hall hall : halls) {
            System.out.println(
                    "ID: " + hall.getId()
                            + " | Name: " + hall.getName()
                            + " | Rows: " + hall.getRows()
                            + " | Seats/row: " + hall.getSeatsPerRow()
                            + " | Capacity: " + hall.getCapacity()
            );
        }
        System.out.println();
    }

    @Override
    public void printSessions(List<Session> sessions) {
        System.out.println("\n=== Sessions ===");
        if (sessions.isEmpty()) {
            System.out.println("No sessions created yet.\n");
            return;
        }

        for (Session session : sessions) {
            System.out.println(
                    "ID: " + session.getId()
                            + " | Movie: " + session.getMovie().getTitle()
                            + " | Hall: " + session.getHall().getName()
                            + " | Start: " + session.getFormattedStartTime()
                            + " | End: " + session.getEndTime().format(Session.DISPLAY_FORMATTER)
                            + " | Price: " + session.getPrice()
            );
        }
        System.out.println();
    }

    @Override
    public void printTickets(List<Ticket> tickets) {
        System.out.println("\n=== Tickets ===");
        if (tickets.isEmpty()) {
            System.out.println("No tickets found.\n");
            return;
        }

        for (Ticket ticket : tickets) {
            System.out.println(
                    "ID: " + ticket.getId()
                            + " | Session: " + ticket.getSessionId()
                            + " | Seat: row " + ticket.getSeat().getRow() + ", seat " + ticket.getSeat().getNumber()
                            + " | Customer: " + (ticket.getCustomerName() == null ? "-" : ticket.getCustomerName())
                            + " | Status: " + ticket.getStatus()
                            + " | Price: " + ticket.getPrice()
            );
        }
        System.out.println();
    }

    @Override
    public void printSeatMap(Session session, List<Ticket> tickets) {
        Map<Seat, TicketStatus> statuses = new LinkedHashMap<>();
        for (Ticket ticket : tickets) {
            statuses.put(ticket.getSeat(), ticket.getStatus());
        }

        System.out.println("\n=== Seat Map ===");
        System.out.println("Movie: " + session.getMovie().getTitle());
        System.out.println("Hall: " + session.getHall().getName());
        System.out.println("Start: " + session.getFormattedStartTime());

        for (int row = 1; row <= session.getHall().getRows(); row++) {
            System.out.print("Row " + row + ": ");
            for (int seatNumber = 1; seatNumber <= session.getHall().getSeatsPerRow(); seatNumber++) {
                Seat seat = new Seat(row, seatNumber);
                TicketStatus status = statuses.get(seat);

                String symbol;
                if (status == null || status == TicketStatus.CANCELLED) {
                    symbol = "[O]";
                } else if (status == TicketStatus.RESERVED) {
                    symbol = "[R]";
                } else {
                    symbol = "[X]";
                }

                System.out.print(symbol);
            }
            System.out.println();
        }

        System.out.println("[O] free | [R] reserved | [X] purchased\n");
    }

    @Override
    public void printTicket(Ticket ticket) {
        System.out.println("\n=== Ticket ===");
        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Session ID: " + ticket.getSessionId());
        System.out.println("Seat: row " + ticket.getSeat().getRow() + ", seat " + ticket.getSeat().getNumber());
        System.out.println("Customer: " + (ticket.getCustomerName() == null ? "-" : ticket.getCustomerName()));
        System.out.println("Status: " + ticket.getStatus());
        System.out.println("Price: " + ticket.getPrice());
        System.out.println();
    }

    @Override
    public void printSessionReport(AttendanceReport report) {
        System.out.println("\n=== Session Report ===");
        System.out.println("Session ID: " + report.getSessionId());
        System.out.println("Movie: " + report.getMovieTitle());
        System.out.println("Start: " + report.getStartTime().format(Session.DISPLAY_FORMATTER));
        System.out.println("Hall capacity: " + report.getHallCapacity());
        System.out.println("Reserved: " + report.getReservedCount());
        System.out.println("Purchased: " + report.getPurchasedCount());
        System.out.println("Cancelled: " + report.getCancelledCount());
        System.out.println("Occupied: " + report.getOccupiedCount());
        System.out.println("Free: " + report.getFreeCount());
        System.out.printf("Occupancy: %.2f%%%n", report.getOccupancyPercent());
        System.out.println("Revenue: " + report.getRevenue());
        System.out.println();
    }

    @Override
    public void printAllReports(List<AttendanceReport> reports) {
        if (reports.isEmpty()) {
            System.out.println("\nNo reports available.\n");
            return;
        }

        for (AttendanceReport report : reports) {
            printSessionReport(report);
        }
    }

    @Override
    public void printSubscribers(Session session, List<String> subscribers) {
        System.out.println("\n=== Session Subscribers ===");
        System.out.println("Session: " + session.getId() + " | " + session.getMovie().getTitle());
        if (subscribers.isEmpty()) {
            System.out.println("No subscribers.\n");
            return;
        }

        for (String subscriber : subscribers) {
            System.out.println(subscriber);
        }
        System.out.println();
    }

    @Override
    public void printNotifications(String customerName, List<SessionNotification> notifications) {
        System.out.println("\n=== Notifications for " + customerName + " ===");
        if (notifications.isEmpty()) {
            System.out.println("No notifications.\n");
            return;
        }

        for (SessionNotification notification : notifications) {
            System.out.println(
                    notification.createdAt().format(Session.DISPLAY_FORMATTER)
                            + " | " + notification.type()
                            + " | Session: " + notification.sessionId()
                            + " | " + notification.message()
            );
        }
        System.out.println();
    }

    @Override
    public void printError(String message) {
        System.out.println("\n[ERROR] " + message + "\n");
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
