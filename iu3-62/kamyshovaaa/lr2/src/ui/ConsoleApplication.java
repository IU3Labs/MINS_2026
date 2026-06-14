package ui;

import ui.handlers.*;
import ui.renderer.GeneralRenderer;
import exception.CinemaException;

import java.time.format.DateTimeParseException;
import java.util.List;

public class ConsoleApplication {

    private final MoviesHandler moviesHandler;
    private final HallsHandler hallsHandler;
    private final ScreeningsHandler screeningsHandler;
    private final TicketsHandler ticketsHandler;
    private final InputHandler input;
    private final GeneralRenderer generalRenderer;

    public ConsoleApplication(
            MoviesHandler moviesHandler,
            HallsHandler hallsHandler,
            ScreeningsHandler screeningsHandler,
            TicketsHandler ticketsHandler,
            InputHandler input,
            GeneralRenderer generalRenderer
    ) {
        this.moviesHandler = moviesHandler;
        this.hallsHandler = hallsHandler;
        this.screeningsHandler = screeningsHandler;
        this.ticketsHandler = ticketsHandler;
        this.input = input;
        this.generalRenderer = generalRenderer;
    }

    public void start() {
        while (true) {
            generalRenderer.printHeader("КИНОТЕАТР");
            generalRenderer.printMenu(List.of(
                new GeneralRenderer.MenuItem("1", "Управление фильмами"),
                new GeneralRenderer.MenuItem("2", "Управление залами"),
                new GeneralRenderer.MenuItem("3", "Управление сеансами"),
                new GeneralRenderer.MenuItem("4", "Управление билетами"),
                new GeneralRenderer.MenuItem("5", "Показать расписание"),
                new GeneralRenderer.MenuItem("0", "Выход")
            ));

            String choice = input.readChoice(List.of());

            try {
                switch (choice) {
                    case "1": runMoviesMenu(); break;
                    case "2": runHallsMenu(); break;
                    case "3": runScreeningsMenu(); break;
                    case "4": runTicketsMenu(); break;
                    case "5": runScheduleMenu(); break;
                    case "0": return;
                    default: generalRenderer.printError("Неверное значение");
                }
            } catch (CinemaException e) {
                generalRenderer.printError(e.getMessage());
            } catch (IllegalArgumentException | DateTimeParseException e) {
                generalRenderer.printError("Некорректный ввод: " + e.getMessage());
            }
        }
    }

    private void runMoviesMenu() {
        while (true) {
            generalRenderer.printSubHeader("Управление фильмами");
            generalRenderer.printMenu(List.of(
                new GeneralRenderer.MenuItem("1", "Показать все фильмы"),
                new GeneralRenderer.MenuItem("2", "Создать фильм"),
                new GeneralRenderer.MenuItem("3", "Редактировать фильм"),
                new GeneralRenderer.MenuItem("4", "Удалить фильм"),
                new GeneralRenderer.MenuItem("0", "Назад")
            ));
            String choice = input.readChoice(List.of());
            if (choice.equals("0")) return;
            switch (choice) {
                case "1": moviesHandler.handleShow(); break;
                case "2": moviesHandler.handleCreate(); break;
                case "3": moviesHandler.handleEdit(); break;
                case "4": moviesHandler.handleDelete(); break;
                default: generalRenderer.printError("Неверное значение");
            }
        }
    }

    private void runHallsMenu() {
        while (true) {
            generalRenderer.printSubHeader("Управление залами");
            generalRenderer.printMenu(List.of(
                new GeneralRenderer.MenuItem("1", "Показать все залы"),
                new GeneralRenderer.MenuItem("2", "Создать зал"),
                new GeneralRenderer.MenuItem("3", "Редактировать зал"),
                new GeneralRenderer.MenuItem("4", "Удалить зал"),
                new GeneralRenderer.MenuItem("0", "Назад")
            ));
            String choice = input.readChoice(List.of());
            if (choice.equals("0")) return;
            switch (choice) {
                case "1": hallsHandler.handleShow(); break;
                case "2": hallsHandler.handleCreate(); break;
                case "3": hallsHandler.handleEdit(); break;
                case "4": hallsHandler.handleDelete(); break;
                default: generalRenderer.printError("Неверное значение");
            }
        }
    }

    private void runScreeningsMenu() {
        while (true) {
            generalRenderer.printSubHeader("Управление сеансами");
            generalRenderer.printMenu(List.of(
                new GeneralRenderer.MenuItem("1", "Показать все сеансы"),
                new GeneralRenderer.MenuItem("2", "Создать сеанс"),
                new GeneralRenderer.MenuItem("3", "Редактировать сеанс"),
                new GeneralRenderer.MenuItem("4", "Удалить сеанс"),
                new GeneralRenderer.MenuItem("0", "Назад")
            ));
            String choice = input.readChoice(List.of());
            if (choice.equals("0")) return;
            switch (choice) {
                case "1": screeningsHandler.handleShowAll(); break;
                case "2": screeningsHandler.handleCreate(); break;
                case "3": screeningsHandler.handleEdit(); break;
                case "4": screeningsHandler.handleDelete(); break;
                default: generalRenderer.printError("Неверное значение");
            }
        }
    }

    private void runTicketsMenu() {
        while (true) {
            generalRenderer.printSubHeader("Управление билетами");
            generalRenderer.printMenu(List.of(
                new GeneralRenderer.MenuItem("1", "Показать все билеты"),
                new GeneralRenderer.MenuItem("2", "Купить билет"),
                new GeneralRenderer.MenuItem("3", "Использовать билет"),
                new GeneralRenderer.MenuItem("4", "Отменить билет"),
                new GeneralRenderer.MenuItem("0", "Назад")
            ));
            String choice = input.readChoice(List.of());
            if (choice.equals("0")) return;
            switch (choice) {
                case "1": ticketsHandler.handleShowAll(); break;
                case "2": ticketsHandler.handleBuy(); break;
                case "3": ticketsHandler.handleUse(); break;
                case "4": ticketsHandler.handleCancel(); break;
                default: generalRenderer.printError("Неверное значение");
            }
        }
    }

    private void runScheduleMenu() {
        while (true) {
            generalRenderer.printSubHeader("Расписание");
            generalRenderer.printMenu(List.of(
                new GeneralRenderer.MenuItem("1", "За один день"),
                new GeneralRenderer.MenuItem("2", "За период"),
                new GeneralRenderer.MenuItem("0", "Назад")
            ));
            String choice = input.readChoice(List.of());
            if (choice.equals("0")) return;
            switch (choice) {
                case "1": screeningsHandler.handleShowByDate(); break;
                case "2": screeningsHandler.handleShowByPeriod(); break;
                default: generalRenderer.printError("Неверное значение");
            }
        }
    }
}
