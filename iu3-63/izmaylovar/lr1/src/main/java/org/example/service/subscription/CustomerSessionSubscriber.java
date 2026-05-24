package org.example.service.subscription;

import java.util.ArrayList;
import java.util.List;

public class CustomerSessionSubscriber implements SessionSubscriber {
    private final String customerName;
    private final List<SessionNotification> notifications = new ArrayList<>();

    public CustomerSessionSubscriber(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String subscriberKey() {
        return customerName;
    }

    @Override
    public void receive(SessionNotification notification) {
        notifications.add(notification);
    }

    @Override
    public List<SessionNotification> notifications() {
        return List.copyOf(notifications);
    }
}
