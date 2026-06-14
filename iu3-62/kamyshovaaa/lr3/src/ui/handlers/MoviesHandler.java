package ui.handlers;

import exception.RepositoryException;
import exception.ValidationException;
import service.MovieService;
import ui.InputHandler;
import ui.renderer.GeneralRenderer;
import ui.renderer.MovieRenderer;

import java.util.UUID;

public class MoviesHandler {
    private final MovieService movieService;
    private final InputHandler input;
    private final GeneralRenderer generalRenderer;
    private final MovieRenderer movieRenderer;

    public MoviesHandler(MovieService movieService,
                         InputHandler input, GeneralRenderer generalRenderer, MovieRenderer movieRenderer) {
        this.movieService = movieService;
        this.input = input;
        this.generalRenderer = generalRenderer;
        this.movieRenderer = movieRenderer;
    }

    public void handleShow() {
        movieRenderer.printMovies(movieService.getAll());
    }

    public void handleCreate() {
        String title = input.readLine("Название: ");
        int duration = input.readInt("Продолжительность (мин): ");
        String genre = input.readLine("Жанр: ");
        int age = input.readInt("Возрастное ограничение: ");

        var result = movieService.create(title, duration, genre, age);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Фильм создан");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleEdit() {
        movieRenderer.printMovies(movieService.getAll());
        UUID id = input.readUuid("ID фильма для редактирования: ");

        String title = input.readLine("Название: ");
        int duration = input.readInt("Продолжительность (мин): ");
        String genre = input.readLine("Жанр: ");
        int age = input.readInt("Возрастное ограничение: ");

        var result = movieService.update(id, title, duration, genre, age);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Фильм обновлён");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleDelete() {
        movieRenderer.printMovies(movieService.getAll());
        UUID id = input.readUuid("ID фильма для удаления: ");

        var result = movieService.delete(id);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Фильм удалён");
        } else {
            generalRenderer.printError(result.getError());
        }
    }
}
