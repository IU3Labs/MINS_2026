package service;

import entity.Ticket;
import entity.Seat;
import service.pricing.IPricingService;

import java.util.List;
import java.util.UUID;

public interface TicketQueryService {
    List<Ticket> getAll();
    List<Seat> getTakenSeats(UUID screeningId);
    List<Ticket> getPaidTicketsByScreening(UUID screeningId);
    List<Ticket> getAvailableTicketsByScreening(UUID screeningId);
    Ticket getById(UUID ticketId);
    IPricingService getPricingService();
}