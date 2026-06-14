package ru.bmstu.repository;

import ru.bmstu.exception.TourNotFoundException;
import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.model.Tour;

import java.util.*;

public class TourRepository implements Repository<Tour, Integer> {
    private final Map<Integer, Tour> tours = new HashMap<>();
    private int currentId = 1;  // счетчик

    @Override
    public Tour save(Tour tour) {
        if (tour.getId() == 0) {
            tour.setId(currentId++);
            tours.put(tour.getId(), tour);
        } else {
            tours.put(tour.getId(), tour);
        }
        return tour;
    }

    @Override
    public Optional<Tour> findById(Integer id) {
        return Optional.ofNullable(tours.get(id));
    }

    @Override
    public List<Tour> findAll() {
        List<Tour> sorted = new ArrayList<>(tours.values());
        sorted.sort(Comparator.comparingInt(Tour::getId));
        return sorted;
    }

    @Override
    public void deleteById(Integer id) throws TourAgencyException {
        if (!tours.containsKey(id)) {
            throw new TourNotFoundException("Тур с ID " + id + " не найден.");
        }
        tours.remove(id);
    }

    public List<Tour> findByNameContaining(String namePart) {
        List<Tour> result = new ArrayList<>();
        for (Tour tour : tours.values()) {
            if (tour.getName().toLowerCase().contains(namePart.toLowerCase())) {
                result.add(tour);
            }
        }
        result.sort(Comparator.comparingInt(Tour::getId));
        return result;
    }
}
