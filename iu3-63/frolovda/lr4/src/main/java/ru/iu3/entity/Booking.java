package ru.iu3.entity;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int roomId;
    private int passId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;

    public Booking(int id, int roomId, int passId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.roomId = roomId;
        this.passId = passId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getPassId() {
        return passId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }
}
