package ru.bmstu.bank.application.pattern.observer;

import ru.bmstu.bank.domain.model.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditLogObserver implements Observer {
    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");

    @Override
    public void update(NotificationEvent event) {
        auditLog.info("AUDIT | {} | Account#{} | {} | Amount: {} | Msg: {}",
                event.getTimestamp(), event.getAccountId(), event.getType(),
                event.getAmount(), event.getMessage());
    }

    @Override
    public String getObserverName() {
        return "AuditLogObserver";
    }
}8