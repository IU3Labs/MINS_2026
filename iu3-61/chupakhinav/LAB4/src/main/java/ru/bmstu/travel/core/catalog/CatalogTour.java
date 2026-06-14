package ru.bmstu.travel.core.catalog;

public record CatalogTour(String id, String title, String destination, double basePrice, int capacity, int bookedSeats) {
    public int availableSeats() {
        return capacity - bookedSeats;
    }
}
