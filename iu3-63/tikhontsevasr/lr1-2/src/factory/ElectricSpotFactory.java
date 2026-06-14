package factory;

import model.ElectricParkingSpot;
import model.ParkingSpot;
import model.SpotType;

public class ElectricSpotFactory implements ParkingSpotFactory {
    @Override
    public SpotType getSupportedType() {
        return SpotType.ELECTRIC;
    }

    @Override
    public ParkingSpot createSpot(int id) {
        return new ElectricParkingSpot(id);
    }
}
