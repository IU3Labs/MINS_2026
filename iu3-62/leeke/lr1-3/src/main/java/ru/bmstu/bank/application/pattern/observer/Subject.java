package ru.bmstu.bank.application.pattern.observer;

import ru.bmstu.bank.domain.model.NotificationEvent;

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(NotificationEvent event);
}