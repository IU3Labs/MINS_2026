package repository;

import model.ParkingSpot;
import model.SpotType;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotReader {
    List<ParkingSpot> findAll();

    List<ParkingSpot> findByType(SpotType type);

    Optional<ParkingSpot> findById(int id);
}
