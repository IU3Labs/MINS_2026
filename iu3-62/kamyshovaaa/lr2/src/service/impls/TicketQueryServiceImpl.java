package service.impls;

import entity.Seat;
import entity.Ticket;
import exception.TicketNotFoundException;
import service.CrudService;
import service.TicketQueryService;
import service.pricing.IPricingService;

import java.util.List;
import java.util.UUID;

public class TicketQueryServiceImpl implements TicketQueryService {

    private final CrudService<Ticket> crudService;
    private final IPricingService pricingService;

    public TicketQueryServiceImpl(CrudService<Ticket> crudService, IPricingService pricingService) {
        this.crudService = crudService;
        this.pricingService = pricingService;
    }

    @Override
    public List<Ticket> getAll() {
        return crudService.getAll();
    }

    @Override
    public List<Seat> getTakenSeats(UUID screeningId) {
        return crudService.getAll().stream()
                .filter(t -> t.getScreeningId().equals(screeningId))
                .filter(t -> t.isPaid() || t.isUsed())
                .map(Ticket::getSeat)
                .toList();
    }

    @Override
    public List<Ticket> getPaidTicketsByScreening(UUID screeningId) {
        return crudService.getAll().stream()
                .filter(t -> t.getScreeningId().equals(screeningId))
                .filter(Ticket::isPaid)
                .toList();
    }

    @Override
    public List<Ticket> getAvailableTicketsByScreening(UUID screeningId) {
        return crudService.getAll().stream()
                .filter(t -> t.getScreeningId().equals(screeningId))
                .filter(Ticket::isAvailable)
                .toList();
    }

    @Override
    public Ticket getById(UUID ticketId) {
        return crudService.getById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));
    }

    @Override
    public IPricingService getPricingService() {
        return pricingService;
    }
}