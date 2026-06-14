package ru.mins.parking.core.repository;

import ru.mins.parking.core.model.ParkingSpot;
import ru.mins.parking.core.model.SpotType;

import java.util.List;

public interface ParkingSpotRepository {
    void save(ParkingSpot spot);

    List<ParkingSpot> findAll();

    List<ParkingSpot> findByType(SpotType spotType);
}
