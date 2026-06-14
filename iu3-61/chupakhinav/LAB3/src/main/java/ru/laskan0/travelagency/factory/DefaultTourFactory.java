package ru.laskan0.travelagency.factory;

import ru.laskan0.travelagency.model.Tour;

public class DefaultTourFactory extends TourFactory {
    @Override
    protected Tour createTour(String id, String title, String destination, double basePrice, int capacity) {
        return new Tour(id, title, destination, basePrice, capacity);
    }
}
