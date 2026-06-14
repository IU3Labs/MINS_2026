package com.bank.client;

import com.bank.contract.*;
import com.bank.core.trace.TraceIdClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleClientApplication {
    private final Scanner scanner = new Scanner(System.in);

    private final ManagedChannel channel = NettyChannelBuilder.forAddress("127.0.0.1", 6565)
            .usePlaintext()
            .build();

    private boolean running = true;

    public static void main(String[] args) {
        new ConsoleClientApplication().start();
    }

    public void start() {
        System.out.println("=== Bank System LR4 gRPC Client ===");
        while (running) {
            showMenu();
            int choice = getValidatedInt("Выберите пункт меню: ");
            handleChoice(choice);
        }
        channel.shutdown();
        System.out.println("Клиент завершён.");
    }

    private void showMenu() {
        System.out.println(" --- ГЛАВНОЕ МЕНЮ ---");
        System.out.println("1. Открыть новый счет");
        System.out.println("2. Пополнить счет");
        System.out.println("3. Снять средства");
        System.out.println("4. Начислить проценты");
        System.out.println("5. Показать выписку по счету");
        System.out.println("6. Показать все счета");
        System.out.println("0. Выход");
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> accrueInterest();
                case 5 -> getStatement();
                case 6 -> getAllAccounts();
                case 0 -> running = false;
                default -> System.out.println("Неверный пункт меню");
            }
        } catch (StatusRuntimeException e) {
            printGrpcError(e);
        } catch (Exception e) {
            System.out.println("Ошибка клиента: " + e.getMessage());
        }
    }

    private CoreBankServiceGrpc.CoreBankServiceBlockingStub stubWithTrace() {
        String traceId = UUID.randomUUID().toString();
        return CoreBankServiceGrpc.newBlockingStub(channel)
                .withInterceptors(new TraceIdClientInterceptor(traceId));
    }

    private void createAccount() {
        System.out.print("Введите ФИО владельца: ");
        String ownerName = scanner.nextLine();
        System.out.print("Введите тип счета (STANDARD / PREMIUM / SAVINGS): ");
        String accountType = scanner.nextLine();

        OperationResponse response = stubWithTrace().createAccount(CreateAccountRequest.newBuilder()
                .setOwnerName(ownerName)
                .setAccountType(accountType)
                .build());
        printOperationResponse(response);
    }

    private void deposit() {
        long accountId = getValidatedLong("Введите ID счета: ");
        double amount = getValidatedDouble("Введите сумму пополнения: ");

        OperationResponse response = stubWithTrace().deposit(DepositRequest.newBuilder()
                .setAccountId(accountId)
                .setAmount(amount)
                .build());
        printOperationResponse(response);
    }

    private void withdraw() {
        long accountId = getValidatedLong("Введите ID счета: ");
        double amount = getValidatedDouble("Введите сумму снятия: ");

        OperationResponse response = stubWithTrace().withdraw(WithdrawRequest.newBuilder()
                .setAccountId(accountId)
                .setAmount(amount)
                .build());
        printOperationResponse(response);
    }

    private void accrueInterest() {
        long accountId = getValidatedLong("Введите ID счета: ");
        double basePercent = getValidatedDouble("Введите базовую процентную ставку: ");

        OperationResponse response = stubWithTrace().accrueInterest(AccrueInterestRequest.newBuilder()
                .setAccountId(accountId)
                .setBasePercent(basePercent)
                .build());
        printOperationResponse(response);
    }

    private void getStatement() {
        long accountId = getValidatedLong("Введите ID счета: ");
        GetAccountStatementResponse response = stubWithTrace().getAccountStatement(AccountByIdRequest.newBuilder()
                .setAccountId(accountId)
                .build());

        printAccount(response.getAccount());
    }

    private void getAllAccounts() {
        GetAllAccountsResponse response = stubWithTrace().getAllAccounts(EmptyRequest.newBuilder().build());
        if (response.getAccountsCount() == 0) {
            System.out.println("Счетов пока нет.");
            return;
        }
        for (AccountDto account : response.getAccountsList()) {
            System.out.printf("ID: %d | Владелец: %s | Тип: %s | Баланс: %.2f руб.%n",
                    account.getId(), account.getOwnerName(), account.getAccountType(), account.getBalance());
        }
    }

    private void printOperationResponse(OperationResponse response) {
        System.out.println(response.getMessage());
        if (response.getSuccess() && response.hasAccount()) {
            System.out.printf("ID: %d | Владелец: %s | Тип: %s | Баланс: %.2f руб.%n",
                    response.getAccount().getId(),
                    response.getAccount().getOwnerName(),
                    response.getAccount().getAccountType(),
                    response.getAccount().getBalance());
        }
    }

    private void printAccount(AccountDto account) {
        System.out.println("=== Выписка по счету " + account.getId() + " (" + account.getOwnerName() + ") ===");
        System.out.println("Тип счета: " + account.getAccountType());
        System.out.println("Текущий баланс: " + account.getBalance());
        System.out.println("История операций:");
        if (account.getTransactionsCount() == 0) {
            System.out.println("  Операций пока нет.");
        } else {
            for (TransactionDto transaction : account.getTransactionsList()) {
                System.out.printf("  [%s] %s: %.2f руб.%n",
                        transaction.getCreatedAt(), transaction.getType(), transaction.getAmount());
            }
        }
    }

    private void printGrpcError(StatusRuntimeException e) {
        Status status = e.getStatus();
        String description = status.getDescription() == null ? "Без подробного описания" : status.getDescription();

        switch (status.getCode()) {
            case INVALID_ARGUMENT -> System.out.println("INVALID_ARGUMENT: " + description);
            case NOT_FOUND -> System.out.println("NOT_FOUND: " + description);
            case FAILED_PRECONDITION -> System.out.println("FAILED_PRECONDITION: " + description);
            case UNAVAILABLE -> System.out.println("UNAVAILABLE: " + description);
            case DEADLINE_EXCEEDED -> System.out.println("DEADLINE_EXCEEDED: " + description);
            default -> System.out.println(status.getCode() + ": " + description);
        }
    }

    private int getValidatedInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Введите целое число.");
                scanner.nextLine();
            }
        }
    }

    private long getValidatedLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                long value = scanner.nextLong();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Введите целое число.");
                scanner.nextLine();
            }
        }
    }

    private double getValidatedDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Введите число.");
                scanner.nextLine();
            }
        }
    }
}
