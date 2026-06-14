package ru.bmstu.service;

import ru.bmstu.exception.TourAgencyException;
import ru.bmstu.grpc.ReferenceServiceClient;
import ru.bmstu.service.interfaces.IClientAuthService;
import ru.bmstu.util.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class ClientAuthService implements IClientAuthService {
    private final ReferenceServiceClient refClient;

    // Регулярное выражение для валидации email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public ClientAuthService(ReferenceServiceClient refClient) {
        this.refClient = refClient;
    }

    private String getTraceId() {
        return UUID.randomUUID().toString();
    }

    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new TourAgencyException("Email не может быть пустым");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new TourAgencyException("Неверный формат email. Email должен быть в формате user@example.com");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new TourAgencyException("Пароль не может быть пустым");
        }
    }

    @Override
    public ReferenceServiceClient.ClientInfo register(String name, String email, String password) {
        String traceId = getTraceId();
        Logger.log(traceId, "ClientAuthService: регистрация клиента " + email);

        // Валидация
        validateEmailFormat(email);
        validatePassword(password);

        // Проверяем, не существует ли уже такой email
        Optional<ReferenceServiceClient.ClientInfo> existing = refClient.getClientByEmail(email, traceId);

        if (existing.isPresent()) {
            throw new TourAgencyException("Клиент с email " + email + " уже существует");
        }

        // Регистрация клиента (нужно добавить метод createClient в Service B)
        Logger.log(traceId, "ClientAuthService: регистрация временно недоступна");
        throw new UnsupportedOperationException(
                "Регистрация новых клиентов временно недоступна. Используйте тестового клиента: test@test.com / test123");
    }

    @Override
    public Optional<ReferenceServiceClient.ClientInfo> login(String email, String password) {
        String traceId = getTraceId();
        Logger.log(traceId, "ClientAuthService: вход клиента " + email);

        try {
            // Валидация
            validateEmailFormat(email);
            validatePassword(password);

            Optional<ReferenceServiceClient.ClientInfo> clientInfoOpt = refClient.getClientByEmail(email, traceId);

            if (clientInfoOpt.isPresent()) {
                ReferenceServiceClient.ClientInfo clientInfo = clientInfoOpt.get();

                // Проверка пароля (только для тестового клиента)
                // В реальном приложении пароль должен проверяться в Service B
                if (email.equals("test@test.com") && password.equals("test123")) {
                    Logger.log(traceId, "ClientAuthService: вход выполнен для " + clientInfo.getName());
                    return Optional.of(clientInfo);
                } else if (!email.equals("test@test.com")) {
                    Logger.log(traceId, "ClientAuthService: неверный пароль для " + email);
                    return Optional.empty();
                }
            }

            Logger.log(traceId, "ClientAuthService: клиент не найден: " + email);
            return Optional.empty();

        } catch (Exception e) {
            Logger.log(traceId, "ClientAuthService: ОШИБКА - Service B недоступен: " + e.getMessage());
            throw new TourAgencyException(
                    "Сервис аутентификации недоступен. Пожалуйста, убедитесь, что Service B запущен.");
        }
    }
}