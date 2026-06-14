package service;

import entity.Screening;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface ScreeningService extends Service<Screening> {
    Result<UUID> create(UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice);
    List<Screening> getAll();
    List<Screening> getByDate(LocalDate date);
    List<Screening> getByPeriod(LocalDate start, LocalDate end);
    Optional<Screening> getById(UUID id);
    Result<Void> update(UUID id, UUID movieId, UUID hallId, LocalDateTime startTime, double ticketPrice);
    Result<Void> delete(UUID id);
    boolean hasScreeningsForEntity(UUID entityId, Function<Screening, UUID> extractor);
}
