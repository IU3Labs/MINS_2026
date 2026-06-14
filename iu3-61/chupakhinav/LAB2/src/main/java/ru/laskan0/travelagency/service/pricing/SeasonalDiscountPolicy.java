package ru.laskan0.travelagency.service.pricing;

import ru.laskan0.travelagency.model.Discount;
import ru.laskan0.travelagency.model.DiscountType;

public class SeasonalDiscountPolicy implements DiscountPolicy {
    @Override
    public boolean supports(Discount discount) {
        return discount != null && discount.getType() == DiscountType.SEASONAL;
    }

    @Override
    public double apply(double basePrice, Discount discount) {
        double priceAfterPercent = basePrice - (basePrice * discount.getPercent() / 100.0);
        double seasonalBonus = 2000.0;
        return Math.max(priceAfterPercent - seasonalBonus, 0.0);
    }
}
