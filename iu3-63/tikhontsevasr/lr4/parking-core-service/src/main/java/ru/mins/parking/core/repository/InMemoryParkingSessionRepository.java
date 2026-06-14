package ru.mins.parking.core.repository;

import ru.mins.parking.core.model.ParkingSession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class InMemoryParkingSessionRepository implements ParkingSessionRepository {
    private final List<ParkingSession> sessions = new ArrayList<>();

    @Override
    public void save(ParkingSession session) {
        if (!sessions.contains(session)) {
            sessions.add(session);
        }
    }

    @Override
    public Optional<ParkingSession> findActiveByLicensePlate(String licensePlate) {
        return sessions.stream()
                .filter(ParkingSession::isActive)
                .filter(session -> session.getVehicle().getLicensePlate().equalsIgnoreCase(licensePlate))
                .findFirst();
    }

    @Override
    public List<ParkingSession> findActiveSessions() {
        return sessions.stream()
                .filter(ParkingSession::isActive)
                .sorted(Comparator.comparing(ParkingSession::getEntryTime))
                .toList();
    }
}
