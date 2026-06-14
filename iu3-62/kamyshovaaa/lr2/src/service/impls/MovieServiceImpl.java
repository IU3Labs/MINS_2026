package service.impls;

import entity.Movie;
import exception.MovieNotFoundException;
import exception.RepositoryException;
import exception.ValidationException;
import service.CrudService;
import service.MovieService;
import service.Result;
import service.ScreeningService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MovieServiceImpl implements MovieService {

    private final CrudService<Movie> crudService;
    private final ScreeningService screeningService;

    public MovieServiceImpl(CrudService<Movie> crudService,
                           ScreeningService screeningService) {
        this.crudService = crudService;
        this.screeningService = screeningService;
    }

    public Result<Void> create(String title, int durationMinutes, String genre, int ageRestriction) {
        try {
            validate(title, durationMinutes, ageRestriction);

            Movie movie = new Movie(
                    UUID.randomUUID(),
                    title.trim(),
                    Duration.ofMinutes(durationMinutes),
                    genre.trim(),
                    ageRestriction
            );
            crudService.create(movie);
            return Result.success(null);
        } catch (ValidationException e) {
            return Result.error(e.getMessage());
        } catch (RepositoryException e) {
            return Result.error("Не удалось создать: " + e.getMessage());
        }
    }

    public List<Movie> getAll() {
        return crudService.getAll();
    }

    public Optional<Movie> getById(UUID id) {
        if (id == null) {
            throw new ValidationException("ID не может быть null");
        }
        return crudService.getById(id);
    }

    public Result<Void> update(UUID id, String title, int durationMinutes, String genre, int ageRestriction) {
        try {
            if (id == null) {
                throw new ValidationException("ID не может быть null");
            }
            getById(id).orElseThrow(() -> new MovieNotFoundException(id.toString()));
            validate(title, durationMinutes, ageRestriction);

            Movie updated = new Movie(id, title.trim(), Duration.ofMinutes(durationMinutes), genre.trim(), ageRestriction);
            crudService.update(updated);
            return Result.success(null);
        } catch (ValidationException | MovieNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }

    public Result<Void> delete(UUID id) {
        try {
            if (id == null) {
                throw new ValidationException("ID не может быть null");
            }
            getById(id).orElseThrow(() -> new MovieNotFoundException(id.toString()));
            
            if (screeningService.hasScreeningsForEntity(id, s -> s.getMovie().getId())) {
                throw new ValidationException("Нельзя удалить фильм, на который назначены сеансы");
            }
            
            crudService.delete(id);
            return Result.success(null);
        } catch (ValidationException | MovieNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }

    private void validate(String title, int durationMinutes, int ageRestriction) {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (durationMinutes <= 0) {
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (ageRestriction < 0) {
            throw new ValidationException("Возрастное ограничение не может быть отрицательным");
        }
    }
}
