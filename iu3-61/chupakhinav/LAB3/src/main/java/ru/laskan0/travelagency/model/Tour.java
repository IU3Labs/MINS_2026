package ru.laskan0.travelagency.model;

import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.exception.TourAvailabilityException;

public class Tour {
    private final String id;
    private final String title;
    private final String destination;
    private final double basePrice;
    private final int capacity;
    private int bookedSeats;

    public Tour(String id, String title, String destination, double basePrice, int capacity) {
        this.id = requireText(id, "ID тура");
        this.title = requireText(title, "Название тура");
        this.destination = requireText(destination, "Направление тура");
        if (basePrice <= 0) {
            throw new InvalidDataException("Базовая цена тура должна быть больше 0");
        }
        if (capacity <= 0) {
            throw new InvalidDataException("Количество мест должно быть больше 0");
        }
        this.basePrice = basePrice;
        this.capacity = capacity;
        this.bookedSeats = 0;
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidDataException(fieldName + " не может быть пустым");
        }
        return value.trim();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDestination() {
        return destination;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public int getAvailableSeats() {
        return capacity - bookedSeats;
    }

    public boolean hasAvailableSeats() {
        return getAvailableSeats() > 0;
    }

    public void reserveSeat() {
        if (!hasAvailableSeats()) {
            throw new TourAvailabilityException("В туре больше нет свободных мест: " + id);
        }
        bookedSeats++;
    }

    public void releaseSeat() {
        if (bookedSeats > 0) {
            bookedSeats--;
        }
    }

    @Override
    public String toString() {
        return "Tour{id='" + id + "', title='" + title + "', destination='" + destination
                + "', price=" + String.format("%.2f", basePrice)
                + ", freeSeats=" + getAvailableSeats() + "/" + capacity + "}";
    }
}
