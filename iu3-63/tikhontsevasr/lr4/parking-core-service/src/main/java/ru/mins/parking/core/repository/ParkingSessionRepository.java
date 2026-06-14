package ru.mins.parking.core.repository;

import ru.mins.parking.core.model.ParkingSession;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository {
    void save(ParkingSession session);

    Optional<ParkingSession> findActiveByLicensePlate(String licensePlate);

    List<ParkingSession> findActiveSessions();
}
