package ru.mins.parking.core.util;

import ru.mins.parking.core.exception.ParkingException;
import ru.mins.parking.core.model.SpotType;
import ru.mins.parking.core.model.TariffType;
import ru.mins.parking.core.model.Vehicle;
import ru.mins.parking.core.model.VehicleType;
import ru.mins.parking.core.service.ParkingCoreService;

import java.util.logging.Logger;

public class DemoDataInitializer {
    private static final Logger LOGGER = Logger.getLogger(DemoDataInitializer.class.getName());

    private final ParkingCoreService parkingCoreService;

    public DemoDataInitializer(ParkingCoreService parkingCoreService) {
        this.parkingCoreService = parkingCoreService;
    }

    public void populate(SpotType regular, SpotType disabled, SpotType electric) {
        run(() -> parkingCoreService.addParkingSpot(1, regular));
        run(() -> parkingCoreService.addParkingSpot(2, regular));
        run(() -> parkingCoreService.addParkingSpot(3, disabled));
        run(() -> parkingCoreService.addParkingSpot(4, electric));
        run(() -> parkingCoreService.addParkingSpot(5, electric));
        run(() -> parkingCoreService.addParkingSpot(6, regular));
        run(() -> parkingCoreService.registerEntry(
                new Vehicle("A123AA77", VehicleType.CAR),
                SpotType.REGULAR,
                TariffType.HOURLY
        ));
        run(() -> parkingCoreService.registerEntry(
                new Vehicle("E777EK77", VehicleType.ELECTRIC_CAR),
                SpotType.ELECTRIC,
                TariffType.NIGHT
        ));
    }

    private void run(Runnable action) {
        TraceContext.set(TraceIdGenerator.newTraceId());
        try {
            action.run();
        } catch (ParkingException exception) {
            LogUtil.error(LOGGER, "Demo initialization skipped: " + exception.getMessage());
        } finally {
            TraceContext.clear();
        }
    }
}
