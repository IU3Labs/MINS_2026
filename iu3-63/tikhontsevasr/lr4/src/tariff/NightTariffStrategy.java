package tariff;

import java.math.BigDecimal;
import java.time.Duration;

public class NightTariffStrategy implements TariffStrategy {
    private final BigDecimal firstThreeHoursRate;
    private final BigDecimal nextHourRate;

    public NightTariffStrategy(BigDecimal firstThreeHoursRate, BigDecimal nextHourRate) {
        this.firstThreeHoursRate = firstThreeHoursRate;
        this.nextHourRate = nextHourRate;
    }



    @Override
    public TariffType getType() {
        return TariffType.NIGHT;
    }

    @Override
    public BigDecimal calculateCost(Duration duration) {
        long minutes = Math.max(1, duration.toMinutes());
        long hours = (minutes + 59) / 60;
        if (hours <= 3) {
            return firstThreeHoursRate;
        }
        return firstThreeHoursRate.add(nextHourRate.multiply(BigDecimal.valueOf(hours - 3)));
    }
}
