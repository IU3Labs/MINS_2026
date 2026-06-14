package ru.iu3.service.interfaces;

import java.time.LocalDateTime;

import ru.iu3.entity.interfaces.Room;

public interface PricingStrategy {

    double calculate(Room room, LocalDateTime startTime, LocalDateTime endTime);
}
