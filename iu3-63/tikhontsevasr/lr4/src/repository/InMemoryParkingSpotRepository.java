package repository;

import exception.InvalidOperationException;
import model.ParkingSpot;
import model.SpotType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryParkingSpotRepository implements ParkingSpotRepository {
    private final List<ParkingSpot> spots = new ArrayList<>();

    @Override
    public List<ParkingSpot> findAll() {
        return spots.stream()
                .sorted(Comparator.comparingInt(ParkingSpot::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSpot> findByType(SpotType type) {
        return spots.stream()
                .filter(spot -> spot.getType() == type)
                .sorted(Comparator.comparingInt(ParkingSpot::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ParkingSpot> findById(int id) {
        return spots.stream()
                .filter(spot -> spot.getId() == id)
                .findFirst();
    }

    @Override
    public void save(ParkingSpot parkingSpot) {
        if (findById(parkingSpot.getId()).isPresent()) {
            throw new InvalidOperationException("Место с id " + parkingSpot.getId() + " уже существует.");
        }
        spots.add(parkingSpot);
    }
}
