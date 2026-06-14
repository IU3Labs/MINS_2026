package tariff;

import java.math.BigDecimal;
import java.time.Duration;

public interface TariffStrategy {
    TariffType getType();

    BigDecimal calculateCost(Duration duration);
}
