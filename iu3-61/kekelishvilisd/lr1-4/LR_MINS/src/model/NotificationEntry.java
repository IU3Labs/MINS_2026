package model;

import java.time.LocalDateTime;

public record NotificationEntry(
        LocalDateTime timestamp,
        String observerName,
        String message
) {
}
