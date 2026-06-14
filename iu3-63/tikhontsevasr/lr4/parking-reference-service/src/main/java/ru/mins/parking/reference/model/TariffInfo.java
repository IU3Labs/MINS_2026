package ru.mins.parking.reference.model;

public record TariffInfo(
        TariffType tariffType,
        double baseRate,
        double extraHourRate,
        int includedHours) {
}
