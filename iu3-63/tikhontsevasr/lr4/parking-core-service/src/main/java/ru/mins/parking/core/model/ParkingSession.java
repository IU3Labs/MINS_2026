package ru.mins.parking.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingSession {
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime entryTime;
    private final TariffType tariffType;
    private LocalDateTime exitTime;
    private BigDecimal cost = BigDecimal.ZERO;

    public ParkingSession(Vehicle vehicle, ParkingSpot parkingSpot, LocalDateTime entryTime, TariffType tariffType) {
        this.vehicle = Objects.requireNonNull(vehicle);
        this.parkingSpot = Objects.requireNonNull(parkingSpot);
        this.entryTime = Objects.requireNonNull(entryTime);
        this.tariffType = Objects.requireNonNull(tariffType);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public TariffType getTariffType() {
        return tariffType;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public boolean isActive() {
        return exitTime == null;
    }

    public void complete(LocalDateTime exitTime, BigDecimal cost) {
        this.exitTime = exitTime;
        this.cost = cost;
    }
}
