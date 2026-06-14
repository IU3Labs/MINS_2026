package ru.mins.parking.core.util;

import ru.mins.parking.core.model.SpotType;
import ru.mins.parking.core.model.TariffType;
import ru.mins.parking.core.model.VehicleType;
import ru.mins.parking.proto.SpotTypeProto;
import ru.mins.parking.proto.TariffTypeProto;
import ru.mins.parking.proto.VehicleTypeProto;

public final class EnumMapper {
    private EnumMapper() {
    }

    public static SpotTypeProto toProto(SpotType spotType) {
        return switch (spotType) {
            case REGULAR -> SpotTypeProto.REGULAR;
            case DISABLED -> SpotTypeProto.DISABLED;
            case ELECTRIC -> SpotTypeProto.ELECTRIC;
        };
    }

    public static TariffTypeProto toProto(TariffType tariffType) {
        return switch (tariffType) {
            case HOURLY -> TariffTypeProto.HOURLY;
            case DAILY -> TariffTypeProto.DAILY;
            case NIGHT -> TariffTypeProto.NIGHT;
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
