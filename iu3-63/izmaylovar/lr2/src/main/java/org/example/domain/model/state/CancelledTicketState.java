package org.example.domain.model.state;

import org.example.domain.exception.InvalidTicketOperationException;
import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;

public class CancelledTicketState implements TicketState {
    @Override
    public TicketStatus status() {
        return TicketStatus.CANCELLED;
    }

    @Override
    public Ticket purchase(Ticket ticket) {
        throw new InvalidTicketOperationException("You can't buy a cancelled ticket");
    }

    @Override
    public Ticket cancel(Ticket ticket) {
        throw new InvalidTicketOperationException("Ticket has already been canceled");
    }
}
