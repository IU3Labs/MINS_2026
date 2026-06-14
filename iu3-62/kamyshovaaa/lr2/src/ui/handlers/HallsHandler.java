package ui.handlers;

import exception.RepositoryException;
import exception.ValidationException;
import service.HallService;
import ui.InputHandler;
import ui.renderer.GeneralRenderer;
import ui.renderer.HallRenderer;

import java.util.UUID;

public class HallsHandler {
    private final HallService hallService;
    private final InputHandler input;
    private final GeneralRenderer generalRenderer;
    private final HallRenderer hallRenderer;

    public HallsHandler(HallService hallService,
                        InputHandler input, GeneralRenderer generalRenderer, HallRenderer hallRenderer) {
        this.hallService = hallService;
        this.input = input;
        this.generalRenderer = generalRenderer;
        this.hallRenderer = hallRenderer;
    }

    public void handleShow() {
        hallRenderer.printHalls(hallService.getAll());
    }

    public void handleCreate() {
        String name = input.readLine("Название зала: ");
        int rows = input.readInt("Количество рядов: ");
        int seatsPerRow = input.readInt("Мест в ряду: ");

        var result = hallService.create(name, rows, seatsPerRow);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Зал создан");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleEdit() {
        hallRenderer.printHalls(hallService.getAll());
        UUID id = input.readUuid("ID зала для редактирования: ");

        String name = input.readLine("Название зала: ");
        int rows = input.readInt("Количество рядов: ");
        int seatsPerRow = input.readInt("Мест в ряду: ");

        var result = hallService.update(id, name, rows, seatsPerRow);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Зал обновлён");
        } else {
            generalRenderer.printError(result.getError());
        }
    }

    public void handleDelete() {
        hallRenderer.printHalls(hallService.getAll());
        UUID id = input.readUuid("ID зала для удаления: ");

        var result = hallService.delete(id);
        if (result.isSuccess()) {
            generalRenderer.printSuccess("Зал удалён");
        } else {
            generalRenderer.printError(result.getError());
        }
    }
}
