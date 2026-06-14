package entity;

import entity.state.TicketContext;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final UUID id;
    private final UUID screeningId;
    private final Seat seat;
    private final LocalDateTime createdAt;
    private final double price;
    private final TicketContext context;

    public Ticket(UUID id, UUID screeningId, Seat seat, double price) {
        this.id = id;
        this.screeningId = screeningId;
        this.seat = seat;
        this.createdAt = LocalDateTime.now();
        this.price = price;
        this.context = new TicketContext();
    }

    public UUID getId() { return id; }
    public UUID getScreeningId() { return screeningId; }
    public Seat getSeat() { return seat; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public double getPrice() { return price; }
    public TicketContext getContext() { return context; }

    public String getStatusName() {
        return context.getStatusName();
    }

    public boolean canCancel() {
        return context.canCancel();
    }

    public boolean isAvailable() {
        return context.isAvailable();
    }

    public boolean isPaid() {
        return context.isPaid();
    }

    public boolean isUsed() {
        return context.isUsed();
    }
}
