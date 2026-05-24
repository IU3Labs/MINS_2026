package org.example.service.validation;

import org.example.domain.model.Seat;
import org.example.domain.model.Session;

public record TicketIssuingContext(Session session, Seat seat, String customerName) {
}
