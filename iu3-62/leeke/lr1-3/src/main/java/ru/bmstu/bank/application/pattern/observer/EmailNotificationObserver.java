package ru.bmstu.bank.application.pattern.observer;

import ru.bmstu.bank.domain.model.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotificationObserver implements Observer {
    private static final Logger log = LoggerFactory.getLogger(EmailNotificationObserver.class);
    private final String recipientEmail;

    public EmailNotificationObserver(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    @Override
    public void update(NotificationEvent event) {
        log.info("EMAIL [{}]: {} | Счет #{} | {}",
                recipientEmail, event.getType(), event.getAccountId(), event.getMessage());
    }

    @Override
    public String getObserverName() {
        return "EmailObserver[" + recipientEmail + "]";
    }
}