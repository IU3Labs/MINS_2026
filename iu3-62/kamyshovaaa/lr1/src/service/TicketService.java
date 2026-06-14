package service;

import entity.Screening;
import entity.Seat;
import entity.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketService extends Service<Ticket> {
    void createInitialTickets(Screening screening);
    Result<Void> useTicket(UUID ticketId);
    Result<Void> cancelTicket(UUID ticketId);
    List<Ticket> getAll();
    List<Seat> getTakenSeats(UUID screeningId);
    List<Ticket> getPaidTicketsByScreening(UUID screeningId);
    Result<UUID> buyTicket(UUID screeningId, Seat seat);
}
