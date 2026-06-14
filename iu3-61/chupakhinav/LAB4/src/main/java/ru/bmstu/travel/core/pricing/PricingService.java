package ru.bmstu.travel.core.pricing;

import ru.bmstu.travel.core.catalog.CatalogDiscount;
import ru.bmstu.travel.core.exception.InvalidDataException;
import ru.bmstu.travel.core.exception.PricingException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PricingService {
    private final List<DiscountPolicy> policies;

    public PricingService() {
        this(List.of(
                new PercentageDiscountPolicy(),
                new VipDiscountPolicy(),
                new SeasonalDiscountPolicy()
        ));
    }

    public PricingService(List<DiscountPolicy> policies) {
        if (policies == null || policies.isEmpty()) {
            throw new InvalidDataException("Список политик скидок не может быть пустым.");
        }
        this.policies = List.copyOf(policies);
    }

    public double calculateFinalPrice(double basePrice, CatalogDiscount discount) {
        if (basePrice <= 0) {
            throw new InvalidDataException("Базовая цена должна быть больше 0.");
        }
        if (discount == null) {
            return basePrice;
        }
        for (DiscountPolicy policy : policies) {
            if (policy.supports(discount)) {
                return round(policy.apply(basePrice, discount));
            }
        }
        throw new PricingException("Не найдено правило расчета для скидки: " + discount.name());
    }

    private static double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
