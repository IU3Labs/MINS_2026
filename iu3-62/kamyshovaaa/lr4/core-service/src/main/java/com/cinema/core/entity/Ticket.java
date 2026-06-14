package com.cinema.core.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final UUID id;
    private final UUID screeningId;
    private final int seatRow;
    private final int seatNumber;
    private final LocalDateTime createdAt;
    private double price;
    private String status;

    public Ticket(UUID id, UUID screeningId, int seatRow, int seatNumber, double price) {
        this.id = id;
        this.screeningId = screeningId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.createdAt = LocalDateTime.now();
        this.price = price;
        this.status = "AVAILABLE";
    }

    public UUID getId() { return id; }
    public UUID getScreeningId() { return screeningId; }
    public int getSeatRow() { return seatRow; }
    public int getSeatNumber() { return seatNumber; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }

    public boolean isAvailable() { return "AVAILABLE".equals(status); }
    public boolean isPaid() { return "PAID".equals(status); }
    public boolean isUsed() { return "USED".equals(status); }
    public boolean canCancel() { return "AVAILABLE".equals(status); }
}