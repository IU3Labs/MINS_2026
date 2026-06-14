package ru.mins.parking.reference.repository;

import ru.mins.parking.reference.model.SpotType;
import ru.mins.parking.reference.model.SpotTypeInfo;
import ru.mins.parking.reference.model.TariffInfo;
import ru.mins.parking.reference.model.TariffType;
import ru.mins.parking.reference.model.VehicleType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class InMemoryReferenceDataRepository implements ReferenceDataRepository {
    private final Map<SpotType, SpotTypeInfo> spotTypes = new EnumMap<>(SpotType.class);
    private final Map<TariffType, TariffInfo> tariffs = new EnumMap<>(TariffType.class);
    private final Map<String, Double> vipDiscounts = new java.util.HashMap<>();

    public InMemoryReferenceDataRepository() {
        spotTypes.put(SpotType.REGULAR, new SpotTypeInfo(
                SpotType.REGULAR, "Обычное", 1.0, Set.of(VehicleType.CAR, VehicleType.MOTORCYCLE)));
        spotTypes.put(SpotType.DISABLED, new SpotTypeInfo(
                SpotType.DISABLED, "Для инвалидов", 0.85, Set.of(VehicleType.ACCESSIBLE_VEHICLE)));

        tariffs.put(TariffType.HOURLY, new TariffInfo(TariffType.HOURLY, 100.0, 100.0, 1));
        tariffs.put(TariffType.DAILY, new TariffInfo(TariffType.DAILY, 1200.0, 0.0, 24));
        tariffs.put(TariffType.NIGHT, new TariffInfo(TariffType.NIGHT, 180.0, 50.0, 3));

        vipDiscounts.put("A123AA77", 10.0);
        vipDiscounts.put("VIP00177", 15.0);
    }

    @Override
    public Optional<SpotTypeInfo> findSpotTypeInfo(SpotType spotType) {
        return Optional.ofNullable(spotTypes.get(spotType));
    }

    @Override
    public Optional<TariffInfo> findTariff(TariffType tariffType) {
        return Optional.ofNullable(tariffs.get(tariffType));
    }

    @Override
    public double findVipDiscount(String licensePlate) {
        return vipDiscounts.getOrDefault(licensePlate.toUpperCase(), 0.0);
    }
}
