package service.notifications;

import java.util.ArrayList;
import java.util.List;
public class NotificationSubject {
    private final List<NotificationObserver> observers = new ArrayList<>();

    public void attach(NotificationObserver observer) {
        observers.add(observer);
    }

    public void detach(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message, NotificationType type) {
        for (NotificationObserver observer : observers) {
            observer.update(message, type);
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}