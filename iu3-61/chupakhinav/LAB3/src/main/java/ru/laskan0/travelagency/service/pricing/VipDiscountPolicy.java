package ru.laskan0.travelagency.service.pricing;

import ru.laskan0.travelagency.model.Discount;
import ru.laskan0.travelagency.model.DiscountType;

public class VipDiscountPolicy implements DiscountPolicy {
    @Override
    public boolean supports(Discount discount) {
        return discount != null && discount.getType() == DiscountType.VIP;
    }

    @Override
    public double apply(double basePrice, Discount discount) {
        int totalPercent = Math.min(discount.getPercent() + 5, 100);
        return basePrice - (basePrice * totalPercent / 100.0);
    }
}
