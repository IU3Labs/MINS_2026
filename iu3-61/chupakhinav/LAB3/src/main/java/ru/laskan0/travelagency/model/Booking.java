package ru.laskan0.travelagency.model;

import java.time.LocalDateTime;

import ru.laskan0.travelagency.exception.BookingStateException;
import ru.laskan0.travelagency.exception.InvalidDataException;

public class Booking {
    public enum Status {
        CREATED,
        CANCELLED
    }

    private final String id;
    private final Client client;
    private final Tour tour;
    private final double finalPrice;
    private final LocalDateTime createdAt;
    private Status status;

    public Booking(String id, Client client, Tour tour, double finalPrice) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("ID бронирования не может быть пустым");
        }
        if (client == null) {
            throw new InvalidDataException("Клиент обязателен для бронирования");
        }
        if (tour == null) {
            throw new InvalidDataException("Тур обязателен для бронирования");
        }
        if (finalPrice <= 0) {
            throw new InvalidDataException("Итоговая цена бронирования должна быть больше 0");
        }
        this.id = id.trim();
        this.client = client;
        this.tour = tour;
        this.finalPrice = finalPrice;
        this.createdAt = LocalDateTime.now();
        this.status = Status.CREATED;
    }

    public String getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Tour getTour() {
        return tour;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isCancelled() {
        return status == Status.CANCELLED;
    }

    public void cancel() {
        if (isCancelled()) {
            throw new BookingStateException("Бронирование уже отменено: " + id);
        }
        this.status = Status.CANCELLED;
    }

    @Override
    public String toString() {
        return "Booking{id='" + id + "', client='" + client.getFullName() + "', tour='" + tour.getTitle()
                + "', finalPrice=" + String.format("%.2f", finalPrice) + ", status=" + status + "}";
    }
}
