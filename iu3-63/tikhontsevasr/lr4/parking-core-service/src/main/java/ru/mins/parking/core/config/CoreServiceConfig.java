package ru.mins.parking.core.config;

public record CoreServiceConfig(String referenceHost, int referencePort, double highOccupancyThreshold) {
}
