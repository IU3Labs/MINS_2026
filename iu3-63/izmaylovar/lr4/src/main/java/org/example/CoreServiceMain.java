package org.example;

import org.example.bootstrap.CoreDataInitializer;
import org.example.console.ConsoleMenu;
import org.example.infrastructure.trace.ServiceLogger;

public class CoreServiceMain {
    private static final ServiceLogger LOGGER = ServiceLogger.forComponent("core-service", CoreServiceMain.class);

    public static void main(String[] args) {
        String referenceHost = System.getProperty("reference.host", "localhost");
        int referencePort = Integer.getInteger("reference.port", 9090);

        var application = new CoreDataInitializer(referenceHost, referencePort).init();
        LOGGER.info("Core service started. Reference service endpoint: " + referenceHost + ":" + referencePort);

        try {
            ConsoleMenu menu = new ConsoleMenu(
                    application.movieService(),
                    application.hallService(),
                    application.sessionService(),
                    application.cinemaService(),
                    application.legacyQuickPriceCalculator(),
                    application.attendanceReportService(),
                    application.reportExportService(),
                    application.sessionSubscriptionService(),
                    application.printer(),
                    application.inputHandler()
            );
            menu.start();
        } finally {
            application.referenceClient().close();
            LOGGER.info("Core service stopped");
        }
    }
}
