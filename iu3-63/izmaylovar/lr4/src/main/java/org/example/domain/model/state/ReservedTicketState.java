package org.example.domain.model.state;

import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;

public class ReservedTicketState implements TicketState {
    @Override
    public TicketStatus status() {
        return TicketStatus.RESERVED;
    }

    @Override
    public Ticket purchase(Ticket ticket) {
        return ticket.withStatus(TicketStatus.PURCHASED);
    }

    @Override
    public Ticket cancel(Ticket ticket) {
        return ticket.withStatus(TicketStatus.CANCELLED);
    }
}
