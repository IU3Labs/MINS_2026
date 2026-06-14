package service;

import exception.ActiveSessionNotFoundException;
import exception.InvalidOperationException;
import exception.NoFreeSpotException;
import exception.VehicleAlreadyParkedException;
import factory.ParkingSpotFactoryRegistry;
import model.ParkingSession;
import model.ParkingSpot;
import model.SpotStatus;
import model.SpotType;
import model.Vehicle;
import observer.ParkingEvent;
import observer.ParkingEventManager;
import observer.ParkingEventType;
import repository.ParkingSessionRepository;
import repository.ParkingSpotRepository;
import tariff.TariffCatalog;
import tariff.TariffStrategy;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingLotService implements ParkingService {
    private final ParkingSpotRepository spotRepository;
    private final ParkingSessionRepository sessionRepository;
    private final ParkingSpotFactoryRegistry factoryRegistry;
    private final TariffCatalog tariffCatalog;
    private final ParkingEventManager eventManager;
    private final Clock clock;
    private final double highOccupancyThreshold;

    public ParkingLotService(ParkingSpotRepository spotRepository,
                             ParkingSessionRepository sessionRepository,
                             ParkingSpotFactoryRegistry factoryRegistry,
                             TariffCatalog tariffCatalog,
                             ParkingEventManager eventManager,
                             Clock clock,
                             double highOccupancyThreshold) {
        this.spotRepository = spotRepository;
        this.sessionRepository = sessionRepository;
        this.factoryRegistry = factoryRegistry;
        this.tariffCatalog = tariffCatalog;
        this.eventManager = eventManager;
        this.clock = clock;
        this.highOccupancyThreshold = highOccupancyThreshold;
    }

    @Override
    public ParkingSpot addParkingSpot(int id, SpotType type) {
        if (id <= 0) {
            throw new InvalidOperationException("Идентификатор места должен быть положительным.");
        }
        ParkingSpot spot = factoryRegistry.getFactory(type).createSpot(id);
        spotRepository.save(spot);
        return spot;
    }

    @Override
    public List<ParkingSpot> getAllSpots() {
        return spotRepository.findAll();
    }

    @Override
    public List<ParkingSpot> getFreeSpots() {
        return spotRepository.findAll().stream()
                .filter(ParkingSpot::isFree)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSession> getActiveSessions() {
        return sessionRepository.findActiveSessions();
    }

    @Override
    public ParkingSession registerEntry(Vehicle vehicle, SpotType preferredSpotType, TariffStrategy tariffStrategy) {
        if (sessionRepository.findActiveByLicensePlate(vehicle.getLicensePlate()).isPresent()) {
            throw new VehicleAlreadyParkedException("Автомобиль " + vehicle.getLicensePlate() + " уже находится на парковке.");
        }

                ParkingSpot freeSpot = spotRepository.findByType(preferredSpotType).stream()
                .filter(ParkingSpot::isFree)
                .findFirst()
                .orElseThrow(() -> new NoFreeSpotException("Нет свободных мест типа: " + preferredSpotType.getDisplayName()));

        freeSpot.occupy();
        ParkingSession session = new ParkingSession(vehicle, freeSpot, LocalDateTime.now(clock), tariffStrategy);
        sessionRepository.save(session);
        eventManager.notifyObservers(new ParkingEvent(
                ParkingEventType.VEHICLE_ENTERED,
                "Автомобиль " + vehicle.getLicensePlate() + " въехал на парковку и занял место #" + freeSpot.getId(),
                LocalDateTime.now(clock)
        ));
        notifyHighOccupancyIfNeeded();
        return session;
    }

    @Override
    public ParkingSession registerExit(String licensePlate) {
        ParkingSession session = sessionRepository.findActiveByLicensePlate(licensePlate)
                .orElseThrow(() -> new ActiveSessionNotFoundException("Для автомобиля " + licensePlate.toUpperCase() + " активная сессия не найдена."));

        if (session.getParkingSpot().getStatus() != SpotStatus.OCCUPIED) {
            throw new InvalidOperationException("Место для выбранной сессии уже свободно.");
        }

        TariffStrategy tariffStrategy = session.getTariffStrategy();
        LocalDateTime exitTime = LocalDateTime.now(clock);
        BigDecimal cost = tariffStrategy.calculateCost(Duration.between(session.getEntryTime(), exitTime));

        session.complete(exitTime, cost);
        session.getParkingSpot().release();
        eventManager.notifyObservers(new ParkingEvent(
                ParkingEventType.VEHICLE_EXITED,
                "Автомобиль " + session.getVehicle().getLicensePlate() + " покинул парковку. Стоимость: " + cost + " руб.",
                exitTime
        ));
        return session;
    }

    @Override
    public Collection<TariffStrategy> getAvailableTariffs() {
        return tariffCatalog.getAll();
    }

    private void notifyHighOccupancyIfNeeded() {
        int total = spotRepository.findAll().size();
        if (total == 0) {
            return;
        }

        int occupied = sessionRepository.findActiveSessions().size();
        double occupancy = occupied * 100.0 / total;
        if (occupancy >= highOccupancyThreshold) {
            eventManager.notifyObservers(new ParkingEvent(
                    ParkingEventType.HIGH_OCCUPANCY,
                    "Загрузка парковки достигла " + String.format("%.2f%%", occupancy),
                    LocalDateTime.now(clock)
            ));
        }
    }
}
