package ru.bmstu.service_a;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import ru.bmstu.grpc.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

public class CoreServiceApp {
    private static final Scanner sc = new Scanner(System.in);
    private static final Logger log = Logger.getLogger("CoreService");
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;
    private final Bank bank;

    public CoreServiceApp(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        stub = ReferenceServiceGrpc.newBlockingStub(channel);
        bank = new Bank();
    }

    public void shutdown() { channel.shutdownNow(); }

    public static void main(String[] args) {
        CoreServiceApp app = new CoreServiceApp("localhost", 50051);
        System.out.println("Service A запущен. Банк готов к работе.");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Открытие вклада");
            System.out.println("2. Закрытие вклада");
            System.out.println("3. Отчёт по вкладам");
            System.out.println("4. Выход");
            System.out.print("Выбор: ");
            String cmd = sc.nextLine().trim();

            try {
                switch (cmd) {
                    case "1" -> app.handleOpenDeposit();
                    case "2" -> app.handleCloseDeposit();
                    case "3" -> System.out.println(app.bank.getReport());
                    case "4" -> running = false;
                    default -> System.out.println("Неверная команда.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
        app.shutdown();
        System.out.println("Работа завершена.");
    }

    private void handleOpenDeposit() {
        System.out.print("Номер счета: ");
        String accountNumber = sc.nextLine().trim();
        String traceId = UUID.randomUUID().toString();
        log.info("[" + traceId + "] Запрос на открытие вклада для счета: " + accountNumber);

        try {
            ValidationRequest req = ValidationRequest.newBuilder()
                    .setTraceId(traceId)
                    .setAccountNumber(accountNumber)
                    .build();
            ValidationResponse resp = stub.getAccountInfo(req);
            log.info("[" + traceId + "] Ответ от Service B: " + resp.getMessage());

            if (!resp.getIsAllowed()) {
                System.out.println("Открытие вклада запрещено: " + resp.getMessage());
                return;
            }
            bank.setInterestRate(resp.getInterestRate());

        } catch (StatusRuntimeException e) {
            Status.Code code = e.getStatus().getCode();
            String desc = e.getStatus().getDescription();

            log.warning("[" + traceId + "] gRPC error: " + code + " - " + desc);

            switch (code) {
                case INVALID_ARGUMENT:
                case NOT_FOUND:
                case INTERNAL:
                    System.err.println("Открытие вклада отклонено: " + desc);
                    return;

                case UNAVAILABLE:
                    System.out.println("Справочный сервис недоступен. Открытие вклада по дефолтной ставке.");
                    break;

                default:
                    System.err.println("Неизвестная ошибка: " + desc);
                    return;
            }
        }

        bank.openDeposit(accountNumber);
    }

    private void handleCloseDeposit() {
        System.out.print("ID вклада для закрытия: ");
        int depositId = Integer.parseInt(sc.nextLine().trim());
        bank.closeDeposit(depositId);
    }

    static class Bank {
        private final Map<Integer, Deposit> active = new HashMap<>();
        private final List<Deposit> history = new ArrayList<>();
        private double interestRate = 5.0;

        Bank() {}

        void setInterestRate(double rate) { this.interestRate = rate; }

        void openDeposit(String accountNumber) {
            int depositId = 1;
            while (active.containsKey(depositId)) depositId++;

            Deposit d = new Deposit(accountNumber, depositId, LocalDateTime.now());
            active.put(depositId, d);
            System.out.println("Вклад успешно открыт. ID вклада: #" + depositId);
        }

        void closeDeposit(int depositId) {
            Deposit d = active.remove(depositId);
            if (d == null) {
                System.out.println("Вклад #" + depositId + " не найден или уже закрыт.");
                return;
            }
            d.closeTime = LocalDateTime.now();
            history.add(d);

            long seconds = Duration.between(d.openTime, d.closeTime).getSeconds();

            double principal = 10000.0;
            double secondsInYear = 365.0 * 24 * 60 * 60;
            double interest = principal * (interestRate / 100.0) * (seconds / secondsInYear);

            System.out.printf("Вклад закрыт. Начисленные проценты: %.2f руб. (время: %d сек, ставка: %.2f%%)\n",
                    interest, seconds, interestRate);
        }

        String getReport() {
            int activeCount = active.size();
            StringBuilder sb = new StringBuilder();
            sb.append("=== ОТЧЁТ ПО ВКЛАДАМ ===\n");
            if (!active.isEmpty()) {
                sb.append("Активные вклады:\n");
                active.forEach((id, d) -> sb.append(String.format("  #%d: Счет %s (открыт %s)\n", id, d.accountNumber, d.openTime)));
            } else {
                sb.append("Нет активных вкладов.\n");
            }
            return sb.toString();
        }

        static class Deposit {
            String accountNumber;
            int depositId;
            LocalDateTime openTime;
            LocalDateTime closeTime;

            Deposit(String acc, int id, LocalDateTime open) {
                accountNumber = acc;
                depositId = id;
                openTime = open;
            }
        }
    }
}