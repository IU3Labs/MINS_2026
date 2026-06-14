package ru.mins.parking.core.service;

import ru.mins.parking.core.exception.ActiveSessionNotFoundException;
import ru.mins.parking.core.exception.InvalidOperationException;
import ru.mins.parking.core.exception.NoFreeSpotException;
import ru.mins.parking.core.exception.ValidationException;
import ru.mins.parking.core.exception.VehicleAlreadyParkedException;
import ru.mins.parking.core.grpc.ReferenceServiceClient;
import ru.mins.parking.core.model.ParkingSession;
import ru.mins.parking.core.model.ParkingSpot;
import ru.mins.parking.core.model.SpotType;
import ru.mins.parking.core.model.TariffType;
import ru.mins.parking.core.model.Vehicle;
import ru.mins.parking.core.repository.ParkingSessionRepository;
import ru.mins.parking.core.repository.ParkingSpotRepository;
import ru.mins.parking.core.util.LogUtil;
import ru.mins.parking.core.util.TraceContext;
import ru.mins.parking.proto.SpotTypeResponse;
import ru.mins.parking.proto.TariffResponse;
import ru.mins.parking.proto.VehicleValidationResponse;
import ru.mins.parking.proto.VipDiscountResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class ParkingCoreService {
    private static final Logger LOGGER = Logger.getLogger(ParkingCoreService.class.getName());

    private final ParkingSpotRepository spotRepository;
    private final ParkingSessionRepository sessionRepository;
    private final ReferenceServiceClient referenceServiceClient;
    private final double highOccupancyThreshold;

    public ParkingCoreService(
            ParkingSpotRepository spotRepository,
            ParkingSessionRepository sessionRepository,
            ReferenceServiceClient referenceServiceClient,
            double highOccupancyThreshold) {
        this.spotRepository = spotRepository;
        this.sessionRepository = sessionRepository;
        this.referenceServiceClient = referenceServiceClient;
        this.highOccupancyThreshold = highOccupancyThreshold;
    }

    public ParkingSpot addParkingSpot(int id, SpotType spotType) {
        TraceContext.ensure();
        if (id <= 0) {
            throw new InvalidOperationException("ID места должен быть положительным");
        }
        ParkingSpot spot = new ParkingSpot(id, spotType);
        spotRepository.save(spot);
        LogUtil.info(LOGGER, "Добавлено место #" + id + ", тип=" + spotType);
        return spot;
    }

    public List<ParkingSpot> getAllSpots() {
        return spotRepository.findAll();
    }

    public List<ParkingSpot> getFreeSpots() {
        return spotRepository.findAll().stream().filter(ParkingSpot::isFree).toList();
    }

    public List<ParkingSession> getActiveSessions() {
        return sessionRepository.findActiveSessions();
    }

    public ParkingSession registerEntry(Vehicle vehicle, SpotType preferredSpotType, TariffType tariffType) {
        TraceContext.ensure();
        if (sessionRepository.findActiveByLicensePlate(vehicle.getLicensePlate()).isPresent()) {
            throw new VehicleAlreadyParkedException("Автомобиль " + vehicle.getLicensePlate() + " уже находится на парковке");
        }

        VehicleValidationResponse validation = referenceServiceClient.validateVehicle(
                vehicle.getLicensePlate(),
                vehicle.getType(),
                preferredSpotType
        );
        if (!validation.getValid()) {
            throw new ValidationException(validation.getMessage());
        }

        referenceServiceClient.getSpotTypeInfo(preferredSpotType);

        ParkingSpot freeSpot = spotRepository.findByType(preferredSpotType).stream()
                .filter(ParkingSpot::isFree)
                .findFirst()
                .orElseThrow(() -> new NoFreeSpotException("Нет свободных мест типа " + preferredSpotType));

        freeSpot.occupy();
        ParkingSession session = new ParkingSession(vehicle, freeSpot, LocalDateTime.now(), tariffType);
        sessionRepository.save(session);
        LogUtil.info(LOGGER, "Зарегистрирован въезд автомобиля " + vehicle.getLicensePlate() + " на место #" + freeSpot.getId());
        logHighOccupancyIfNeeded();
        return session;
    }

    public ParkingSession registerExit(String licensePlate) {
        TraceContext.ensure();
        ParkingSession session = sessionRepository.findActiveByLicensePlate(licensePlate)
                .orElseThrow(() -> new ActiveSessionNotFoundException("Активная сессия для автомобиля " + licensePlate + " не найдена"));

        TariffResponse tariff = referenceServiceClient.getTariffByType(session.getTariffType());
        SpotTypeResponse spotInfo = referenceServiceClient.getSpotTypeInfo(session.getParkingSpot().getType());
        VipDiscountResponse vipDiscount = referenceServiceClient.getVipDiscount(session.getVehicle().getLicensePlate());

        LocalDateTime exitTime = LocalDateTime.now();
        BigDecimal cost = calculateCost(
                tariff,
                spotInfo.getPriceCoefficient(),
                vipDiscount.getDiscountPercent(),
                Duration.between(session.getEntryTime(), exitTime)
        );

        session.complete(exitTime, cost);
        session.getParkingSpot().release();
        LogUtil.info(LOGGER, "Зарегистрирован выезд автомобиля " + licensePlate + ", стоимость=" + cost);
        return session;
    }

    private BigDecimal calculateCost(TariffResponse tariff, double spotCoefficient, double vipDiscountPercent, Duration duration) {
        long minutes = Math.max(1, duration.toMinutes());
        long hours = Math.max(1, (minutes + 59) / 60);
        BigDecimal baseCost = switch (tariff.getTariffType()) {
            case HOURLY -> BigDecimal.valueOf(tariff.getBaseRate() * hours);
            case DAILY -> {
                long days = Math.max(1, (minutes + 24L * 60 - 1) / (24L * 60));
                yield BigDecimal.valueOf(tariff.getBaseRate() * days);
            }
            case NIGHT -> {
                BigDecimal start = BigDecimal.valueOf(tariff.getBaseRate());
                long extraHours = Math.max(0, hours - tariff.getIncludedHours());
                yield start.add(BigDecimal.valueOf(tariff.getExtraHourRate() * extraHours));
            }
            default -> throw new InvalidOperationException("Неподдерживаемый тариф");
        };

        BigDecimal withCoefficient = baseCost.multiply(BigDecimal.valueOf(spotCoefficient));
        BigDecimal discountFactor = BigDecimal.ONE.subtract(BigDecimal.valueOf(vipDiscountPercent / 100.0));
        return withCoefficient.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    }

    private void logHighOccupancyIfNeeded() {
        int total = spotRepository.findAll().size();
        if (total == 0) {
            return;
        }

        double occupancy = sessionRepository.findActiveSessions().size() * 100.0 / total;
        if (occupancy >= highOccupancyThreshold) {
            LogUtil.info(LOGGER, "Высокая загрузка парковки: " + String.format("%.2f%%", occupancy));
        }
    }
}
