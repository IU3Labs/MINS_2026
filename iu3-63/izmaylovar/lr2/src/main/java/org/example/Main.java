package org.example;

import org.example.bootstrap.DataInitializer;
import org.example.console.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        var application = new DataInitializer().init();
        ConsoleMenu menu = new ConsoleMenu(
                application.movieService(),
                application.hallService(),
                application.sessionService(),
                application.cinemaService(),
                application.attendanceReportService(),
                application.reportExportService(),
                application.sessionSubscriptionService(),
                application.printer(),
                application.inputHandler()
        );

        menu.start();
    }
}
