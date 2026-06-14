package model;

public class ElectricParkingSpot extends ParkingSpot {
    public ElectricParkingSpot(int id) {
        super(id, SpotType.ELECTRIC);
    }

    @Override
    public String getSpotLabel() {
        return "Электро-место";
    }
}
