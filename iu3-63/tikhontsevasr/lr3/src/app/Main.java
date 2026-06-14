package app;

import badmodule.BadParkingCalculator;
import factory.DisabledSpotFactory;
import factory.ElectricSpotFactory;
import factory.ParkingSpotFactoryRegistry;
import factory.RegularSpotFactory;
import observer.AdminNotificationObserver;
import observer.LoggerObserver;
import observer.ParkingEventManager;
import report.OccupancyReportService;
import repository.InMemoryParkingSessionRepository;
import repository.InMemoryParkingSpotRepository;
import repository.ParkingSessionRepository;
import repository.ParkingSpotRepository;
import service.ParkingLotService;
import service.ParkingService;
import tariff.DailyTariffStrategy;
import tariff.HourlyTariffStrategy;
import tariff.NightTariffStrategy;
import tariff.TariffCatalog;
import ui.ConsoleUI;
import util.DemoDataInitializer;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        ParkingSpotRepository spotRepository = new InMemoryParkingSpotRepository();
        ParkingSessionRepository sessionRepository = new InMemoryParkingSessionRepository();

        ParkingSpotFactoryRegistry factoryRegistry = new ParkingSpotFactoryRegistry(List.of(
                new RegularSpotFactory(),
                new DisabledSpotFactory(),
                new ElectricSpotFactory()
        ));

        TariffCatalog tariffCatalog = new TariffCatalog(List.of(
                new HourlyTariffStrategy(BigDecimal.valueOf(100)),
                new DailyTariffStrategy(BigDecimal.valueOf(1200)),
                new NightTariffStrategy(BigDecimal.valueOf(180), BigDecimal.valueOf(50))
        ));

        ParkingEventManager eventManager = new ParkingEventManager();
        eventManager.subscribe(new LoggerObserver());
        eventManager.subscribe(new AdminNotificationObserver());

        ParkingService parkingService = new ParkingLotService(
                spotRepository,
                sessionRepository,
                factoryRegistry,
                tariffCatalog,
                eventManager,
                Clock.systemDefaultZone(),
                80.0
        );

        OccupancyReportService occupancyReportService = new OccupancyReportService(spotRepository, sessionRepository);
        BadParkingCalculator badParkingCalculator = new BadParkingCalculator();

        new DemoDataInitializer(parkingService).populate();
        new ConsoleUI(parkingService, occupancyReportService, badParkingCalculator).start();
    }
}
