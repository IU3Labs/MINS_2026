package ru.laskan0.travelagency.service.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ru.laskan0.travelagency.model.Booking;

public class BookingHistoryObserver implements BookingObserver {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final List<String> eventLog = new ArrayList<>();

    @Override
    public void onBookingCreated(Booking booking) {
        String entry = buildEntry(
                "CREATE",
                "Создано бронирование " + booking.getId()
                        + " для клиента " + booking.getClient().getFullName()
                        + " на тур \"" + booking.getTour().getTitle() + "\""
                        + ", итоговая цена: " + String.format("%.2f", booking.getFinalPrice()));
        eventLog.add(entry);
        System.out.println(entry);
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        String entry = buildEntry(
                "CANCEL",
                "Отменено бронирование " + booking.getId()
                        + ", статус: " + booking.getStatus());
        eventLog.add(entry);
        System.out.println(entry);
    }

    public List<String> getEventLog() {
        return List.copyOf(eventLog);
    }

    private String buildEntry(String eventType, String message) {
        return "[Observer " + eventType + "] "
                + LocalDateTime.now().format(FORMATTER)
                + " - " + message;
    }
}
