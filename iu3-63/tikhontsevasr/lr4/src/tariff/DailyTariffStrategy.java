package tariff;

import java.math.BigDecimal;
import java.time.Duration;

public class DailyTariffStrategy implements TariffStrategy {
    private final BigDecimal dailyRate;

    public DailyTariffStrategy(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public TariffType getType() {
        return TariffType.DAILY;
    }

    @Override
    public BigDecimal calculateCost(Duration duration) {
        long minutes = Math.max(1, duration.toMinutes());
        long days = (minutes + (24L * 60 - 1)) / (24L * 60);
        return dailyRate.multiply(BigDecimal.valueOf(days));
    }
}
