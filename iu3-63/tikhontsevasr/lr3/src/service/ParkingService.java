package service;

import model.ParkingSession;
import model.ParkingSpot;
import model.SpotType;
import model.Vehicle;
import tariff.TariffStrategy;

import java.util.Collection;
import java.util.List;

public interface ParkingService {
    ParkingSpot addParkingSpot(int id, SpotType type);

    List<ParkingSpot> getAllSpots();

    List<ParkingSpot> getFreeSpots();

    List<ParkingSession> getActiveSessions();

    ParkingSession registerEntry(Vehicle vehicle, SpotType preferredSpotType, TariffStrategy tariffStrategy);

    ParkingSession registerExit(String licensePlate);

    Collection<TariffStrategy> getAvailableTariffs();
}
