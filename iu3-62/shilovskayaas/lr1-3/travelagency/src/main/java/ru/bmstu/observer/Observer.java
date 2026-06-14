package ru.bmstu.observer;

import ru.bmstu.model.Booking;

public interface Observer {
    void update(Booking booking, String event);
    String getObserverName();
}