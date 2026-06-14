package ru.mins.parking.core.repository;

import ru.mins.parking.core.exception.InvalidOperationException;
import ru.mins.parking.core.model.ParkingSpot;
import ru.mins.parking.core.model.SpotType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InMemoryParkingSpotRepository implements ParkingSpotRepository {
    private final List<ParkingSpot> spots = new ArrayList<>();

    @Override
    public void save(ParkingSpot spot) {
        boolean exists = spots.stream().anyMatch(current -> current.getId() == spot.getId());
        if (exists) {
            throw new InvalidOperationException("Место с id " + spot.getId() + " уже существует");
        }
        spots.add(spot);
    }

    @Override
    public List<ParkingSpot> findAll() {
        return spots.stream()
                .sorted(Comparator.comparingInt(ParkingSpot::getId))
                .toList();
    }

    @Override
    public List<ParkingSpot> findByType(SpotType spotType) {
        return spots.stream()
                .filter(spot -> spot.getType() == spotType)
                .sorted(Comparator.comparingInt(ParkingSpot::getId))
                .toList();
    }
}
