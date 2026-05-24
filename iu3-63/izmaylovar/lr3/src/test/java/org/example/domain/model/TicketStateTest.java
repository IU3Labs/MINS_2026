package org.example.domain.model;

import org.example.domain.exception.InvalidTicketOperationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketStateTest {
    @Test
    void reservedTicketUsesStateObjectToBecomePurchased() {
        Ticket reserved = new Ticket(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Seat(1, 1),
                "Alice",
                TicketStatus.RESERVED,
                new BigDecimal("500"),
                LocalDateTime.of(2026, 4, 8, 18, 0)
        );

        Ticket purchased = reserved.markPurchased();

        assertEquals(TicketStatus.PURCHASED, purchased.getStatus());
    }

    @Test
    void cancelledTicketCannotBePurchasedAgain() {
        Ticket cancelled = new Ticket(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Seat(1, 1),
                "Alice",
                TicketStatus.CANCELLED,
                new BigDecimal("500"),
                LocalDateTime.of(2026, 4, 8, 18, 0)
        );

        assertThrows(InvalidTicketOperationException.class, cancelled::markPurchased);
    }
}
