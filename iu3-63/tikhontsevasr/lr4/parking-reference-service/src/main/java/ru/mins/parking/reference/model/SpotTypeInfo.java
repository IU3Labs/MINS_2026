package ru.mins.parking.reference.model;

import java.util.Set;

public record SpotTypeInfo(
        SpotType spotType,
        String displayName,
        double priceCoefficient,
        Set<VehicleType> allowedVehicleTypes) {
}
