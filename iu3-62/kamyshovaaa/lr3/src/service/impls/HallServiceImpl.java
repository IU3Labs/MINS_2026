package service.impls;

import entity.Hall;
import exception.HallNotFoundException;
import exception.RepositoryException;
import exception.ValidationException;
import service.CrudService;
import service.HallService;
import service.Result;
import service.ScreeningService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HallServiceImpl implements HallService {

    private final CrudService<Hall> crudService;
    private final ScreeningService screeningService;

    public HallServiceImpl(CrudService<Hall> crudService,
                          ScreeningService screeningService) {
        this.crudService = crudService;
        this.screeningService = screeningService;
    }

    public Result<Void> create(String name, int rows, int seatsPerRow) {
        try {
            validateHallParams(rows, seatsPerRow);

            Hall hall = new Hall(UUID.randomUUID(), name, rows, seatsPerRow);
            crudService.create(hall);
            return Result.success(null);
        } catch (ValidationException e) {
            return Result.error(e.getMessage());
        } catch (RepositoryException e) {
            return Result.error("Не удалось создать: " + e.getMessage());
        }
    }

    public List<Hall> getAll() {
        return crudService.getAll();
    }

    public Optional<Hall> getById(UUID id) {
        if (id == null) {
            throw new ValidationException("ID не может быть null");
        }
        return crudService.getById(id);
    }

    public Result<Void> update(UUID id, String name, int rows, int seatsPerRow) {
        try {
            if (id == null) {
                throw new ValidationException("ID не может быть null");
            }
            getById(id).orElseThrow(() -> new HallNotFoundException(id.toString()));
            validateHallParams(rows, seatsPerRow);

            Hall updated = new Hall(id, name, rows, seatsPerRow);
            crudService.update(updated);
            return Result.success(null);
        } catch (ValidationException | HallNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }

    public Result<Void> delete(UUID id) {
        try {
            if (id == null) {
                throw new ValidationException("ID не может быть null");
            }
            getById(id).orElseThrow(() -> new HallNotFoundException(id.toString()));
            
            if (screeningService.hasScreeningsForEntity(id, s -> s.getHall().getId())) {
                throw new ValidationException("Нельзя удалить зал, на который назначены сеансы");
            }
            
            crudService.delete(id);
            return Result.success(null);
        } catch (ValidationException | HallNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }

    private void validateHallParams(int rows, int seatsPerRow) {
        if (rows <= 0) {
            throw new ValidationException("Количество рядов должно быть положительным");
        }
        if (seatsPerRow <= 0) {
            throw new ValidationException("Количество мест в ряду должно быть положительным");
        }
    }
}
