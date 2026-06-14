package ru.iu3.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import ru.iu3.entity.Booking;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.exceptions.ConflictException;
import ru.iu3.exceptions.NotFoundExeption;
import ru.iu3.repository.interfaces.Repository;
import ru.iu3.service.interfaces.PricingStrategy;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.validation.BookingValidator;

public class BookingServiceImpl implements BookingService {
    private RoomService roomService;
    private PassService passService;
    private Repository<Booking, Integer> bookingRepository;
    private BookingValidator bookingValidator;
    private PricingStrategy pricingStrategy;

    public BookingServiceImpl(RoomService roomService, PassService passService,
            Repository<Booking, Integer> bookingRepository, BookingValidator bookingValidator,
            PricingStrategy pricingStrategy) {
        this.roomService = roomService;
        this.passService = passService;
        this.bookingRepository = bookingRepository;
        this.bookingValidator = bookingValidator;
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public double createBooking(int roomId, int passId, LocalDateTime startTime, LocalDateTime endTime) {
        bookingValidator.validateBookingRequest(roomId, passId, startTime, endTime, roomService.getRoomById(roomId).getIsLocked());
        roomService.getRoomById(roomId);
        passService.getPassById(passId);
        checkIntersection(roomId, startTime, endTime);
        int bookingId = generateBookingId();
        bookingRepository.add(new Booking(bookingId, roomId, passId, startTime, endTime));

        Room room = roomService.getRoomById(roomId);
        return pricingStrategy.calculate(room, startTime, endTime);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.getAll();
    }

    @Override
    public List<Room> getRoomsForPass(int passId) {
        passService.getPassById(passId);

        List<Room> rooms = new ArrayList<>();
        for (Booking booking : bookingRepository.getAll()) {
            if (booking.isActive() && booking.getPassId() == passId) {
                rooms.add(roomService.getRoomById(booking.getRoomId()));
            }
        }
        return new ArrayList<>(rooms);
    }

    @Override
    public Booking getBookingById(int id) {
        Booking booking = bookingRepository.getById(id);
        if (booking == null) {
            throw new NotFoundExeption("Бронирование с таким ID не найдено.");
        }
        return booking;
    }

    @Override
    public void cancelBooking(int id) {
        Booking booking = getBookingById(id);
        booking.deactivate();
    }

    private void checkIntersection(int roomId, LocalDateTime startTime, LocalDateTime endTime) {
        for (Booking booking : bookingRepository.getAll()) {
            if (booking.getRoomId() == roomId && booking.isActive() &&
                    (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime()))) {
                throw new ConflictException("Время бронирования пересекается с существующим бронированием.");
            }
        }
    }

    private int generateBookingId() {
        int maxId = 0;
        for (Booking booking : bookingRepository.getAll()) {
            if (booking.getId() > maxId) {
                maxId = booking.getId();
            }
        }
        return maxId + 1;
    }
}
