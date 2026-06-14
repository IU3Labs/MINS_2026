package service.impls;

import entity.Screening;
import entity.Ticket;
import entity.Seat;
import entity.TicketStatus;
import exception.ScreeningNotFoundException;
import exception.TicketNotFoundException;
import exception.ValidationException;
import service.CrudService;
import service.Result;
import service.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TicketServiceImpl implements TicketService {

    private final CrudService<Ticket> crudService;
    private final CrudService<Screening> screeningCrudService;

    public TicketServiceImpl(CrudService<Ticket> crudService,
                            CrudService<Screening> screeningCrudService) {
        this.crudService = crudService;
        this.screeningCrudService = screeningCrudService;
    }

    public List<Ticket> getAll() {
        return crudService.getAll();
    }

    public Optional<Ticket> getById(UUID id) {
        if (id == null) {
            throw new ValidationException("ID не может быть null");
        }
        return crudService.getById(id);
    }

    public void createInitialTickets(Screening screening) {
        if (screening == null) {
            throw new ValidationException("Сеанс не может быть null");
        }
        for (Seat seat : screening.getHall().getSeats()) {
            Ticket ticket = new Ticket(UUID.randomUUID(), screening.getId(), seat, screening.getTicketPrice());
            crudService.create(ticket);
        }
    }

    public Result<Void> useTicket(UUID ticketId) {
        try {
            if (ticketId == null) {
                throw new ValidationException("ID билета не может быть null");
            }
            Ticket ticket = getTicket(ticketId);
            if (!ticket.getStatus().canTransitionTo(TicketStatus.USED)) {
                throw new ValidationException("Нельзя использовать билет со статусом " + ticket.getStatus());
            }
            crudService.update(ticket.withStatus(TicketStatus.USED));
            return Result.success(null);
        } catch (ValidationException | TicketNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }

    public Result<Void> cancelTicket(UUID ticketId) {
        try {
            if (ticketId == null) {
                throw new ValidationException("ID билета не может быть null");
            }
            Ticket ticket = getTicket(ticketId);

            if (ticket.getStatus() == TicketStatus.USED) {
                throw new ValidationException("Нельзя отменить уже использованный билет");
            }

            Screening screening = screeningCrudService.getById(ticket.getScreeningId())
                    .orElseThrow(() -> new ScreeningNotFoundException(ticket.getScreeningId().toString()));

            if (screening.getStartTime().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Нельзя отменить билет на уже начавшийся сеанс");
            }

            if (!ticket.getStatus().canTransitionTo(TicketStatus.AVAILABLE)) {
                throw new ValidationException("Нельзя отменить билет со статусом " + ticket.getStatus());
            }

            crudService.update(ticket.withStatus(TicketStatus.AVAILABLE));
            return Result.success(null);
        } catch (ValidationException | TicketNotFoundException | ScreeningNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }

    public List<Seat> getTakenSeats(UUID screeningId) {
        if (screeningId == null) {
            throw new ValidationException("ID сеанса не может быть null");
        }
        return crudService.getAll().stream()
                .filter(t -> t.getScreeningId().equals(screeningId))
                .filter(t -> t.getStatus() == TicketStatus.PAID || t.getStatus() == TicketStatus.USED)
                .map(Ticket::getSeat)
                .toList();
    }

    public List<Ticket> getPaidTicketsByScreening(UUID screeningId) {
        if (screeningId == null) {
            throw new ValidationException("ID сеанса не может быть null");
        }
        return crudService.getAll().stream()
                .filter(t -> t.getScreeningId().equals(screeningId))
                .filter(t -> t.getStatus() == TicketStatus.PAID)
                .toList();
    }

    public Result<UUID> buyTicket(UUID screeningId, Seat seat) {
        try {
            if (screeningId == null || seat == null) {
                throw new ValidationException("Параметры не могут быть null");
            }
            Ticket ticket = crudService.getAll().stream()
                    .filter(t -> t.getScreeningId().equals(screeningId))
                    .filter(t -> t.getSeat().equals(seat))
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("Билет на это место не найден"));

            if (ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new ValidationException("Билет уже куплен");
            }

            crudService.update(ticket.withStatus(TicketStatus.PAID));
            return Result.success(ticket.getId());
        } catch (ValidationException e) {
            return Result.error(e.getMessage());
        }
    }

    private Ticket getTicket(UUID ticketId) {
        return crudService.getById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));
    }
}
