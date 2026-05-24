package org.example.console;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.report.AttendanceReport;
import org.example.service.subscription.SessionNotification;

import java.util.List;

public interface Printer {
    void printMovies(List<Movie> movies);

    void printHalls(List<Hall> halls);

    void printSessions(List<Session> sessions);

    void printTickets(List<Ticket> tickets);

    void printSeatMap(Session session, List<Ticket> tickets);

    void printTicket(Ticket ticket);

    void printSessionReport(AttendanceReport report);

    void printAllReports(List<AttendanceReport> reports);

    void printSubscribers(Session session, List<String> subscribers);

    void printNotifications(String customerName, List<SessionNotification> notifications);

    void printError(String message);

    void printMessage(String message);
}
