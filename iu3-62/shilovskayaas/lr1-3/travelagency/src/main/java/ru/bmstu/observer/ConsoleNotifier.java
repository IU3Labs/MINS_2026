package ru.bmstu.observer;

import ru.bmstu.model.Booking;

public class ConsoleNotifier implements Observer {
    private static ConsoleNotifier instance;

    private ConsoleNotifier() {}

    public static ConsoleNotifier getInstance() {
        if (instance == null) {
            instance = new ConsoleNotifier();
        }
        return instance;
    }

    @Override
    public void update(Booking booking, String event) {
        System.out.println("\n[КОНСОЛЬ-АДМИН] Событие: " + event);
        System.out.println("   Бронирование #" + booking.getId() + ", статус: " + booking.getStatusName());
    }

    @Override
    public String getObserverName() {
        return "ConsoleNotifier";
    }
}