package model;

public class DisabledParkingSpot extends ParkingSpot {
    public DisabledParkingSpot(int id) {
        super(id, SpotType.DISABLED);
    }

    @Override
    public String getSpotLabel() {
        return "Место для инвалидов";
    }
}
