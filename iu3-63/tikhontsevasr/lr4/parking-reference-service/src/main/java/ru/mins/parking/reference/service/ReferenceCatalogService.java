package ru.mins.parking.reference.service;

import ru.mins.parking.reference.exception.ReferenceDataNotFoundException;
import ru.mins.parking.reference.model.SpotType;
import ru.mins.parking.reference.model.SpotTypeInfo;
import ru.mins.parking.reference.model.TariffInfo;
import ru.mins.parking.reference.model.TariffType;
import ru.mins.parking.reference.model.VehicleType;
import ru.mins.parking.reference.model.VehicleValidationResult;
import ru.mins.parking.reference.repository.ReferenceDataRepository;

public class ReferenceCatalogService {
    private final ReferenceDataRepository repository;

    public ReferenceCatalogService(ReferenceDataRepository repository) {
        this.repository = repository;
    }

    public TariffInfo getTariff(TariffType tariffType) {
        return repository.findTariff(tariffType)
                .orElseThrow(() -> new ReferenceDataNotFoundException("Тариф не найден: " + tariffType));
    }

    public SpotTypeInfo getSpotTypeInfo(SpotType spotType) {
        return repository.findSpotTypeInfo(spotType)
                .orElseThrow(() -> new ReferenceDataNotFoundException("Тип места не найден: " + spotType));
    }

    public VehicleValidationResult validateVehicle(String licensePlate, VehicleType vehicleType, SpotType requestedSpotType) {
        if (licensePlate == null || licensePlate.isBlank()) {
            return new VehicleValidationResult(false, "Номер автомобиля не должен быть пустым");
        }

        SpotTypeInfo info = getSpotTypeInfo(requestedSpotType);
        if (!info.allowedVehicleTypes().contains(vehicleType)) {
            return new VehicleValidationResult(false, "Тип автомобиля " + vehicleType + " не допускается для места " + requestedSpotType);
        }

        return new VehicleValidationResult(true, "Автомобиль успешно проверен");
    }

    public double getVipDiscount(String licensePlate) {
        return repository.findVipDiscount(licensePlate == null ? "" : licensePlate.trim());
    }
}
