package service;

import library.client.ReferenceGrpcClient;
import exceptions.LibrarySystemException;
import exceptions.UserNotFoundException;
import interfaces.DateProvider;
import interfaces.PublicationServiceInterface;
import interfaces.UserServiceInterface;
import interfaces.LoanServiceInterface;
import models.Publication;
import models.User;
import repos.PublicationRepository;
import repos.UserRepository;
import service.dto.OverdueInfo;
import service.dto.ActiveLoanInfo;
import service.dto.UserLoanHistory;
import service.fines.FineCalculationStrategy;
import service.fines.SimpleFineStrategy;
import service.notifications.NotificationSubject;
import service.notifications.NotificationObserver;
import service.notifications.NotificationType;
import service.commands.Command;
import service.commands.CommandHistory;

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

    private final ReferenceGrpcClient grpcClient;
    // ✅ ИСПРАВЛЕНО: Конструктор принимает ровно 6 параметров (как в Application.java)
    public LibraryService(PublicationServiceInterface publicationService,
                          UserServiceInterface userService,
                          LoanServiceInterface loanService,
                          PublicationRepository publicationRepository,
                          UserRepository userRepository,
                          DateProvider dateProvider,
                          ReferenceGrpcClient grpcClient) {
        this.publicationService = publicationService;
        this.userService = userService;
        this.loanService = loanService;
        this.publicationRepository = publicationRepository;
        this.userRepository = userRepository;
        this.dateProvider = dateProvider;

        this.fineStrategy = new SimpleFineStrategy();
        this.notificationSubject = new NotificationSubject();
        this.commandHistory = new CommandHistory();

        this.grpcClient = new ReferenceGrpcClient();
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
    public void borrowPublication(int pubId, int userId, LocalDate dueDate)
            throws LibrarySystemException {

        //System.out.println("🔍 [DEBUG] Начало выдачи: pubId=" + pubId + ", userId=" + userId);

        // 1. Проверка: если дата указана, она не должна быть в прошлом
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            AuditService.logError("BORROW_PUBLICATION", String.valueOf(userId), "Дата в прошлом: " + dueDate);
            throw new LibrarySystemException("❌ Дата возврата не может быть в прошлом.");
        }

        // 2. 🌐 ВСЕГДА вызываем Reference Service для валидации (даже если дата пустая!)
        try {
            User user = getUser(userId);

            // Если дата указана — считаем запрошенные дни, иначе 0 (нам нужна только категория)
            int requestedDays = (dueDate != null)
                    ? (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate)
                    : 0;

            //System.out.println("📡 [DEBUG] Запрос в Reference: email=" + user.getEmail() + ", days=" + requestedDays);

            // 🔥 КЛЮЧЕВОЙ ВЫЗОВ — происходит ВСЕГДА
            var v = grpcClient.validateBorrow(user.getEmail(), requestedDays);

            //System.out.println("📥 [DEBUG] Ответ Reference: " + v);

            if (!v.isValid()) {
                AuditService.logError("BORROW_PUBLICATION", String.valueOf(userId),
                        "Отказано Reference: " + v.maxDays());
                throw new LibrarySystemException("❌ " + v.maxDays() + " (Лимит для " + v.category() + ")");
            }

            // 3. Если дата НЕ была указана — используем лимит из Reference Service
            if (dueDate == null) {
                dueDate = LocalDate.now().plusDays(v.maxDays());
                //System.out.println("⚙️  [DEBUG] Дата не указана, используем лимит из Reference: " + v.maxDays() + " дней");
            }

        } catch (UserNotFoundException e) {
            //System.out.println("❌ [DEBUG] Пользователь не найден: " + userId);
            throw new LibrarySystemException("Пользователь не найден");
        }

        // 4. Выполняем локальную логику выдачи
        //System.out.println("💾 [DEBUG] Вызов loanService.borrowPublication...");
        loanService.borrowPublication(pubId, userId, dueDate);
        AuditService.logSuccess("BORROW_PUBLICATION", String.valueOf(userId), "ID=" + pubId);
        //System.out.println("✅ [DEBUG] Выдача успешна!");
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
        var v = grpcClient.validateBorrow(user.getEmail(), 0); // days=0 не важен, получаем категорию
        return v.maxDays();
    }

    public boolean canExtendLoan(int userId, int currentLoanDays) throws UserNotFoundException {
        User user = getUser(userId);
        var v = grpcClient.validateBorrow(user.getEmail(), 0);
        return currentLoanDays < v.maxDays();
    }

    public String getUserCategory(int userId) throws UserNotFoundException {
        User user = getUser(userId);
        return grpcClient.validateBorrow(user.getEmail(), 0).category();
    }

    public int getMaxLoanDays(int userId) throws UserNotFoundException {
        return calculateLoanPeriod(userId, "Книга");
    }

    public String getLoanPeriodRules() { return grpcClient.getLoanPeriodRules();
    }

    // В конце класса LibraryService добавь:
    public ReferenceGrpcClient getGrpcClient() {
        return grpcClient;
    }
}