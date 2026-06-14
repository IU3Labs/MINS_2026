package ru.bmstu.observer;

import ru.bmstu.model.Booking;
import ru.bmstu.util.Logger;

public class LoggerNotifier implements Observer {
    private static LoggerNotifier instance;

    private LoggerNotifier() {}

    public static LoggerNotifier getInstance() {
        if (instance == null) {
            instance = new LoggerNotifier();
        }
        return instance;
    }

    @Override
    public void update(Booking booking, String event) {
        String message = String.format("Событие: %s | Бронь ID: %d | Статус: %s",
                event, booking.getId(), booking.getStatusName());
        Logger.log(message);
    }

    @Override
    public String getObserverName() {
        return "LoggerNotifier";
    }
}