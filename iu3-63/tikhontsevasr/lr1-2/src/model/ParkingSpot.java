package model;

import java.util.Objects;

public abstract class ParkingSpot {
    private final int id;
    private final SpotType type;
    private SpotStatus status;

    public ParkingSpot(int id, SpotType type) {
        this.id = id;
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.status = SpotStatus.FREE;
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
        this.status = SpotStatus.OCCUPIED;
    }

    public void release() {
        this.status = SpotStatus.FREE;
    }

    public abstract String getSpotLabel();

    @Override
    public String toString() {
        return getSpotLabel() + " #" + id + " | тип: " + type.getDisplayName() + " | статус: " + status.getDisplayName();
    }
}
