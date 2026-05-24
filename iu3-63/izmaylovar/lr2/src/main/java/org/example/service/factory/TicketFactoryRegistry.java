package org.example.service.factory;

import org.example.domain.exception.ValidationException;
import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TicketFactoryRegistry {
    private final Map<TicketStatus, TicketFactory> factories = new EnumMap<>(TicketStatus.class);

    public TicketFactoryRegistry(List<TicketFactory> factories) {
        // New ticket creation strategies are registered here without changing booking logic.
        for (TicketFactory factory : factories) {
            this.factories.put(factory.supportedStatus(), factory);
        }
    }

    public Ticket create(TicketStatus status, Session session, Seat seat, String customerName) {
        TicketFactory factory = factories.get(status);
        if (factory == null) {
            throw new ValidationException("No ticket factory registered for status: " + status);
        }
        return factory.create(session, seat, customerName);
    }
}
