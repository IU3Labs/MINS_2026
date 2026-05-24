package org.example.service.subscription;

import org.example.domain.model.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SessionSubscriptionService {
    // Observer-style in-memory subscriptions for customers interested in session updates.
    private final Map<UUID, Map<String, SessionSubscriber>> subscriptionsBySession = new LinkedHashMap<>();
    private final Map<String, CustomerSessionSubscriber> subscribersByName = new LinkedHashMap<>();

    public void subscribe(UUID sessionId, String customerName) {
        if (customerName == null || customerName.isBlank()) {
            return;
        }

        CustomerSessionSubscriber subscriber = subscribersByName.computeIfAbsent(
                customerName,
                CustomerSessionSubscriber::new
        );

        subscriptionsBySession
                .computeIfAbsent(sessionId, ignored -> new LinkedHashMap<>())
                .put(subscriber.subscriberKey(), subscriber);
    }

    public void unsubscribe(UUID sessionId, String customerName) {
        Map<String, SessionSubscriber> subscribers = subscriptionsBySession.get(sessionId);
        if (subscribers == null) {
            return;
        }
        subscribers.remove(customerName);
        if (subscribers.isEmpty()) {
            subscriptionsBySession.remove(sessionId);
        }
    }

    public List<String> getSubscribers(UUID sessionId) {
        Map<String, SessionSubscriber> subscribers = subscriptionsBySession.get(sessionId);
        if (subscribers == null) {
            return List.of();
        }
        return new ArrayList<>(subscribers.keySet());
    }

    public List<SessionNotification> getNotifications(String customerName) {
        CustomerSessionSubscriber subscriber = subscribersByName.get(customerName);
        if (subscriber == null) {
            return List.of();
        }
        return subscriber.notifications();
    }

    public void notifySessionUpdated(Session session, String message) {
        notifySubscribers(session, SessionNotificationType.SESSION_UPDATED, message);
    }

    public void notifySessionCancelled(Session session) {
        notifySubscribers(
                session,
                SessionNotificationType.SESSION_CANCELLED,
                "Session was removed from the schedule."
        );
        subscriptionsBySession.remove(session.getId());
    }

    private void notifySubscribers(Session session, SessionNotificationType type, String message) {
        Map<String, SessionSubscriber> subscribers = subscriptionsBySession.get(session.getId());
        if (subscribers == null) {
            return;
        }

        SessionNotification notification = new SessionNotification(
                session.getId(),
                session.getMovie().getTitle(),
                type,
                message,
                LocalDateTime.now()
        );

        for (SessionSubscriber subscriber : subscribers.values()) {
            subscriber.receive(notification);
        }
    }
}
