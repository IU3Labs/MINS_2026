package ru.bmstu.bank.application.service;

import org.springframework.stereotype.Service;
import ru.bmstu.bank.application.pattern.observer.AccountSubject;
import ru.bmstu.bank.application.pattern.observer.Observer;
import ru.bmstu.bank.domain.model.NotificationEvent;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    private final Map<Long, AccountSubject> subjectRegistry = new ConcurrentHashMap<>();

    public void attachObserver(Long accountId, Observer observer) {
        subjectRegistry.computeIfAbsent(accountId, id -> new AccountSubject()).attach(observer);
        System.out.println("Наблюдатель подключен к счету #" + accountId);
    }

    public void notify(Long accountId, NotificationEvent.EventType type, BigDecimal amount, String message) {
        AccountSubject subject = subjectRegistry.get(accountId);
        if (subject != null) {
            subject.notifyObservers(new NotificationEvent(accountId, type, amount, message));
        }
    }
}