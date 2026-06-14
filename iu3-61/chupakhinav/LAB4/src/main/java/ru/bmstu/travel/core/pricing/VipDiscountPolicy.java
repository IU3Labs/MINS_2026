package ru.bmstu.travel.core.pricing;

import ru.bmstu.travel.core.catalog.CatalogDiscount;

public class VipDiscountPolicy implements DiscountPolicy {
    @Override
    public boolean supports(CatalogDiscount discount) {
        return "VIP".equals(discount.discountType());
    }

    @Override
    public double apply(double basePrice, CatalogDiscount discount) {
        return basePrice * (1 - Math.min(discount.percent() + 5, 30) / 100.0);
    }
}
