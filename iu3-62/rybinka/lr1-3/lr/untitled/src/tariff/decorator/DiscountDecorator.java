package tariff.decorator;

import tariff.TariffCalculator;

import java.time.LocalDateTime;

public class DiscountDecorator extends TariffDecorator {
    private final double discountFactor;

    public DiscountDecorator(TariffCalculator wrapped, double discountFactor) {
        super(wrapped);
        this.discountFactor = discountFactor;
    }

    @Override
    public double calculate(LocalDateTime entry, LocalDateTime exit) {
        return wrapped.calculate(entry, exit) * discountFactor;
    }
}