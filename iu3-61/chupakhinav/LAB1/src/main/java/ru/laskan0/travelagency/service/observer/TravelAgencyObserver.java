package ru.laskan0.travelagency.service.observer;

import ru.laskan0.travelagency.model.Booking;
import ru.laskan0.travelagency.model.Client;
import ru.laskan0.travelagency.model.Tour;

public interface TravelAgencyObserver {
    void onClientAdded(Client client);

    void onTourAdded(Tour tour);

    void onBookingCreated(Booking booking);

    void onBookingCancelled(Booking booking);
}
