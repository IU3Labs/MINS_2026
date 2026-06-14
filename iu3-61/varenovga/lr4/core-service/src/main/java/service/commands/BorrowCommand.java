package service.commands;

import exceptions.LibrarySystemException;
import service.LibraryService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class BorrowCommand implements Command {
    private final LibraryService libraryService;
    private final int publicationId;
    private final int userId;
    private final String dueDateString;
    private boolean executed = false;

    public BorrowCommand(LibraryService libraryService, int publicationId,
                         int userId, String dueDateString) {
        this.libraryService = libraryService;
        this.publicationId = publicationId;
        this.userId = userId;
        this.dueDateString = dueDateString;
    }

    @Override
    public void execute() {
        try {
            LocalDate dueDate = null;
            if (dueDateString != null && !dueDateString.trim().isEmpty()) {
                dueDate = LocalDate.parse(dueDateString,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            }
            libraryService.borrowPublication(publicationId, userId, dueDate);
            executed = true;
        } catch (LibrarySystemException e) {
            System.out.println("❌ ОШИБКА: " + e.getMessage());
        }
    }

    @Override
    public void undo() {
        if (executed) {
            try {
                libraryService.returnPublication(publicationId);
                executed = false;
            } catch (LibrarySystemException e) {
                System.out.println("❌ ОШИБКА при отмене: " + e.getMessage());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Выдача: издание ID=" + publicationId + ", пользователь ID=" + userId;
    }
}