package ru.iu3.lab4.coreservice.client;

/**
 * DTO для транспорта, полученного из Reference Service
 */
public record VehicleInfo(String id, String type, double capacity) {}