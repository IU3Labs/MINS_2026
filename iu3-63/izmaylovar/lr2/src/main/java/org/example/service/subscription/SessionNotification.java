package org.example.service.subscription;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionNotification(
        UUID sessionId,
        String movieTitle,
        SessionNotificationType type,
        String message,
        LocalDateTime createdAt
) {
}
