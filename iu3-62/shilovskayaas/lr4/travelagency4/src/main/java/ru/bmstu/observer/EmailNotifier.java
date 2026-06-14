package ru.bmstu.observer;

import ru.bmstu.model.Booking;


public class EmailNotifier implements Observer {
    private final String clientEmail;

    public EmailNotifier(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    @Override
    public void update(Booking booking, String event) {
        System.out.println("\n[EMAIL] отправлено на " + clientEmail);
        System.out.println("   Тема: Изменение статуса бронирования #" + booking.getId());
        System.out.println("   Сообщение: " + event);
    }

    @Override
    public String getObserverName() {
        return "EmailNotifier(" + clientEmail + ")";
    }
}