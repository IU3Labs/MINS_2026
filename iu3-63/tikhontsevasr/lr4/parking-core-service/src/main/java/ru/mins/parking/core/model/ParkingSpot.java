package ru.mins.parking.core.model;

public class ParkingSpot {
    private final int id;
    private final SpotType type;
    private SpotStatus status = SpotStatus.FREE;

    public ParkingSpot(int id, SpotType type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public boolean isFree() {
        return status == SpotStatus.FREE;
    }

    public void occupy() {
        status = SpotStatus.OCCUPIED;
    }

    public void release() {
        status = SpotStatus.FREE;
    }

    @Override
    public String toString() {
        return "Место #" + id + " | тип=" + type + " | статус=" + status;
    }
}
