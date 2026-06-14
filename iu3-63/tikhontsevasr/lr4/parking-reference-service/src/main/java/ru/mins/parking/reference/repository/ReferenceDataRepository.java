package ru.mins.parking.reference.repository;

import ru.mins.parking.reference.model.SpotType;
import ru.mins.parking.reference.model.SpotTypeInfo;
import ru.mins.parking.reference.model.TariffInfo;
import ru.mins.parking.reference.model.TariffType;

import java.util.Optional;

public interface ReferenceDataRepository {
    Optional<SpotTypeInfo> findSpotTypeInfo(SpotType spotType);

    Optional<TariffInfo> findTariff(TariffType tariffType);

    double findVipDiscount(String licensePlate);
}
