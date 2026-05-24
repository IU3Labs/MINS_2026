package org.example.domain.model;

import org.example.domain.model.state.TicketStateRegistry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Ticket {
    private final UUID id;
    private final UUID sessionId;
    private final Seat seat;
    private final String customerName;
    private final TicketStatus status;
    private final BigDecimal price;
    private final LocalDateTime createdAt;

    public Ticket(UUID id,
                  UUID sessionId,
                  Seat seat,
                  String customerName,
                  TicketStatus status,
                  BigDecimal price,
                  LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id);
        this.sessionId = Objects.requireNonNull(sessionId);
        this.seat = Objects.requireNonNull(seat);
        this.customerName = (customerName == null || customerName.isBlank()) ? null : customerName;
        this.status = Objects.requireNonNull(status);
        this.price = Objects.requireNonNull(price);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public Ticket markPurchased() {
        return TicketStateRegistry.resolve(status).purchase(this);
    }

    public Ticket cancel() {
        return TicketStateRegistry.resolve(status).cancel(this);
    }

    public Ticket withStatus(TicketStatus newStatus) {
        return new Ticket(id, sessionId, seat, customerName, newStatus, price, createdAt);
    }

    public UUID getId() { return id; }
    public UUID getSessionId() { return sessionId; }
    public Seat getSeat() { return seat; }
    public String getCustomerName() { return customerName; }
    public TicketStatus getStatus() { return status; }
    public BigDecimal getPrice() { return price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
