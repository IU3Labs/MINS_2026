package ui.handlers;

import entity.Screening;
import exception.RepositoryException;
import exception.ValidationException;
import service.ScreeningService;
import ui.InputHandler;
import ui.renderer.GeneralRenderer;
import ui.renderer.ScreeningRenderer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ScreeningsHandler {
    private final ScreeningService screeningService;
    private final MoviesHandler moviesHandler;
    private final HallsHandler hallsHandler;
    private final InputHandler input;
    private final GeneralRenderer generalRenderer;
    private final ScreeningRenderer screeningRenderer;

    public ScreeningsHandler(ScreeningService screeningService, MoviesHandler moviesHandler,
                             HallsHandler hallsHandler, InputHandler input,
                             GeneralRenderer generalRenderer, ScreeningRenderer screeningRenderer) {
        this.screeningService = screeningService;
        this.moviesHandler = moviesHandler;
        this.hallsHandler = hallsHandler;
        this.input = input;
        this.generalRenderer = generalRenderer;
        this.screeningRenderer = screeningRenderer;
    }

    public void handleShowAll() {
        screeningRenderer.printScreenings(screeningService.getAll());
    }

    public void handleShowByDate() {
        LocalDate date = input.readDate("Дата (yyyy-MM-dd): ");
        List<Screening> screenings = screeningService.getByDate(date);
        screeningRenderer.printScreenings(screenings);
    }

    public void handleShowByPeriod() {
        LocalDate start = input.readDate("Дата начала (yyyy-MM-dd): ");
        LocalDate end = input.readDate("Дата конца (yyyy-MM-dd): ");
        List<Screening> screenings = screeningService.getByPeriod(start, end);
        screeningRenderer.printScreenings(screenings);
    }

    public void handleCreate() {
        moviesHandler.handleShow();
        UUID movieId = input.readUuid("ID фильма: ");

        hallsHandler.handleShow();
        UUID hallId = input.readUuid("ID зала: ");

        LocalDateTime time = input.readDateTime("Дата (yyyy-MM-dd): ", "Время (HH:mm): ");
        double price = input.readDouble("Цена билета: ");

        var result = screeningService.create(movieId, hallId, time, price);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Сеанс создан");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleEdit() {
        handleShowAll();
        UUID id = input.readUuid("ID сеанса для редактирования: ");

        moviesHandler.handleShow();
        UUID movieId = input.readUuid("ID фильма: ");

        hallsHandler.handleShow();
        UUID hallId = input.readUuid("ID зала: ");

        LocalDateTime time = input.readDateTime("Дата (yyyy-MM-dd): ", "Время (HH:mm): ");
        double price = input.readDouble("Цена билета: ");

        var result = screeningService.update(id, movieId, hallId, time, price);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Сеанс обновлён");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleDelete() {
        handleShowAll();
        UUID id = input.readUuid("ID сеанса для удаления: ");
        var result = screeningService.delete(id);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Сеанс удалён");
        } else {
            generalRenderer.printError(result.getError());
        }
    }
}
