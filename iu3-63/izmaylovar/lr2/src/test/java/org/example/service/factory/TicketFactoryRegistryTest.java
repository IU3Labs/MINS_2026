package org.example.service.factory;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketFactoryRegistryTest {
    private final Session session = new Session(
            UUID.randomUUID(),
            new Movie(UUID.randomUUID(), "Dune", 166, "Sci-Fi", "16+"),
            new Hall(UUID.randomUUID(), "Red Hall", 5, 6),
            LocalDateTime.of(2026, 4, 8, 18, 0),
            new BigDecimal("550")
    );

    @Test
    void createsReservedAndPurchasedTicketsThroughCommonFactoryContract() {
        TicketFactoryRegistry registry = new TicketFactoryRegistry(
                List.of(new ReservedTicketFactory(), new PurchasedTicketFactory())
        );

        Ticket reserved = registry.create(TicketStatus.RESERVED, session, new Seat(1, 1), "Alice");
        Ticket purchased = registry.create(TicketStatus.PURCHASED, session, new Seat(1, 2), "Bob");

        assertEquals(TicketStatus.RESERVED, reserved.getStatus());
        assertEquals(TicketStatus.PURCHASED, purchased.getStatus());
        assertEquals(session.getId(), reserved.getSessionId());
        assertEquals(session.getId(), purchased.getSessionId());
        assertEquals(session.getPrice(), reserved.getPrice());
        assertEquals(session.getPrice(), purchased.getPrice());
    }
}
