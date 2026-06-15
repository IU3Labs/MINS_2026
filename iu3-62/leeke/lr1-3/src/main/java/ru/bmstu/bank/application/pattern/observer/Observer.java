package ru.bmstu.bank.application.pattern.observer;

import ru.bmstu.bank.domain.model.NotificationEvent;

public interface Observer {
    void update(NotificationEvent event);
    String getObserverName();
}