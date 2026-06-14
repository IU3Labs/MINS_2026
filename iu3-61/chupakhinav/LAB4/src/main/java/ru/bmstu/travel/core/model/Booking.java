package ru.bmstu.travel.core.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private final String id;
    private final String clientId;
    private final String clientName;
    private final String tourId;
    private final String tourTitle;
    private final double finalPrice;
    private final String createdAt;
    private BookingStatus status;

    public Booking(String id, String clientId, String clientName, String tourId, String tourTitle, double finalPrice) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.tourId = tourId;
        this.tourTitle = tourTitle;
        this.finalPrice = finalPrice;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.status = BookingStatus.CREATED;
    }

    public String id() {
        return id;
    }

    public String clientId() {
        return clientId;
    }

    public String clientName() {
        return clientName;
    }

    public String tourId() {
        return tourId;
    }

    public String tourTitle() {
        return tourTitle;
    }

    public double finalPrice() {
        return finalPrice;
    }

    public String createdAt() {
        return createdAt;
    }

    public BookingStatus status() {
        return status;
    }

    public void cancel() {
        status = BookingStatus.CANCELLED;
    }
}
