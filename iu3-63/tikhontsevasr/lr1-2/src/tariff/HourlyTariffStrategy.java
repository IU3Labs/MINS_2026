package tariff;

import java.math.BigDecimal;
import java.time.Duration;

public class HourlyTariffStrategy implements TariffStrategy {
    private final BigDecimal hourlyRate;

    public HourlyTariffStrategy(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }


    @Override
    public TariffType getType() {
        return TariffType.HOURLY;
    }

    @Override
    public BigDecimal calculateCost(Duration duration) {
        long minutes = Math.max(1, duration.toMinutes());
        long hours = (minutes + 59) / 60;
        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }
}
