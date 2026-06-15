package tariff.decorator;

import java.time.LocalDateTime;
import tariff.TariffCalculator;

public abstract class TariffDecorator implements TariffCalculator {
    protected final TariffCalculator wrapped;

    public TariffDecorator(TariffCalculator wrapped) {
        this.wrapped = wrapped;
    }
}