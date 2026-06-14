package ru.bmstu.travel.core.catalog;

public record TourOperationResult(boolean success, String message, CatalogTour tour) {
}
