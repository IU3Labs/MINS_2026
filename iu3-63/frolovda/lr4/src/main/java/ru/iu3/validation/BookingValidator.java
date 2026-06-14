package ru.iu3.validation;

import java.time.LocalDateTime;

import ru.iu3.exceptions.ValidationException;

public class BookingValidator {

    public void validateBookingRequest(int roomId, int passId, LocalDateTime startTime, LocalDateTime endTime, boolean isRoomLocked) {
        if (isRoomLocked) {
            throw new ValidationException("Комната заблокирована и не может быть забронирована.");
        }
        if (roomId <= 0) {
            throw new ValidationException("ID комнаты должен быть положительным числом.");
        }
        if (passId <= 0) {
            throw new ValidationException("ID пропуска должен быть положительным числом.");
        }
        if (startTime == null || endTime == null) {
            throw new ValidationException("Время начала и окончания бронирования не может быть null.");
        }
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("Время начала бронирования должно быть раньше времени окончания.");
        }
    }
}
