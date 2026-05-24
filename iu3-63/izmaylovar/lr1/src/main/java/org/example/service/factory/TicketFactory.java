package org.example.service.factory;

import org.example.domain.model.Seat;
import org.example.domain.model.Session;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;

public interface TicketFactory {
    TicketStatus supportedStatus();

    Ticket create(Session session, Seat seat, String customerName);
}
