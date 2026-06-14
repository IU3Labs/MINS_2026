package ru.mins.parking.reference.util;

import ru.mins.parking.proto.SpotTypeProto;
import ru.mins.parking.proto.TariffTypeProto;
import ru.mins.parking.proto.VehicleTypeProto;
import ru.mins.parking.reference.model.SpotType;
import ru.mins.parking.reference.model.TariffType;
import ru.mins.parking.reference.model.VehicleType;

public final class EnumMapper {
    private EnumMapper() {
    }

    public static SpotType fromProto(SpotTypeProto spotType) {
        return switch (spotType) {
            case REGULAR -> SpotType.REGULAR;
            case DISABLED -> SpotType.DISABLED;
            case ELECTRIC -> SpotType.ELECTRIC;
            default -> throw new IllegalArgumentException("Unsupported spot type: " + spotType);
        };
    }

    public static SpotTypeProto toProto(SpotType spotType) {
        return switch (spotType) {
            case REGULAR -> SpotTypeProto.REGULAR;
            case DISABLED -> SpotTypeProto.DISABLED;
            case ELECTRIC -> SpotTypeProto.ELECTRIC;
        };
    }

    public static TariffType fromProto(TariffTypeProto tariffType) {
        return switch (tariffType) {
            case HOURLY -> TariffType.HOURLY;
            case DAILY -> TariffType.DAILY;
            case NIGHT -> TariffType.NIGHT;
            default -> throw new IllegalArgumentException("Unsupported tariff type: " + tariffType);
        };
    }

    public static VehicleType fromProto(VehicleTypeProto vehicleType) {
        return switch (vehicleType) {
            case CAR -> VehicleType.CAR;
            case MOTORCYCLE -> VehicleType.MOTORCYCLE;
            case ELECTRIC_CAR -> VehicleType.ELECTRIC_CAR;
            case ACCESSIBLE_VEHICLE -> VehicleType.ACCESSIBLE_VEHICLE;
            default -> throw new IllegalArgumentException("Unsupported vehicle type: " + vehicleType);
        };
    }

    public static VehicleTypeProto toProto(VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> VehicleTypeProto.CAR;
            case MOTORCYCLE -> VehicleTypeProto.MOTORCYCLE;
            case ELECTRIC_CAR -> VehicleTypeProto.ELECTRIC_CAR;
            case ACCESSIBLE_VEHICLE -> VehicleTypeProto.ACCESSIBLE_VEHICLE;
        };
    }
}
