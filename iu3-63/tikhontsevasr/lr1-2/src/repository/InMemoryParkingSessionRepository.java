package repository;

import model.ParkingSession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryParkingSessionRepository implements ParkingSessionRepository {
    private final List<ParkingSession> sessions = new ArrayList<>();

    @Override
    public List<ParkingSession> findAll() {
        return sessions.stream()
                .sorted(Comparator.comparing(ParkingSession::getEntryTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSession> findActiveSessions() {
        return sessions.stream()
                .filter(ParkingSession::isActive)
                .sorted(Comparator.comparing(ParkingSession::getEntryTime))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ParkingSession> findActiveByLicensePlate(String licensePlate) {
        String normalized = licensePlate.trim().toUpperCase();
        return sessions.stream()
                .filter(ParkingSession::isActive)
                .filter(session -> session.getVehicle().getLicensePlate().equals(normalized))
                .findFirst();
    }

    @Override
    public void save(ParkingSession session) {
        if (!sessions.contains(session)) {
            sessions.add(session);
        }
    }
}
