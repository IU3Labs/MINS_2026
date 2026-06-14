package entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final UUID id;
    private final UUID screeningId;
    private final Seat seat;
    private final LocalDateTime createdAt;
    private final double price;
    private final TicketStatus status;

    public Ticket(UUID id, UUID screeningId, Seat seat, double price) {
        this.id = id;
        this.screeningId = screeningId;
        this.seat = seat;
        this.createdAt = LocalDateTime.now();
        this.price = price;
        this.status = TicketStatus.AVAILABLE;
    }

    private Ticket(UUID id, UUID screeningId, Seat seat, LocalDateTime createdAt, double price, TicketStatus status) {
        this.id = id;
        this.screeningId = screeningId;
        this.seat = seat;
        this.createdAt = createdAt;
        this.price = price;
        this.status = status;
    }

    public UUID getId() { return id; }
    public UUID getScreeningId() { return screeningId; }
    public Seat getSeat() { return seat; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public double getPrice() { return price; }
    public TicketStatus getStatus() { return status; }

    public Ticket withStatus(TicketStatus newStatus) {
        return new Ticket(id, screeningId, seat, createdAt, price, newStatus);
    }
}
