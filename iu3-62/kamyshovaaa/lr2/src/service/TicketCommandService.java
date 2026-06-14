package service;

import entity.Seat;
import entity.Ticket;

import java.util.UUID;

public interface TicketCommandService {
    Result<UUID> buyTicket(UUID screeningId, Seat seat, int categoryIndex);
    Result<Void> useTicket(UUID ticketId);
    Result<Void> cancelTicket(UUID ticketId);
}