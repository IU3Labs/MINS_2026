package util;

import model.SpotType;
import model.Vehicle;
import model.VehicleType;
import service.ParkingService;
import tariff.TariffStrategy;
import tariff.TariffType;

public class DemoDataInitializer {
    private final ParkingService parkingService;

    public DemoDataInitializer(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    public void populate() {
        parkingService.addParkingSpot(1, SpotType.REGULAR);
        parkingService.addParkingSpot(2, SpotType.REGULAR);
        parkingService.addParkingSpot(3, SpotType.DISABLED);
        parkingService.addParkingSpot(4, SpotType.ELECTRIC);
        parkingService.addParkingSpot(5, SpotType.ELECTRIC);
        parkingService.addParkingSpot(6, SpotType.REGULAR);

        parkingService.registerEntry(new Vehicle("A123AA77", VehicleType.CAR), SpotType.REGULAR, findTariff(TariffType.HOURLY));
        parkingService.registerEntry(new Vehicle("E777EK77", VehicleType.ELECTRIC_CAR), SpotType.ELECTRIC, findTariff(TariffType.NIGHT));
    }

    private TariffStrategy findTariff(TariffType type) {
        return parkingService.getAvailableTariffs().stream()
                .filter(strategy -> strategy.getType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tariff not found: " + type));
    }
}
