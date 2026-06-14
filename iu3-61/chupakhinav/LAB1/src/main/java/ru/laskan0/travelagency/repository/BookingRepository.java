package ru.laskan0.travelagency.repository;

import java.util.List;
import java.util.Optional;

import ru.laskan0.travelagency.model.Booking;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(String id);
    List<Booking> findAll();
    boolean existsById(String id);
}
