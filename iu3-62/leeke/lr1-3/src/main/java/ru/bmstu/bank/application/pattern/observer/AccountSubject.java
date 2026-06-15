package ru.bmstu.bank.application.pattern.observer;

import ru.bmstu.bank.domain.model.NotificationEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AccountSubject implements Subject {
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(NotificationEvent event) {
        for (Observer observer : observers) {
            try {
                observer.update(event);
            } catch (Exception e) {
                // Один упавший наблюдатель не должен ломать остальных
                System.err.println("Ошибка в наблюдателе " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }

    public void notifyDeposit(Long accountId, BigDecimal amount) {
        var event = new NotificationEvent(accountId, NotificationEvent.EventType.DEPOSIT, amount,
                "Поступление: " + amount + " руб.");
        notifyObservers(event);
    }

    public void notifyLowBalance(Long accountId, BigDecimal balance, BigDecimal threshold) {
        var event = new NotificationEvent(accountId, NotificationEvent.EventType.LOW_BALANCE, balance,
                "Внимание: баланс " + balance + " руб. ниже порога " + threshold + " руб.");
        notifyObservers(event);
    }
}