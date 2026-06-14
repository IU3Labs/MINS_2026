package ru.iu3.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import ru.iu3.entity.Booking;
import ru.iu3.entity.interfaces.Room;

public interface BookingService {

    double createBooking(int roomId, int passId, LocalDateTime startTime, LocalDateTime endTime);

    List<Booking> getAllBookings();

    List<Room> getRoomsForPass(int passId);

    Booking getBookingById(int id);

    void cancelBooking(int id);
}
