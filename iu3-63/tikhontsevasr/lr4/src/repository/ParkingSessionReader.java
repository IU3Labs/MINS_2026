package repository;

import model.ParkingSession;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionReader {
    List<ParkingSession> findAll();

    List<ParkingSession> findActiveSessions();

    Optional<ParkingSession> findActiveByLicensePlate(String licensePlate);
}
