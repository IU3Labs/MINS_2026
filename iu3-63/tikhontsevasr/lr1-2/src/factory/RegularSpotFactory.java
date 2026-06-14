package factory;

import model.ParkingSpot;
import model.RegularParkingSpot;
import model.SpotType;

public class RegularSpotFactory implements ParkingSpotFactory {
    @Override
    public SpotType getSupportedType() {
        return SpotType.REGULAR;
    }

    @Override
    public ParkingSpot createSpot(int id) {
        return new RegularParkingSpot(id);
    }
}
