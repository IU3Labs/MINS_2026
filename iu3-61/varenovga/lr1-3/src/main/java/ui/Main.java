package main.java.ui;

import main.java.exceptions.LibrarySystemException;
import main.java.service.LibraryService;
import main.java.service.IdGenerator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Application.ServiceBundle bundle = Application.createDefaultServices();
        LibraryService library = bundle.libraryService;
        IdGenerator pubIdGen = bundle.publicationIdGenerator;
        IdGenerator userIdGen = bundle.userIdGenerator;

        DataSeeder.seedData(library);

        Scanner scanner = new Scanner(System.in);
        MenuRenderer menuRenderer = new MenuRenderer();
        UserInputHandler inputHandler = new UserInputHandler();

        boolean running = true;
        inputHandler.log("=== БИБЛИОТЕЧНАЯ СИСТЕМА ===");

        while (running) {
            menuRenderer.printMenu();
            String choice = inputHandler.readChoice(scanner);

            try {
                MenuAction action = MenuAction.fromCode(choice)
                        .orElseThrow(() -> new IllegalArgumentException("Неверный выбор"));

                switch (action) {
                    case SHOW_CATALOG -> MenuHandler.printCatalog(library, inputHandler);
                    case SEARCH_BY_TITLE -> MenuHandler.searchByTitle(library, scanner, inputHandler);
                    case SEARCH_BY_AUTHOR -> MenuHandler.searchByAuthor(library, scanner, inputHandler);
                    case BORROW_PUBLICATION -> MenuHandler.borrowPublication(library, scanner, inputHandler);
                    case RETURN_PUBLICATION -> MenuHandler.returnPublication(library, scanner, inputHandler);
                    case OVERDUE_REPORT -> MenuHandler.printOverdueReport(library, inputHandler);
                    case ADD_PUBLICATION -> MenuHandler.addPublication(library, scanner, inputHandler);
                    case ADD_USER -> MenuHandler.addUser(library, scanner, inputHandler);
                    case SHOW_USERS -> MenuHandler.printUsers(library, inputHandler);
                    case USER_HISTORY -> MenuHandler.printUserHistory(library, scanner, inputHandler);
                    case ACTIVE_LOANS -> MenuHandler.printActiveLoans(library, inputHandler);
                    case TEST_OVERDUE -> MenuHandler.testOverdue(library, scanner, inputHandler);
                    case DELETE_PUBLICATION -> MenuHandler.deletePublication(library, scanner, inputHandler);
                    case DELETE_USER -> MenuHandler.deleteUser(library, scanner, inputHandler);
                    case SYSTEM_INFO -> MenuHandler.printSystemInfo(library, pubIdGen, userIdGen, inputHandler);
                    case CHANGE_FINE_STRATEGY -> MenuHandler.changeFineStrategy(library, scanner, inputHandler);
                    case SHOW_FINE_CALCULATION -> MenuHandler.showFineCalculation(library, scanner, inputHandler);
                    case ADD_EMAIL_OBSERVER -> MenuHandler.addEmailObserver(library, scanner, inputHandler);
                    case SEND_OVERDUE_NOTIFY -> MenuHandler.sendOverdueNotifications(library, scanner, inputHandler);
                    case CALCULATE_LOAN_PERIOD -> MenuHandler.calculateLoanPeriod(library, scanner, inputHandler);
                    case CHECK_LOAN_EXTENSION -> MenuHandler.checkLoanExtension(library, scanner, inputHandler);
                    case SHOW_USER_CATEGORY -> MenuHandler.showUserCategory(library, scanner, inputHandler);
                    case SHOW_HISTORY -> MenuHandler.showHistory(library, inputHandler);
                    case EXIT -> {
                        running = false;
                        inputHandler.log("Выход из системы. До свидания!");
                        inputHandler.log("📊 Observer сработал раз: " +
                                main.java.service.notifications.EmailNotificationObserver.getNotificationCounter());
                    }
                }
            } catch (IllegalArgumentException e) {
                inputHandler.error("Неверный выбор. Попробуйте снова.");
            } catch (LibrarySystemException e) {
                inputHandler.error(e.getMessage());
            } catch (Exception e) {
                inputHandler.error("Неожиданная ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }
}