package main.java.service.notifications;

public interface NotificationObserver {
    void update(String message, NotificationType type);
    String getObserverName();
}