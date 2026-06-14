package com.cinema.core.client;

import com.cinema.proto.reference.ReferenceServiceGrpc;
import com.cinema.proto.reference.ReferenceServiceOuterClass.*;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ReferenceServiceClient {
    private static final Logger log = LoggerFactory.getLogger(ReferenceServiceClient.class);

    private final String host;
    private final int port;
    private final int maxRetries;
    private volatile ManagedChannel channel;
    private volatile ReferenceServiceGrpc.ReferenceServiceBlockingStub stub;
    private final AtomicBoolean available = new AtomicBoolean(false);
    private final AtomicLong lastFailureTime = new AtomicLong(0);

    private static final long INITIAL_RETRY_DELAY_MS = 1000;   // 1 секунда
    private static final long MAX_RETRY_DELAY_MS = 30000;      // 30 секунд

    public ReferenceServiceClient(String host, int port, int maxRetries) {
        this.host = host;
        this.port = port;
        this.maxRetries = maxRetries;
        createChannel();
        checkConnection();
    }

    public static ReferenceServiceClient create(String host, int port, int maxRetries) {
        return new ReferenceServiceClient(host, port, maxRetries);
    }

    private synchronized void createChannel() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdownNow();
        }
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = ReferenceServiceGrpc.newBlockingStub(channel);
    }

    private void checkConnection() {
        try {
            stub.listMovies(com.google.protobuf.Empty.getDefaultInstance());
            available.set(true);
            lastFailureTime.set(0);
            log.info("Connected to Reference Service at {}:{}", host, port);
        } catch (Exception e) {
            available.set(false);
            lastFailureTime.set(System.currentTimeMillis());
            log.warn("Reference Service not available at {}:{}: {}", host, port, e.getMessage());
        }
    }

    private static final long AVAILABLE_TIMEOUT_MS = 5000; // 5 секунд
    private AtomicLong lastCheckTime = new AtomicLong(0);

    public boolean isAvailable() {
        long now = System.currentTimeMillis();
        if (available.get() && now - lastCheckTime.get() < AVAILABLE_TIMEOUT_MS) {
            return true;
        }
        // Кэш устарел или сервер был недоступен — делаем реальную проверку
        synchronized (this) {
            if (now - lastCheckTime.get() >= AVAILABLE_TIMEOUT_MS) {
                try {
                    stub.listMovies(Empty.getDefaultInstance());
                    available.set(true);
                    lastCheckTime.set(now);
                } catch (Exception e) {
                    available.set(false);
                    lastCheckTime.set(now);
                    tryReconnect(); // попробуем переподключиться
                }
            }
            return available.get();
        }
    }

    public String getErrorMessage() {
        return "Reference Service at " + host + ":" + port + " is unavailable";
    }

    private synchronized boolean tryReconnect() {
        log.info("Attempting to reconnect to Reference Service...");
        createChannel();          // создаём новый канал
        try {
            stub.listMovies(com.google.protobuf.Empty.getDefaultInstance());
            available.set(true);
            lastFailureTime.set(0);
            log.info("Successfully reconnected to Reference Service");
            return true;
        } catch (Exception e) {
            available.set(false);
            log.warn("Reconnection failed: {}", e.getMessage());
            return false;
        }
    }

    public Map<String, Object> getScreening(String traceId, String screeningId) {
        // Если сервис недоступен, пробуем переподключиться один раз
        if (!available.get()) {
            if (tryReconnect()) {
                // после успешного переподключения продолжаем выполнение
            } else {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("error", "Reference Service is unavailable. Please try again later.");
                return errorResult;
            }
        }

        int attempt = 0;
        long delay = INITIAL_RETRY_DELAY_MS;

        while (attempt < maxRetries) {
            try {
                var stubWithTrace = stub.withInterceptors(new TraceIdClientInterceptor(traceId));
                GetScreeningRequest request = GetScreeningRequest.newBuilder()
                        .setScreeningId(screeningId)
                        .build();

                ScreeningDetailResponse response = stubWithTrace.getScreening(request);

                // Успешный ответ — сбрасываем флаг недоступности
                if (!available.get()) {
                    available.set(true);
                    lastFailureTime.set(0);
                }

                Map<String, Object> screening = new HashMap<>();
                screening.put("id", response.getId());
                screening.put("movie_id", response.getMovieId());
                screening.put("hall_id", response.getHallId());
                screening.put("start_time", response.getStartTime());
                screening.put("ticket_price", response.getTicketPrice());
                screening.put("rows", response.getRows());
                screening.put("seats_per_row", response.getSeatsPerRow());

                Map<String, Object> result = new HashMap<>();
                result.put("screening", screening);
                return result;

            } catch (StatusRuntimeException e) {
                if (e.getStatus().getCode() == io.grpc.Status.Code.UNAVAILABLE) {
                    attempt++;
                    log.warn("[{}] Reference Service unavailable, attempt {}/{}", traceId, attempt, maxRetries);

                    if (attempt < maxRetries) {
                        try {
                            Thread.sleep(delay);
                            delay = Math.min(delay * 2, MAX_RETRY_DELAY_MS); // exponential backoff
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                        // Перед следующей попыткой пересоздаём канал (на случай, если адрес изменился)
                        createChannel();
                    } else {
                        available.set(false);
                        Map<String, Object> errorResult = new HashMap<>();
                        errorResult.put("error", "Reference Service unavailable after " + maxRetries + " attempts");
                        return errorResult;
                    }
                } else {
                    // Другая ошибка (NOT_FOUND, INVALID_ARGUMENT и т.д.) — не повторяем
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("error", e.getStatus().getDescription());
                    return errorResult;
                }
            } catch (Exception e) {
                log.error("[{}] Unexpected error in getScreening", traceId, e);
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("error", "Internal error: " + e.getMessage());
                return errorResult;
            }
        }

        Map<String, Object> fallbackError = new HashMap<>();
        fallbackError.put("error", "Reference Service is unavailable");
        return fallbackError;
    }

    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}