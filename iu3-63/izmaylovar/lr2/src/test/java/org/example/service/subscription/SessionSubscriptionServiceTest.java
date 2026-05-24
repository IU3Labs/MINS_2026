package org.example.service.subscription;

import org.example.domain.model.Hall;
import org.example.domain.model.Movie;
import org.example.domain.model.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SessionSubscriptionServiceTest {
    @Test
    void sendsNotificationsToSubscribedCustomers() {
        Session session = new Session(
                UUID.randomUUID(),
                new Movie(UUID.randomUUID(), "Dune", 166, "Sci-Fi", "16+"),
                new Hall(UUID.randomUUID(), "Blue Hall", 5, 5),
                LocalDateTime.of(2026, 4, 8, 18, 0),
                new BigDecimal("450")
        );

        SessionSubscriptionService subscriptionService = new SessionSubscriptionService();
        subscriptionService.subscribe(session.getId(), "Alice");

        subscriptionService.notifySessionUpdated(session, "Session start time changed.");

        var notifications = subscriptionService.getNotifications("Alice");
        assertEquals(1, notifications.size());
        assertEquals(SessionNotificationType.SESSION_UPDATED, notifications.getFirst().type());
    }
}
