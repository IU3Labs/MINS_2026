package ru.bmstu.travel.core.history;

import ru.bmstu.travel.core.model.Booking;

import java.util.ArrayList;
import java.util.List;

public class TravelAgencyHistoryObserver {
    private final List<String> events = new ArrayList<>();

    public synchronized void onBookingCreated(Booking booking) {
        events.add(String.format(
                "Создано бронирование %s для клиента %s на тур %s.",
                booking.id(),
                booking.clientName(),
                booking.tourTitle()
        ));
    }

    public synchronized void onBookingCancelled(Booking booking) {
        events.add(String.format("Отменено бронирование %s.", booking.id()));
    }

    public synchronized void record(String message) {
        events.add(message);
    }

    public synchronized List<String> getEventLog() {
        return new ArrayList<>(events);
    }
}
