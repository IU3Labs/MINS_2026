package tariff.decorator;

import tariff.TariffCalculator;

import java.time.LocalDateTime;

public class NightSurchargeDecorator extends TariffDecorator {
    private final double multiplier;

    public NightSurchargeDecorator(TariffCalculator wrapped, double multiplier) {
        super(wrapped);
        this.multiplier = multiplier;
    }

    @Override
    public double calculate(LocalDateTime entry, LocalDateTime exit) {
        double baseCost = wrapped.calculate(entry, exit);
        int entryHour = entry.getHour();
        int exitHour = exit.getHour();
        // Если въезд после 22:00 или выезд до 06:00, применяем наценку
        if (entryHour >= 22 || exitHour < 6) {
            return baseCost * multiplier;
        }
        return baseCost;
    }
}