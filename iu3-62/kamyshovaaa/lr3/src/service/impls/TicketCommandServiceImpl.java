package service.impls;

import entity.Screening;
import entity.Seat;
import entity.Ticket;
import exception.ScreeningNotFoundException;
import exception.TicketNotFoundException;
import exception.ValidationException;
import service.CrudService;
import service.Result;
import service.TicketCommandService;
import service.pricing.IPricingService;
import service.uow.IUnitOfWork;

import java.time.LocalDateTime;
import java.util.UUID;

public class TicketCommandServiceImpl implements TicketCommandService {

    private final CrudService<Ticket> crudService;
    private final CrudService<Screening> screeningCrudService;
    private final IPricingService pricingService;
    private final IUnitOfWork<Ticket> unitOfWork;

    public TicketCommandServiceImpl(CrudService<Ticket> crudService,
                                   CrudService<Screening> screeningCrudService,
                                   IPricingService pricingService,
                                   IUnitOfWork<Ticket> unitOfWork) {
        this.crudService = crudService;
        this.screeningCrudService = screeningCrudService;
        this.pricingService = pricingService;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public Result<UUID> buyTicket(UUID screeningId, Seat seat, int categoryIndex) {
        try {
            if (screeningId == null || seat == null) {
                throw new ValidationException("Параметры не могут быть null");
            }
            
            Ticket ticket = crudService.getAll().stream()
                    .filter(t -> t.getScreeningId().equals(screeningId))
                    .filter(t -> t.getSeat().equals(seat))
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("Билет на это место не найден"));

            if (!ticket.isAvailable()) {
                throw new ValidationException("Билет уже куплен");
            }

            Screening screening = screeningCrudService.getById(screeningId)
                    .orElseThrow(() -> new ScreeningNotFoundException(screeningId.toString()));
            
            pricingService.setStrategy(pricingService.getStrategies().get(categoryIndex).getCategory().getStrategyId());
            double basePrice = screening.getTicketPrice();
            double newPrice = pricingService.calculatePrice(basePrice);
            
            ticket.getContext().pay();
            ticket.setPrice(newPrice);
            unitOfWork.registerDirty(ticket);
            unitOfWork.commit();

            return Result.success(ticket.getId());
        } catch (ValidationException e) {
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Void> useTicket(UUID ticketId) {
        try {
            if (ticketId == null) {
                throw new ValidationException("ID билета не может быть null");
            }
            
            Ticket ticket = crudService.getById(ticketId)
                    .orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));
            
            ticket.getContext().use();
            unitOfWork.registerDirty(ticket);
            unitOfWork.commit();
            
            return Result.success(null);
        } catch (ValidationException | TicketNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<Void> cancelTicket(UUID ticketId) {
        try {
            if (ticketId == null) {
                throw new ValidationException("ID билета не может быть null");
            }
            
            Ticket ticket = crudService.getById(ticketId)
                    .orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));

            if (!ticket.canCancel()) {
                throw new ValidationException("Нельзя отменить билет со статусом " + ticket.getStatus());
            }

            Screening screening = screeningCrudService.getById(ticket.getScreeningId())
                    .orElseThrow(() -> new ScreeningNotFoundException(ticket.getScreeningId().toString()));

            if (screening.getStartTime().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Нельзя отменить билет на уже начавшийся сеанс");
            }

            ticket.getContext().cancel();
            unitOfWork.registerDirty(ticket);
            unitOfWork.commit();
            
            return Result.success(null);
        } catch (ValidationException | TicketNotFoundException | ScreeningNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }
}