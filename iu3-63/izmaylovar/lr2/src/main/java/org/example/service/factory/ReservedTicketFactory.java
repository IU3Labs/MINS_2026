package org.example.service.factory;

import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservedTicketFactory implements TicketFactory {
    @Override
    public TicketStatus supportedStatus() {
        return TicketStatus.RESERVED;
    }

    @Override
    public Ticket create(Session session, Seat seat, String customerName) {
        return new Ticket(
                UUID.randomUUID(),
                session.getId(),
                seat,
                customerName,
                TicketStatus.RESERVED,
                session.getPrice(),
                LocalDateTime.now()
        );
    }
}
