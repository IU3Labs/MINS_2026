package ru.laskan0.travelagency.service.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ru.laskan0.travelagency.model.Booking;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Tour;

public class TravelAgencyHistoryObserver implements TravelAgencyObserver {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final List<String> eventLog = new ArrayList<>();

    @Override
    public void onClientAdded(Client client) {
        String entry = buildEntry(
                "CLIENT_ADD",
                "Добавлен клиент " + client.getId()
                        + ": " + client.getFullName()
                        + ", телефон: " + client.getPhone());
        eventLog.add(entry);
        System.out.println(entry);
    }

    @Override
    public void onTourAdded(Tour tour) {
        String entry = buildEntry(
                "TOUR_ADD",
                "Добавлен тур " + tour.getId()
                        + ": \"" + tour.getTitle() + "\""
                        + ", направление: " + tour.getDestination()
                        + ", цена: " + String.format("%.2f", tour.getBasePrice()));
        eventLog.add(entry);
        System.out.println(entry);
    }

    @Override
    public void onBookingCreated(Booking booking) {
        String entry = buildEntry(
                "BOOKING_CREATE",
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
                "BOOKING_CANCEL",
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
