package ru.bmstu.bank.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.bmstu.bank.application.antipattern.QuickExportGodClass;
import ru.bmstu.bank.application.pattern.command.CommandInvoker;
import ru.bmstu.bank.application.pattern.observer.AuditLogObserver;
import ru.bmstu.bank.application.pattern.observer.EmailNotificationObserver;
import ru.bmstu.bank.application.pattern.observer.Observer;
import ru.bmstu.bank.application.pattern.strategy.*;
import ru.bmstu.bank.application.service.AccountService;
import ru.bmstu.bank.application.service.NotificationService;
import ru.bmstu.bank.application.service.TransactionService;
import ru.bmstu.bank.domain.model.Account;
import ru.bmstu.bank.domain.model.Transaction;
import ru.bmstu.bank.domain.exception.BankingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Component
public class BankConsoleApp implements CommandLineRunner {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final NotificationService notificationService;

    public BankConsoleApp(AccountService accountService,
                          TransactionService transactionService,
                          NotificationService notificationService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("Банковская система запущена.");

        while (running) {
            printMenu();
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createAccount(scanner);
                    case "2" -> deposit(scanner);
                    case "3" -> withdraw(scanner);
                    case "4" -> applyInterest(scanner);
                    case "5" -> printStatement(scanner);
                    case "6" -> listAccounts();
                    case "7" -> undoLastOperation();
                    case "8" -> attachObserver(scanner);
                    case "9" -> changeStrategy(scanner);
                    case "10" -> runAntipatternExport(scanner);
                    case "0" -> { System.out.println("Завершение."); running = false; }
                    default -> System.out.println("Неверный ввод.");
                }
            } catch (BankingException e) { System.err.println(e.getMessage()); }
            catch (NumberFormatException e) { System.err.println("Ошибка формата числа."); }
            catch (Exception e) { System.err.println("Ошибка: " + e.getMessage()); }
        }
    }

    private void printMenu() {
        System.out.println("\nМЕНЮ:");
        System.out.println("1. Создать счет");
        System.out.println("2. Пополнить");
        System.out.println("3. Снять средства");
        System.out.println("4. Начислить проценты (по текущему тарифу)");
        System.out.println("5. Получить выписку");
        System.out.println("6. Список счетов");
        System.out.println("7. Отменить последнюю операцию");
        System.out.println("8. Подключить уведомления");
        System.out.println("9. Сменить тариф/стратегию начисления %");
        System.out.println("10. Быстрый экспорт JSON");
        System.out.println("0. Выход");
    }

    private void createAccount(Scanner sc) {
        System.out.print("Имя владельца: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) throw new IllegalArgumentException("Имя пусто");
        Account acc = accountService.createAccount(name);
        System.out.println("Счет создан. ID: " + acc.getId());
    }

    private Long promptAccountId(Scanner sc) {
        System.out.print("ID счета: ");
        return Long.parseLong(sc.nextLine().trim());
    }

    private BigDecimal promptAmount(Scanner sc) {
        System.out.print("Сумма: ");
        return new BigDecimal(sc.nextLine().trim());
    }

    private void deposit(Scanner sc) {
        transactionService.depositViaCommand(promptAccountId(sc), promptAmount(sc));
        System.out.println("Пополнено");
    }

    private void withdraw(Scanner sc) {
        transactionService.withdraw(promptAccountId(sc), promptAmount(sc));
        System.out.println("Снято");
    }

    private void applyInterest(Scanner sc) {
        Long id = promptAccountId(sc);
        transactionService.applyInterest(id);
        System.out.println("Проценты начислены автоматически по закреплённой стратегии.");
    }

    private void changeStrategy(Scanner sc) {
        Long id = promptAccountId(sc);
        System.out.println("Выберите тариф:");
        System.out.println("1. Фиксированный 5% годовых");
        System.out.println("2. Прогрессивный (до 12% при балансе >100к)");
        System.out.println("3. Промо 15% (на 3 месяца)");

        InterestStrategy strategy = switch (sc.nextLine().trim()) {
            case "1" -> new FixedInterestStrategy(new BigDecimal("0.05"));
            case "2" -> new TieredInterestStrategy()
                    .addTier(BigDecimal.ZERO, new BigDecimal("0.03"))
                    .addTier(new BigDecimal("10000"), new BigDecimal("0.07"))
                    .addTier(new BigDecimal("100000"), new BigDecimal("0.12"));
            case "3" -> new PromotionalInterestStrategy(new BigDecimal("0.15"),
                    java.time.LocalDateTime.now().plusMonths(3), new BigDecimal("0.05"));
            default -> throw new IllegalArgumentException("Неверный выбор");
        };

        accountService.changeInterestStrategy(id, strategy);
        System.out.println("Тариф изменён: " + strategy.getDescription());
    }

    private void printStatement(Scanner sc) {
        Long id = promptAccountId(sc);
        List<Transaction> txs = transactionService.getStatement(id);
        if (txs.isEmpty()) { System.out.println("Пусто."); return; }
        System.out.println("Выписка по счету " + id + ":");
        txs.forEach(t -> {
            BigDecimal displayAmount = t.getAmount();
            if (t.getType().equals("WITHDRAW")) {
                displayAmount = displayAmount.negate();
            }
            System.out.printf("  [%s] %-10s | %+10.2f руб. | %s\n", t.getDate().toLocalDate(), t.getType(), displayAmount, t.getStatus());
        });
    }

    private void listAccounts() {
        accountService.getAllAccounts().forEach(a ->
                System.out.printf("  ID:%d | %s | Баланс:%.2f\n", a.getId(), a.getOwnerName(), a.getBalance()));
    }

    private void undoLastOperation() {
        transactionService.getCommandInvoker().undoLast();
        System.out.println("Последняя операция отменена");
    }

    private void attachObserver(Scanner sc) {
        Long id = promptAccountId(sc);
        System.out.println("Тип: 1.Email 2.AuditLog");
        String type = sc.nextLine().trim();

        Observer observer = switch (type) {
            case "1" -> {
                System.out.print("Email: ");
                yield new EmailNotificationObserver(sc.nextLine().trim());
            }
            case "2" -> new AuditLogObserver();
            default -> throw new IllegalArgumentException("Нет");
        };

        notificationService.attachObserver(id, observer);
    }

    private void runAntipatternExport(Scanner sc) {
        System.out.print("ID счета для экспорта: ");
        Long id = Long.parseLong(sc.nextLine());
        QuickExportGodClass godModule = new QuickExportGodClass(accountService, transactionService);
        String result = godModule.exportAndCalculateFee(id);
        System.out.println("Результат работы антипаттерн-модуля: " + result);
    }
}