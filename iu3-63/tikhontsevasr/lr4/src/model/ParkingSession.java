package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import tariff.TariffStrategy;

public class ParkingSession {
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal cost;
    private final TariffStrategy tariffStrategy;

    public ParkingSession(Vehicle vehicle, ParkingSpot parkingSpot, LocalDateTime entryTime, TariffStrategy tariffStrategy) {
        this.vehicle = Objects.requireNonNull(vehicle, "vehicle must not be null");
        this.parkingSpot = Objects.requireNonNull(parkingSpot, "parkingSpot must not be null");
        this.entryTime = Objects.requireNonNull(entryTime, "entryTime must not be null");
        this.tariffStrategy = Objects.requireNonNull(tariffStrategy, "tariffStrategy must not be null");
        this.cost = BigDecimal.ZERO;
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

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public TariffStrategy getTariffStrategy() {
        return tariffStrategy;
    }

    public boolean isActive() {
        return exitTime == null;
    }

    public void complete(LocalDateTime exitTime, BigDecimal cost) {
        this.exitTime = Objects.requireNonNull(exitTime, "exitTime must not be null");
        this.cost = Objects.requireNonNull(cost, "cost must not be null");
    }

    @Override
    public String toString() {
        String exitInfo = exitTime == null ? "активна" : "выезд: " + exitTime;
        return vehicle + " | место #" + parkingSpot.getId() + " | въезд: " + entryTime + " | " + exitInfo + " | тариф: " + tariffStrategy.getType().name();
    }
}
