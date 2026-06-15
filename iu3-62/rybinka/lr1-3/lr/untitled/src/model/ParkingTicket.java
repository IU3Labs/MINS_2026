package model;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String licensePlate;
    private final int spaceId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public ParkingTicket(String licensePlate, int spaceId, LocalDateTime entryTime) {
        this.licensePlate = licensePlate;
        this.spaceId = spaceId;
        this.entryTime = entryTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public String getLicensePlate() { return licensePlate; }
    public int getSpaceId() { return spaceId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
}