package model;

import java.util.Objects;

public class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = Objects.requireNonNull(licensePlate, "licensePlate must not be null").trim().toUpperCase();
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return licensePlate + " (" + type.getDisplayName() + ")";
    }
}
