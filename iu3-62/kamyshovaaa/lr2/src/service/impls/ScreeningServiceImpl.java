package service.impls;

import entity.Hall;
import entity.Movie;
import entity.Screening;
import entity.Ticket;
import exception.HallNotFoundException;
import exception.MovieNotFoundException;
import exception.ScreeningNotFoundException;
import exception.ValidationException;
import service.CrudService;
import service.HallService;
import service.MovieService;
import service.Result;
import service.ScreeningService;
import service.TicketCommandService;
import service.TicketQueryService;
import service.uow.IUnitOfWork;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ScreeningServiceImpl implements ScreeningService {

    private final CrudService<Screening> crudService;
    private final MovieService movieService;
    private final HallService hallService;
    private final TicketQueryService ticketQueryService;
    private final IUnitOfWork<Screening> screeningUoW;
    private final IUnitOfWork<Ticket> ticketUoW;

    public ScreeningServiceImpl(CrudService<Screening> crudService,
                                MovieService movieService,
                                HallService hallService,
                                TicketCommandService ticketCommandService,
                                TicketQueryService ticketQueryService,
                                IUnitOfWork<Screening> screeningUoW,
                                IUnitOfWork<Ticket> ticketUoW) {
        this.crudService = crudService;
        this.movieService = movieService;
        this.hallService = hallService;
        this.ticketQueryService = ticketQueryService;
        this.screeningUoW = screeningUoW;
        this.ticketUoW = ticketUoW;
    }

    public Result<UUID> create(UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        try {
            validateScreeningInput(movieId, hallId, startTime, ticketPrice);
            validateScreening(startTime, ticketPrice);

            Movie movie = movieService.getById(movieId)
                    .orElseThrow(() -> new MovieNotFoundException(movieId.toString()));
            Hall hall = hallService.getById(hallId)
                    .orElseThrow(() -> new HallNotFoundException(hallId.toString()));

            Screening screening = new Screening(UUID.randomUUID(), movie, hall, startTime, ticketPrice);
            screeningUoW.registerNew(screening);

            for (entity.Seat seat : hall.getSeats()) {
                Ticket ticket = new Ticket(UUID.randomUUID(), screening.getId(), seat, ticketPrice);
                ticketUoW.registerNew(ticket);
            }

            screeningUoW.commit();
            ticketUoW.commit();

            return Result.success(screening.getId());
        } catch (ValidationException | MovieNotFoundException | HallNotFoundException e) {
            screeningUoW.rollback();
            ticketUoW.rollback();
            return Result.error(e.getMessage());
        } catch (Exception e) {
            screeningUoW.rollback();
            ticketUoW.rollback();
            return Result.error("Не удалось создать сеанс: " + e.getMessage());
        }
    }

    public List<Screening> getAll() {
        return crudService.getAll();
    }

    public List<Screening> getByDate(LocalDate date) {
        if (date == null) {
            throw new ValidationException("Дата не может быть null");
        }
        return crudService.getAll().stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .toList();
    }

    public List<Screening> getByPeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new ValidationException("Даты не могут быть null");
        }
        return crudService.getAll().stream()
                .filter(s -> {
                    LocalDate date = s.getStartTime().toLocalDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .toList();
    }

    public Optional<Screening> getById(UUID id) {
        if (id == null) {
            throw new ValidationException("ID не может быть null");
        }
        return crudService.getById(id);
    }

    public Result<Void> update(UUID id, UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        try {
            validateScreeningInput(id, movieId, hallId, startTime, ticketPrice);
            
            Screening existing = getById(id)
                    .orElseThrow(() -> new ScreeningNotFoundException(id.toString()));

            if (existing.getStartTime().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Нельзя редактировать прошедший сеанс");
            }

            validateScreening(startTime, ticketPrice);

            Movie movie = movieService.getById(movieId)
                    .orElseThrow(() -> new MovieNotFoundException(movieId.toString()));
            Hall hall = hallService.getById(hallId)
                    .orElseThrow(() -> new HallNotFoundException(hallId.toString()));

            Screening updated = new Screening(id, movie, hall, startTime, ticketPrice);
            screeningUoW.registerDirty(updated);
            screeningUoW.commit();
            return Result.success(null);
        } catch (ValidationException | MovieNotFoundException | HallNotFoundException | ScreeningNotFoundException e) {
            screeningUoW.rollback();
            return Result.error(e.getMessage());
        }
    }

    public Result<Void> delete(UUID id) {
        try {
            if (id == null) {
                throw new ValidationException("ID сеанса не может быть null");
            }
            Screening screening = getById(id)
                    .orElseThrow(() -> new ScreeningNotFoundException(id.toString()));
            
            boolean hasBoughtTickets = crudService.getAll().stream()
                    .filter(s -> s.getId().equals(id))
                    .flatMap(s -> ticketQueryService.getTakenSeats(s.getId()).stream())
                    .count() > 0;
            
            if (hasBoughtTickets) {
                throw new ValidationException("Нельзя удалить сеанс с купленными билетами");
            }
            
            screeningUoW.registerRemoved(screening, screening.getId());
            screeningUoW.commit();
            return Result.success(null);
        } catch (ValidationException | ScreeningNotFoundException e) {
            screeningUoW.rollback();
            return Result.error(e.getMessage());
        }
    }

    public boolean hasScreeningsForEntity(UUID entityId, java.util.function.Function<Screening, UUID> extractor) {
        return crudService.getAll().stream()
                .anyMatch(s -> extractor.apply(s).equals(entityId));
    }

    private void validateScreeningInput(UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        if (movieId == null || hallId == null || startTime == null) {
            throw new ValidationException("Параметры не могут быть null");
        }
    }

    private void validateScreeningInput(UUID id, UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        if (id == null || movieId == null || hallId == null || startTime == null) {
            throw new ValidationException("Параметры не могут быть null");
        }
    }

    private void validateScreening(LocalDateTime startTime, double ticketPrice) {
        if (ticketPrice <= 0) {
            throw new ValidationException("Цена билета должна быть положительной");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Нельзя создать сеанс в прошлом");
        }
    }
}
