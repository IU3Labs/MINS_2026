package org.example.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AttendanceReport {
    private final UUID sessionId;
    private final String movieTitle;
    private final LocalDateTime startTime;
    private final int hallCapacity;
    private final int reservedCount;
    private final int purchasedCount;
    private final int cancelledCount;
    private final int occupiedCount;
    private final int freeCount;
    private final double occupancyPercent;
    private final BigDecimal revenue;

    public AttendanceReport(UUID sessionId, String movieTitle, LocalDateTime startTime,
                            int hallCapacity, int reservedCount, int purchasedCount,
                            int cancelledCount, int occupiedCount, int freeCount,
                            double occupancyPercent, BigDecimal revenue) {
        this.sessionId = sessionId;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.hallCapacity = hallCapacity;
        this.reservedCount = reservedCount;
        this.purchasedCount = purchasedCount;
        this.cancelledCount = cancelledCount;
        this.occupiedCount = occupiedCount;
        this.freeCount = freeCount;
        this.occupancyPercent = occupancyPercent;
        this.revenue = revenue;
    }

    public UUID getSessionId() { return sessionId; }
    public String getMovieTitle() { return movieTitle; }
    public LocalDateTime getStartTime() { return startTime; }
    public int getHallCapacity() { return hallCapacity; }
    public int getReservedCount() { return reservedCount; }
    public int getPurchasedCount() { return purchasedCount; }
    public int getCancelledCount() { return cancelledCount; }
    public int getOccupiedCount() { return occupiedCount; }
    public int getFreeCount() { return freeCount; }
    public double getOccupancyPercent() { return occupancyPercent; }
    public BigDecimal getRevenue() { return revenue; }
}