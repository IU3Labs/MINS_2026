package org.example.domain.model.state;

import org.example.domain.exception.InvalidTicketOperationException;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;

public class PurchasedTicketState implements TicketState {
    @Override
    public TicketStatus status() {
        return TicketStatus.PURCHASED;
    }

    @Override
    public Ticket purchase(Ticket ticket) {
        throw new InvalidTicketOperationException("Ticket already purchased");
    }

    @Override
    public Ticket cancel(Ticket ticket) {
        return ticket.withStatus(TicketStatus.CANCELLED);
    }
}
