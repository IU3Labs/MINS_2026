package factory;

import model.DisabledParkingSpot;
import model.ParkingSpot;
import model.SpotType;

public class DisabledSpotFactory implements ParkingSpotFactory {
    @Override
    public SpotType getSupportedType() {
        return SpotType.DISABLED;
    }

    @Override
    public ParkingSpot createSpot(int id) {
        return new DisabledParkingSpot(id);
    }
}
