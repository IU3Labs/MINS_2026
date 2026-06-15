package tariff;

import java.time.LocalDateTime;

public interface TariffCalculator {
    double calculate(LocalDateTime entry, LocalDateTime exit);
}