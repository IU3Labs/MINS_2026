package ru.laskan0.travelagency.repository;

import java.util.List;
import java.util.Optional;

import ru.laskan0.travelagency.model.Tour;

public interface TourRepository {
    Tour save(Tour tour);
    Optional<Tour> findById(String id);
    List<Tour> findAll();
    boolean existsById(String id);
}
