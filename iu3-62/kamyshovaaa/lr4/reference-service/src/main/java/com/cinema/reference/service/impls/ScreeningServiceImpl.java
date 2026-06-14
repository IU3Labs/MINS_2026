package com.cinema.reference.service.impls;

import com.cinema.reference.entity.Hall;
import com.cinema.reference.entity.Movie;
import com.cinema.reference.entity.Screening;
import com.cinema.reference.exception.HallNotFoundException;
import com.cinema.reference.exception.MovieNotFoundException;
import com.cinema.reference.exception.ScreeningNotFoundException;
import com.cinema.reference.exception.ValidationException;
import com.cinema.reference.service.*;
import com.cinema.reference.service.uow.IUnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ScreeningServiceImpl implements ScreeningService {

    private static final Logger log = LoggerFactory.getLogger(ScreeningServiceImpl.class);

    private final CrudService<Screening> crudService;
    private final MovieService movieService;
    private final HallService hallService;
    private final IUnitOfWork<Screening> screeningUoW;

    // Убираем всё, что связано с TicketQueryService — его нет в Reference Service!
    public ScreeningServiceImpl(CrudService<Screening> crudService,
                                MovieService movieService,
                                HallService hallService,
                                IUnitOfWork<Screening> screeningUoW) {
        this.crudService = crudService;
        this.movieService = movieService;
        this.hallService = hallService;
        this.screeningUoW = screeningUoW;
    }

    /**
     * Создаёт сеанс. Билеты создаются отдельно в Core Service.
     */
    public Result<UUID> create(UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        try {
            log.debug("Creating screening: movieId={}, hallId={}, startTime={}, price={}",
                    movieId, hallId, startTime, ticketPrice);

            validateScreeningInput(movieId, hallId, startTime, ticketPrice);
            validateScreening(startTime, ticketPrice);

            Movie movie = movieService.getById(movieId)
                    .orElseThrow(() -> new MovieNotFoundException(movieId.toString()));
            Hall hall = hallService.getById(hallId)
                    .orElseThrow(() -> new HallNotFoundException(hallId.toString()));

            Screening screening = new Screening(UUID.randomUUID(), movie, hall, startTime, ticketPrice);
            screeningUoW.registerNew(screening);
            screeningUoW.commit();

            log.info("Screening created with id: {}", screening.getId());
            return Result.success(screening.getId());

        } catch (ValidationException | MovieNotFoundException | HallNotFoundException e) {
            log.warn("Failed to create screening: {}", e.getMessage());
            screeningUoW.rollback();
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while creating screening", e);
            screeningUoW.rollback();
            return Result.error("Failed to create screening: " + e.getMessage());
        }
    }

    /**
     * Получение всех сеансов.
     */
    public List<Screening> getAll() {
        return crudService.getAll();
    }

    /**
     * Получение сеансов по дате.
     */
    public List<Screening> getByDate(LocalDate date) {
        if (date == null) {
            throw new ValidationException("Date cannot be null");
        }
        return crudService.getAll().stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .toList();
    }

    /**
     * Получение сеансов за период.
     */
    public List<Screening> getByPeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new ValidationException("Start and end dates cannot be null");
        }
        return crudService.getAll().stream()
                .filter(s -> {
                    LocalDate date = s.getStartTime().toLocalDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .toList();
    }

    /**
     * Получение сеанса по ID.
     */
    public Optional<Screening> getById(UUID id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return crudService.getById(id);
    }

    /**
     * Обновление сеанса.
     */
    public Result<Void> update(UUID id, UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        try {
            log.debug("Updating screening: id={}, movieId={}, hallId={}, startTime={}, price={}",
                    id, movieId, hallId, startTime, ticketPrice);

            validateScreeningInput(id, movieId, hallId, startTime, ticketPrice);

            Screening existing = getById(id)
                    .orElseThrow(() -> new ScreeningNotFoundException(id.toString()));

            if (existing.getStartTime().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Cannot edit past screening");
            }

            validateScreening(startTime, ticketPrice);

            Movie movie = movieService.getById(movieId)
                    .orElseThrow(() -> new MovieNotFoundException(movieId.toString()));
            Hall hall = hallService.getById(hallId)
                    .orElseThrow(() -> new HallNotFoundException(hallId.toString()));

            Screening updated = new Screening(id, movie, hall, startTime, ticketPrice);
            screeningUoW.registerDirty(updated);
            screeningUoW.commit();

            log.info("Screening updated: {}", id);
            return Result.success(null);

        } catch (ValidationException | MovieNotFoundException | HallNotFoundException | ScreeningNotFoundException e) {
            log.warn("Failed to update screening: {}", e.getMessage());
            screeningUoW.rollback();
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while updating screening", e);
            screeningUoW.rollback();
            return Result.error("Failed to update screening: " + e.getMessage());
        }
    }

    /**
     * Удаление сеанса.
     *
     * В Reference Service нет билетов, поэтому проверка на купленные билеты
     * должна выполняться в Core Service. Здесь мы просто удаляем сеанс.
     */
    public Result<Void> delete(UUID id) {
        try {
            log.debug("Deleting screening: id={}", id);

            if (id == null) {
                throw new ValidationException("Screening ID cannot be null");
            }
            Screening screening = getById(id)
                    .orElseThrow(() -> new ScreeningNotFoundException(id.toString()));

            // В Reference Service нет информации о билетах,
            // поэтому проверку на купленные билеты делает Core Service перед вызовом удаления

            screeningUoW.registerRemoved(screening, screening.getId());
            screeningUoW.commit();

            log.info("Screening deleted: {}", id);
            return Result.success(null);

        } catch (ValidationException | ScreeningNotFoundException e) {
            log.warn("Failed to delete screening: {}", e.getMessage());
            screeningUoW.rollback();
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while deleting screening", e);
            screeningUoW.rollback();
            return Result.error("Failed to delete screening: " + e.getMessage());
        }
    }

    /**
     * Проверка, есть ли сеансы для сущности (фильма или зала).
     */
    public boolean hasScreeningsForEntity(UUID entityId, java.util.function.Function<Screening, UUID> extractor) {
        return crudService.getAll().stream()
                .anyMatch(s -> extractor.apply(s).equals(entityId));
    }

    // ==================== Private validation methods ====================

    private void validateScreeningInput(UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        if (movieId == null || hallId == null || startTime == null) {
            throw new ValidationException("Movie ID, Hall ID and Start Time are required");
        }
    }

    private void validateScreeningInput(UUID id, UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice) {
        if (id == null || movieId == null || hallId == null || startTime == null) {
            throw new ValidationException("ID, Movie ID, Hall ID and Start Time are required");
        }
    }

    private void validateScreening(LocalDateTime startTime, double ticketPrice) {
        if (ticketPrice <= 0) {
            throw new ValidationException("Ticket price must be positive");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Cannot create screening in the past");
        }
    }
}