package ru.laskan0.travelagency.repository.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.laskan0.travelagency.model.Tour;
import ru.laskan0.travelagency.repository.TourRepository;

public class InMemoryTourRepository implements TourRepository {
    private final Map<String, Tour> tours = new LinkedHashMap<>();

    @Override
    public Tour save(Tour tour) {
        tours.put(tour.getId(), tour);
        return tour;
    }

    @Override
    public Optional<Tour> findById(String id) {
        return Optional.ofNullable(tours.get(id));
    }

    @Override
    public List<Tour> findAll() {
        return new ArrayList<>(tours.values());
    }

    @Override
    public boolean existsById(String id) {
        return tours.containsKey(id);
    }
}
