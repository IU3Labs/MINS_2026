package ru.laskan0.travelagency.factory;

import ru.laskan0.travelagency.exception.InvalidDataException;
import ru.laskan0.travelagency.model.Tour;

public abstract class TourFactory {
    public final Tour create(String id, String title, String destination, double basePrice, int capacity) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("ID тура не может быть пустым");
        }
        if (title == null || title.isBlank()) {
            throw new InvalidDataException("Название тура не может быть пустым");
        }
        if (destination == null || destination.isBlank()) {
            throw new InvalidDataException("Направление тура не может быть пустым");
        }
        return createTour(id.trim(), title.trim(), destination.trim(), basePrice, capacity);
    }

    protected abstract Tour createTour(String id, String title, String destination, double basePrice, int capacity);
}
