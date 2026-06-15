package org.parking.service_a;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.parking.grpc.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class CoreServiceApp {
    private static final Scanner sc = new Scanner(System.in);
    private static final Logger log = Logger.getLogger("CoreService");
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;
    private final ParkingLot parkingLot;

    public CoreServiceApp(String host, int port, int capacity) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        stub = ReferenceServiceGrpc.newBlockingStub(channel);
        parkingLot = new ParkingLot(capacity);
    }

    public void shutdown() { channel.shutdownNow(); }

    public static void main(String[] args) {
        System.out.print("Введите количество машиномест: ");
        int capacity = Integer.parseInt(sc.nextLine().trim());

        CoreServiceApp app = new CoreServiceApp("localhost", 50051, capacity);
        System.out.println("Service A (Core) запущен. Парковка готова.");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Въезд");
            System.out.println("2. Выезд");
            System.out.println("3. Отчёт по загрузке");
            System.out.println("4. Выход");
            System.out.print("Выбор: ");
            String cmd = sc.nextLine().trim();

            try {
                switch (cmd) {
                    case "1" -> app.handleEnter();
                    case "2" -> app.handleExit();
                    case "3" -> System.out.println(app.parkingLot.getReport());
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

    private void handleEnter() {
        System.out.print("Гос. номер: ");
        String plate = sc.nextLine().trim();
        String traceId = UUID.randomUUID().toString();
        log.info("[" + traceId + "] Запрос на въезд для: " + plate);

        try {
            ValidationRequest req = ValidationRequest.newBuilder()
                    .setTraceId(traceId)
                    .setLicensePlate(plate)
                    .build();
            ValidationResponse resp = stub.getParkingRules(req);
            log.info("[" + traceId + "] Ответ от Service B: " + resp.getMessage());

            if (!resp.getIsAllowed()) {
                System.out.println("Въезд запрещён: " + resp.getMessage());
                return;
            }
            parkingLot.setBaseRate(resp.getHourlyRate());

        } catch (StatusRuntimeException e) {
            Status.Code code = e.getStatus().getCode();
            String desc = e.getStatus().getDescription();

            log.warning("[" + traceId + "] gRPC error: " + code + " - " + desc);

            //пустой номер, номер в чс, ошибка на сервере (...), сервис б вырублен
            switch (code) {
                case INVALID_ARGUMENT:
                case NOT_FOUND:
                case INTERNAL:
                    System.err.println("Въезд отклонён: " + desc);
                    return;

                case UNAVAILABLE:
                    System.out.println("Справочный сервис недоступен. Въезд разрешён по дефолтному тарифу.");
                    break;

                default:
                    System.err.println("Неизвестная ошибка: " + desc);
                    return;
            }
        }

        parkingLot.enter(plate);
    }

    private void handleExit() {
        System.out.print("Номер места для выезда: ");
        int spotId = Integer.parseInt(sc.nextLine().trim());
        parkingLot.exit(spotId);
    }

    static class ParkingLot {
        private final int capacity;
        private final Map<Integer, Session> active = new HashMap<>();
        private final List<Session> history = new ArrayList<>();
        private double baseRate = 1.0;

        ParkingLot(int capacity) { this.capacity = capacity; }

        void setBaseRate(double rate) { this.baseRate = rate; }

        void enter(String plate) {
            if (active.size() >= capacity) {
                System.out.println("Нет свободных мест.");
                return;
            }
            int spotId = 1;
            while (active.containsKey(spotId)) spotId++;

            Session s = new Session(plate, spotId, LocalDateTime.now());
            active.put(spotId, s);
            System.out.println("Въезд разрешён. Место #" + spotId);
        }

        void exit(int spotId) {
            Session s = active.remove(spotId);
            if (s == null) {
                System.out.println("Место #" + spotId + " не занято.");
                return;
            }
            s.exitTime = LocalDateTime.now();
            history.add(s);

            long seconds = Duration.between(s.entryTime, s.exitTime).getSeconds();
            double cost = seconds * baseRate;

            System.out.printf("Выезд разрешён. Стоимость: %.2f руб. (время: %d сек)\n", cost, seconds);
        }

        String getReport() {
            int occupied = active.size();
            double percent = capacity == 0 ? 0 : (occupied * 100.0 / capacity);
            StringBuilder sb = new StringBuilder();
            sb.append("=== ОТЧЁТ ПО ЗАГРУЗКЕ ===\n");
            sb.append(String.format("Всего мест: %d | Занято: %d | Загрузка: %.1f%%\n", capacity, occupied, percent));
            if (!active.isEmpty()) {
                sb.append("Занятые места:\n");
                active.forEach((id, s) -> sb.append(String.format("  #%d: %s (с %s)\n", id, s.plate, s.entryTime)));
            } else {
                sb.append("Парковка свободна.\n");
            }
            return sb.toString();
        }

        static class Session {
            String plate;
            int spotId;
            LocalDateTime entryTime;
            LocalDateTime exitTime;
            Session(String p, int s, LocalDateTime e) { plate=p; spotId=s; entryTime=e; }
        }
    }
}