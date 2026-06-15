package tariff;

import exception.InvalidTimeException;

import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyTariffCalculator implements TariffCalculator {
    private final double ratePerHour;

    public HourlyTariffCalculator(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    @Override
    public double calculate(LocalDateTime entry, LocalDateTime exit) {
        if (exit.isBefore(entry)) {
            throw new InvalidTimeException("Время выезда раньше времени въезда");
        }
        long minutes = Duration.between(entry, exit).toMinutes();
        return (minutes*60) * ratePerHour;
    }
}