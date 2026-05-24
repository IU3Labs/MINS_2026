package org.example.service.subscription;

import java.util.List;

public interface SessionSubscriber {
    String subscriberKey();

    void receive(SessionNotification notification);

    List<SessionNotification> notifications();
}
