package main.java.ui;

import main.java.exceptions.LibrarySystemException;
import main.java.exceptions.UserNotFoundException;
import main.java.service.LibraryService;
import main.java.service.IdGenerator;
import main.java.service.dto.OverdueInfo;
import main.java.service.dto.ActiveLoanInfo;
import main.java.service.dto.UserLoanHistory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import main.java.models.User;

public class MenuHandler {
    public static void printCatalog(LibraryService library, UserInputHandler ui) {
        ui.log("\n=== КАТАЛОГ ===");
        library.getCatalog().forEach(p -> {
            String status = p.isAvailable() ? "[Доступно] " : "[Выдано] ";
            ui.log(status + " [" + p.getType() + "]  " + p);
        });
        if (library.getCatalog().isEmpty()) {
            ui.log("Каталог пуст.");
        }
        ui.log("===============");
    }

    public static void searchByTitle(LibraryService library, Scanner scanner, UserInputHandler ui) {
        String query = ui.readLine(scanner, "Название: ");
        library.searchByTitle(query).forEach(p ->
                ui.log("[" + p.getType() + "]  " + p));
    }

    public static void searchByAuthor(LibraryService library, Scanner scanner, UserInputHandler ui) {
        String query = ui.readLine(scanner, "Автор: ");
        library.searchByAuthor(query).forEach(p ->
                ui.log("[" + p.getType() + "]  " + p));
    }

    public static void borrowPublication(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws LibrarySystemException {
        int pubId = ui.readInt(scanner, "ID издания: ");
        int userId = ui.readInt(scanner, "ID пользователя: ");
        ui.log("Дата возврата (дд.мм.гггг), пусто = 14 дней: ");
        String dueDateStr = scanner.nextLine();
        var command = new main.java.service.commands.BorrowCommand(library, pubId, userId, dueDateStr);
        library.executeCommand(command);
    }

    public static void returnPublication(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws LibrarySystemException {
        int pubId = ui.readInt(scanner, "ID издания: ");
        var command = new main.java.service.commands.ReturnCommand(library, pubId);
        library.executeCommand(command);
    }

    public static void printOverdueReport(LibraryService library, UserInputHandler ui) {
        ui.log("\n=== ОТЧЁТ ПО ЗАДОЛЖЕННОСТЯМ ===");
        ui.log("Дата: " + library.getCurrentDate());
        var report = library.getOverdueReport();
        if (report.isEmpty()) {
            ui.log("✅ Задолженностей нет");
        } else {
            for (OverdueInfo item : report) {
                ui.log(String.format("❗ %s (%s) — '%s', просрочка: %d дн.",
                        item.userName(), item.userEmail(), item.publicationTitle(), item.daysOverdue()));
            }
        }
        ui.log("=================================");
    }

    public static void addPublication(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws LibrarySystemException {
        ui.log("\nТип издания: ");
        ui.log("1. Книга");
        ui.log("2. Журнал");
        int typeChoice = ui.readInt(scanner, "Выберите (1-2): ");
        LibraryService.PublicationType type;
        String extraInfoLabel;
        if (typeChoice == 1) {
            type = LibraryService.PublicationType.BOOK;
            extraInfoLabel = "ISBN";
        } else if (typeChoice == 2) {
            type = LibraryService.PublicationType.JOURNAL;
            extraInfoLabel = "Номер выпуска";
        } else {
            ui.error("Неверный тип издания");
            return;
        }
        String title = ui.readLine(scanner, "Название: ");
        String author = ui.readLine(scanner, "Автор/Редакция: ");
        int year = ui.readInt(scanner, "Год: ");
        String extraInfo = ui.readLine(scanner, extraInfoLabel + ": ");
        var pub = library.createPublication(type, title, author, year, extraInfo);
        ui.log("✅ Добавлено: [ID=" + pub.getId() + "] [" + pub.getType() + "]  " + pub);
    }

    public static void addUser(LibraryService library, Scanner scanner, UserInputHandler ui) {
        String name = ui.readLine(scanner, "Имя: ");
        String email = ui.readLine(scanner, "Email: ");
        var user = library.createUser(name, email);
        ui.log("✅ Пользователь создан: " + user);
    }

    public static void printUsers(LibraryService library, UserInputHandler ui) {
        ui.log("\n=== ПОЛЬЗОВАТЕЛИ ===");
        library.getAllUsers().forEach(u -> ui.log(u.toString()));
        if (library.getAllUsers().isEmpty()) {
            ui.log("Нет пользователей");
        }
        ui.log("====================");
    }

    public static void printUserHistory(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws UserNotFoundException {
        int userId = ui.readInt(scanner, "ID пользователя: ");
        ui.log("\n=== ИСТОРИЯ: " + library.getUser(userId).getName() + " ===");
        var history = library.getUserHistory(userId);
        if (history.isEmpty()) {
            ui.log("Активных выдач нет");
        } else {
            for (UserLoanHistory h : history) {
                String overdue = h.isOverdue() ? " ❗ПРОСРОЧЕНО" : " ";
                ui.log(String.format("- '%s' | до: %s%s", h.publicationTitle(), h.dueDate(), overdue));
            }
        }
        ui.log("==============================");
    }

    public static void printActiveLoans(LibraryService library, UserInputHandler ui) {
        ui.log("\n=== АКТИВНЫЕ ВЫДАЧИ ===");
        var loans = library.getActiveLoans();
        if (loans.isEmpty()) {
            ui.log("Нет активных выдач");
        } else {
            for (ActiveLoanInfo loan : loans) {
                String status = loan.isOverdue() ? "❗ПРОСРОЧЕНО" : "Активно";
                ui.log(String.format("[%s] '%s' → %s | до: %s",
                        status, loan.publicationTitle(), loan.userName(), loan.dueDate()));
            }
        }
        ui.log("=======================");
    }

    public static void testOverdue(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws LibrarySystemException {
        int pubId = ui.readInt(scanner, "ID издания: ");
        int userId = ui.readInt(scanner, "ID пользователя: ");
        library.createTestOverdueLoan(pubId, userId, 30);
        ui.log("✅ Тестовая выдача создана: ");
        ui.log("   📅 Дата выдачи: " + library.getCurrentDate());
        ui.log("   📅 Дата возврата: " + library.getCurrentDate().minusDays(30));
        ui.log("   ⚠️  Просрочка: 30 дней");
    }

    public static void deletePublication(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws LibrarySystemException {
        int id = ui.readInt(scanner, "ID издания: ");
        library.deletePublication(id);
        ui.log("✅ Издание удалено");
    }

    public static void deleteUser(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws LibrarySystemException {
        int id = ui.readInt(scanner, "ID пользователя: ");
        library.deleteUser(id);
        ui.log("✅ Пользователь удалён");
    }

    public static void printSystemInfo(LibraryService library, IdGenerator pubGen,
                                       IdGenerator userGen, UserInputHandler ui) {
        ui.log("\n=== СИСТЕМА ===");
        ui.log("Издания: " + library.getCatalog().size());
        ui.log("Пользователи: " + library.getAllUsers().size());
        ui.log("Свободные ID изданий: " + pubGen.getAvailableCount());
        ui.log("Свободные ID пользователей: " + userGen.getAvailableCount());
        ui.log("==============");
    }

    public static void changeFineStrategy(LibraryService library, Scanner scanner, UserInputHandler ui) {
        ui.log("\nВыберите стратегию: ");
        ui.log("1. Простая (10 руб/день)");
        ui.log("2. Непростая (100 руб/день)");
        int choice = ui.readInt(scanner, "Ваш выбор: ");
        if (choice == 1) {
            library.setFineStrategy(new main.java.service.fines.SimpleFineStrategy());
            ui.log("✅ Стратегия изменена");
        }
        if (choice == 2) {
            library.setFineStrategy(new main.java.service.fines.NotSimpleFineStrategy());
            ui.log("✅ Стратегия изменена");
        }
    }

    public static void showFineCalculation(LibraryService library, Scanner scanner, UserInputHandler ui) {
        ui.log("\n=== РАСЧЁТ ШТРАФА ПО ПРОСРОЧКАМ ===");
        ui.log("Текущая стратегия: " + library.getCurrentFineStrategy());
        var overdueReport = library.getOverdueReport();
        if (overdueReport.isEmpty()) {
            ui.log("✅ Нет активных просрочек для расчёта штрафа");
            return;
        }
        ui.log("\n📋 АКТИВНЫЕ ПРОСРОЧКИ: ");
        for (int i = 0; i < overdueReport.size(); i++) {
            var item = overdueReport.get(i);
            ui.log(String.format("  %d. '%s' — %s (%s), просрочка: %d дн.",
                    i + 1, item.publicationTitle(), item.userName(), item.userEmail(), item.daysOverdue()));
        }
        int choice = ui.readInt(scanner, "Выберите просрочку (1-" + overdueReport.size() + "): ");
        if (choice < 1 || choice > overdueReport.size()) {
            ui.error("Неверный выбор");
            return;
        }
        var selected = overdueReport.get(choice - 1);
        LocalDate returnDate = library.getCurrentDate();
        double fine = library.calculateFine(selected.dueDate(), returnDate);
        ui.log("\n💰 РЕЗУЛЬТАТ РАСЧЁТА: ");
        ui.log("   Издание: " + selected.publicationTitle());
        ui.log("   Пользователь: " + selected.userName() + " (" + selected.userEmail() + ")");
        ui.log("   Дата возврата: " + selected.dueDate());
        ui.log("   Дата фактического возврата: " + returnDate);
        ui.log("   Дней просрочки: " + selected.daysOverdue());
        ui.log("   💵 Штраф: " + fine + " руб.");
    }

    public static void addEmailObserver(LibraryService library, Scanner scanner, UserInputHandler ui) {
        ui.log("\n=== ДОБАВИТЬ EMAIL-ПОДПИСЧИКА ===");
        var users = library.getAllUsers();
        if (users.isEmpty()) {
            ui.log("❌ Нет пользователей в системе");
            return;
        }
        ui.log("📋 ПОЛЬЗОВАТЕЛИ: ");
        for (var user : users) {
            ui.log("   ID=" + user.getId() + " — " + user.getName() + " (" + user.getEmail() + ")");
        }
        int userId = ui.readInt(scanner, "Выберите ID пользователя для уведомлений: ");
        try {
            var user = library.getUser(userId);
            var observer = new main.java.service.notifications.EmailNotificationObserver(user.getName(), user.getEmail());
            library.addNotificationObserver(observer);
            ui.log("✅ Email-подписчик добавлен: " + user.getName() + " (" + user.getEmail() + ")");
        } catch (UserNotFoundException e) {
            ui.error("Пользователь с ID=" + userId + " не найден");
        }
    }

    public static void sendOverdueNotifications(LibraryService library, Scanner scanner, UserInputHandler ui)
            throws UserNotFoundException {
        ui.log("\n🔔 ОТПРАВКА УВЕДОМЛЕНИЙ ПО ПРОСРОЧКАМ");
        var overdueReport = library.getOverdueReport();
        if (overdueReport.isEmpty()) {
            ui.log("✅ Нет пользователей с просрочками");
            return;
        }
        var userOverdues = new HashMap<String, ArrayList<OverdueInfo>>();
        for (var item : overdueReport) {
            userOverdues.computeIfAbsent(item.userName(), k -> new ArrayList<>()).add(item);
        }
        ui.log("\n📋 ПОЛЬЗОВАТЕЛИ С ПРОСРОЧКАМИ: ");
        int index = 1;
        var userList = new ArrayList<String>();
        for (var entry : userOverdues.entrySet()) {
            String userName = entry.getKey();
            int count = entry.getValue().size();
            ui.log(String.format("  %d. %s — %d просроченных книг", index++, userName, count));
            userList.add(userName);
        }
        int choice = ui.readInt(scanner, "Выберите пользователя (1-" + userList.size() + "): ");
        if (choice < 1 || choice > userList.size()) {
            ui.error("Неверный выбор");
            return;
        }
        String selectedUser = userList.get(choice - 1);
        int userId = -1;
        for (var user : library.getAllUsers()) {
            if (user.getName().equals(selectedUser)) {
                userId = user.getId();
                break;
            }
        }
        if (userId == -1) {
            ui.error("Пользователь не найден");
            return;
        }
        var user = library.getUser(userId);
        var userOverdueList = userOverdues.get(selectedUser);
        var observer = new main.java.service.notifications.EmailNotificationObserver(
                user.getName(),
                user.getEmail(),
                userOverdueList,
                library.getCurrentDate()
        );
        ui.log("\n📧 ОТПРАВКА УВЕДОМЛЕНИЯ: ");
        observer.sendOverdueNotification();
    }

    public static void showHistory(LibraryService library, UserInputHandler ui) {
        library.showCommandHistory();
    }

    public static void calculateLoanPeriod(LibraryService library, Scanner scanner, UserInputHandler ui) {
        ui.log("\n=== РАСЧЁТ СРОКА ВЫДАЧИ ===");
        int userId = ui.readInt(scanner, "ID пользователя: ");
        ui.log("Тип издания: ");
        ui.log("1. Обычная книга");
        ui.log("2. Редкая книга");
        int typeChoice = ui.readInt(scanner, "Выберите (1-2): ");
        String pubType = (typeChoice == 1) ? "Книга" : "Редкая книга";
        try {
            int days = library.calculateLoanPeriod(userId, pubType);
            User user = library.getUser(userId);
            String category = library.getUserCategory(userId);
            ui.log("\n📅 РЕЗУЛЬТАТ: ");
            ui.log("   Пользователь: " + user.getName());
            ui.log("   Категория: " + category);
            ui.log("   Тип издания: " + pubType);
            ui.log("   ✅ Срок выдачи: " + days + " дней");
            ui.log("   ⚠️  Правила могут измениться без уведомления");
        } catch (UserNotFoundException e) {
            ui.error("Пользователь не найден");
        }
    }

    public static void checkLoanExtension(LibraryService library, Scanner scanner, UserInputHandler ui) {
        ui.log("\n=== ПРОВЕРКА ВОЗМОЖНОСТИ ПРОДЛЕНИЯ ===");
        int userId = ui.readInt(scanner, "ID пользователя: ");
        int currentDays = ui.readInt(scanner, "Текущий срок выдачи (дней): ");
        try {
            boolean canExtend = library.canExtendLoan(userId, currentDays);
            User user = library.getUser(userId);
            String category = library.getUserCategory(userId);
            ui.log("\n📋 ИНФОРМАЦИЯ: ");
            ui.log("   Пользователь: " + user.getName());
            ui.log("   Категория: " + category);
            ui.log("   Текущий срок: " + currentDays + " дней");
            if (canExtend) {
                ui.log("   ✅ Продление ВОЗМОЖНО");
            } else {
                ui.log("   ❌ Продление НЕВОЗМОЖНО (достигнут лимит)");
            }
            ui.log("\n" + library.getLoanPeriodRules());
        } catch (UserNotFoundException e) {
            ui.error("Пользователь не найден");
        }
    }

    public static void showUserCategory(LibraryService library, Scanner scanner, UserInputHandler ui) {
        ui.log("\n=== КАТЕГОРИЯ ПОЛЬЗОВАТЕЛЯ ===");
        int userId = ui.readInt(scanner, "ID пользователя: ");
        try {
            User user = library.getUser(userId);
            String category = library.getUserCategory(userId);
            ui.log("\n📋 ИНФОРМАЦИЯ: ");
            ui.log("   ID: " + userId);
            ui.log("   Имя: " + user.getName());
            ui.log("   Email: " + user.getEmail());
            ui.log("   🎭 Категория: " + category);
            ui.log("\n" + library.getLoanPeriodRules());
        } catch (UserNotFoundException e) {
            ui.error("Пользователь не найден");
        }
    }
}