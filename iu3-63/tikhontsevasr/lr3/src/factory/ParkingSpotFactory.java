package factory;

import model.ParkingSpot;
import model.SpotType;

public interface ParkingSpotFactory {
    SpotType getSupportedType();

    ParkingSpot createSpot(int id);
}
