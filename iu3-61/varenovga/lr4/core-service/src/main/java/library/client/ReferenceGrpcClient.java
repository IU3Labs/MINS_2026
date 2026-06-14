package library.client;

import library.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReferenceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(ReferenceGrpcClient.class);
    private final ManagedChannel channel;
    private final ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;

    // ⚙️ Настройки ретраев
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 500;

    public ReferenceGrpcClient() {
        log.info("🔗 Подключение к Reference Service на localhost:9091...");
        channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()
                .build();
        // 🔥 Увеличенный таймаут + ретраи = надёжность
        stub = ReferenceServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(600, TimeUnit.SECONDS);
        log.info("✅ gRPC канал создан");
    }

    // ✅ Главный метод: валидация выдачи с ретраями
    public ValidationResult validateBorrow(String email, int days) {
        String trace = UUID.randomUUID().toString().substring(0, 8);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                var req = ValidateBorrowRequest.newBuilder()
                        .setUserEmail(email)
                        .setRequestedDays(days)
                        .setTraceId(trace)
                        .build();

                var resp = stub.validateBorrow(req);

                if (attempt > 1) {
                    log.info("[TRACE:{}] Успех после {} попытки", trace, attempt);
                }

                log.info("[TRACE:{}] Ответ Reference: Valid={}, Cat={}, Max={}",
                        trace, resp.getIsValid(), resp.getUserCategory(), resp.getMaxAllowedDays());

                return new ValidationResult(
                        resp.getIsValid(),
                        resp.getMaxAllowedDays(),
                        resp.getUserCategory()
                );

            } catch (StatusRuntimeException e) {
                log.warn("[TRACE:{}] Попытка {}/{} не удалась: {} ({})",
                        trace, attempt, MAX_RETRIES, e.getStatus().getCode(), e.getStatus().getDescription());

                if (attempt == MAX_RETRIES) {
                    log.warn("[TRACE:{}] Все попытки исчерпаны. Fallback (14 дней, REGULAR).", trace);
                    System.out.println("⚠️  [FALLBACK] Reference Service не ответил, используем значения по умолчанию");
                    return new ValidationResult(true, 14, "REGULAR");
                }

                // 🔄 Экспоненциальная задержка перед следующей попыткой
                long backoff = INITIAL_BACKOFF_MS * (1L << (attempt - 1)); // 500ms → 1s → 2s
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        // На случай, если цикл прервался
        return new ValidationResult(true, 14, "REGULAR");
    }

    // ✅ Валидация пользователя с ретраями
    public ValidationResultUser validateUser(String name, String email) {
        String trace = UUID.randomUUID().toString().substring(0, 8);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                var req = ValidateUserRequest.newBuilder()
                        .setName(name)
                        .setEmail(email)
                        .setTraceId(trace)
                        .build();
                var resp = stub.validateUser(req);

                if (attempt > 1) {
                    log.info("[TRACE:{}] Успех после {} попытки", trace, attempt);
                }

                return new ValidationResultUser(resp.getIsValid(), resp.getUserCategory(), resp.getErrorMessage());

            } catch (StatusRuntimeException e) {
                log.warn("[TRACE:{}] Попытка {}/{} не удалась: {}", trace, attempt, MAX_RETRIES, e.getStatus().getCode());
                if (attempt == MAX_RETRIES) {
                    log.warn("[TRACE:{}] Fallback для валидации юзера.", trace);
                    return new ValidationResultUser(true, "REGULAR", "");
                }
                try { Thread.sleep(INITIAL_BACKOFF_MS * (1L << (attempt - 1))); }
                catch (InterruptedException ignored) { Thread.currentThread().interrupt(); break; }
            }
        }
        return new ValidationResultUser(true, "REGULAR", "");
    }

    // ✅ Валидация издания с ретраями
    public boolean validatePublication(String title, int year, String isbn) {
        String trace = UUID.randomUUID().toString().substring(0, 8);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                var req = ValidatePublicationRequest.newBuilder()
                        .setTitle(title)
                        .setYear(year)
                        .setIsbn(isbn != null ? isbn : "")
                        .setTraceId(trace)
                        .build();
                var resp = stub.validatePublication(req);

                if (attempt > 1) {
                    log.info("[TRACE:{}] Успех после {} попытки", trace, attempt);
                }

                return resp.getIsValid();

            } catch (StatusRuntimeException e) {
                log.warn("[TRACE:{}] Попытка {}/{} не удалась: {}", trace, attempt, MAX_RETRIES, e.getStatus().getCode());
                if (attempt == MAX_RETRIES) {
                    log.warn("[TRACE:{}] Fallback для валидации издания.", trace);
                    return true; // Разрешаем по умолчанию
                }
                try { Thread.sleep(INITIAL_BACKOFF_MS * (1L << (attempt - 1))); }
                catch (InterruptedException ignored) { Thread.currentThread().interrupt(); break; }
            }
        }
        return true;
    }

    // ✅ Получение правил с ретраями
    public String getLoanPeriodRules() {
        String trace = UUID.randomUUID().toString().substring(0, 8);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                var req = GetRulesInfoRequest.newBuilder().setTraceId(trace).build();
                var resp = stub.getRulesInfo(req);

                if (attempt > 1) {
                    log.info("[TRACE:{}] Успех после {} попытки", trace, attempt);
                }

                return resp.getRulesText();

            } catch (StatusRuntimeException e) {
                log.warn("[TRACE:{}] Попытка {}/{} не удалась: {}", trace, attempt, MAX_RETRIES, e.getStatus().getCode());
                if (attempt == MAX_RETRIES) {
                    log.warn("[TRACE:{}] Fallback для правил выдачи.", trace);
                    return "📋 ПРАВИЛА СРОКА ВЫДАЧИ (локальный fallback):\n" +
                            "• Обычные: 14 дней\n" +
                            "• Студенты: 30 дней (21 в июне)\n" +
                            "• Преподаватели: 60 дней\n" +
                            "• VIP: 90 дней";
                }
                try { Thread.sleep(INITIAL_BACKOFF_MS * (1L << (attempt - 1))); }
                catch (InterruptedException ignored) { Thread.currentThread().interrupt(); break; }
            }
        }
        return "📋 ПРАВИЛА СРОКА ВЫДАЧИ (локальный fallback):\n• Обычные: 14 дней";
    }

    // ✅ Проверка здоровья (без ретраев, быстро)
    public boolean isHealthy() {
        try {
            var resp = stub.healthCheck(Empty.newBuilder().build());
            return resp.getAlive();
        } catch (StatusRuntimeException e) {
            return false;
        }
    }

    // ✅ DTO для результатов
    public record ValidationResult(boolean isValid, int maxDays, String category) {}
    public record ValidationResultUser(boolean isValid, String category, String error) {}

    public void shutdown() {
        channel.shutdownNow();
    }
}