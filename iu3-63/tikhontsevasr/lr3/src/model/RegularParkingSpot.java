package model;

public class RegularParkingSpot extends ParkingSpot {
    public RegularParkingSpot(int id) {
        super(id, SpotType.REGULAR);
    }

    @Override
    public String getSpotLabel() {
        return "Обычное место";
    }
}
