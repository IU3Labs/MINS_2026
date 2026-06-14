package ru.laskan0.travelagency.service.pricing;

import ru.laskan0.travelagency.model.Discount;
import ru.laskan0.travelagency.model.DiscountType;

public class PercentageDiscountPolicy implements DiscountPolicy {
    @Override
    public boolean supports(Discount discount) {
        return discount != null && discount.getType() == DiscountType.PERCENTAGE;
    }

    @Override
    public double apply(double basePrice, Discount discount) {
        return basePrice - (basePrice * discount.getPercent() / 100.0);
    }
}
