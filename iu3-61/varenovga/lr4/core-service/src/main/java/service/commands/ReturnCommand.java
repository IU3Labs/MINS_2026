package service.commands;

import exceptions.LibrarySystemException;
import service.LibraryService;
public class ReturnCommand implements Command {
    private final LibraryService libraryService;
    private final int publicationId;
    private boolean executed = false;

    public ReturnCommand(LibraryService libraryService, int publicationId) {
        this.libraryService = libraryService;
        this.publicationId = publicationId;
    }

    @Override
    public void execute() {
        try {
            libraryService.returnPublication(publicationId);
            executed = true;
        } catch (LibrarySystemException e) {
            System.out.println("❌ ОШИБКА: " + e.getMessage());
        }
    }

    @Override
    public void undo() {
        System.out.println("⚠️  Отмена возврата невозможна");
    }

    @Override
    public String getDescription() {
        return "Возврат: издание ID=" + publicationId;
    }
}