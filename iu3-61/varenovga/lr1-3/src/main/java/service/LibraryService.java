package main.java.service;

import main.java.exceptions.LibrarySystemException;
import main.java.exceptions.UserNotFoundException;
import main.java.interfaces.DateProvider;
import main.java.interfaces.PublicationServiceInterface;
import main.java.interfaces.UserServiceInterface;
import main.java.interfaces.LoanServiceInterface;
import main.java.models.Publication;
import main.java.models.User;
import main.java.repos.PublicationRepository;
import main.java.repos.UserRepository;
import main.java.service.dto.OverdueInfo;
import main.java.service.dto.ActiveLoanInfo;
import main.java.service.dto.UserLoanHistory;
import main.java.service.fines.FineCalculationStrategy;
import main.java.service.fines.SimpleFineStrategy;
import main.java.service.fines.NotSimpleFineStrategy;
import main.java.service.notifications.NotificationSubject;
import main.java.service.notifications.NotificationObserver;
import main.java.service.notifications.NotificationType;
import main.java.service.commands.Command;
import main.java.service.commands.CommandHistory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LibraryService {
    private final PublicationServiceInterface publicationService;
    private final UserServiceInterface userService;
    private final LoanServiceInterface loanService;
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;
    private final DateProvider dateProvider;

    private FineCalculationStrategy fineStrategy;
    private final NotificationSubject notificationSubject;
    private final CommandHistory commandHistory;

    // ✅ ИСПРАВЛЕНО: Конструктор принимает ровно 6 параметров (как в Application.java)
    public LibraryService(PublicationServiceInterface publicationService,
                          UserServiceInterface userService,
                          LoanServiceInterface loanService,
                          PublicationRepository publicationRepository,
                          UserRepository userRepository,
                          DateProvider dateProvider) {
        this.publicationService = publicationService;
        this.userService = userService;
        this.loanService = loanService;
        this.publicationRepository = publicationRepository;
        this.userRepository = userRepository;
        this.dateProvider = dateProvider;

        this.fineStrategy = new SimpleFineStrategy();
        this.notificationSubject = new NotificationSubject();
        this.commandHistory = new CommandHistory();
    }

    public enum PublicationType {
        BOOK("Книга"), JOURNAL("Журнал");
        private final String displayName;
        PublicationType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    // === ЛР2: Command ===
    public void executeCommand(Command command) { command.execute(); commandHistory.push(command); }
    public void undoLastCommand() { commandHistory.undo(); }
    public void showCommandHistory() { commandHistory.showHistory(); }

    // === ЛР2: Observer ===
    public void addNotificationObserver(NotificationObserver observer) { notificationSubject.attach(observer); }
    public void removeNotificationObserver(NotificationObserver observer) { notificationSubject.detach(observer); }
    public void notifyAboutOverdue(String publicationTitle, String userName) {
        notificationSubject.notifyObservers("Книга '" + publicationTitle + "' просрочена!", NotificationType.OVERDUE);
    }
    public void notifyAboutAvailable(String publicationTitle) {
        notificationSubject.notifyObservers("Книга '" + publicationTitle + "' доступна!", NotificationType.AVAILABLE);
    }
    public int getObserverCount() { return notificationSubject.getObserverCount(); }

    // === ЛР2: Strategy ===
    public void setFineStrategy(FineCalculationStrategy strategy) { this.fineStrategy = strategy; }
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) { return fineStrategy.calculateFine(dueDate, returnDate); }
    public String getCurrentFineStrategy() { return fineStrategy.getDescription(); }

    // === CRUD ===
    public Publication createPublication(PublicationType type, String title, String author, int year, String extraInfo) throws LibrarySystemException {
        Publication pub = publicationService.createPublication(type, title, author, year, extraInfo);
        AuditService.logSuccess("CREATE_PUBLICATION", "SYSTEM", "Создано: " + title);
        return pub;
    }
    public List<Publication> searchByTitle(String query) { return publicationService.searchByTitle(query); }
    public List<Publication> searchByAuthor(String query) { return publicationService.searchByAuthor(query); }
    public List<Publication> getCatalog() { return publicationService.getCatalog(); }
    public void deletePublication(int pubId) throws LibrarySystemException {
        publicationService.deletePublication(pubId);
        AuditService.logSuccess("DELETE_PUBLICATION", "SYSTEM", "ID=" + pubId);
    }

    public User createUser(String name, String email) {
        User user = userService.createUser(name, email);
        AuditService.logSuccess("CREATE_USER", String.valueOf(user.getId()), name);
        return user;
    }
    public User getUser(int userId) throws UserNotFoundException { return userService.getUser(userId); }
    public List<User> getAllUsers() { return userService.getAllUsers(); }
    public void deleteUser(int userId) throws LibrarySystemException {
        if (loanService.hasActiveLoans(userId)) {
            AuditService.logError("DELETE_USER", "SYSTEM", "Попытка удалить пользователя с активными выдачами: ID=" + userId);
            throw new LibrarySystemException("Нельзя удалить пользователя: есть активные выдачи");
        }
        userService.deleteUser(userId);
        AuditService.logSuccess("DELETE_USER", "SYSTEM", "ID=" + userId);
    }

    // === Выдача/Возврат ===
    public void borrowPublication(int pubId, int userId, LocalDate dueDate) throws LibrarySystemException {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            AuditService.logError("BORROW_PUBLICATION", String.valueOf(userId), "Дата возврата в прошлом: " + dueDate);
            throw new LibrarySystemException("❌ Дата возврата не может быть в прошлом.");
        }
        if (dueDate != null) {
            try {
                int maxDays = getMaxLoanDays(userId);
                int requestedDays = (int) ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
                if (requestedDays > maxDays) {
                    String category = getUserCategory(userId);
                    AuditService.logError("BORROW_PUBLICATION", String.valueOf(userId),
                            String.format("Превышен лимит: категория=%s, запрошено=%d дн., лимит=%d дн.", category, requestedDays, maxDays));
                    throw new LibrarySystemException(String.format(" Превышен максимальный срок выдачи!\n   Категория: %s, Лимит: %d дн., Запрошено: %d дн.", category, maxDays, requestedDays));
                }
            } catch (UserNotFoundException e) {
                AuditService.logError("BORROW_PUBLICATION", String.valueOf(userId), "Пользователь не найден: " + userId);
                throw new LibrarySystemException("Пользователь не найден");
            }
        }
        loanService.borrowPublication(pubId, userId, dueDate);
        AuditService.logSuccess("BORROW_PUBLICATION", String.valueOf(userId), "ID=" + pubId);
    }

    public void returnPublication(int pubId) throws LibrarySystemException {
        try {
            loanService.returnPublication(pubId);
            AuditService.logSuccess("RETURN_PUBLICATION", "SYSTEM", "ID=" + pubId);
        } catch (LibrarySystemException e) {
            AuditService.logError("RETURN_PUBLICATION", "SYSTEM", "Ошибка возврата ID=" + pubId + ": " + e.getMessage());
            throw e;
        }
    }

    public void createTestOverdueLoan(int pubId, int userId, int daysOverdue) throws LibrarySystemException {
        LocalDate pastDueDate = dateProvider.now().minusDays(daysOverdue);
        loanService.borrowPublicationForTest(pubId, userId, pastDueDate);
        AuditService.logSuccess("TEST_OVERDUE", String.valueOf(userId), "ID=" + pubId);
    }

    // === Отчёты ===
    public List<OverdueInfo> getOverdueReport() { return loanService.getOverdueReport(); }
    public List<ActiveLoanInfo> getActiveLoans() { return loanService.getActiveLoans(); }
    public List<UserLoanHistory> getUserHistory(int userId) throws UserNotFoundException { return loanService.getUserHistory(userId); }
    public LocalDate getCurrentDate() { return dateProvider.now(); }

    // === ЛР3: LoanPeriodService (Антипаттерны) ===
    public int calculateLoanPeriod(int userId, String publicationType) throws UserNotFoundException {
        User user = getUser(userId);
        String category = LoanPeriodService.getUserCategory(user.getEmail());
        return LoanPeriodService.calculatePeriod(category, publicationType);
    }
    public boolean canExtendLoan(int userId, int currentLoanDays) throws UserNotFoundException {
        User user = getUser(userId);
        String category = LoanPeriodService.getUserCategory(user.getEmail());
        return LoanPeriodService.canExtend(category, currentLoanDays);
    }
    public String getUserCategory(int userId) throws UserNotFoundException {
        User user = getUser(userId);
        return LoanPeriodService.getUserCategory(user.getEmail());
    }
    public int getMaxLoanDays(int userId) throws UserNotFoundException { return calculateLoanPeriod(userId, "Книга"); }
    public String getLoanPeriodRules() { return LoanPeriodService.getRulesInfo(); }
}