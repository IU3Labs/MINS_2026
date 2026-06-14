package ru.iu3.service.pricing;

import java.time.Duration;
import java.time.LocalDateTime;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.service.interfaces.PricingStrategy;

public class StandardStrategy implements PricingStrategy {

    @Override
    public double calculate(Room room, LocalDateTime startTime, LocalDateTime endTime) {
        long minutes = Duration.between(startTime, endTime).toMinutes();
        double hours = minutes / 60.0;
        return hours * room.getHourlyRate();
    }
}
