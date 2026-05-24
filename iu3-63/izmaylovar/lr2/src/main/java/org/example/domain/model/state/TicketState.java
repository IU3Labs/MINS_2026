package org.example.domain.model.state;

import org.example.domain.model.Ticket;
import org.example.domain.model.TicketStatus;

public interface TicketState {
    TicketStatus status();

    Ticket purchase(Ticket ticket);

    Ticket cancel(Ticket ticket);
}
